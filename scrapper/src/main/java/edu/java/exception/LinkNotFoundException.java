package edu.java.exception;

import java.net.URL;
import org.springframework.http.HttpStatus;

public class LinkNotFoundException extends ScrapperException {

    private static final String LINK_IS_NOT_FOUND = "Link is not found";

    public LinkNotFoundException(URL url) {
        super(LINK_IS_NOT_FOUND + ": " + url, LINK_IS_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public LinkNotFoundException(Long linkId) {
        super(LINK_IS_NOT_FOUND + ": " + linkId, LINK_IS_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}

