package com.revature.p1.app.models;

import com.revature.p1.orm.annotations.Column;

@Table(name="app_user")
public class AppUser extends OrmTable {
    @Column
    String name;
    @Column
    String password;
}



