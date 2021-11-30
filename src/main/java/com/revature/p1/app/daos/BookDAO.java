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
}
