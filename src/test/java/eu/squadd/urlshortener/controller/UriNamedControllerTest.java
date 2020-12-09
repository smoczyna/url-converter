package eu.squadd.urlshortener.controller;

import eu.squadd.urlshortener.UrlShortenerApplicationTests;
import eu.squadd.urlshortener.model.ConvertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;
import org.springframework.web.util.NestedServletException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UriNamedControllerTest extends UrlShortenerApplicationTests {

    @Test
    public void testConfiguration() {
        this.testConfiguration("uriController");
    }

    @Test
    void convertUrlTest1() throws Exception {
        String shortUrl = "www.this.is.hero";
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        ConvertRequest request = new ConvertRequest(shortUrl, longUrl);

        ResultActions response = this.mvc.perform(post("/url-converter-named/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJson(request)))
                .andExpect(status().isOk());

        String convertedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(convertedUrl, "Converter always returns something");
        Assert.hasText(shortUrl, convertedUrl);

        String id = convertedUrl.substring(convertedUrl.lastIndexOf('/') + 1);
        this.mvc.perform(get("/url-converter-named/get/" + "/" + id)
                .header("short-url", "www.this.is.hero"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void convertInvalidURlTest() {
        String shortUrl = "www.this.is.hero";
        String longUrl = "htttps://theguardian.com/football/blog/2020/nov/25";
        ConvertRequest request = new ConvertRequest(shortUrl, longUrl);

        Exception exception = assertThrows(Exception.class, () -> {
            this.mvc.perform(post("/url-converter-named/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.toJson(request)))
                    .andExpect(status().isOk());
        });
        assertEquals("Request processing failed; nested exception is java.lang.Exception: Invalid URL provided", exception.getMessage());
    }

    @Test
    void convertUrlTest2() throws Exception {
        String shortUrl = "www.this.is.hero";
        String longUrl = "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu";
        String request = String.format("{'shortUrl': '%s', 'longUrl': '%s'}", shortUrl, longUrl);

        ResultActions response = this.mvc.perform(post("/url-converter-named/add-plain-text")
                .content(request))
                .andExpect(status().isOk());

        String convertedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(convertedUrl, "Converter always returns something");
        Assert.hasText(shortUrl, convertedUrl);

        String id = convertedUrl.substring(convertedUrl.lastIndexOf('/') + 1);

        Exception exception = assertThrows(NestedServletException.class, () -> {
            this.mvc.perform(get("/url-converter-named/get/" + id))
                    .andExpect(status().is2xxSuccessful());
        });
        assertEquals("Request processing failed; nested exception is java.util.NoSuchElementException: Generated before Short URL need to be provided in the header: short-url", exception.getMessage());

        this.mvc.perform(get("/url-converter-named/redirect/" + "/" + id)
                .header("short-url", "www.this.is.hero"))
                .andExpect(status().is3xxRedirection());

        exception = assertThrows(NestedServletException.class, () -> {
            ResultActions failedResponse = this.mvc.perform(get("/url-converter-named/redirect/" + id));
        });
        assertEquals("Request processing failed; nested exception is java.util.NoSuchElementException: Generated before Short URL need to be provided in the header: short-url", exception.getMessage());
    }
}
