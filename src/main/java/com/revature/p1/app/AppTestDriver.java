package com.revature.p1.app;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.util.ClassSchema;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class AppTestDriver {

    private static final Properties props = new Properties();

    public static void main(String[] args) throws SQLException {

        try (FileReader fr = new FileReader("src/main/java/com/revature/p1/app/resources/db.properties"))
         {
            props.load(fr);
            Connection conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
//        Book testBook1 = new Book("one", "Book the First", "Sir Frampton", 984, "/assets/covers/btf.png");
//        Book testBook2 = new Book("two", "Book the Second", "Sir Frampton", 2258, "/assets/covers/btt.png");
//        Book testBook3 = new Book("three", "Everybody Poops", "Angela Somethingorother", 16, "/assets/covers/ep.png");
            ClassSchema.setConnection(conn);
            ClassSchema bookSchema = new ClassSchema(Book.class);

            Book newBook = new Book();
            Book bookInstance = (Book)bookSchema.getNewInstanceFromIdInDatabase(newBook, "433f67b6-46fe-4a09-9cd1-6c0ccd695293");

            System.out.println(bookInstance.title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Feed book in to our ORM

    }
}
