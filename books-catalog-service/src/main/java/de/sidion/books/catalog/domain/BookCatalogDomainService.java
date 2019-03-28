package de.sidion.books.catalog.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is the central service of the book catalog context.
 */
@Service
@RequiredArgsConstructor
public class BookCatalogDomainService {

    private final BookRepository repository;

    /**
     * Get all books.
     *
     * @return the list of books
     */
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    /**
     * Deletes a book. Throws a #UserNotAuthorizedException if the user has insufficient privileges to do so.
     *
     * @param role the role of the user calling this method
     * @param id the book id
     * @return true if book was deleted, false otherwise
     */
    public boolean deleteBook(String role, int id) {
        if (!role.equalsIgnoreCase("SUPER_USER")) {
           throw new UserNotAuthorizedException("Only a SUPER_USER is allowed to delete books");
        }
        return repository.delete(id);
    }
}
