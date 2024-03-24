package edu.java.bot.processors;

import java.util.Collections;
import java.util.Map;

public interface TextProcessor {
    String process(String option, Map<String, String> keyWords);

    String process(String option, Map<String, String> keyWords, String defaultValue);

    default String process(String option) {
        return process(option, Collections.emptyMap());
    }

}
