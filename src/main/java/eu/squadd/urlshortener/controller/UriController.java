package eu.squadd.urlshortener.controller;

import eu.squadd.urlshortener.model.ShortenRequest;
import eu.squadd.urlshortener.service.UriConverterService;
import eu.squadd.urlshortener.util.UriValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/shortener")
public class UriController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriController.class);

    private final UriConverterService service;

    public UriController(UriConverterService service) {
        this.service = service;
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public String shortenUrl(@RequestBody @Validated final ShortenRequest shortenRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to shorten: " + shortenRequest.getUrl());
        String longUrl = shortenRequest.getUrl();
        if (UriValidator.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            String shortenedUrl = service.shortenURL(localURL, shortenRequest.getUrl());
            LOGGER.info("Shortened url to: " + shortenedUrl);
            return shortenedUrl;
        }
        throw new Exception("Please enter a valid URL");
    }

    @GetMapping("/get/{id}")
    public RedirectView redirectUrl(@PathVariable String id) throws Exception { //}, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        LOGGER.info("Received shortened url to redirect: " + id);
        String redirectUrlString = service.getLongURLFromID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }
}
