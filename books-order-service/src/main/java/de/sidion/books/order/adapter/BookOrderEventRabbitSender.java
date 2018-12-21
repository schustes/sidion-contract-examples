package de.sidion.books.order.adapter;

import de.sidion.books.order.domain.BookOrderedEvent;
import de.sidion.books.order.domain.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOrderEventRabbitSender implements NotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishBookOrderedEvent(BookOrderedEvent event) {
        log.info("Sending event {} via rabbitTemplate {}", event, rabbitTemplate);

        rabbitTemplate.convertAndSend("order-exchange", "orders.books.#", event);

    }
}
