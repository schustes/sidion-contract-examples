package de.sidion.books.order.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookOrderDomainService {

    private final BookOrderRepository orderRepository;
    private final NotificationService notificationService;

    public void createBookOrder(String customerId, String bookId, String isbn) {
        BookOrder order = orderRepository.createOrder(customerId, bookId, isbn);
        sendBookOrderedEvent(order);
    }

    private void sendBookOrderedEvent(BookOrder order) {
        notificationService.publishBookOrderedEvent(BookOrderedEvent.of(order));
    }
}
