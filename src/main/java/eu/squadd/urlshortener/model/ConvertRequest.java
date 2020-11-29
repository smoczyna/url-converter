package eu.squadd.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConvertRequest {
    private String shortUrl;
    private String longUrl;

    @JsonCreator
    public ConvertRequest(@JsonProperty("shortUrl") String shortUrl, @JsonProperty("longUrl") String longUrl) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
