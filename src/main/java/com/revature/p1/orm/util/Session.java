package com.revature.p1.orm.util;

import com.revature.p1.app.models.Book;
import com.revature.p1.orm.util.types.SqlColumn;

import java.util.Collections;
import java.util.List;

public class Session {

    private static SessionConfiguration config;
    
    private ClassSchema classSchema;
    
    public static void setConfig(SessionConfiguration sessionConfiguration) {
        Session.config = sessionConfiguration;
    }

    public Session(ClassSchema classSchema) {
        this.classSchema = classSchema;
    }

    public void update(Object obj) {
        System.out.println(obj.toString());
    }

    public void delete(Object obj) {}

    // Employee employee = (Employee)session.get(Employee.class, EmployeeID);
    // TODO: implement ID wrapper class(?)
    public Object get(String objID) {
        return null;
    }

    public List createQuery(String query) {
        return Collections.emptyList();
    }
}
