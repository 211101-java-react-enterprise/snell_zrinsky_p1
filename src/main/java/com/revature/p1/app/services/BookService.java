package com.revature.p1.app.services;

import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
// TODO - Replace SqlTable with something more sensical
import com.revature.p1.orm.util.ClassSchema;

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
        return bookDAO.getById(uuid);
    }

    public boolean insertBook(Book book) {
        return bookDAO.insert(book);
    }

    public boolean updateBook(Book book) {
        return bookDAO.update(book);
    }

    public boolean deleteBook(Book book) {
        return bookDAO.delete(book);
    }
}
