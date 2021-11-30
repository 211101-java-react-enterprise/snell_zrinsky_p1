package com.revature.p1.app.web.servlets;

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

@WebServlet("/book")
public class BookServlet extends HttpServlet {

    private final BookService bookService;
    private final ObjectMapper mapper;

    public BookServlet(BookService bookService, ObjectMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        List<Book> books;

        HttpSession session = req.getSession(false);
        books = bookService.findAllBooks();

        if (books.isEmpty()) {
            resp.setStatus(404);
            return;
        }

        String payload = mapper.writeValueAsString(books);
        resp.getWriter().write(payload);
    }

}
