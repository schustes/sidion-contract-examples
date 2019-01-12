package de.sidion.books.client.contracts;

import de.sidion.books.client.BookClientApplication;
import de.sidion.books.client.adapter.BookRestUpstreamService;
import de.sidion.books.client.domain.Book;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BookClientApplication.class)
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, minPort = 9999, maxPort = 9999,
        ids = "de.sidion.books:books-catalog-service-scc-only-test")
public class BookCatalogConsumerSccStubrunnerTest {

    @Autowired
    BookRestUpstreamService bookService;

    @Test
    public void verifyBookCatalogGetAllBooksContract() throws Exception {

        List<Book> books = bookService.getAllBooks();

        assertThat(books, Matchers.notNullValue());

        Book book = books.get(0);
        assertThat(book.getId(), Matchers.isA(String.class));
        assertThat(book.getAuthorFirstName(), Matchers.isA(String.class));
        assertThat(book.getAuthorLastName(), Matchers.isA(String.class));
        assertThat(book.getTitle(), Matchers.isA(String.class));
        assertThat(book.getIsbn().length(), Matchers.is(17));


    }

}
