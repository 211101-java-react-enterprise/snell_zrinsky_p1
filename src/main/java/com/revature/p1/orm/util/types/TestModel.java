package com.revature.p1.orm.util.types;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.types.ColumnType;
import com.revature.p1.orm.annotations.Table;

@Table(name = "TEST_MODEL")
public class TestModel {
    @Column(name = "ID", type=ColumnType.ID, isUnique = true)
    public int id;
    @Column(name = "NAME", type=ColumnType.STRING)
    public String name;
    @Column(name = "HAS_COOL_SHADES_YO", type=ColumnType.BOOLEAN, isNullable=true)
    public boolean hasCoolShadesYo = true;
}
