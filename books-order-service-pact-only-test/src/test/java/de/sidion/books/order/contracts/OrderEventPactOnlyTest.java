package de.sidion.books.order.contracts;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import de.sidion.books.order.adapter.BookOrderEventRabbitSender;
import de.sidion.books.order.adapter.BookOrderRepositoryInMemoryBackend;
import de.sidion.books.order.domain.BookOrderDomainService;
import de.sidion.books.order.domain.BookOrderRepository;
import de.sidion.books.order.domain.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;

@RunWith(PactRunner.class) //Cannot be run with SpringRunner!
@Provider("book-order-service")
@PactBroker(host="localhost", port="80", protocol = "http")
public class OrderEventPactOnlyTest {

    //Amqp Client: Is not a real client, but a class scanning for the @PactVerifier annotated method in the given packages.
    @TestTarget
    public final Target target = new AmqpTarget(Collections.singletonList("de.sidion.books.order.*"));

    private BookOrderRepository repo;

    private NotificationService notificationService;

    private BookOrderDomainService service;

    //we need a mock somewhere to intercept the chain and find out if the message sent to the message server is correct
    private RabbitTemplate rabbitTemplateSpy = Mockito.spy(RabbitTemplate.class);

    @Before
    public void setup() {
        MessageConverter converter = new Jackson2JsonMessageConverter(); //convert objects using jackson
        rabbitTemplateSpy.setMessageConverter(converter);
        repo = new BookOrderRepositoryInMemoryBackend();
        notificationService = new BookOrderEventRabbitSender(rabbitTemplateSpy);
        service = new BookOrderDomainService(repo, notificationService);
    }

    @State("orderCommandReceived()")
    public void prepareState() {
        //optional method to prepare test data etc.
    }

    //Returns the string representation of the amqp payload sent via the messaging service
    @Test
    @PactVerifyProvider("a message from order-queue")
    public String verifyMessageForOrder() throws Exception {

        String bookId = "1";
        String customerId = "1";
        String isbn = "978-3-86680-192-9";

        //intercept RabbitTemplate spy to find out which message is actually converted to
        StringBuilder payload = new StringBuilder();
        doAnswer(invocation -> {
            Message amqpMessage = invocation.getArgument(2);
            payload.append(new String(amqpMessage.getBody()));
            return null;
        }).when(rabbitTemplateSpy).send(
                eq("order-exchange"),
                eq("orders.books.#"),
                any(Message.class),
                nullable(CorrelationData.class));

        //call the real service method
        service.createBookOrder(customerId, bookId, isbn);

        return payload.toString();
    }


}