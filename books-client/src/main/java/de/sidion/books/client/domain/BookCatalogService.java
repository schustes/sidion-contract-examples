package de.sidion.books.client.domain;

import java.util.List;

public interface BookCatalogService {

    List<Book> getAllBooks();

    void deleteBook(String id);

}
