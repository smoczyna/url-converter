package eu.squadd.urlshortener.controller;

import com.google.gson.Gson;
import eu.squadd.urlshortener.UrlShortenerApplicationTests;
import eu.squadd.urlshortener.model.ConvertRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

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
        ConvertRequest request = new ConvertRequest(longUrl);

        ResultActions response = this.mvc.perform(post("/shortener/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.toJson(request)))
                .andExpect(status().isOk());

        String shortenedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(shortenedUrl, "Shortener always return something");
        Assert.hasText("http:localhostshortener/", shortenedUrl);

        String id = shortenedUrl.substring(shortenedUrl.lastIndexOf('/') + 1);
        response = this.mvc.perform(get("/shortener/get/" + id))
                .andExpect(status().is3xxRedirection());
//                .andExpect(MockMvcResultMatchers.view().name(longUrl));
    }

    @Test
    void shortenUrlTest2() throws Exception {
        String longUrl = "https://www.llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogochuchaf.eu";
        String request = String.format("{'url': '%s'}", longUrl);
        ResultActions response = this.mvc.perform(post("/shortener/add-plain-text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk());

        String shortenedUrl = response.andReturn().getResponse().getContentAsString();
        Assert.notNull(shortenedUrl, "Shortener always return something");
        Assert.hasText("http:localhostshortener/", shortenedUrl);
    }

    @Test
    void redirectUrl1() throws Exception {
        String id = "q";
        ResultActions response = this.mvc.perform(get("/shortener/get/" + id)).andExpect(status().is3xxRedirection());
    }
}
