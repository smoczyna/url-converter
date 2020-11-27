package eu.squadd.urlshortener.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UriConverterServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriConverterServiceTest.class);

    @Autowired
    private UriConverterService service;

    @Test
    void shortenURL() {
        String localUrl = "www.this.was.hero";
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        String result = this.service.shortenURL(localUrl, longUrl);
        LOGGER.info(result);
        assertNotNull(result);

    }

    @Test
    void getLongURLFromID() throws Exception {
        String localUrl = "c";
        String longUrl = this.service.getLongURLFromID(localUrl);
        LOGGER.info(longUrl);
        assertNotNull(longUrl);
    }
}
