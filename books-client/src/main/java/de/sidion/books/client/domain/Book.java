package de.sidion.books.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder =  Book.BookBuilder.class)
public class Book {

    String id;
    String authorFirstName;
    String authorLastName;
    String title;
    String isbn;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class BookBuilder {

    }

}