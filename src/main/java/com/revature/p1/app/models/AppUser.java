package com.revature.p1.app.models;

import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;

@Table(name="app_user")
public class AppUser {
    @Column(name="NAME")
    String name;
    @Column(name="PASSWORD")
    String password;
}



