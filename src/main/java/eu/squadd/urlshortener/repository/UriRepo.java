package eu.squadd.urlshortener.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * author smoczyna
 * Redis CRUD repository
 * Do all the job about storing and retrieving data
 */

@Repository
public class UriRepo {
    private final Jedis jedis;
    private final String idKey;
    private final String urlKey;
    private static final Logger LOGGER = LoggerFactory.getLogger(UriRepo.class);

    public UriRepo() {
//        this.jedis = new Jedis("localhost", 6379, 3600);
        this.jedis = new Jedis("redis");
        this.idKey = "id";
        this.urlKey = "url:";
    }

    public UUID generateUID() {
        return UUID.randomUUID();
    }

    public synchronized Long generateId() {
        Long id = this.jedis.incr(this.idKey);
        LOGGER.info("Incrementing ID: {}", id - 1);
        return id - 1;
    }

    public synchronized Long generateNamedId(String shortUrl) {
        String namedId = String.format("%s:%s", this.idKey, shortUrl);
        Long id = this.jedis.incr(namedId);
        LOGGER.info("Incrementing ID for: {} {}", shortUrl, id - 1);
        return id - 1;
    }


    public synchronized void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving key: {} with {}", key, longUrl);
        this.jedis.set(key, longUrl);
    }

    public synchronized void saveHUrl(String key, String longUrl) {
        LOGGER.info("Saving key: {} with {}", key, longUrl);
        this.jedis.hset(urlKey, key, longUrl);
    }

    public synchronized void saveNamedHKey(String shortUrl, String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", key, longUrl);
        this.jedis.hset(shortUrl, key, longUrl);
    }

    public String getUrlByKey(String key) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", key);
        String url = this.jedis.get(key);
        LOGGER.info("Retrieved url from {}, result {}", url, key);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + key + " does not exist");
        }
        return url;
    }

    public String getHUrlByKey(String key) throws NoSuchElementException {
        LOGGER.info("Retrieving URl with {}", key);
        String url = this.jedis.hget(urlKey, key);
        LOGGER.info("Retrieved url from {}, result {}", key, url);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + key + " does not exist");
        }
        return url;
    }

    public String getUrlById(Long id) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", id);
        String url = this.jedis.hget(urlKey, "url:" + id);
        LOGGER.info("Retrieved url from {}, result {}", url, id);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + id + " does not exist");
        }
        return url;
    }

    public String getNamedUrlById(String shortUrl, Long id) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", id);
        String url = this.jedis.hget(shortUrl, "url:" + id);
        LOGGER.info("Retrieved url from {}, result {}", url, id);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + id + " does not exist");
        }
        return url;
    }

    public Long deleteKey(String key) {
        LOGGER.info("Deleting at {}", key);
        return this.jedis.del(key);
    }

    public Long deleteHKey(String key) {
        LOGGER.info("Deleting child at {}", key);
        return this.jedis.hdel(urlKey, key);
    }

    public Long deleteHKeyById(Long id) throws NoSuchElementException {
        LOGGER.info("Deleting entry at {}", id);
        Long count = this.jedis.hdel(urlKey, "url:" + id);
        LOGGER.info("URL under key {} permanently delete", id);
        if (count == 0) {
            throw new NoSuchElementException("URL at key " + id + " does not exist");
        }
        return count;
    }

    public Long deleteNamedHKeyById(String shortUrl, Long id) throws NoSuchElementException {
        LOGGER.info("Deleting entry at {}", id);
        Long count = this.jedis.hdel(shortUrl, "url:" + id);
        LOGGER.info("URL under key {} permanently delete", id);
        if (count == 0) {
            throw new NoSuchElementException("URL at key " + id + " does not exist");
        }
        return count;
    }

    public Map<String, String> getAllKeyEntries(String key) {
        LOGGER.info("Getting all children of {}", key);
        Map<String, String> result = this.jedis.hgetAll(key);
        LOGGER.info("All entries of {} retrieved", key);
        return result;
    }
}
