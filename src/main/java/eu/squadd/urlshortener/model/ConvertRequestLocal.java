package eu.squadd.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
