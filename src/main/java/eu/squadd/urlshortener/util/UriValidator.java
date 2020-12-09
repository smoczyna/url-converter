package eu.squadd.urlshortener.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author smoczyna
 */

public class UriValidator {
    public static final UriValidator INSTANCE = new UriValidator();
    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    private UriValidator() {
    }

    public boolean validateURL(String url) {
        Matcher m = URL_PATTERN.matcher(url);
        return m.matches();
    }

    public static String formatLocalURLToShort(String localURL) {
        return localURL.substring(0, localURL.lastIndexOf("/")).concat("/");
    }
}
