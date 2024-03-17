package edu.java.provider.sources;

import edu.java.configuration.ApplicationConfig;
import edu.java.provider.api.Info;
import edu.java.provider.api.WebClientInfoProvider;
import edu.java.provider.sources.response.GithubInfoResponse;
import java.net.URL;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubInfoProvider extends WebClientInfoProvider {

    private static final String GITHUB_API_LINK = "https://api.github.com";

    private static final Pattern REPOSITORY_PATTERN = Pattern.compile("https://github.com/(.+)/(.+)");

    public GithubInfoProvider(@Value("${provider.github.url}") String apiUrl) {
        super(apiUrl);
    }

    public GithubInfoProvider() {
        super(GITHUB_API_LINK);
    }

    @Autowired
    public GithubInfoProvider(ApplicationConfig applicationConfig) {
        super(WebClient.builder()
            .baseUrl(GITHUB_API_LINK)
            .defaultHeaders(headers -> {
                if (applicationConfig.githubToken() != null) {
                    headers.set("Authorization", "Bearer " + applicationConfig.githubToken());
                }
            })
            .build()
        );
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
        Info infoNull = new Info(null, null, null, null);
        if (!isSupported(url)) {
            return infoNull;
        }
        var info = executeRequest(
            "/repos/" + url.getPath(),
            GithubInfoResponse.class,
            GithubInfoResponse.EMPTY
        );
        if (info == null || info.equals(GithubInfoResponse.EMPTY)) {
            return infoNull;
        }
        return new Info(url, info.fullName(), info.description(), info.lastModified());
    }
}
