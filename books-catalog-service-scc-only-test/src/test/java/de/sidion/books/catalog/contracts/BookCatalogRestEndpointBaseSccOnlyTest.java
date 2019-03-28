package de.sidion.books.catalog.contracts;

import de.sidion.books.catalog.BookCatalogApplication;
import de.sidion.books.catalog.adapter.BookCatalogRestEndpoint;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BookCatalogApplication.class)
public abstract class BookCatalogRestEndpointBaseSccOnlyTest {

    @Autowired
    private BookCatalogRestEndpoint endpoint;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(endpoint);
    }

}