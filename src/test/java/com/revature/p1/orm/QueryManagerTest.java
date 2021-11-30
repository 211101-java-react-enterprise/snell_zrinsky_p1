package com.revature.p1.orm;

import com.revature.p1.orm.annotations.Table;
import com.revature.p1.orm.data.QueryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.mockito.Mockito.*;

public class QueryManagerTest {

    private static final Properties props = new Properties();

    QueryManager sut = new QueryManager();
    // Create a mock class

    // TODO - Test for annotations and their values

    @Test
    public void test_getQueryBuilder_returnsQueryBuilder_GivenValidClass() {
        @Table(name = "test_model")
        class TestModel {
            // This space intentionally left blank
        }

        try (FileReader fr = new FileReader("src/test/resources/db.properties")) {
            props.load(fr);
            Connection conn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
            // Map POJO to a relational model
            QueryManager.setConnection(conn);

            QueryBuilder<TestModel> testQueryBuilder = sut.getQueryBuilder(TestModel.class);

            Assert.assertTrue("Expected a QueryBuilder to be not null",testQueryBuilder != null);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
