package com.revature.p1.orm;

import com.revature.p1.orm.util.AnnotationParser;
import com.revature.p1.orm.util.types.SqlTable;
import com.revature.p1.orm.util.types.TestModel;

public class OrmDriver {

    public static void main(String[] args) {
        TestModel test = new TestModel();
        SqlTable sqlTest = new AnnotationParser(test).generateSqlTable();
        System.out.println(sqlTest.getName());
    }
}
