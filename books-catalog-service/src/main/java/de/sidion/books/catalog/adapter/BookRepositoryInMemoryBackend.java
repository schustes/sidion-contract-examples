package de.sidion.books.catalog.adapter;

import de.sidion.books.catalog.domain.BookRepository;
import de.sidion.books.catalog.domain.Book;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookRepositoryInMemoryBackend implements BookRepository {

    private Map<Integer, Book> books = new HashMap<>();

    @PostConstruct
    public void init() {
        Book book = Book.builder()
                .authorFirstName("John")
                .authorLastName("Doe")
                .title("Endless possibilities in contract testing")
                .id(1)
                .isbn("521-8-99350-913-2")
                .someAdditionalField1("bla")
                .someAdditionalField2("blub")
                .build();

        books.put(book.getId(), book);

    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    @Override
    public boolean delete(int id) {
        return books.remove(id) != null;
    }

    @Override
    public void deleteAll() {
        books.clear();
    }

}
