package de.sidion.books.order.adapter;

import de.sidion.books.order.domain.BookOrder;
import de.sidion.books.order.domain.BookOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookOrderRepositoryInMemoryBackend implements BookOrderRepository {

    private Map<String, BookOrder> orders = new HashMap<>();

    @Override
    public List<BookOrder> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public BookOrder createOrder(String customerId, String bookId, String isbn) {
        BookOrder order = BookOrder.of(bookId, isbn, customerId);
        orders.put(order.getBookId(), order);
        return order;
    }

}
