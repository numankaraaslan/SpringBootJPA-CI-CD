package com.bilgeadam.springbootrestjpa.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bilgeadam.springbootrestjpa.model.Book;
import com.bilgeadam.springbootrestjpa.service.BookService;

// http://localhost:8080/book
@RestController
@RequestMapping(path = "book")
public class BookController
{
	private BookService bookService;

	public BookController(BookService bookService)
	{
		this.bookService = bookService;
	}

	@GetMapping(path = { "", "/" })
	public ResponseEntity<List<Book>> index()
	{
		// http://localhost:8080/book
		List<Book> books = bookService.findAll();
		return ResponseEntity.ok(books);
	}

	@PostMapping(path = "save")
	public ResponseEntity<String> addbook(@RequestBody Book newBook)
	{
		// http://localhost:8080/book/save
		newBook = bookService.save(newBook);
		if (newBook != null)
		{
			return ResponseEntity.created(URI.create("localhost:8080/book/bookdetail/" + newBook.getId())).body("Successfully saved");
		}
		else
		{
			return ResponseEntity.internalServerError().body("Error happened");
		}
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<Book> getbookdetail(@PathVariable(name = "id") int id)
	{
		// http://localhost:8080/book/1
		Book book = bookService.findById(id);
		if (book != null)
		{
			return ResponseEntity.ok(book);
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "findbyname")
	public ResponseEntity<List<Book>> findbyname(@RequestParam(name = "bookName", required = true) String bookName)
	{
		// http://localhost:8080/book/findbyname?bookName=madonna
		List<Book> books = bookService.findByNameLike(bookName, Sort.by(Order.asc("name")));
		return ResponseEntity.ok(books);
	}

	@GetMapping(path = "findbyauthor")
	public ResponseEntity<List<Book>> findbyauthor(@RequestHeader(name = "authorName", required = true) String authorName)
	{
		// http://localhost:8080/book/findbyauthor (authorName - sabahattin)
		List<Book> books = bookService.findByAuthorName(authorName);
		return ResponseEntity.ok(books);
	}

	@DeleteMapping(path = "deletebook/{id}")
	public ResponseEntity<String> deletebook(@PathVariable(name = "id") int id)
	{
		// http://localhost:8080/book/deletebook/1
		Book result = bookService.findById(id);
		if (result != null)
		{
			bookService.deleteByID(id);
			return ResponseEntity.ok("Successfully deleted");
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}

}