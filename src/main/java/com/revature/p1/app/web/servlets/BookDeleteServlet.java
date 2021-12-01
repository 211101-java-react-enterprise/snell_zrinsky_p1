package com.revature.p1.app.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1.app.services.BookService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BookDeleteServlet extends HttpServlet {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookDeleteServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {


    }
}
