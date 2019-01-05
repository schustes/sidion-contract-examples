package de.sidion.books.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@EnableRabbit
@SpringBootApplication
public class BooksOrderApplication {

	/*
	@Component
	@RequiredArgsConstructor
	public class AppStartupRunner implements ApplicationRunner {

		private final BookOrderEventRabbitSender bookOrderEventRabbitSender;

		@Override
		public void run(ApplicationArguments args) {
			bookOrderEventRabbitSender.sendTestMessage();
		}
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(BooksOrderApplication.class, args);
	}
}
