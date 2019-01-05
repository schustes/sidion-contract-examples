package de.sidion.books.client.adapter;

import de.sidion.books.client.domain.Book;
import de.sidion.books.client.domain.BookCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookRestUpstreamService implements BookCatalogService {

    private static final String UPSTREAM_SCHEME = "http";
    private static final String UPSTREAM_HOST = "localhost";

    private static final String ROLE_HEADER = "X-ROLE";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String ROLE_HEADER_VALUE = "unprivileged use";
    private static final String CONTENT_HEADER_VALUE = "application/json;charset=UTF-8";

    private final RestTemplate restTemplate;

    @Value("${catalog.port}")
    private int upstreamPort;

    @Override
    public List<Book> getAllBooks() {
        String url = UPSTREAM_SCHEME + "://" + UPSTREAM_HOST + ":" + upstreamPort + "/books";

        return Arrays.asList(restTemplate.exchange(url, HttpMethod.GET, createHeaders(), Book[].class).getBody());

    }

    @Override
    public void deleteBook(String id) {
        String url = UPSTREAM_SCHEME + "://" + UPSTREAM_HOST + ":" + upstreamPort + "/book/" + id;

        restTemplate.exchange(url, HttpMethod.DELETE, createHeaders(), Void.class);

    }

    private HttpEntity<?> createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(ROLE_HEADER, Collections.singletonList(ROLE_HEADER_VALUE));
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(CONTENT_HEADER_VALUE));
        headers.put(HttpHeaders.ACCEPT, Collections.singletonList("application/json"));
        return new HttpEntity<>(headers);
    }
}
