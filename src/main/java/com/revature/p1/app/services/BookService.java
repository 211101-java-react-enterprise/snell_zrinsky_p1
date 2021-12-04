package com.revature.p1.app.services;

import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;

import java.util.List;

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
        if(uuid.length() != 36) return null;
        return bookDAO.getById(uuid);
    }

    public boolean insertBook(Book book) {
        if(!isBookValid(book)) return false;
        return bookDAO.insert(book);
    }

    public boolean updateBook(Book book) {
        if(!isBookValid(book)) return false;
        return bookDAO.update(book);
    }

    public boolean deleteBook(Book book) {
        if(!isBookValid(book)) return false;
        return bookDAO.delete(book);
    }

    public boolean isBookValid(Book book) {
        if (book == null) return false;
        if (book.getTitle() == null || book.getTitle().trim().equals("")) return false;
        if (book.getAuthor() == null || book.getAuthor().trim().equals("")) return false;
        if (book.getPageCount() == null || book.getPageCount() < 0) return false;

        return true;
    }
}
