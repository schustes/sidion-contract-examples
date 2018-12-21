package de.sidion.books.catalog.domain;

import java.util.List;

public interface BookRepository {

    List<Book>  findAll();

    boolean delete(int id);
}
