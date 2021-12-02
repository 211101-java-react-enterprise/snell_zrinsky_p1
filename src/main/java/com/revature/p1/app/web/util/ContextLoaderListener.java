package com.revature.p1.app.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1.app.daos.BookDAO;
import com.revature.p1.app.models.Book;
import com.revature.p1.app.services.BookService;
import com.revature.p1.app.web.servlets.BookServlet;
import com.revature.p1.orm.QueryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ContextLoaderListener implements ServletContextListener {

    public final static Logger logger = LogManager.getLogger();
    private static Connection conn;
    private static final Properties props = new Properties();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("init");
//        logger.info("Initializing application");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            Class.forName("org.postgresql.Driver");
//            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//            Properties props = new Properties();
//            props.load(classLoader.getResourceAsStream("db.properties"));
//            conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
//            // Map POJO to a relational model
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return;
//        }

//        QueryManager.setConnection(conn);
//        BookDAO bookDAO = new BookDAO(QueryManager.getQueryBuilder(Book.class));
//        BookService bookService = new BookService(bookDAO);
//        BookServlet bookServlet = new BookServlet(bookService, objectMapper);
//
//        ServletContext context = sce.getServletContext();
//        context.addServlet("BookServlet", bookServlet).addMapping("/books");

//        logger.info("Application initialized");
//        System.out.println("done init");
    }
}
