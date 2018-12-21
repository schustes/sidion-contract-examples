package de.sidion.books.client.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookCatalogService bookCatalogService;
    private final BookOrderDispatcher bookOrderDispatcher;

    private int messagesReceivedCounter;

    public List<Book> fetchBooks() {
        return bookCatalogService.getAllBooks();
    }

    public void deleteBook(String bookId) {
        bookCatalogService.deleteBook(bookId);

    }

    public void orderConfirmed(BookOrderedEvent event) {
        messagesReceivedCounter ++;
        log.info("Received order confirmation message {}. {} orders have been created ", event, messagesReceivedCounter);
    }

    public int getMessagesReceivedCounter() {
        return messagesReceivedCounter;
    }

    public void orderBook(BookOrderCommand orderCommand) {
        bookOrderDispatcher.orderBook(orderCommand);
    }

}
