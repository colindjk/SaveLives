/*
 * Created by Pingxin Shang on 2017.03.13  * 
 * Copyright © 2017 Pingxin Shang. All rights reserved. * 
 */
package org.savelives.sessionbeans;

/*
 * @author ping
 */
import org.savelives.account.User;
import com.google.gson.Gson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.bson.BsonDocument;
import org.bson.BsonRegularExpression;
import org.bson.Document;

@Stateless
public class UserFacade {

    private static final Logger LOGGER = Logger.getLogger(UserFacade.class.getName());
    private final static String HOST = "localhost";
    private final static int PORT = 27017;
    public final static String DATABASE = "save_lifes";
    public final static String COLLECTION = "account";

    public MongoClient mongoClient() {
        return new MongoClient(new ServerAddress(HOST, PORT));
    }

    public void create(User u) {
        System.out.print("create in userfacade called");
        MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT));
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);

        if (u != null) {
            Document d = new Document().append("id", u.getId())
                    .append("username", u.getUsername())
                    .append("password", u.getPassword())
                    .append("firstName", u.getFirstName())
                    .append("middleName", u.getMiddleName())
                    .append("lastName", u.getLastName())
                    .append("address1", u.getAddress1())
                    .append("address2", u.getAddress2())
                    .append("city", u.getCity())
                    .append("state", u.getState())
                    .append("zipCode", u.getZipCode())
                    .append("securityQuestion", u.getSecurityQuestion())
                    .append("securityAnswer", u.getSecurityAnswer())
                    .append("email", u.getEmail());
            collection.insertOne(d);
        }
    }

    public void update(User u) {
        MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT));
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);

        if (u != null) {
            Document d = new Document().append("id", u.getId())
                    .append("username", u.getUsername())
                    .append("password", u.getPassword())
                    .append("firstName", u.getFirstName())
                    .append("middleName", u.getMiddleName())
                    .append("lastName", u.getLastName())
                    .append("address1", u.getAddress1())
                    .append("address2", u.getAddress2())
                    .append("city", u.getCity())
                    .append("state", u.getState())
                    .append("zipCode", u.getZipCode())
                    .append("securityQuestion", u.getSecurityQuestion())
                    .append("securityAnswer", u.getSecurityAnswer())
                    .append("email", u.getEmail());
            collection.updateOne(new Document("id", u.getId()), new Document("$set", d));
        }
    }

    public void delete(User u) {
        MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT));
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
        collection.deleteOne(new Document("id", u.getId()));
    }

    public List<User> find(String filter) {
        List<User> list = new ArrayList<>();
        MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT));
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
        FindIterable<Document> iter;
        if (filter == null || filter.trim().length() == 0) {
            iter = collection.find();
        } else {
            BsonRegularExpression bsonRegex = new BsonRegularExpression(filter);
            BsonDocument bsonDoc = new BsonDocument();
            bsonDoc.put("username", bsonRegex);
            iter = collection.find(bsonDoc);
        }
        iter.forEach(new Block<Document>() {
            @Override
            public void apply(Document doc) {
                list.add(new Gson().fromJson(doc.toJson(), User.class));
            }
        });
        return list;
    }

    public User findByUsername(String username) {
        Document d;
        User u;
        
        MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT));
        MongoCollection<Document> collection = mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);

        if (username.isEmpty()) {
            return null;
        } else {
            d = collection.find(eq("username", username)).first();
        }
        if (d == null) {
            return null;
        } else {
            u = new Gson().fromJson(d.toJson(), User.class);
        }
        return u;
    }
}
