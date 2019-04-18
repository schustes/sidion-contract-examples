package de.sidion.books.catalog.contracts;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRestPactRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("book-catalog-service")
@PactBroker(host="localhost", port="80", protocol = "http")
public class BookCatalogRestEndpointPactOnlyTest {

    //Http Client
    @TestTarget
    public final Target target = new HttpTarget(8087);

    @Test
    @State("get")
    public void verifySuccessfulGet() {
        //here code could be added to provide the state for this interaction, for example adding test data.
    }

    @Test
    @State("delete-with-insufficient-privileges")
    public void verifyUnsuccessfulDelete() {
        //here code could be added to provide the state for this interaction, for example adding test data.
    }

}