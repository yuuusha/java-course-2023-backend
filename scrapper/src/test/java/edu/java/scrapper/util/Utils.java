package edu.java.scrapper.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    @SneakyThrows
    public static String readAll(String fileName) {
        return new String(Utils.class.getResourceAsStream(fileName).readAllBytes());
    }

}

