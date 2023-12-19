package com.tekeoglan.model;

public class Operator extends User {
    public Operator() {
       super();
    }
    public Operator(int id, String fullName, String userName, String password, UserType type) {
        super(id, fullName, userName, password, type);
    }
}
