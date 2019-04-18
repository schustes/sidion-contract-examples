package de.sidion.books.client.adapter;

import de.sidion.books.client.config.MessagingConfiguration;
import de.sidion.books.client.domain.BookDomainService;
import de.sidion.books.client.domain.BookOrderedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOrderedEventRabbitListener {

    private final BookDomainService bookDomainService;

    @RabbitListener(queues = MessagingConfiguration.BOOK_ORDER_QUEUE, containerFactory = "simpleListenerContainerFactory")
    public void receiveMessage(BookOrderedEvent event) {
        String isbn = event.getIsbn();
        String customer = event.getCustomerId();

        if (isbn == null) {
            throw new IllegalArgumentException("no isbn given!");
        }

        log.info("The book with ISBN {} was bought from customer {}", isbn, customer);
        bookDomainService.orderConfirmed(event);
    }
}

