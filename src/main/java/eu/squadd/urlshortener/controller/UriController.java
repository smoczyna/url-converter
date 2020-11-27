package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.model.ConvertRequest;
import eu.squadd.urlshortener.service.UriConverterService;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/shortener")
public class UriController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriController.class);

    private final UriConverterService service;

    public UriController(UriConverterService service) {
        this.service = service;
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String shortenUrl(@RequestBody @Validated final ConvertRequest convertRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + convertRequest.getUrl());
        String longUrl = convertRequest.getUrl();
        if (UriValidator.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            String shortenedUrl = service.shortenURL(localURL, convertRequest.getUrl());
            LOGGER.info("Shortened url to: " + shortenedUrl);
            return shortenedUrl;
        }
        throw new Exception("Please enter a valid URL");
    }

    @PostMapping(value = "/add-plain-text", consumes = "application/json")
    public String shortenUrl(@RequestBody final String shortenJsonRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + shortenJsonRequest);
        ConvertRequest convertRequest = new Gson().fromJson(shortenJsonRequest, ConvertRequest.class);
        String longUrl = convertRequest.getUrl();
        if (UriValidator.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            String shortenedUrl = service.shortenURL(localURL, longUrl);
            LOGGER.info("Shortened url to: " + shortenedUrl);
            return shortenedUrl;
        }
        throw new Exception("Please enter a valid URL");
    }

    @GetMapping("/get/{id}")
    public RedirectView redirectUrl(@PathVariable String id) throws NoSuchElementException {
        LOGGER.info("Received shortened url to redirect: " + id);
        String redirectUrlString = service.getLongUrlWithUniqueID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }

//    @GetMapping("/get/{key}")
//    public RedirectView redirectUrl(@PathVariable String key) throws NoSuchElementException {
//        LOGGER.info("Received shortened url to redirect: " + key);
//        String redirectUrlString = service.getLongUrlWithKey(key);
//        LOGGER.info("Original URL: " + redirectUrlString);
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("http://" + redirectUrlString);
//        return redirectView;
//    }
}

