package com.revature.p1.orm.util.types;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;

@Table(name = "TEST_MODEL")
public class TestModel {
    @Column(name = "ID")
    public int id;
    @Column(name = "NAME")
    public String name;
    @Column(name = "TEST")
    public String test;
}
