package de.sidion.books.order.contracts;

import de.sidion.books.order.BooksOrderApplication;
import de.sidion.books.order.domain.BookOrderDomainService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BooksOrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class OderEventSccOnlyTest {

    @Autowired
    private BookOrderDomainService service;

    protected void bookOrdered() {
        service.createBookOrder("1", "2123", "978-3-86680-192-9");
    }
}