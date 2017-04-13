/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

/**
 *
 * @author taiwenjin
 */
public class CrimeCase {
    private String date;
    private String time;
    private String code;
    private String location;
    private String description;
    private String weapon;
    private String post;
    private String district;
    private Double coorX;


    private Double coorY;
    
    public CrimeCase(String date, String time, String code, String location, String description, String weapon, String post, String district, Double coorX, Double coorY){
        this.date = date;
        this.time = time;
        this.code = code;
        this.location = location;
        this.description = description;
        this.weapon = weapon;
        this.post = post;
        this.district = district;
        this.coorX = coorX;
        this.coorY = coorY;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public void setCode(String code) {
        this.code = code;
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
