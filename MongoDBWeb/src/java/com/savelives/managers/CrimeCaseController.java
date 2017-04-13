/*
 * Created by Taiwen Jin on 2017.04.10  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package com.savelives.managers;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.savelives.managers.Constants.DATABASE_HOSTNAME;
import static com.savelives.managers.Constants.DATABASE_NAME;
import static com.savelives.managers.Constants.DATABASE_PASSWORD;
import static com.savelives.managers.Constants.DATABASE_USER;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.bson.Document;
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
    private final String CrimeCollectionName = "CrimeCaseCollection";
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection coll;

    public void dataQuery() {

        JSONArray jsonArray;

        List<ServerAddress> seeds = new ArrayList<>();
        List<MongoCredential> credentialsList = new ArrayList<>();

        ServerAddress serverAddress = new ServerAddress(DATABASE_HOSTNAME, 27017);
        seeds.add(serverAddress);

        MongoCredential credential = MongoCredential.createCredential(DATABASE_USER, DATABASE_NAME, DATABASE_PASSWORD.toCharArray());
        credentialsList.add(credential);
        mongoClient = new MongoClient(seeds, credentialsList);
        //mongoClient = new MongoClient(seeds);
        db = mongoClient.getDatabase(DATABASE_NAME);
        coll = db.getCollection(CrimeCollectionName);
     
        if (coll.count() == 0) {
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
                    Document doc = new Document("crimedate", date)
                            .append("crimetime", time)
                            .append("crimecode", code)
                            .append("location", location)
                            .append("description", description)
                            .append("weapon", weapon)
                            .append("post", post)
                            .append("district", district)
                            .append("coorX", coorX)
                            .append("coorY", coorY);
                    coll.insertOne(doc);
                }

            } catch (Exception ex) {
                System.out.println("EXCEPTION: " + ex.getMessage());
            }
        }else{
            System.out.println("LOG: Database already contains crime cases");
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
