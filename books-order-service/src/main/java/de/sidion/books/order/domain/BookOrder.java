package de.sidion.books.order.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class BookOrder {
    String id;
    String bookId;
    String isbn;
    String customerId;

    public static BookOrder of(String bookId, String isbn, String customerId) {

        String id = UUID.randomUUID().toString();
        return new BookOrder(id, bookId, isbn, customerId);
    }

}
