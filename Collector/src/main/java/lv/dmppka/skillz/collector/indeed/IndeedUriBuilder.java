package lv.dmppka.skillz.collector.indeed;

import lv.dmppka.skillz.common.Country;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
public class IndeedUriBuilder {
    private static final String SCHEME_HTTP = "http";

    private static final String HOST_US = "www.indeed.com";
    private static final String HOST_SPANISH = "www.indeed.es";

    private static final String PATH_JOB_LIST_SPANISH = "/ofertas";
    private static final String PATH_JOB_LIST_US = "/jobs";

    private static final String PARAM_KEY_QUERY = "q";
    private static final String PARAM_KEY_LOCATION = "l";
    private static final String PARAM_KEY_START = "start";

    private static final int ADS_PER_PAGE = 10;

    public String buildJobListUrl(String query, Country country, int page) {
        String url = null;
        try {
            url = new URIBuilder()
                    .setScheme(SCHEME_HTTP)
                    .setHost(getHost(country))
                    .setPath(getPath(country))
                    .setParameter(PARAM_KEY_QUERY, query)
                    .setParameter(PARAM_KEY_LOCATION, country.getName())
                    .setParameter(PARAM_KEY_START, getStartParameter(page))
                    .build()
                    .toString();
        } catch (URISyntaxException ignored) {}

        return url;
    }

    private String getHost(Country country) {
        switch (country) {
            case SPAIN: return HOST_SPANISH;
            default: return HOST_US;
        }
    }

    private String getPath(Country country) {
        switch (country) {
            case SPAIN: return PATH_JOB_LIST_SPANISH;
            default: return PATH_JOB_LIST_US;
        }
    }

    private String getStartParameter(int page) {
        int start = (page - 1) * ADS_PER_PAGE;
        return Integer.toString(start);
    }
}
