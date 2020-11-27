package eu.squadd.urlshortener.service;

import eu.squadd.urlshortener.repository.UriRepo;
import eu.squadd.urlshortener.util.IdConverter;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        Long id = uriRepo.generateId();
        LOGGER.info("generated ID: " + id);

        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        LOGGER.info("Unique ID: " + uniqueID);

        uriRepo.saveHUrl("url:" + dictionaryKey, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return shortenedURL;
    }

    public String getLongUrlWithUniqueID(String uniqueID) throws NoSuchElementException {
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = uriRepo.getUrlById(dictionaryKey);
        LOGGER.info("Converting short URL back to {}", longUrl);
        return longUrl;
    }

    public String getLongUrlWithKey(String key) throws NoSuchElementException {
        String longUrl = uriRepo.getHUrlByKey(key);
        LOGGER.info("Shortened URL conversion back to long {}", longUrl);
        return longUrl;
    }

    public Long deleteLongUrlWithUniqueID(String uniqueID) throws NoSuchElementException {
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        Long count = uriRepo.deleteHKeyById(dictionaryKey);
        LOGGER.info("Long URL with ID {} permanently deleted", uniqueID);
        return count;
    }
}
