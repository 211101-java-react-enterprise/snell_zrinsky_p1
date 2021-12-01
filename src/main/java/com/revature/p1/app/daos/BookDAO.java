package com.revature.p1.app.daos;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.data.QueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.Query;
import java.util.List;

public class BookDAO {

    private final QueryBuilder bookQueryBuilder;
    public static final Logger logger = LogManager.getLogger();

    public BookDAO(QueryBuilder bookQueryBuilder) {
        this.bookQueryBuilder = bookQueryBuilder;
    }

    public List<Book> findAll() {
        return bookQueryBuilder.createSelectQueryFrom("");
    }

    public List<Book> getById(String uuid) {
        return bookQueryBuilder.createSelectQueryFrom("WHERE id = " + uuid);
    }

    public boolean insert(Book book) {
        // TODO - Check book's UUID is unique
        return bookQueryBuilder.createInsertQueryFrom(book) > 0;
    }

    public boolean update(Book book) {
        return bookQueryBuilder.createUpdateQueryFrom(book) > 0;
    }

    public boolean delete(Book book) {
        return bookQueryBuilder.createDeleteQueryFrom(book) > 0;
    }
}
