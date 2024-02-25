package edu.java.contributor.api;

import java.net.URL;

public interface InfoContributor {

    boolean isSupported(URL url);

    String getSource();

    Info getInfo(URL url);

}
