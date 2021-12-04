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

    private BookService bookService;
    private ObjectMapper objectMapper;

    public BookServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

//    public BookServlet(){
//        super();
//    }

    // doGet -> Read
    // doPost -> Create New
    // doPut -> Update || Replace If Exists
    // doDelete -> Delete

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("The other anything");
        resp.getWriter().write("<h1>/book works!</h1>");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("So many anything");
        boolean successfulDelete = false;
        resp.getWriter().write("Literally anything hahahahaha");
        resp.setContentType("application/json");

        try {
            Book targetBook = objectMapper.readValue(req.getInputStream(), Book.class);
            // resp.getWriter().write(targetBook.getTitle());
            successfulDelete = bookService.deleteBook(targetBook);
            resp.setStatus(200);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(406); // 406 -> Not acceptable

        }
        resp.getWriter().write("Object deletion is " + successfulDelete);
        // String payload = objectMapper.writeValueAsString();
        // resp.getWriter().write();

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("So many anything");
        boolean successfulUpdate = false;
        resp.getWriter().write("Literally anything hahahahaha");
        resp.setContentType("application/json");

        try {
            Book targetBook = objectMapper.readValue(req.getInputStream(), Book.class);
            // resp.getWriter().write(targetBook.getTitle());
            successfulUpdate = bookService.updateBook(targetBook);
            resp.setStatus(200);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(406); // 406 -> Not acceptable

        }
        resp.getWriter().write("Object deletion is " + successfulUpdate);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("So many anything");
        boolean successfulInsert = false;
        resp.getWriter().write("Literally anything hahahahaha");
        resp.setContentType("application/json");

        try {
            Book targetBook = objectMapper.readValue(req.getInputStream(), Book.class);
            // resp.getWriter().write(targetBook.getTitle());
            successfulInsert = bookService.insertBook(targetBook);
            resp.setStatus(200);
        } catch (Exception e) {
            resp.getWriter().write(e.getMessage());
            resp.setStatus(406); // 406 -> Not acceptable

        }
        resp.getWriter().write("Object deletion is " + successfulInsert);

    }
}
