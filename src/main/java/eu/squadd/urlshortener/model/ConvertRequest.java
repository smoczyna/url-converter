package eu.squadd.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * author smoczyna
 * this payload is used by so called 'named convertor'
 * it this case short URL comes from the request (given by user)
 */
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

    public String getLongUrl() {
        return longUrl;
    }

}
