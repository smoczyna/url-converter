package eu.squadd.urlshortener.service;

import eu.squadd.urlshortener.repository.UriRepo;
import eu.squadd.urlshortener.util.IdConverter;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UriConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriConverterService.class);

    private final UriRepo uriRepo;

    public UriConverterService(UriRepo uriRepo) {
        this.uriRepo = uriRepo;
    }

    public String shortenURL(String localURL, String longUrl) {
        LOGGER.info("Shortening {}", longUrl);
        Long id = uriRepo.generateId();
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        uriRepo.saveHUrl("url:" + id, longUrl);
        String baseString = UriValidator.formatLocalURLFromShortener(localURL);
        String shortenedURL = baseString + uniqueID;
        return shortenedURL;
    }

    public String getLongURLFromID(String uniqueID) throws Exception {
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        String longUrl = uriRepo.getUrlById(dictionaryKey);
        LOGGER.info("Converting shortened URL back to {}", longUrl);
        return longUrl;
    }
}
