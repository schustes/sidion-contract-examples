package de.sidion.books.client.contracts;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.MessagePactProviderRule;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.model.v3.messaging.MessagePact;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BookOrderedEventConsumerPactTest {

    @Rule
    public MessagePactProviderRule mockProvider = new MessagePactProviderRule(this);

    private byte[] currentMessage;

    @Pact(provider = "book-order-service",
            consumer = "books-client-book-ordered-event-consumer")
    public MessagePact createPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();

        body.stringType("customerId");
        body.stringMatcher("isbn", "[0-9]{3}-[0-9]{1}-[0-9]{5}-[0-9]{3}-[0-9]{1}", "978-3-86680-192-9");

        Map<String, String> metadata = new HashMap<>();
        MessagePact pact =  builder
                .given("orderCommandReceived()")
                .expectsToReceive("a message sent via order-exchange")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();

        pact.getMessages().get(0).getMetaData().remove("Content-Type"); //header is set by pact-jvm automatically, but not understood by spring-contract

        return pact;
    }

    @Test
    @PactVerification({"book-order-service"})
    public void verify() throws Exception {
        Assert.assertNotNull(new String(currentMessage)); //a verifies is however always needed to generate the pact
    }

    //set by the MessagePactProviderRule via reflection
    public void setMessage(byte[] messageContents) {
        currentMessage = messageContents;
    }

}
