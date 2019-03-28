package de.sidion.books.client.adapter;

import de.sidion.books.client.config.MessagingConfiguration;
import de.sidion.books.client.domain.BookOrderedEvent;
import de.sidion.books.client.domain.BookDomainService;
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
        bookDomainService.orderConfirmed(event);
    }
}

