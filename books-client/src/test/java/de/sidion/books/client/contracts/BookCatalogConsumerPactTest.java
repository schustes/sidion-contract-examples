package de.sidion.books.client.contracts;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import de.sidion.books.client.domain.Book;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BookCatalogConsumerPactTest {

    private static final String ROLE_HEADER = "X-ROLE";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String ROLE_HEADER_VALUE = "unprivileged use";
    private static final String CONTENT_HEADER_VALUE = "application/json;charset=UTF-8";

    @Rule
    public PactProviderRuleMk2 mockProvider =
            new PactProviderRuleMk2("book-catalog-service", "localhost", 8081, this);

    @Pact(consumer="books-client-catalog-rest-consumer")
    public RequestResponsePact pact(PactDslWithProvider builder) throws Exception {


        DslPart dslPart = new PactDslJsonArray()
                .object()
                .integerType("id")
                .stringMatcher("authorFirstName", ".*", "John")
                .stringMatcher("authorLastName", ".*", "Doe")
                .stringMatcher("title", ".*", "A book")
                .stringMatcher("isbn", "[0-9]{3}-[0-9]{1}-[0-9]{5}-[0-9]{3}-[0-9]{1}", "978-3-86680-192-9");

        return builder

                .given("get")
                .uponReceiving("A successful Api GET call")
                .path("/books")
                .headers(expectedRequestHeaders())
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(dslPart)
                .headers(responseHeaders())

                .given("delete-with-insufficient-privileges")
                .uponReceiving("A DELETE request with insufficient privileges")
                .headers(expectedRequestHeaders())
                .matchPath("/books/[0-9]*", "/books/123")
                .method("DELETE")
                .willRespondWith()
                .status(401)

                .toPact();

    }

    private Map<String, String> responseHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=UTF-8");
        return map;
    }

    private Map<String, String> expectedRequestHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put(CONTENT_HEADER, CONTENT_HEADER_VALUE);
        map.put(ROLE_HEADER, ROLE_HEADER_VALUE);
        return map;
    }

    private HttpEntity<?> requestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.put(ROLE_HEADER, Collections.singletonList(ROLE_HEADER_VALUE));
        headers.put(CONTENT_HEADER, Collections.singletonList(CONTENT_HEADER_VALUE));
        return new HttpEntity<>(headers);
    }

    @Test
    @PactVerification
    public void verifyPact() {
        RestTemplate template = new RestTemplate();

        verifyGetReturnsBooks(template);

        verifyDeleteIsPreventedForNormalUser(template);

    }

    private void verifyGetReturnsBooks(RestTemplate template) {

        ResponseEntity<Book[]> response =
                template.exchange("http://localhost:" + 8081 + "/books", HttpMethod.GET, requestHeaders(), Book[].class);

        assertThat(response.getStatusCode().value(), equalTo(200));

        Book[] books = response.getBody();
        assertThat(books, Matchers.notNullValue());
        assertThat(books.length, Matchers.is(1));
        Book book = books[0];
        assertThat(book.getId(), Matchers.isA(String.class));
        assertThat(book.getAuthorFirstName(), Matchers.isA(String.class));
        assertThat(book.getAuthorLastName(), Matchers.isA(String.class));
        assertThat(book.getTitle(), Matchers.isA(String.class));
        assertThat(book.getIsbn().length(), Matchers.is(17));

    }

    private void verifyDeleteIsPreventedForNormalUser(RestTemplate template) {

        try {

            template.exchange("http://localhost:" + 8081 + "/books/123", HttpMethod.DELETE, requestHeaders(), Void.class);

        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("401"));
        }

    }


}
