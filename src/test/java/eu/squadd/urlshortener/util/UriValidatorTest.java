package eu.squadd.urlshortener.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UriValidatorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriValidatorTest.class);

    @Test
    void validateURL() {
        String longUrl = "https://www.google.com/search?channel=fs&client=ubuntu&q=long+urls";
        boolean isValid = UriValidator.INSTANCE.validateURL(longUrl);
        assertTrue(isValid);
    }

    @Test
    public void formatLocalURLTest1() {
        String shortUrl = "http://localhost:8080/tester";
        String result = UriValidator.formatLocalURLToShort(shortUrl);
        assertNotNull(result);
        LOGGER.info("formatted url: " + result);
        assertEquals("http://localhost:8080/", result);
    }

    @Test
    public void formatLocalURLTest2() {
        String shortUrl = "http://www.this.is.my.hero/this-section-is-gone";
        String result = UriValidator.formatLocalURLToShort(shortUrl);
        assertNotNull(result);
        LOGGER.info("formatted url: " + result);
        assertEquals("http://www.this.is.my.hero/", result);
    }

    @Test
    public void formatLocalUrlTest3() {
        String shortURl = "https://euobserver.com/coronavirus/150217";
        String result = UriValidator.formatLocalURLToShort(shortURl);
        assertNotNull(result);
        LOGGER.info("formatted url: " + result);
        assertEquals("https://euobserver.com/coronavirus/", result);
    }
}
