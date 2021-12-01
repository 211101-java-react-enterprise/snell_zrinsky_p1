package com.revature.p1.app.web.servlets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// @WebServlet("/book")
public class BookServlet extends HttpServlet {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    // doPost -> Create New
    // doGet -> Read
    // doPut -> Update || Replace If Exists
    // doDelete -> Delete

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.getWriter().write("<h1>/book works!</h1>");
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<h1>/book works!</h1>");
    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.getWriter().write("<h1>/book works!</h1>");
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        boolean successfulDelete = false;
//
//        try {
//            Book targetBook = objectMapper.readValue(req.getInputStream(), Book.class);
//            successfulDelete = bookService.deleteBook(targetBook);
//            resp.setStatus(200);
//        } catch (JsonParseException e) {
//            resp.setStatus(406); // 406 -> Not acceptable
//        }
//        resp.getWriter().write("<h1>" + successfulDelete + "</h1>");
//        // String payload = objectMapper.writeValueAsString();
//        // resp.getWriter().write();
//
//    }

}
