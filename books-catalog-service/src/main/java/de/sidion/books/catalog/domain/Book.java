package de.sidion.books.catalog.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder =  Book.BookBuilder.class)
@ToString
public class Book {

    int id;
    String authorFirstName;
    String authorLastName;
    String title;
    String isbn;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class BookBuilder {

    }

}
