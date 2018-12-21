package de.sidion.books.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = BookOrderedEvent.BookOrderedEventBuilder.class)
@ToString
public class BookOrderedEvent {

    String isbn;
    String customerId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookOrderedEventBuilder {

    }

}
