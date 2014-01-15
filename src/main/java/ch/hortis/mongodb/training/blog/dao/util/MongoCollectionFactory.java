package ch.hortis.mongodb.training.blog.dao.util;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.UnknownHostException;
import java.util.*;

@Component
public class MongoCollectionFactory {

    private static final String K_HOSTNAME = "hostname";
    private static final String K_DB_NAME = "dbname";

    @Autowired
    @Qualifier("mongoConfiguration")
    private Properties configuration;

    private String dbName;
    private List<ServerAddress> servers = new ArrayList<ServerAddress>();
    private Map<String, MongoCollection> loadedCollections = new HashMap<String, MongoCollection>();
    private Jongo jongo;

    @PostConstruct
    public void reload() throws UnknownHostException {
        loadFromProperties();
        jongo = new Jongo(new MongoClient(servers).getDB(dbName));
    }

    private void loadFromProperties() throws UnknownHostException {
        String[] servers = configuration.getProperty(K_HOSTNAME, "127.0.0.1:27017").split(",");
        for (String server : servers) {
            String[] servSplit = server.split(":");
            this.servers.add(new ServerAddress(servSplit[0], Integer.valueOf(servSplit[1])));
        }
        dbName = configuration.getProperty(K_DB_NAME, "default");
    }

    public MongoCollection getCollection(String collectionName) {
        MongoCollection mongoCollection = loadedCollections.get(collectionName);
        if (mongoCollection == null) {
            mongoCollection = jongo.getCollection(collectionName);
            loadedCollections.put(collectionName, mongoCollection);
        }
        return mongoCollection;
    }

    @PreDestroy
    private void detroy() {
        jongo = null;
    }


}
