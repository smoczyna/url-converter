package eu.squadd.urlshortener.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UriConverterServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriConverterServiceTest.class);

    @Autowired
    private UriConverterService service;

    @Test
    void testServiceLocalUrl() {
        String longUrl = "https://www.theguardian.com/football/this-is-url-sent-from-repo-test-IE";
        String result = this.service.convertLocalUrl("localhost:8080/urlshortener", longUrl);
        LOGGER.info(result);
        assertNotNull(result);
        Assert.hasText("localhost:8080/urlshortener", result);

        String uniqueID = result.substring(result.lastIndexOf('/') + 1);
        String longUrlBack = this.service.getLongUrlWithUniqueID(uniqueID);
        LOGGER.info(longUrl);
        assertNotNull(longUrl);
        assertEquals(longUrl, longUrlBack);

        Map<String, String> allKeys = this.service.getAllKeyEntries("url:");
        assertNotNull(allKeys);
        Assert.notEmpty(allKeys, "There should some testing URLs saved at this stage");

        Long count = this.service.deleteLongUrlWithUniqueID(uniqueID);
        assertEquals(1, count);
    }

    @Test
    public void testServiceNamedUrl() {
        String shortUrl = "www.this.is.hero";
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        String result = this.service.convertUrl(shortUrl, longUrl);
        LOGGER.info(result);
        assertNotNull(result);
        Assert.hasText(shortUrl, result);

        String uniqueID = result.substring(result.lastIndexOf('/') + 1);
        String longUrlBack = this.service.getNamedLongUrlWithUniqueID(shortUrl, uniqueID);
        LOGGER.info(longUrl);
        assertNotNull(longUrl);
        assertEquals(longUrl, longUrlBack);

        Map<String, String> allKeys = this.service.getAllKeyEntries(shortUrl);
        assertNotNull(allKeys);
        Assert.notEmpty(allKeys, "There should some testing URLs saved at this stage");

        Long count = this.service.deleteNamedLongUrlWithUniqueID(shortUrl, uniqueID);
        assertEquals(1, count);

        allKeys = this.service.getAllKeyEntries(shortUrl);
        assertNotNull(allKeys);
        assertTrue(allKeys.size()>=0);

        count = this.service.deleteKey(shortUrl);
        assertTrue(count>=0);
    }

    @Test
    void getLongURLWithKey() {
        Map<String, String> allKeys = this.service.getAllKeyEntries("url:");
        assertNotNull(allKeys);
        for (Map.Entry entry : allKeys.entrySet()) {
            String result = this.service.getLongUrlWithKey(entry.getKey().toString());
            LOGGER.info(result);
            String expected = entry.getValue().toString();
            LOGGER.info(expected);
            assertEquals(expected, result);
        }
    }

    @Test
    void getLongURLFailTest() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            String result = this.service.getLongUrlWithKey("url:15365463");
            LOGGER.info(result);
        });
        LOGGER.info(exception.getMessage());
    }
}
