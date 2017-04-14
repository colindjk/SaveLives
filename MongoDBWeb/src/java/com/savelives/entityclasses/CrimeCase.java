/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.entityclasses;

import java.util.Date;
import org.bson.Document;

/**
 *
 * @author taiwenjin
 */
public class CrimeCase {

    private Date date;
    private String time;
    private String code;
    private String location;
    private String description;
    private String weapon;
    private String post;
    private String district;
    private Double coorX;
    private Double coorY;

    /**
     * Custom constructor. Builds CrimeCase object 
     * based on given document
     * @param doc document
     */
    public CrimeCase(Document doc) {
        this.code = doc.getString("crimecode");
        this.time = doc.getString("crimetime");
        this.coorX = doc.getDouble("coorX");
        this.coorY = doc.getDouble("coorY");
        this.date = doc.getDate("crimedate");
        this.description = doc.getString("description");
        this.location = doc.getString("location");
        this.weapon = doc.getString("weapon");
        this.post = doc.getString("post");
        this.district = doc.getString("district");
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String crimeCode) {
        this.code = crimeCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getCoorX() {
        return coorX;
    }

    public void setCoorX(Double coorX) {
        this.coorX = coorX;
    }

    public Double getCoorY() {
        return coorY;
    }

    public void setCoorY(Double coorY) {
        this.coorY = coorY;
    }

}
