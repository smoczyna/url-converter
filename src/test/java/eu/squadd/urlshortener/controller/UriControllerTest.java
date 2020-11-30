package eu.squadd.urlshortener.controller;

import eu.squadd.urlshortener.UrlShortenerApplicationTests;
import eu.squadd.urlshortener.model.ConvertRequestLocal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UriControllerTest extends UrlShortenerApplicationTests {

    @Test
    public void testConfiguration() {
        this.testConfiguration("uriController");
    }

    @Test
    void shortenUrlTest1() throws Exception {
        String longUrl = "https://www.theguardian.com/football/blog/2020/nov/25/diego-maradona-argentina-child-genius-who-became-the-fulfilment-of-a-prophecy?utm_source=pocket-newtab-global-en-GB";
        ConvertRequestLocal request = new ConvertRequestLocal(longUrl);

        ResultActions response = this.mvc.perform(post("/url-converter/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJson(request)))
                .andExpect(status().isOk());

        String shortenedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(shortenedUrl, "Shortener always return something");
        Assert.hasText("http:localhosturl-converter/", shortenedUrl);

        String id = shortenedUrl.substring(shortenedUrl.lastIndexOf('/') + 1);
        this.mvc.perform(get("/url-converter/get/" + id)).andExpect(status().is3xxRedirection());
    }

    @Test
    void shortenInvalidURlTest() throws Exception {
        String longUrl = "htttps://theguardian.com/football/blog/2020/nov/25";
        ConvertRequestLocal request = new ConvertRequestLocal(longUrl);

        Exception exception = assertThrows(Exception.class, () -> {
        ResultActions response = this.mvc.perform(post("/url-converter/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.toJson(request)))
                    .andExpect(status().isOk());
        });
        assertEquals("Request processing failed; nested exception is java.lang.Exception: Invalid URL provided", exception.getMessage());
    }

    @Test
    void shortenUrlTest2() throws Exception {
        String longUrl = "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu";
        String request = String.format("{'url': '%s'}", longUrl);

        ResultActions response = this.mvc.perform(post("/url-converter/add-plain-text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk());

        String shortenedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(shortenedUrl, "Shortener always return something");
        Assert.hasText("http:localhosturl-converter/", shortenedUrl);

        String id = shortenedUrl.substring(shortenedUrl.lastIndexOf('/') + 1);
        this.mvc.perform(get("/url-converter/get/" + id)).andExpect(status().is3xxRedirection());
    }
}
