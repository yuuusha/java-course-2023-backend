package edu.java.exception;

import java.net.URL;
import org.springframework.http.HttpStatus;

public class LinkIsNotSupportedException extends ScrapperException {

    public LinkIsNotSupportedException(URL link) {
        super(
            "Link is not supported",
            String.format("Link %s is not supported", link.toString()),
            HttpStatus.BAD_REQUEST
        );
    }
}
