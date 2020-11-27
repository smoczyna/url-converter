package eu.squadd.urlshortener.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UriValidatorTest {

    @Test
    void validateURL() {
        String longUrl = "https://www.google.com/search?channel=fs&client=ubuntu&q=long+urls";
        boolean isValid = UriValidator.INSTANCE.validateURL(longUrl);
        assertTrue(isValid);
    }
}
