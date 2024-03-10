package edu.java.provider.api;

import java.net.URL;

public interface InfoProvider {

    boolean isSupported(URL url);

    String getSource();

    Info getInfo(URL url);

}
