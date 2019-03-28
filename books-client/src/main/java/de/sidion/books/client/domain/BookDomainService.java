package de.sidion.books.client.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The book domain service acts here as a kind of choreographer for presenting and ordering books. Its function is to
 * act as a Rest client for upstream services, as Message sender communicating with a message broker, and as a Message
 * Receiver.
 *
 * The purpose of this service is to cover all possible scenarios that can be covered with contract tests.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookDomainService {

    private final BookCatalogService bookCatalogService;
    private final BookOrderDispatcher bookOrderDispatcher;

    private int messagesReceivedCounter;

    /**
     * Fetches books from books catalog.
     *
     * @return List of books
     */
    public List<Book> fetchBooks() {
        return bookCatalogService.getAllBooks();
    }

    /**
     * Deletes a book from the catalog.
     *
     * @param bookId the book to delete
     */
    public void deleteBook(String bookId) {
        bookCatalogService.deleteBook(bookId);
    }

    /**
     * Dispatches a book order.
     *
     * @param orderCommand the order-book command
     */
    public void orderBook(BookOrderCommand orderCommand) {
        bookOrderDispatcher.orderBook(orderCommand);
    }

    /**
     * Callback method for notifications about ordered books.
     *
     * @param event event representing a book order
     */
    public void orderConfirmed(BookOrderedEvent event) {
        messagesReceivedCounter ++;
        log.info("Received order confirmation message {}. {} orders have been created ", event, messagesReceivedCounter);
    }

    /**
     * Dummy method for testing that events were acually received.
     *
     * @return the number of received events by this service instance
     */
    public int getMessagesReceivedCounter() {
        return messagesReceivedCounter;
    }

}
