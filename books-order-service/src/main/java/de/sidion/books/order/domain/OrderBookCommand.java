package de.sidion.books.order.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = OrderBookCommand.OrderBookCommandBuilder.class)
@ToString
public class OrderBookCommand {
    String customerId;
    String bookId;
    String isbn;

    @JsonPOJOBuilder(withPrefix = "")
    public static class OrderBookCommandBuilder {

    }
}
