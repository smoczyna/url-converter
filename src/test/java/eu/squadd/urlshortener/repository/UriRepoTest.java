package eu.squadd.urlshortener.repository;

import eu.squadd.urlshortener.util.IdConverter;
import eu.squadd.urlshortener.util.UriValidator;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UriRepoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriRepoTest.class);

    @Autowired
    private UriRepo uriRepo;

    @Test
    void repoCrudTest() {
        // saving 1 URL
        String key = this.uriRepo.generateUID().toString();
        String longUrl = "https://www.google.com/search?channel=fs&client=ubuntu&q=long+urls";
        String id = this.uriRepo.generateId().toString();
        this.uriRepo.saveUrl(key, longUrl);

        // getting saved URL
        String url = this.uriRepo.getUrlByKey(key);
        assertNotNull(url);
        assertEquals(longUrl, url);

        this.uriRepo.deleteKey(key);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            this.uriRepo.getUrlByKey(key);
        });
        String expectedMessage = String.format("URL at key %s does not exist", key);
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void urlShorteningLocalTest() {
        String longUrl = "https://www.theguardian.com/football/this-is-url-sent-from-repo-test-IE";
        LOGGER.info("Shorten up this url: {}", longUrl);

        Long id = uriRepo.generateId();
        LOGGER.info("generated ID: " + id);
        assertNotNull(id);

        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID: " + id);
        assertNotNull(uniqueID);

        uriRepo.saveHUrl("url:" + id, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener("localhost:8080/urlshortener");
        String shortenedURL = baseString + uniqueID;
        LOGGER.info("Shortened URL: " + shortenedURL);
        assertNotNull(shortenedURL);

        String longUrlBack = this.uriRepo.getUrlById(id);
        assertNotNull(longUrlBack);
        assertEquals(longUrl, longUrlBack);

        String deletedKey = "url:" + id;
        this.uriRepo.deleteHKey(deletedKey);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            this.uriRepo.getUrlById(id);
        });
        assertEquals("URL at key " + id +" does not exist", exception.getMessage());
    }

    @Test
    void urlShorteningGivenTest() {
        String shortUrl = "www.this.is.hero";
        String longUrl = "https://www.theguardian.com/football/this-is-url-sent-from-repo-test-IE";
        LOGGER.info("Shorten up this url: {}", longUrl);

        Long id = uriRepo.generateId();
        LOGGER.info("generated ID: " + id);
        assertNotNull(id);

        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID: " + id);
        assertNotNull(uniqueID);

        uriRepo.saveNamedHKey(shortUrl, "url:" + id, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener(shortUrl);
        String shortenedURL = baseString + uniqueID;
        LOGGER.info("Shortened URL: " + shortenedURL);
        assertNotNull(shortenedURL);

        String longUrlBack = this.uriRepo.getNamedUrlById(shortUrl, id);
        assertNotNull(longUrlBack);
        assertEquals(longUrl, longUrlBack);

        this.uriRepo.deleteNamedHKeyById(shortUrl, id);

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            this.uriRepo.getNamedUrlById(shortUrl, id);
        });
        assertEquals("URL at key " + id +" does not exist", exception.getMessage());
    }

    @Test
    void deleteUrlFailTest() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            this.uriRepo.deleteHKeyById(145346756L);
        });
        assertEquals("URL at key 145346756 does not exist", exception.getMessage());
    }

    @Test
    void getAllKeyEntries() {
        Map<String, String> result = this.uriRepo.getAllKeyEntries("url:");
        assertNotNull(result);
        long i = 0;
        for (Map.Entry entry : result.entrySet()) {
            Assert.hasText("url:", entry.getKey().toString());
            assertNotNull(entry.getValue());
            i++;
        }
        LOGGER.info("Elements checked: " + i);
    }
}
