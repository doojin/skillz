package lv.dmppka.skillz.collector.indeed.dom;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class IndeedDomParser {

    private static final String QUERY_SELECTOR_ADVERTISEMENT_LINK = "a[href^=/cmp/]";
    private static final String ATTRIBUTE_ABSOLUTE_URL = "abs:href";

    private static final String SELECTOR_JOB_TITLE = ".jobtitle > font";
    private static final String SELECTOR_COMPANY = ".company";
    private static final String SELECTOR_SUMMARY = "#job_summary";

    public List<String> getAdvertisementUrls(Document document) {
        Elements advertisementLinks = document.select(QUERY_SELECTOR_ADVERTISEMENT_LINK);
        return advertisementLinks.stream()
                .map(link -> link.attr(ATTRIBUTE_ABSOLUTE_URL))
                .filter(this::isJobLink)
                .collect(toList());
    }

    public String getTitle(Document document) {
        return getElementText(document, SELECTOR_JOB_TITLE);
    }

    public String getCompanyName(Document document) {
        return getElementText(document, SELECTOR_COMPANY);
    }

    public String getDescription(Document document) {
        return getElementText(document, SELECTOR_SUMMARY);
    }

    private boolean isJobLink(String link) {
        return link.contains("/jobs/");
    }

    private String getElementText(Document document, String selector) {
        return document.select(selector).first().html();
    }
}
