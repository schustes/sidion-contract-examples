package de.sidion.books.order.contracts;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.AmqpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.sidion.books.order.domain.BookOrder;
import de.sidion.books.order.domain.BookOrderDomainService;
import de.sidion.books.order.domain.BookOrderRepository;
import de.sidion.books.order.domain.BookOrderedEvent;
import de.sidion.books.order.domain.NotificationService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Uncommend the lines with annotation to run this test. It was commented because the amqp target uses reflection classes
 * which are not available in jdks > 9. If you use jdk 8 anyway, you can safely uncomment th lines.
 */
//@RunWith(PactRunner.class)
@Provider("book-order-service")
@PactBroker(host="localhost", port="80", protocol = "http")
public class OrderEventPactOnlyTest {

    @TestTarget
    public final Target target = new AmqpTarget(Collections.singletonList("de.sidion.books.order.*"));

    private BookOrderRepository repo = Mockito.mock(BookOrderRepository.class);

    private NotificationService notificationService = Mockito.mock(NotificationService.class);//new BookOrderEventRabbitSender(rabbitTemplate);

    private BookOrderDomainService service = new BookOrderDomainService(repo, notificationService);

    @State("orderCommandReceived()")
    public void prepareState() {
        //optional method to prepare test data etc.
    }

    //@Test
    //@Ignore("Requires a JDK 8 because of reflection usage in messaging target. Do not run this test unless a jdk 8 is used")
    //@PactVerifyProvider("order-exchange")
    public String verifyMessageForOrder() throws Exception {

        String bookId = "1";
        String customerId = "1";
        String isbn = "978-3-86680-192-9";
        when(repo.createOrder(any(), any(), any())).thenReturn(BookOrder.of(bookId, isbn, customerId));

        ArgumentCaptor<BookOrderedEvent> captor = ArgumentCaptor.forClass(BookOrderedEvent.class);
        service.createBookOrder("1", "1", "978-3-86680-192-9");

        Mockito.verify(notificationService).publishBookOrderedEvent(captor.capture());

        BookOrderedEvent event = captor.getValue();//new BookOrderedEvent("978-3-86680-192-9", "1");
        return new ObjectMapper().writeValueAsString(event);
    }


}