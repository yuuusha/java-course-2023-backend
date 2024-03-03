package edu.java.bot.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLService {

    private URLService() {
    }

    public static URL createURL(String link) {
        try {
            return URI.create(link).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
