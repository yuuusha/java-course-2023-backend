package edu.java.exception;

import java.net.URL;
import org.springframework.http.HttpStatus;

public class LinkAlreadyAddedException extends ScrapperException {
    public LinkAlreadyAddedException(URL link) {
        super("Link already added", String.format("Link %s already added", link.toString()), HttpStatus.BAD_REQUEST);
    }
}
