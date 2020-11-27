package eu.squadd.urlshortener.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * author Jaja
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
        this.jedis = new Jedis();
        this.idKey = "id";
        this.urlKey = "url:";
    }

    public UUID generateUID() {
        return UUID.randomUUID();
    }

    public Long generateId() {
        Long id = this.jedis.incr(idKey);
        LOGGER.info("Incrementing ID: {}", id - 1);
        return id - 1;
    }

    public void saveUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", key, longUrl);
        this.jedis.set(key, longUrl);
    }

    public void saveHUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", key, longUrl);
        this.jedis.hset(urlKey, key, longUrl);
    }

    public String getUrlByKey(String key) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", key);
        String url = this.jedis.get(key);
        LOGGER.info("Retrieved {} at {}", url, key);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + key + " does not exist");
        }
        return url;
    }

    public String getHUrlByKey(String key) throws NoSuchElementException {
        LOGGER.info("Retrieving URl with {}", key);
        String url = this.jedis.hget(urlKey, key);
        LOGGER.info("Retrieved {} at {}", key, url);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + key + " does not exist");
        }
        return url;
    }

    public String getUrlById(Long id) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", id);
        String url = this.jedis.hget(urlKey, "url:" + id);
        LOGGER.info("Retrieved {} at {}", url, id);
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
        LOGGER.info("Deleting child at {}", id);
        Long count = this.jedis.hdel(urlKey, "url:" + id);
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
