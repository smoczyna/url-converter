package eu.squadd.urlshortener.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IdConverterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdConverterTest.class);

    @Test
    void testShortIdConversion() {
        Long id = 132L;
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID generated: " + uniqueID);
        assertNotNull(uniqueID);

        Long idBack = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Key value retrieved: " + idBack);
        assertNotNull(idBack);
        assertEquals(id, idBack);
    }

    @Test
    void testMediumLongIdConversion() {
        Long id = 132544565768989L;
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID generated: " + uniqueID);
        assertNotNull(uniqueID);

        Long idBack = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Key value retrieved: " + idBack);
        assertNotNull(idBack);
        assertEquals(id, idBack);
    }

    @Test
    void testMaxLongIdConversion() {
        Long id = 999999999999999L;
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID generated: " + uniqueID);
        assertNotNull(uniqueID);

        Long idBack = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Key value retrieved: " + idBack);
        assertNotNull(idBack);
        assertEquals(id, idBack);
    }

    @Test
    void testTooLongIdConversion() {
        Long id = 1325445657689894867L;
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        LOGGER.info("Unique ID generated: " + uniqueID);
        assertNotNull(uniqueID);

        Long idBack = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Key value retrieved: " + idBack);
        assertNotNull(idBack);
        assertNotEquals(id, idBack);
    }
}
