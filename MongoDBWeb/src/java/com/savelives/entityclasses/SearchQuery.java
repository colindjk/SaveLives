/*
 * Created by Pingxin Shang on 2017.04.25  * 
 * Copyright © 2017 Pingxin Shang. All rights reserved. * 
 */
package com.savelives.entityclasses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author ping
 */
public class SearchQuery {

    private LocalDateTime createTime;
    private Date from;
    private Date to;
    private ArrayList<String> categories;
    private ArrayList<String> crimeCodes;

    public SearchQuery() {
    }

    /**
     * Custom Constructor. Builds searchQuery object based on given parameter
     *
     * @param createTime Date and time the Query is created
     * @param from Search from date
     * @param to Search to date
     * @param categories Search categories
     * @param crimeCodes Search crime codes
     */
    public SearchQuery(LocalDateTime createTime, Date from, Date to,
            ArrayList<String> categories, ArrayList<String> crimeCodes) {
        this.createTime = createTime;
        this.from = from;
        this.to = to;
        this.categories = categories;
        this.crimeCodes = crimeCodes;
    }

    public SearchQuery(Document doc) {

        // Convert Document to onj to handle array
        JSONObject obj = new JSONObject(doc);

        // Set Formatter
        DateTimeFormatter dnt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateFormat d = new SimpleDateFormat("yyyy/mm/dd");

        this.createTime = LocalDateTime.parse(obj.getString("createTime"), dnt);
        try {
            this.from = d.parse(obj.getString("from"));
            this.to = d.parse(obj.getString("to"));
        } catch (ParseException ex) {
            Logger.getLogger(SearchQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.categories = new ArrayList<>();
        JSONArray arr = obj.getJSONArray("categories");
        for (int i = 0; i < arr.length(); i++) {
            this.categories.add(arr.getString(i));
        }

        this.crimeCodes = new ArrayList<>();
        arr = obj.getJSONArray("crimeCodes");
        for (int i = 0; i < arr.length(); i++) {
            this.crimeCodes.add(arr.getString(i));
        }
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getCrimeCodes() {
        return crimeCodes;
    }

    public void setCrimeCodes(ArrayList<String> crimeCodes) {
        this.crimeCodes = crimeCodes;
    }

    public Document toDocument() {

        DateTimeFormatter dnt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DateFormat d = new SimpleDateFormat("yyyy/mm/dd");

        String ct = dnt.format(this.createTime);
        String f = d.format(this.from);
        String t = d.format(this.to);
        JSONArray cg = new JSONArray(this.categories);
        JSONArray cc = new JSONArray(this.crimeCodes);

        return new Document()
                .append("createTime", ct)
                .append("from", f)
                .append("to", t)
                .append("categories", cg)
                .append("crimeCodes", cc);
    }
}