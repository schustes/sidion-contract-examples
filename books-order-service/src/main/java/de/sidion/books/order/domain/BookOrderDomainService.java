package de.sidion.books.order.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The BookOrderDomainService is the central service for the book-order context.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookOrderDomainService {

    private final BookOrderRepository orderRepository;
    private final NotificationService notificationService;

    /**
     * Order a book.
     *
     * @param customerId the customer id
     * @param bookId the book id
     * @param isbn the book isbn
     */
    public void createBookOrder(String customerId, String bookId, String isbn) {
        BookOrder order = orderRepository.createOrder(customerId, bookId, isbn);
        sendBookOrderedEvent(order);
    }

    /**
     * Notifies interested parties about a new book order.
     *
     * @param order the order to notify about
     */
    private void sendBookOrderedEvent(BookOrder order) {
        notificationService.publishBookOrderedEvent(BookOrderedEvent.of(order));
    }
}
