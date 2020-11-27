package eu.squadd.urlshortener.service;

import eu.squadd.urlshortener.repository.UriRepo;
import eu.squadd.urlshortener.util.IdConverter;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class UriConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriConverterService.class);

    private final UriRepo uriRepo;

    public UriConverterService(UriRepo uriRepo) {
        this.uriRepo = uriRepo;
    }

    public String shortenURL(String localURL, String longUrl) {
        LOGGER.info("Converting {}", longUrl);
        Long id = this.uriRepo.generateId();
        LOGGER.info("Generated ID: " + id);

        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Unique ID: " + uniqueID);

        this.uriRepo.saveHUrl("url:" + dictionaryKey, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return shortenedURL;
    }

    public String getLongUrlWithUniqueID(String uniqueID) throws NoSuchElementException {
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = this.uriRepo.getUrlById(dictionaryKey);
        LOGGER.info("Converted short URL back to {}", longUrl);
        return longUrl;
    }

    public String getLongUrlWithKey(String key) throws NoSuchElementException {
        String longUrl = this.uriRepo.getHUrlByKey(key);
        LOGGER.info("Shortened URL conversion back to long {}", longUrl);
        return longUrl;
    }

    public Long deleteLongUrlWithUniqueID(String uniqueID) throws NoSuchElementException {
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        Long count = uriRepo.deleteHKeyById(dictionaryKey);
        LOGGER.info("Long URL with ID {} permanently deleted", uniqueID);
        return count;
    }

    protected Map<String, String> getAllKeyEntries(String key) {
        LOGGER.info("Getting all URLs of key {}", key);
        return this.uriRepo.getAllKeyEntries(key);
    }
}
