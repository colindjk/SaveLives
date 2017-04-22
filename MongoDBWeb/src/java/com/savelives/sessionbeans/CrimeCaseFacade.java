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
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lte;
import com.savelives.entityclasses.CrimeCase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.bson.Document;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

@Stateless
/**
 *
 * @author Berthe
 */
public class CrimeCaseFacade {

    private static final Logger LOGGER = Logger.getLogger(CrimeCaseFacade.class.getName());
    private static final String COLLECTION_NAME = "CrimeCases";
    private static final Client CLIENT = new Client(COLLECTION_NAME);

    /**
     * Get a number of crimes from the database
     *
     * @param NumbOfCrimes number of crimes to get
     * @return list of crimes as map model
     */
    public MapModel getCrimesModel(int NumbOfCrimes) {

        MapModel crimes = new DefaultMapModel();
        MongoCollection<Document> collection = CLIENT.getCollection();
        collection.find().limit(NumbOfCrimes).forEach(new Consumer<Document>() {
            @Override
            public void accept(Document doc) {
                if (doc.containsKey("coorX") && doc.containsKey("coorY")) {
                    try {
                        crimes.addOverlay(new CrimeCase(doc));
                    } catch (ParseException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        return crimes;
    }

    public MapModel filterCrimes(Date from, Date to, List<String> categories, List<String> crimeCodes) {
        
        //validate dates (this will throw exceptions if it fails)
        this.validateDates(from, to);

        // Query database
        MongoCollection<Document> collection = CLIENT.getCollection();
        //Filter by date first
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        FindIterable<Document> cursor = collection.find(and(gte("crimedate", formater.format(from) + "T00:00:00.000"),
                lte("crimedate", formater.format(to) + "T23:59:59.999"), in("description", categories), in("crimecode", crimeCodes)));

        //Filter by category
        MapModel crimes = new DefaultMapModel();
        cursor.forEach(new Consumer<Document>() {
            @Override
            public void accept(Document doc) {
                if (doc.containsKey("coorX") && doc.containsKey("coorY")) {
                    try {
                        crimes.addOverlay(new CrimeCase(doc));
                    } catch (ParseException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        return crimes;
    }

    /**
     * Helper function to check validity of dates Throws exceptions with error
     * message if validation fails
     *
     * @param from start date
     * @param to end date
     */
    private void validateDates(Date from, Date to) {
        //dates cannot be null
        if (from == null || to == null) {
            throw new IllegalArgumentException("A date range is required");
        }
        //Validate parameter consistency
        if (from.after(to)) {
            throw new IllegalArgumentException("'from' Date must be earlier than 'to' Date.");
        }
        LocalDate f = new java.sql.Date(from.getTime()).toLocalDate();
        LocalDate t = new java.sql.Date(to.getTime()).toLocalDate();
        if (Period.between(f, t).getMonths() > 12) {
            throw new UnsupportedOperationException("Range cannot be longer than a year.");
        }
    }
    
    /**
     * Get a list of distinct values for a given field name
     * @param fieldName field name
     * @return list of values
     */
    public List<String> getDistinct(String fieldName) {
        List<String> result = new ArrayList<>();
        return CLIENT.getCollection().distinct(fieldName, String.class).into(result);
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
            LOGGER.log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
