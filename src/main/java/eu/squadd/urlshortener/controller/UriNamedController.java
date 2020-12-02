package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.model.ConvertRequest;
import eu.squadd.urlshortener.service.UriConverterService;
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

@Api(value = "/url-converter-nam3e", description = "Named URL Converter Controller, user can pass short URL they use")
@RestController
@RequestMapping("/url-converter-named")
public class UriNamedController extends AbstractController {

    public UriNamedController(UriConverterService service) {
        super(service);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<String> convertUrl(@RequestBody @Validated final ConvertRequest convertRequest) throws Exception {
        LOGGER.info("Received url to convert: " + convertRequest.getLongUrl());
        return new ResponseEntity<>(this.convertGiven(convertRequest.getShortUrl(), convertRequest.getLongUrl()), HttpStatus.OK);
    }

    @ApiOperation(value = "Submits long URL for conversion as a text", notes = "Returns generated short ULR", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful conversion of long URL", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @PostMapping(value = "/add-plain-text")
    public ResponseEntity<String> convertUrl(@RequestBody final String strJsonConvertRequest) throws Exception {
        LOGGER.info("Received url to convert: " + strJsonConvertRequest);
        ConvertRequest convertRequest = new Gson().fromJson(strJsonConvertRequest, ConvertRequest.class);
        return new ResponseEntity<>(this.convertGiven(convertRequest.getShortUrl(), convertRequest.getLongUrl()), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<String> redirectUrl(@PathVariable String id, HttpServletRequest request) throws NoSuchElementException {
        String shortUrl = request.getHeader("short-url");
        if (shortUrl == null) throw new NoSuchElementException("Generated before Short URL need to be provided in the header: short-url");
        String redirectUrlString = service.getNamedLongUrlWithUniqueID(shortUrl, id);
        LOGGER.info("Original URL: " + redirectUrlString);
        return new ResponseEntity<>(redirectUrlString, HttpStatus.OK);
    }

    @GetMapping("/redirect/{id}")
    public RedirectView redirectShorUrl(@PathVariable String id, HttpServletRequest request) throws NoSuchElementException {
        LOGGER.info("Received uniqueID to redirect: " + id);
        String shortUrl = request.getHeader("short-url");
        if (shortUrl == null) throw new NoSuchElementException("Generated before Short URL need to be provided in the header: short-url");
        String redirectUrlString = service.getNamedLongUrlWithUniqueID(shortUrl, id);
        LOGGER.info("Original URL: " + redirectUrlString);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + redirectUrlString);
        return redirectView;
    }
}
