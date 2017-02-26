/*
 * Created by Taiwen Jin on 2017.02.26  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.taiwenjin.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author taiwenjin
 */
@Named(value = "mongoDBController")
@SessionScoped

public class MongoDBController implements Serializable {

    private final MongoClient mongoClient;
    private final DB db;
    private final DBCollection coll;

    private String name;
    private String email;
    private String phone;
    private String major;

    public MongoDBController() {
        mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("TestDatabase");
        coll = db.getCollection("UserInfoCollection");
    }

    public String insertData() {
        BasicDBObject doc = new BasicDBObject("name", name)
                .append("email", email)
                .append("phone", phone)
                .append("major", major);
        coll.insert(doc);
        return "results?faces-redirect=true";
    }
    
    public String clearValues() {

        name = "";
        email = "";
        phone = "";
        major = "";
        /* Return the name of the XHTML page to show */
        return "index?faces-redirect=true";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

}
