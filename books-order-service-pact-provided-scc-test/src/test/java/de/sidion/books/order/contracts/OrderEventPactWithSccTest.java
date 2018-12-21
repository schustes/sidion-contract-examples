package de.sidion.books.order.contracts;

import de.sidion.books.common.PactPublisherRule;
import de.sidion.books.order.BooksOrderApplication;
import de.sidion.books.order.domain.BookOrderDomainService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BooksOrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class OrderEventPactWithSccTest {

    @Rule
    public PactPublisherRule pactPublisherRule = new PactPublisherRule();

    @Autowired
    private BookOrderDomainService service;

    @Autowired
    private Environment env;

    @Before
    public void init() {
        pactPublisherRule.configure(env);
    }

    protected void orderCommandReceived() {
        service.createBookOrder("1", "2123", "978-3-86680-192-9");
    }


}