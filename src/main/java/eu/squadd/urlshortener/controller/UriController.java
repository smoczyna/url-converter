package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.model.ConvertRequestLocal;
import eu.squadd.urlshortener.service.UriConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

/**
 * author smoczyna
 */

@RestController
@RequestMapping("/url-converter")
public class UriController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriController.class);

    public UriController(UriConverterService service) {
        super(service);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<String> convertUrl(@RequestBody @Validated ConvertRequestLocal convertRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + convertRequest.getUrl());
        String localURL = request.getRequestURL().toString();
        return new ResponseEntity<>(this.convertLocal(localURL, convertRequest.getUrl()), HttpStatus.OK);
    }

    @PostMapping(value = "/add-plain-text")
    public ResponseEntity<String> convertUrl(@RequestBody final String strJsonConvertRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + strJsonConvertRequest);
        ConvertRequestLocal convertRequest = new Gson().fromJson(strJsonConvertRequest, ConvertRequestLocal.class);
        String localURL = request.getRequestURL().toString();
        return new ResponseEntity<>(this.convertLocal(localURL, convertRequest.getUrl()), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public RedirectView redirectUrl(@PathVariable String id) throws NoSuchElementException {
        LOGGER.info("Received uniqueID to redirect: " + id);
        String redirectUrlString = service.getLongUrlWithUniqueID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }
}

