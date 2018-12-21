package de.sidion.books.order.adapter;

import de.sidion.books.order.config.MessagingConfiguration;
import de.sidion.books.order.domain.BookOrderDomainService;
import de.sidion.books.order.domain.OrderBookCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOrderCommandRabbitListener {

    private final BookOrderDomainService orderDomainService;

    @RabbitListener(queues = MessagingConfiguration.BOOK_ORDER_QUEUE, containerFactory = "simpleListenerContainerFactory")
    public void receiveMessage(OrderBookCommand command) {
        log.info("Received OrderCommand {} ", command);

        orderDomainService.createBookOrder(command.getCustomerId(), command.getBookId(), command.getIsbn());
    }

}
