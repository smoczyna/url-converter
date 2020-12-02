package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.model.ConvertRequestLocal;
import eu.squadd.urlshortener.service.UriConverterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
 * first conversion controller
 * this one treats original user URL (the one it comes from)
 * as the base to create short URL
 */

@Api(value = "/url-converter", description = "URL Converter Controller")
@RestController
@RequestMapping("/url-converter")
public class UriController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UriController.class);

    public UriController(UriConverterService service) {
        super(service);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    @ApiOperation(value = "Submits long URL for conversion", notes = "Returns generated short ULR", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful conversion of long URL", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    public ResponseEntity<String> convertUrl(@RequestBody @Validated ConvertRequestLocal convertRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + convertRequest.getUrl());
        String localURL = request.getRequestURL().toString();
        return new ResponseEntity<>(this.convertLocal(localURL, convertRequest.getUrl()), HttpStatus.OK);
    }

    @PostMapping(value = "/add-plain-text")
    @ApiOperation(value = "Submits long URL for conversion as plain text", notes = "Returns generated short ULR", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful conversion of long URL", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    public ResponseEntity<String> convertUrl(@RequestBody final String strJsonConvertRequest, HttpServletRequest request) throws Exception {
        LOGGER.info("Received url to convert: " + strJsonConvertRequest);
        ConvertRequestLocal convertRequest = new Gson().fromJson(strJsonConvertRequest, ConvertRequestLocal.class);
        String localURL = request.getRequestURL().toString();
        return new ResponseEntity<>(this.convertLocal(localURL, convertRequest.getUrl()), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "Get long URL from the short one generated before", notes = "Returns original long URL", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found and returned long URL", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    public ResponseEntity<String> getLongUrl(@PathVariable String id) throws NoSuchElementException {
        LOGGER.info("Received uniqueID to redirect: " + id);
        String redirectUrlString = service.getLongUrlWithUniqueID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        return new ResponseEntity<>(redirectUrlString, HttpStatus.OK);
    }

    @GetMapping("/redirect/{id}")
    @ApiOperation(value = "Redirect short URL (generated before) to the original long location")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "Successfully found long URL and redirected the call to it", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    public RedirectView redirectShorUrl(@PathVariable String id, HttpServletRequest request) {
        String shortUrl = request.getHeader("short-url");
        if (shortUrl == null)
            throw new NoSuchElementException("Generated before Short URL need to be provided in the header: short-url");
        String redirectUrlString = service.getLongUrlWithUniqueID(id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }

}

