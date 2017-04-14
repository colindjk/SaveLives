/*
 * Created by Cheick Berthe on 2017.04.14  * 
 * Copyright © 2017 Cheick Berthe. All rights reserved. * 
 */
package com.savelives.sessionbeans;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.savelives.entityclasses.CrimeCase;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.bson.Document;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

@Stateless
/**
 *
 * @author Berthe
 */
public class CrimeCaseFacade {

    private static final Logger LOGGER = Logger.getLogger(CrimeCaseFacade.class.getName());
    private final Client crimeCaseClient;
    private final String COLLECTION_NAME = "CrimeCases";

    /**
     * Default Constructor
     */
    public CrimeCaseFacade() {
        crimeCaseClient = new Client(COLLECTION_NAME);
    }

    public List<CrimeCase> getAll() {
        MongoCollection<Document> collection = crimeCaseClient.getCollection();
        if (collection.count() == 0) {
            populateCollection();
        }
        FindIterable<Document> cursor = collection.find();
        List<CrimeCase> crimes = new ArrayList<>();
        for (Document doc : cursor) {
            crimes.add(new CrimeCase(doc));
        }
        return crimes;

    }

    /**
     * Call this to populate local database if it doesn't contain any crime data
     */
    private void populateCollection() {
        try {

            String CrimeCaseJSONData = readUrlContent("https://data.baltimorecity.gov/resource/4ih5-d5d5.json");

            JSONArray jsonArray = new JSONArray(CrimeCaseJSONData);
            MongoCollection coll = crimeCaseClient.getCollection();
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
    }

    private String readUrlContent(String webServiceURL) throws Exception {

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
