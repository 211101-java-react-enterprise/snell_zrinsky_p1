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

    public List<Book> findAllBooks() {
        return bookDAO.findAll();
    }
}
