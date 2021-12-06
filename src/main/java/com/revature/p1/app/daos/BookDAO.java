package com.revature.p1.app.daos;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.data.QueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BookDAO {

    public static final Logger logger = LogManager.getLogger();
    private final QueryBuilder<Book> bookQueryBuilder;

    public BookDAO(QueryBuilder bookQueryBuilder) {
        this.bookQueryBuilder = bookQueryBuilder;
    }

    public List<Book> findAll() {
        return bookQueryBuilder.createSelectQueryFrom("");
    }

    public List getById(String uuid) {
        return bookQueryBuilder.createSelectQueryFrom(uuid);
    }

    public boolean insert(Book book) {
        // TODO - Check book's UUID is unique
        return bookQueryBuilder.createInsertQueryFrom(book) > 0;
    }

    public boolean update(Book book) {
        return bookQueryBuilder.createUpdateQueryFrom(book) > 0;
    }

    public boolean delete(String uuid) {
        return bookQueryBuilder.createDeleteQueryFrom(uuid) > 0;
    }
}
