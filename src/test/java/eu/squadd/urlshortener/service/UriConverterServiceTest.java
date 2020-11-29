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
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        String result = this.service.convertLocalUrl("localhost:8080/urlshortener", longUrl);
        LOGGER.info(result);
        assertNotNull(result);
        Assert.hasText("localhost:8080/urlshortener", result);

        String uniqueID = result.substring(result.lastIndexOf('/') + 1);
        String longUrlBack = this.service.getLongUrlWithUniqueID(uniqueID);
        LOGGER.info(longUrl);
        assertNotNull(longUrl);
        assertEquals(longUrl, longUrlBack);

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

        Long count = this.service.deleteNamedLongUrlWithUniqueID(shortUrl, uniqueID);
        assertEquals(1, count);
    }

    @Test
    void getLongURLWithKey() {
        String result = this.service.getLongUrlWithKey("url:15");
        LOGGER.info(result);
        String expected = "https://www.theguardian.com/football/this-is-url-sent-from-repo-test-IE";
        LOGGER.info(expected);
        assertEquals(expected, result);
    }

    @Test
    void getLongURLFailTest() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            String result = this.service.getLongUrlWithKey("url:15365463");
            LOGGER.info(result);
        });
        LOGGER.info(exception.getMessage());
    }

    @Test
    public void getAllKeyEntries() {
        Map<String, String> result = this.service.getAllKeyEntries("url:");
        assertNotNull(result);
        Assert.notEmpty(result, "There should some testing URLs saved at this stage");
    }
}
