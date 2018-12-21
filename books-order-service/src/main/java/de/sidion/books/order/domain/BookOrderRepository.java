package de.sidion.books.order.domain;

import java.util.List;

public interface BookOrderRepository {

    List<BookOrder> findAll();

    BookOrder createOrder(String customerId, String bookId, String isbn);
}
