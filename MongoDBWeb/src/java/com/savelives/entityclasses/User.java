/*
 * Created by Pingxin Shang on 2017.03.13  * 
 * Copyright © 2017 Pingxin Shang. All rights reserved. * 
 */
package com.savelives.entityclasses;

import org.bson.Document;

/*
 * @author ping
 */
public class User {

    private String id;
    private String username;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private int securityQuestion;
    private String securityAnswer;
    private String email;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }
    /**
     * Custom Constructor. Builds user object
     * based on given parameter
     * @param id user id
     * @param username username
     * @param password user password
     * @param firstName First Name
     * @param lastName Last Name
     * @param address1 Address Line 1
     * @param city City
     * @param state State
     * @param zipCode Zip Code
     * @param securityQuestion Security Question Number
     * @param securityAnswer Security Answer
     * @param email user email
     */
    public User(String id, String username,
            String password, String firstName,
            String lastName, String address1,
            String city, String state,
            String zipCode, int securityQuestion,
            String securityAnswer, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.email = email;
    }
    /**
     * Custom constructor. Converts given document 
     * to user object
     * @param doc document
     */
    public User(Document doc) {
        this.id = doc.getObjectId("_id").toString();
        this.address1 = doc.getString("address1");
        this.address2 = doc.getString("address2");
        this.city = doc.getString("city");
        this.email = doc.getString("email");
        this.firstName = doc.getString("firstName");
        this.middleName = doc.getString("middleName");
        this.lastName = doc.getString("lastName");
        this.password = doc.getString("password");
        this.securityAnswer = doc.getString("securityAnswer");
        this.securityQuestion = doc.getInteger("securityQuestion");
        this.state = doc.getString("state");
        this.username = doc.getString("username");
        this.zipCode = doc.getString("zipCode");
    }

    /* Getters and Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(int securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setZipcode(String zipcode) {
        zipCode = zipcode;
    }

    public String getZipcode() {
        return zipCode;
    }

    public Document toDocument() {
        return new Document()
                .append("username", getUsername())
                .append("password", getPassword())
                .append("firstName", getFirstName())
                .append("middleName", getMiddleName())
                .append("lastName", getLastName())
                .append("address1", getAddress1())
                .append("address2", getAddress2())
                .append("city", getCity())
                .append("state", getState())
                .append("zipCode", getZipCode())
                .append("securityQuestion", getSecurityQuestion())
                .append("securityAnswer", getSecurityAnswer())
                .append("email", getEmail());
    }
}
