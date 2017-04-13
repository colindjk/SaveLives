/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.taiwenjin.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author taiwenjin
 */
@SessionScoped
@Named(value = "crimeCaseController")
public class CrimeCaseController implements Serializable {

    private final String crimeCaseJSONDataURL = "https://data.baltimorecity.gov/resource/4ih5-d5d5.json";

    private MongoClient mongoClient;
    private DB db;
    private DBCollection coll;

    public void dataQuery() {

        JSONArray jsonArray;
        String password = "CSD@s17";
        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        seeds.add(serverAddress);
   
        //  MongoCredential credential = MongoCredential.createCredential("CS3984", "admin", password.toCharArray());
        //  credentialsList.add(credential);
        
        //  mongoClient = new MongoClient(seeds, credentialsList);
        mongoClient = new MongoClient(seeds);
        db = mongoClient.getDB("TestDatabase");
        coll = db.getCollection("CrimeCaseCollection");

        try {

            String CrimeCaseJSONData = readUrlContent(crimeCaseJSONDataURL);

            jsonArray = new JSONArray(CrimeCaseJSONData);
            
            for (int i = 0; i < 50; i++) {
                JSONObject tempCrimeCaseJSONObject = jsonArray.getJSONObject(i);
                String date = tempCrimeCaseJSONObject.optString("crimedate", "");
                String time = tempCrimeCaseJSONObject.optString("crimetime", "");

                String code = tempCrimeCaseJSONObject.optString("crimecode", "");
                String location = tempCrimeCaseJSONObject.optString("location", "");
                String description = tempCrimeCaseJSONObject.optString("description", "");
                String weapon = tempCrimeCaseJSONObject.optString("weapon", "");
                String post = tempCrimeCaseJSONObject.optString("post", "");
                String district = tempCrimeCaseJSONObject.optString("district", "");
                JSONObject location_1 = tempCrimeCaseJSONObject.getJSONObject("location_1");
                JSONArray coor = location_1.getJSONArray("coordinates");
                Double coorX = (Double) coor.get(0);
                Double coorY = (Double) coor.get(1);
                BasicDBObject doc = new BasicDBObject("crimedate", date)
                        .append("crimetime", time)
                        .append("crimecode", code)
                        .append("location", location)
                        .append("description", description)
                        .append("weapon", weapon)
                        .append("post", post)
                        .append("district", district)
                        .append("coorX", coorX)
                        .append("coorY", coorY);
                coll.insert(doc);
            }

        } catch (Exception ex) {

        }
    }

    public String readUrlContent(String webServiceURL) throws Exception {

        BufferedReader reader = null;

        try {
            URL url = new URL(webServiceURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder buffer = new StringBuilder();

            char[] chars = new char[10240];

            int numberOfCharactersRead;

            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            return buffer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
