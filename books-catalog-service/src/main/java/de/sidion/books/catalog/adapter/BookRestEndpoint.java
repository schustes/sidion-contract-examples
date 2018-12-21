package de.sidion.books.catalog.adapter;

import de.sidion.books.catalog.domain.BookDomainService;
import de.sidion.books.catalog.domain.Book;
import de.sidion.books.catalog.domain.UserNotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookRestEndpoint {

    private final BookDomainService bookDomainService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookDomainService.getAllBooks();
    }

    @DeleteMapping(path = "{id}")
    public void deleteBook(HttpServletRequest request, @PathVariable("id") int id) {
        try {
            bookDomainService.deleteBook(request.getHeader("X-ROLE"), id);
        } catch (UserNotAuthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
