package edu.java.provider.sources;

import edu.java.provider.api.Info;
import edu.java.provider.api.WebClientInfoProvider;
import edu.java.provider.sources.response.StackOverflowInfoResponse;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;

public class StackOverflowInfoProvider extends WebClientInfoProvider {

    private static final String STACKOVERFLOW_API_LINK = "https://api.stackexchange.com/2.3";
    private static final Pattern QUESTION_PATTERN = Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public StackOverflowInfoProvider(
        @Value("${provider.stackoverflow.url}") String apiUrl
    ) {
        super(apiUrl);
    }

    public StackOverflowInfoProvider() {
        super(STACKOVERFLOW_API_LINK);
    }

    @Override
    public boolean isSupported(URL url) {
        return QUESTION_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public String getSource() {
        return "stackoverflow";
    }

    @Override
    public Info getInfo(URL url) {
        Info infoNull = new Info(null, null, null, null);
        Matcher matcher = QUESTION_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return infoNull;
        }
        var questionId = matcher.group(1);
        var info = executeRequest(
            "/questions/" + questionId + "?site=stackoverflow",
            StackOverflowInfoResponse.class,
            StackOverflowInfoResponse.EMPTY
        );
        if (info == null || info.equals(StackOverflowInfoResponse.EMPTY) || info.items().length == 0) {
            return infoNull;
        }
        return new Info(url, info.items()[0].title(), null, info.items()[0].lastModified());
    }
}
