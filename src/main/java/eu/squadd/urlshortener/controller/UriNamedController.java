package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.model.ConvertRequest;
import eu.squadd.urlshortener.service.UriConverterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.NoSuchElementException;

/**
 * author smoczyna
 */

@RestController
@RequestMapping("/shortener-named")
public class UriNamedController extends AbstractController {

    public UriNamedController(UriConverterService service) {
        super(service);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> convertUrl(@RequestBody @Validated final ConvertRequest convertRequest) throws Exception {
        LOGGER.info("Received url to convert: " + convertRequest.getLongUrl());
        return new ResponseEntity<>(this.convertGiven(convertRequest.getShortUrl(), convertRequest.getLongUrl()), HttpStatus.OK);
    }

    @PostMapping(value = "/add-plain-text")
    public ResponseEntity<String> convertUrl(@RequestBody final String strJsonConvertRequest) throws Exception {
        LOGGER.info("Received url to convert: " + strJsonConvertRequest);
        ConvertRequest convertRequest = new Gson().fromJson(strJsonConvertRequest, ConvertRequest.class);
        return new ResponseEntity<>(this.convertGiven(convertRequest.getShortUrl(), convertRequest.getLongUrl()), HttpStatus.OK);
    }

    @GetMapping("/get/{shortUrl}/{id}")
    public RedirectView redirectUrl(@PathVariable String shortUrl, @PathVariable String id) throws NoSuchElementException {
        LOGGER.info("Received uniqueID to redirect: " + id);
        String redirectUrlString = service.getNamedLongUrlWithUniqueID(shortUrl, id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }
}
