package de.sidion.books.client.contracts;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.MessagePactProviderRule;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.model.v3.messaging.MessagePact;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sidion.books.client.adapter.BookOrderedEventRabbitListener;
import de.sidion.books.client.domain.BookDomainService;
import de.sidion.books.client.domain.BookOrderedEvent;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookOrderedEventConsumerPactTest {

    @Rule
    public MessagePactProviderRule mockProvider = new MessagePactProviderRule(this);

    @Pact(
            provider = "book-order-service",
            consumer = "books-client-book-ordered-event-consumer"
    )
    public MessagePact createPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();

        body.stringType("customerId");
        body.stringMatcher("isbn", "[0-9]{3}-[0-9]{1}-[0-9]{5}-[0-9]{3}-[0-9]{1}", "978-3-86680-192-9");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("sentTo", "order-exchange");
        MessagePact pact =  builder
                .given("orderCommandReceived()")
                .expectsToReceive("a message from order-queue")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();

        pact.getMessages().get(0).getMetaData().remove("Content-Type"); //header is set by pact-jvm automatically, but not understood by spring-contract

        return pact;
    }

    @Test
    @PactVerification({"book-order-service"})
    public void verify() throws Exception {

        //Test setup - no Spring context etc possible with pact messaging.
        BookDomainService bookDomainService = new BookDomainService(null, null);
        BookOrderedEventRabbitListener listener = new BookOrderedEventRabbitListener(bookDomainService);

        //Pact creates the message from the contract dsl
        byte[] rawMessage = mockProvider.getMessage();
        String receivedJsonMessage = new String(rawMessage);

        //verify that the message can actually be parsed to the expected object
        BookOrderedEvent event = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readerFor(BookOrderedEvent.class).readValue(receivedJsonMessage);

        //trivial that the message can actually be processed by the client
        int counterBefore = bookDomainService.getMessagesReceivedCounter();
        listener.receiveMessage(event);
        int counterAfter = bookDomainService.getMessagesReceivedCounter();

        assertThat(counterBefore, equalTo(counterAfter - 1 ));
    }

    //this is an alternative way to inject the message by the MessagePactProviderRule via reflection
    public void setMessage(byte[] messageContents) { }

}
