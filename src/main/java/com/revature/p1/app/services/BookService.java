package com.revature.p1.app.services;

import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;

import java.util.List;
import java.util.UUID;

// Takes parser, some configuration(not here - elsewhere)

public class BookService {

    private final BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    public List<Book> getBookById(String uuid) {
        try {
            return bookDAO.getById(UUID.fromString(uuid).toString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean insertBook(Book book) {
        if(!isBookValid(book)) return false;
        return bookDAO.insert(book);
    }

    public boolean updateBook(Book book) {
        if(!isBookValid(book)) return false;
        return bookDAO.update(book);
    }

    public boolean deleteBook(String uuid) {
        try {
            return bookDAO.delete(UUID.fromString(uuid).toString());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean isBookValid(Book book) {
        if(book.getTitle() == null || book.getTitle().isEmpty()) return false;
        if(book.getAuthor() == null || book.getAuthor().isEmpty()) return false;
        if(book.getCoverImage() == null || book.getCoverImage().isEmpty()) return false;
        return book.getPageCount() > 0;
    }
}
