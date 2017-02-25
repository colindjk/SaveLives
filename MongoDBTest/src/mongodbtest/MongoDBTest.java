/*
 * Created by Taiwen Jin on 2017.02.25  * 
 * Copyright © 2017 Taiwen Jin. All rights reserved. * 
 */
package mongodbtest;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 *
 * @author taiwenjin
 */
public class MongoDBTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // Now connect to your databases
            DB db = mongoClient.getDB("TestDatabase");
            DBCollection coll = db.getCollection("UserInfoCollection");
            BasicDBObject doc = new BasicDBObject("name", "Taiwen Jin")
                    .append("email", "kim6@vt.edu")
                    .append("phone", "540-808-8544")
                    .append("major", "Computer Science");
            coll.insert(doc);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

}
