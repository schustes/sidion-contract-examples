package de.sidion.books.order.contracts;

import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit.PactRunner;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(PactRunner.class)
@Provider("book-order-service")
@PactBroker(host="localhost", port="80", protocol = "http")
public class OrderEventPactOnlyTest {

    @TestTarget
    public final Target target = new AmqpTarget(Collections.singletonList("de.sidion.books.order.*"));

    private BookOrderRepository repo = Mockito.mock(BookOrderRepository.class);

    private NotificationService notificationService = Mockito.mock(NotificationService.class);

    private BookOrderDomainService service = new BookOrderDomainService(repo, notificationService);

    @State("orderCommandReceived()")
    public void prepareState() {
        //optional method to prepare test data etc.
    }

    @Test
    @PactVerifyProvider("a message sent via order-exchange")
    public String verifyMessageForOrder() throws Exception {

        String bookId = "1";
        String customerId = "1";
        String isbn = "978-3-86680-192-9";
        when(repo.createOrder(any(), any(), any())).thenReturn(BookOrder.of(bookId, isbn, customerId));

        ArgumentCaptor<BookOrderedEvent> captor = ArgumentCaptor.forClass(BookOrderedEvent.class);
        service.createBookOrder("1", "1", "978-3-86680-192-9");

        Mockito.verify(notificationService).publishBookOrderedEvent(captor.capture());

        BookOrderedEvent event = captor.getValue();
        return new ObjectMapper().writeValueAsString(event);
    }


}