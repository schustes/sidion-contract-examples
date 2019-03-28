package de.sidion.books.client.adapter;

import de.sidion.books.client.domain.Book;
import de.sidion.books.client.domain.BookOrderCommand;
import de.sidion.books.client.domain.BookDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/books")
@RestController
public class BookServiceRestEndpoint {

    private final BookDomainService bookDomainService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookDomainService.fetchBooks();
    }

    @DeleteMapping(path = "{id}")
    public void deleteBook(@PathVariable("id") String id) {
        bookDomainService.deleteBook(id);
    }

    @PostMapping(consumes = "application/json")
    public void orderBook(@RequestBody BookOrderCommand orderCommand) {
        bookDomainService.orderBook(orderCommand);
    }

}
