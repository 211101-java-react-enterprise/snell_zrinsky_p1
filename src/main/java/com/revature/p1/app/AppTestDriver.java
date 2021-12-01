package com.revature.p1.app;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.data.QueryBuilder;
import com.revature.p1.orm.QueryManager;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class AppTestDriver {

    private static final Properties props = new Properties();

    public static void main(String[] args) throws SQLException {
//
//        try (FileReader fr = new FileReader("src/main/java/com/revature/p1/app/resources/db.properties"))
//         {
//            props.load(fr);
//            Connection conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
//            // Map POJO to a relational model
//             QueryManager.setConnection(conn);
//             QueryBuilder bookQueryBuilder = QueryManager.getQueryBuilder(Book.class);
//              ArrayList<Book> books = bookQueryBuilder.createSelectQueryFrom("WHERE id = '433f67b6-46fe-4a09-9cd1-6c0ccd695293'");
//             System.out.println(books.get(0).getTitle());
//            Book insertBook = new Book("The Grandest History of Cheez-Its", "The Baron of Cheddar", 568, "/assets/covers/cheeeeeese.png");
//            bookQueryBuilder.createInsertQueryFrom(insertBook);
//            insertBook.title = "Cheez-Nips: the Nefarious Cousin of -Its";
//            bookQueryBuilder.createUpdateQueryFrom(insertBook);
//             System.out.println("~~~~~~~~ FLAG L.44 ~~~~~~~~");
//            bookQueryBuilder.createDeleteQueryFrom(insertBook);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Feed book in to our ORM

    }
}
