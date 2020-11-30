package eu.squadd.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * author smoczyna
 * this payload class is used by so called 'local convertor'
 * in this case short URL is build from the original request
 */
public class ConvertRequestLocal {
    private String url;

    @JsonCreator
    public ConvertRequestLocal(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
