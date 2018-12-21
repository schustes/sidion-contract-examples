package de.sidion.books.order.domain;

public interface NotificationService {

    void publishBookOrderedEvent(BookOrderedEvent event);
}
