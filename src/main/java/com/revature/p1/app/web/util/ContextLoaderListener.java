package com.revature.p1.app.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;
import com.revature.p1.app.web.servlets.BookServlet;
import com.revature.p1.orm.QueryManager;
import com.revature.p1.orm.data.QueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {

    public static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("init");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            QueryManager queryManager = QueryManager.configure("db.properties");
            QueryBuilder<Book> bookBuilder = queryManager.getQueryBuilder(Book.class);
            BookDAO bookDAO = new BookDAO(bookBuilder);
            BookService bookService = new BookService(bookDAO);
            BookServlet bookServlet = new BookServlet(bookService, objectMapper);
            ServletContext context = sce.getServletContext();
            context.addServlet("BookServlet", bookServlet).addMapping("/books");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return;
        }
        System.out.println("done init");
    }
}
