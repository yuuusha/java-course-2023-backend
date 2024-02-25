package edu.java.contributor.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.contributor.api.Info;
import edu.java.contributor.api.WebClientInfoContributor;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubInfoContributor extends WebClientInfoContributor {

    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    @Autowired
    public GithubInfoContributor(@Value("${contributor.github.url}") String apiUrl) {
        super(apiUrl);
    }

    public GithubInfoContributor() {
        super("https://api.github.com");
    }

    @Override
    public boolean isSupported(URL url) {
        return REPOSITORY_PATTERN.matcher(url.toString()).matches();
    }

    @Override
    public String getSource() {
        return "github";
    }

    @Override
    public Info getInfo(URL url) {
        if (!isSupported(url)) {
            return null;
        }
        var info = executeRequest(
            "/repos/" + url.getPath(),
            GithubInfoResponse.class,
            GithubInfoResponse.EMPTY
        );
        if (info == null || info.equals(GithubInfoResponse.EMPTY)) {
            return null;
        }
        return new Info(url, info.fullName(), info.description(), info.lastModified());
    }

    private record GithubInfoResponse(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("updated_at")
        OffsetDateTime lastModified,
        String description
    ) {
        public static final GithubInfoResponse EMPTY = new GithubInfoResponse(null, null, null);
    }
}
