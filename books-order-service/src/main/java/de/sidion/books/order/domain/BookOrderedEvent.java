package de.sidion.books.order.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@ToString
@Builder
@JsonDeserialize(builder =  BookOrderedEvent.BookOrderedEventBuilder.class)
public class BookOrderedEvent {

    String isbn;
    String customerId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookOrderedEventBuilder {

    }

    public static BookOrderedEvent of(BookOrder order) {
        return new BookOrderedEvent(order.getIsbn(), order.getCustomerId());
    }

}
