package com.revature.p1.app;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.util.ClassSchema;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
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

            // Retrieving SQL data, mapping to a POJO
            // TODO - Build out ability of user to query through our nonsense
            Book newBook = new Book();
            Book bookInstance = (Book)bookSchema.getNewInstanceFromIdInDatabase("433f67b6-46fe-4a09-9cd1-6c0ccd695293");

            // Map POJO to a relational model
            Book writingBook = new Book("A Grand History of Cheez-Its", "The Baron of Cheddar", 568, "/assets/covers/cheeeeeese.png");
            bookSchema.insertNewDatabaseRecord(writingBook);
            bookInstance.title = "Upd8ted T!TL3";
            bookSchema.updateRecord(bookInstance, bookInstance.id);

            System.out.println(bookInstance.title);

            List<Object> result = bookSchema.createQuery("WHERE title = 'Upd8ted T!TL3'");
            System.out.println(result.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Feed book in to our ORM

    }
}
