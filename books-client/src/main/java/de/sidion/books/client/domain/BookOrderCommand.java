package de.sidion.books.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = BookOrderCommand.BookOrderCommandBuilder.class)
@ToString
public class BookOrderCommand {
    String customerId;
    String bookId;
    String isbn;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class BookOrderCommandBuilder {

    }
}
