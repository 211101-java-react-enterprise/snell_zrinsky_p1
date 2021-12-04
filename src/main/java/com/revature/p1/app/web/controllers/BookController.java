package com.revature.p1.app.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;
import com.revature.p1.app.web.dtos.BooksRequest;
import com.revature.p1.app.web.dtos.BooksResponse;
import com.revature.p1.orm.QueryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController() {
        ObjectMapper objectMapper = new ObjectMapper();
        BookService bookService;

        try {
            Class.forName("org.postgresql.Driver");
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Properties props = new Properties();
            props.load(classLoader.getResourceAsStream("db.properties"));
            Connection conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
            QueryManager.setConnection(conn);
            BookDAO bookDAO = new BookDAO(QueryManager.getQueryBuilder(Book.class));
            this.bookService = new BookService(bookDAO);
        } catch (Exception e) {
            // logger.error(e.getMessage());
            return;
        }

    }

    @GetMapping(value = "/{uuid}")
    public BooksResponse getBooksById(@PathVariable String uuid) {
        System.out.println(uuid);
        List<Book> selected = bookService.getBookById(uuid);
        System.out.println(selected);
        Book selectedBook = selected.get(0);
        System.out.println(selectedBook.toString());
        return new BooksResponse(selectedBook.getId(), selectedBook.getTitle(), selectedBook.getAuthor(), selectedBook.getPageCount(), selectedBook.getCoverImage());
    }

    @PostMapping(value = "/")
    public BooksResponse insertBook(@RequestBody BooksRequest booksRequest) {
        System.out.println(booksRequest);
        // Retrieve info from booksRequest
        Book insertedBook = new Book(booksRequest.getUuid(), booksRequest.getTitle(), booksRequest.getAuthor(), booksRequest.getPageCount(), booksRequest.getCoverImage());
        // Do the update logic from bookService
        if (bookService.insertBook(insertedBook)) {
            return new BooksResponse(insertedBook.getId(), insertedBook.getTitle(), insertedBook.getAuthor(), insertedBook.getPageCount(), insertedBook.getCoverImage());
        } else {
            return null;
        }
    }

    @PutMapping(value = "/{uuid}")
    public BooksResponse updateBookById(@PathVariable String uuid, @RequestBody BooksRequest booksRequest) {
        System.out.println(booksRequest);
        // Retrieve info from booksRequest
        Book updatedBook = new Book(uuid, booksRequest.getTitle(), booksRequest.getAuthor(), booksRequest.getPageCount(), booksRequest.getCoverImage());
        // Do the update logic from bookService
        if (bookService.updateBook(updatedBook)) {
            return new BooksResponse(uuid, updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getPageCount(), updatedBook.getCoverImage());
        } else {
            return null;
        }
    }
    // TODO - Potentially overload so you don't have to pass in the UUID through URL

    // TODO - Dusting
    @DeleteMapping(value = "/{uuid}")
    public String deleteResponse(@PathVariable String uuid) {
        Book bookToDelete = new Book();
        bookToDelete.setId(uuid);
        this.bookService.deleteBook(bookToDelete);

        return "The UUID to delete is " + uuid;

    }
}
