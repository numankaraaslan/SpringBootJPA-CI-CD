package com.bilgeadam.springbootrestjpa;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bilgeadam.springbootrestjpa.model.Book;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class SpringbootTests
{
	@Autowired
	public TestRestTemplate template;

	@Test
	@Order(value = 2)
	void getbyidtest()
	{
		String url = "http://localhost:8080/book";
		ResponseEntity<Book[]> result = template.getForEntity(url, Book[].class);
		Book lastBook = result.getBody()[result.getBody().length - 1];
		url = "http://localhost:8080/book/" + lastBook.getId();
		Book result2 = template.getForEntity(url, Book.class).getBody();
		Assertions.assertEquals("someisbn", result2.getIsbn());
		Assertions.assertEquals(2025, result2.getYear());
	}

	@Test
	@Order(value = 1)
	void savetest()
	{
		String url = "http://localhost:8080/book/save";
		Book newBook = new Book("book" + new Random().nextInt(10000), "someisbn", 2025, "author" + new Random().nextInt(10000));
		ResponseEntity<String> result = template.postForEntity(url, newBook, String.class);
		Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
	}
}