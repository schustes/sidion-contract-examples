package de.sidion.books.catalog.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookDomainService {

    private final BookRepository repository;

    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    public boolean deleteBook(String role, int id) {
        if (!role.equalsIgnoreCase("SUPER_USER")) {
           throw new UserNotAuthorizedException("Only a SUPER_USER is allowed to delete books");
        }
        return repository.delete(id);
    }
}
