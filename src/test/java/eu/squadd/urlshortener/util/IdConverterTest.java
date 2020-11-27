package eu.squadd.urlshortener.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class IdConverterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdConverterTest.class);

    @Test
    void testIdConvertion() {
        Long id = 132544565768989789L;
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID generated: " + uniqueID);
        assertNotNull(uniqueID);

        Long key = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Key value retrieved: " + key);
        assertNotNull(key);
    }
}
