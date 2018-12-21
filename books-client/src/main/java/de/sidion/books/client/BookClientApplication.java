package de.sidion.books.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BookClientApplication {

    public static void main(String[] args) {
		SpringApplication.run(BookClientApplication.class, args);
	}

}
