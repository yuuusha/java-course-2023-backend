package edu.java.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public final class URLCreator {
    private URLCreator() {
    }

    public static URL createURL(String link) {
        try {
            return URI.create(link).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
