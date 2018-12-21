package de.sidion.books.order;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@EnableRabbit
@SpringBootApplication
@RestController
public class BooksOrderApplication {


	private static final String ORDER_EXCHANGE = "order-exchange";
	private static final String BOOK_ORDER_QUEUE = "order-test-queue";

	@Bean
	public Queue orderQueue() {
		return new Queue(BOOK_ORDER_QUEUE);
	}

	@Bean("order-exchange")
	public TopicExchange orderExchange() {
		return new TopicExchange(ORDER_EXCHANGE);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("orders.books.#");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	private MessageConverter jsonMessageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());

		return new Jackson2JsonMessageConverter(objectMapper);
	}

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
