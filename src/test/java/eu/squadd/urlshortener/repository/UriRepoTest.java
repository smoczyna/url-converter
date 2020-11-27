package eu.squadd.urlshortener.repository;

import eu.squadd.urlshortener.util.IdConverter;
import eu.squadd.urlshortener.util.UriValidator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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
    void urlShorteningTest() throws Exception {
        String localUrl = "www.this.is.hero";
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        LOGGER.info("Shorten up this url: {}", longUrl);

        Long id = uriRepo.generateId();
        LOGGER.info("generated ID: " + id);
        assertNotNull(id);

        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID: " + id);
        assertNotNull(uniqueID);

        uriRepo.saveHUrl("url:" + id, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener(localUrl);
        String shortenedURL = baseString + uniqueID;
        LOGGER.info("Shortened URL: " + shortenedURL);
        assertNotNull(shortenedURL);

        String longUrlBack = this.uriRepo.getUrlById(id);
        assertNotNull(longUrlBack);
        assertEquals(longUrl, longUrlBack);

        this.uriRepo.deleteKey("url:");

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            this.uriRepo.getUrlById(100L);
        });
        assertEquals("URL at key 100 does not exist", exception.getMessage());
    }

}
