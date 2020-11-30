package eu.squadd.urlshortener.controller;

import eu.squadd.urlshortener.service.UriConverterService;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(UriController.class);

    protected final UriConverterService service;

    public AbstractController(UriConverterService service) {
        this.service = service;
    }

    protected String convertLocal(String shortBase, String longUrl) throws Exception {
        if (UriValidator.INSTANCE.validateURL(longUrl)) {
            String shortUrl = service.convertLocalUrl(shortBase, longUrl);
            LOGGER.info("Converted url to: " + shortUrl);
            return shortUrl;
        } else {
            throw new Exception("Invalid URL provided");
        }
    }

    protected String convertGiven(String shortBase, String longUrl) throws Exception {
        boolean validatedShort = UriValidator.INSTANCE.validateURL(shortBase);
        boolean validatedLong = UriValidator.INSTANCE.validateURL(longUrl);
        if (validatedShort && validatedLong) {
            String shortUrl = service.convertUrl(shortBase, longUrl);
            LOGGER.info("Converted url to: " + shortUrl);
            return shortUrl;
        } else {
            throw new Exception("Invalid URL provided");
        }
    }
}
