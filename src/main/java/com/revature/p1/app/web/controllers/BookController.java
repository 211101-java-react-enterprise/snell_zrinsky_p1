package com.revature.p1.app.web.controllers;

import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;
import com.revature.p1.app.web.dtos.BooksRequest;
import com.revature.p1.app.web.dtos.BooksResponse;
import com.revature.p1.orm.QueryManager;
import com.revature.p1.orm.data.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController() {
        try {
            Class.forName("org.postgresql.Driver");
            QueryManager queryManager = QueryManager.configure("db.properties");
            QueryBuilder<Book> bookBuilder = queryManager.getQueryBuilder(Book.class);
            BookDAO bookDAO = new BookDAO(bookBuilder);
            this.bookService = new BookService(bookDAO);
        } catch (Exception e) {
            e.printStackTrace();
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
        System.out.println("Book Request: " + booksRequest);
        // Retrieve info from booksRequest
        Book insertedBook = new Book(booksRequest.getTitle(), booksRequest.getAuthor(), booksRequest.getPageCount(), booksRequest.getCoverImage());
        System.out.println("Inserted Book: " + insertedBook);
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

    @DeleteMapping(value = "/{uuid}")
    public String deleteResponse(@PathVariable String uuid) {
        Book bookToDelete = new Book();
        bookToDelete.setId(UUID.fromString(uuid).toString());
        this.bookService.deleteBook(uuid);
        return "The UUID to delete is " + uuid;
    }
}
