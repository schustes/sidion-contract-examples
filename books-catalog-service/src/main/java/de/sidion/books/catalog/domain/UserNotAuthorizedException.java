package de.sidion.books.catalog.domain;

public class UserNotAuthorizedException extends  RuntimeException {
    public  UserNotAuthorizedException(String message) {
        super(message);
    }
}
