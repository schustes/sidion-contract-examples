package de.sidion.books.client.adapter;

import de.sidion.books.client.domain.BookOrderCommand;
import de.sidion.books.client.domain.BookOrderDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOrderCommandRabbitSender implements BookOrderDispatcher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void orderBook(BookOrderCommand command) {
        log.info("sending OrderCommandMessage {} with rabbitTemplate {}", command, rabbitTemplate);
        rabbitTemplate.convertAndSend("order-exchange", "orders.books.#", command);
    }
}
