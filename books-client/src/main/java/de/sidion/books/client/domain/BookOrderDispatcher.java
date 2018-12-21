package de.sidion.books.client.domain;

public interface BookOrderDispatcher {

    void orderBook(BookOrderCommand command);
}
