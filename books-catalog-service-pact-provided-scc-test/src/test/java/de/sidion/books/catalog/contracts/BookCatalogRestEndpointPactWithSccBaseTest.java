package de.sidion.books.catalog.contracts;

import de.sidion.books.catalog.BookCatalogApplication;
import de.sidion.books.catalog.adapter.BookCatalogRestEndpoint;
import de.sidion.books.common.PactPublisherRule;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookCatalogApplication.class)
public abstract class BookCatalogRestEndpointPactWithSccBaseTest {

    @Autowired
    BookCatalogRestEndpoint endpoint;

    @Autowired
    private Environment env;

    @Rule
    public PactPublisherRule pactPublisherRule = new PactPublisherRule();

    @Before
    public void init() {
        pactPublisherRule.configure(env);
    }

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(endpoint);
    }

}