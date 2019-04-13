package de.sidion.books.catalog.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder =  Book.BookBuilder.class)
@ToString
public class Book {

    int id;
    //String authorFirstName;
    //String authorLastName;
    String title;
    String isbn;
    List<Author> authors;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class BookBuilder {

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author {
        String fistName;
        String lastName;
    }

}
