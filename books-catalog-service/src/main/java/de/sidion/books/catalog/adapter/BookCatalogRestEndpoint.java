package de.sidion.books.catalog.adapter;

import de.sidion.books.catalog.domain.Book;
import de.sidion.books.catalog.domain.BookCatalogDomainService;
import de.sidion.books.catalog.domain.UserNotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookCatalogRestEndpoint {

    private final BookCatalogDomainService bookCatalogDomainService;

    @GetMapping
    public List<Book> getAllBooks() {
        List<Book> books = bookCatalogDomainService.getAllBooks();
        return books;
    }

    @DeleteMapping(path = "{id}")
    public void deleteBook(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            bookCatalogDomainService.deleteBook(request.getHeader("X-ROLE"), id);
        } catch (UserNotAuthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    private class NotFoundException extends RuntimeException {
    }
}
