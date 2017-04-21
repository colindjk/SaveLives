/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.entityclasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.faces.context.FacesContext;
import org.bson.Document;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

/**
 *
 * @author taiwenjin
 */
public class CrimeCase extends Marker {

    private static final String MARKER_ICON = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/resources/images/map-icon.png";
    private Date date;
    private LocalTime time;
    private String code;
    private String location;
    private String weapon;
    private String post;
    private String district;

    /**
     * Custom constructor. Builds CrimeCase object based on given document
     *
     * @param doc document to be converted
     * @throws java.text.ParseException if an attribute could not be parsed
     */
    public CrimeCase(Document doc) throws ParseException {

        super(new LatLng(doc.containsKey("coorY") ? doc.getDouble("coorY") : null, doc.containsKey("coorX") ? doc.getDouble("coorX") : null),
                doc.getString("description"),
                null,
                MARKER_ICON
        );
        if ((!doc.containsKey("coorY")) || (!doc.containsKey("coorX"))) {
            super.setVisible(false);
        }

        this.code = doc.getString("crimecode");
        try{
            this.time = LocalTime.parse(doc.getString("crimetime"), DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch(DateTimeParseException ex){
            // This exeception is thrown since some of the time values are in the format HHmm.ss. e.g. 2228.00
            // so retry formatting with that format. A better alternative to this could be checking the length of
            // the string and formatting accordingly. 
            this.time = LocalTime.parse(doc.getString("crimetime"), DateTimeFormatter.ofPattern("Hmm.ss"));
        }
        
        this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(doc.getString("crimedate"));

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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
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
        return super.getTitle();
    }

    public void setDescription(String description) {
        super.setTitle(description);
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
        return super.getLatlng().getLng();
    }

    public Double getCoorY() {
        return super.getLatlng().getLat();
    }

    public void setCoordinates(Double coorY, Double coorX) {
        if (coorX == null || coorY == null) {
            super.setVisible(false);
        } else {
            super.setLatlng(new LatLng(coorY, coorX));
        }
    }

    public boolean hasLocation() {
        return super.isVisible();
    }
}
