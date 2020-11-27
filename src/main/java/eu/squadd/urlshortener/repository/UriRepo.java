package eu.squadd.urlshortener.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.NoSuchElementException;
import java.util.UUID;

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
        LOGGER.info("Saving: {} at {}", longUrl, key);
        this.jedis.set(key, longUrl);
    }

    public void saveHUrl(String key, String longUrl) {
        LOGGER.info("Saving: {} at {}", longUrl, key);
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

    public String getUrlById(Long id) throws NoSuchElementException {
        LOGGER.info("Retrieving at {}", id);
        String url = this.jedis.hget(urlKey, "url:" + id);
        LOGGER.info("Retrieved {} at {}", url, id);
        if (url == null) {
            throw new NoSuchElementException("URL at key " + id + " does not exist");
        }
        return url;
    }

    public void deleteKey(String key) {
        LOGGER.info("Deleting at {}", key);
        this.jedis.del(key);
    }

}
