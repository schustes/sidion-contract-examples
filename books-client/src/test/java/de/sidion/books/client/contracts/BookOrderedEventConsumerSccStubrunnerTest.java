package de.sidion.books.client.contracts;

import de.sidion.books.client.domain.BookDomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "de.sidion.books:books-order-service-scc-only-test")
@DirtiesContext
public class BookOrderedEventConsumerSccStubrunnerTest {

    @Autowired
    StubTrigger stubTrigger;

    @Autowired
    BookDomainService service;

    @Test
    public void verifyBookOrderedEventContract() throws Exception {

        int counterBefore = service.getMessagesReceivedCounter();
        stubTrigger.trigger("book-order");
        int counterAfter = service.getMessagesReceivedCounter();

        assertThat(counterAfter, equalTo(counterBefore + 1));
    }

}
