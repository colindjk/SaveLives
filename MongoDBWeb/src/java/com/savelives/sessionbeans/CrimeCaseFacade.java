/*
 * Created by Cheick Berthe on 2017.04.14  * 
 * Copyright © 2017 Cheick Berthe. All rights reserved. * 
 */
package com.savelives.sessionbeans;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.savelives.entityclasses.CrimeCase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.bson.Document;

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
        List<CrimeCase> crimes = new ArrayList<>();
        try {
            MongoCollection<Document> collection = crimeCaseClient.getCollection();
            if (collection.count() == 0) {

                populateCollection(collection);

            }

            FindIterable<Document> cursor = collection.find().limit(50);
            //Document item = collection.find().first();
            //crimes.add(new CrimeCase(item));
            for (Document doc : cursor) {

                crimes.add(new CrimeCase(doc));

            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(CrimeCaseFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return crimes;
    }

    /**
     * Call this to populate local database if it doesn't contain any crime data
     *
     * @param collection
     * @throws IOException
     */
    private void populateCollection(MongoCollection<Document> collection) throws IOException {
        JsonReader reader = null;
        String CrimeCaseURL = "https://data.baltimorecity.gov/resource/4ih5-d5d5.json?$$app_token=yjfXOuQMUxKqegMpx42YoV9RJ&$limit=5000000";

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    new URL(CrimeCaseURL).openStream(), Charset.forName("UTF-8")));

            reader = new JsonReader(r);

            JsonParser parser = new JsonParser();

            reader.beginArray(); // the initial '['
            while (reader.hasNext()) {
                JsonObject tempCrimeCaseJSONObject = parser.parse(reader).getAsJsonObject();

                JsonElement code = tempCrimeCaseJSONObject.get("crimecode");
                JsonElement date = tempCrimeCaseJSONObject.get("crimedate");
                JsonElement time = tempCrimeCaseJSONObject.get("crimetime");
                JsonElement description = tempCrimeCaseJSONObject.get("description");

                JsonElement district = tempCrimeCaseJSONObject.get("district");

                JsonElement location = tempCrimeCaseJSONObject.get("location");
                JsonElement weapon = tempCrimeCaseJSONObject.get("weapon");
                JsonElement post = tempCrimeCaseJSONObject.get("post");
                JsonObject location_1 = tempCrimeCaseJSONObject.getAsJsonObject("location_1");
                JsonArray coor = (location_1 == null) ? null : location_1.getAsJsonArray("coordinates");
                Double coorX = (coor == null) ? null : coor.get(0).getAsDouble();
                Double coorY = (coor == null) ? null : coor.get(1).getAsDouble();
                JsonElement neighborhood = tempCrimeCaseJSONObject.get("neighborhood");

                Document doc = new Document();
                if (date != null) {
                    doc.append("crimedate", date.getAsString());
                }
                if (time != null) {
                    doc.append("crimetime", time.getAsString());
                }
                if (code != null) {
                    doc.append("crimecode", code.getAsString());
                }
                if (location != null) {
                    doc.append("location", location.getAsString());
                }
                if (description != null) {
                    doc.append("description", description.getAsString());
                }
                if (weapon != null) {
                    doc.append("weapon", weapon.getAsString());
                }
                if (post != null) {
                    doc.append("post", post.getAsString());
                }
                if (district != null) {
                    doc.append("district", district.getAsString());
                }
                if ((coorX != null) && (coorY != null)) {
                    doc.append("coorX", coorX).append("coorY", coorY);
                }
                if (neighborhood != null) {
                    doc.append("neighborhood", neighborhood.getAsString());
                }
                collection.insertOne(doc);
            }
        } catch (JsonIOException | JsonSyntaxException | IOException ex) {
            Logger.getLogger(CrimeCaseFacade.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
