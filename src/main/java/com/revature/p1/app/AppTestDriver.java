package com.revature.p1.app;

import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;
import com.revature.p1.orm.util.AnnotationParser;

public class AppTestDriver {

    public static void main(String[] args) {

        Book testBook1 = new Book("one", "Book the First", "Sir Frampton", 984, "/assets/covers/btf.png");
        Book testBook2 = new Book("two", "Book the Second", "Sir Frampton", 2258, "/assets/covers/btt.png");
        Book testBook3 = new Book("three", "Everybody Poops", "Angela Somethingorother", 16, "/assets/covers/ep.png");
        BookService bookService = new BookService();

        // Feed book in to our ORM

    }
}
