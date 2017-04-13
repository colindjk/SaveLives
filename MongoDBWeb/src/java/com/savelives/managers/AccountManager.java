/*
 * Created by Taiwen Jin on 2017.02.26  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.savelives.managers.Constants.DATABASE_HOSTNAME;
import static com.savelives.managers.Constants.DATABASE_NAME;
import static com.savelives.managers.Constants.DATABASE_PORT;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author taiwenjin
 */
@Named(value = "mongoDBController")
@SessionScoped

public class AccountManager implements Serializable {

    private final MongoClient mongoClient;
    private final MongoDatabase db;
    private final MongoCollection coll;
    private static final String UserCollectionName = "UsersCollection";
    
    private String name;
    private String email;
    private String phone;
    private String major;

    public AccountManager() {
        mongoClient = new MongoClient(DATABASE_HOSTNAME, DATABASE_PORT);
        db = mongoClient.getDatabase(DATABASE_NAME);
        coll = db.getCollection(UserCollectionName);
    }

    public String insertData() {
        BasicDBObject doc = new BasicDBObject("name", name)
                .append("email", email)
                .append("phone", phone)
                .append("major", major);
        coll.insertOne(doc);
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