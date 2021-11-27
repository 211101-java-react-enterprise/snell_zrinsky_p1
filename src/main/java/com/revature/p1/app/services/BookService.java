package com.revature.p1.app.services;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.util.AnnotationParser;
// TODO - Replace SqlTable with something more sensical
import com.revature.p1.orm.util.ClassSchema;

// Takes parser, some configuration(not here - elsewhere)

public class BookService {
    ClassSchema bookTable;
    // Where they import/interact with our library
    private void initialize() {
        // Check to see if this exists in SQL. If not, make it.
//        bookTable = AnnotationParser.parse(Book.class);

    }

    // Constructor maybe?

    public void addBook(Book book) {
        // Persist book to database
    }

    public void updateBook(Book book){

    }

    public void getBook(String id){

    }

    public void getAllBooks() {

    }

    public void burnBook(Book book) {

    }
}
