package lv.dmppka.skillz.collector.indeed.api;

import lv.dmppka.skillz.collector.http.DocumentFetcher;
import lv.dmppka.skillz.collector.indeed.IndeedUriBuilder;
import lv.dmppka.skillz.collector.indeed.dom.IndeedDomParser;
import lv.dmppka.skillz.common.Country;
import lv.dmppka.skillz.common.model.JobAdvertisement;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class IndeedApi {

    private static final String SITE_INDEED = "indeed.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(IndeedApi.class);

    private final IndeedUriBuilder uriBuilder;
    private final DocumentFetcher documentFetcher;
    private final IndeedDomParser domParser;

    @Autowired
    public IndeedApi(IndeedUriBuilder uriBuilder, DocumentFetcher documentFetcher, IndeedDomParser domParser) {
        this.uriBuilder = uriBuilder;
        this.documentFetcher = documentFetcher;
        this.domParser = domParser;
    }

    public List<JobAdvertisement> getJobAdvertisements(String query, Country country, int page) {
        Document jobListDocument;

        try { jobListDocument = getJobListDocument(query, country, page); }
        catch (IOException e) {
            LOGGER.error("Failed to receive job list for query: {}, country: {}, page: {}", query, country.getName(), page);
            return emptyList();
        }

        List<String> advertisementUrls =  domParser.getAdvertisementUrls(jobListDocument);
        return getJobAdvertisements(advertisementUrls);
    }

    private Document getJobListDocument(String query, Country country, int page) throws IOException {
        String uri = uriBuilder.buildJobListUrl(query, country, page);
        return documentFetcher.fetch(uri);
    }

    private List<JobAdvertisement> getJobAdvertisements(List<String> advertisementUrls) {
        return advertisementUrls.parallelStream()
                .map(this::extractJobAdvertisement)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private JobAdvertisement extractJobAdvertisement(String url) {
        try {
            Document advertisementDocument = documentFetcher.fetch(url);
            return buildAdvertisement(url, advertisementDocument);
        } catch (IOException e) {
            LOGGER.error("Failed to receive job advertisment from url: {}", url);
            return null;
        }
    }

    private JobAdvertisement buildAdvertisement(String url, Document document) {
        JobAdvertisement advertisement = new JobAdvertisement();
        advertisement.setUrl(url);
        advertisement.setSite(SITE_INDEED);
        advertisement.setTitle(domParser.getTitle(document));
        advertisement.setDescription(domParser.getDescription(document));
        advertisement.setCompany(domParser.getCompanyName(document));
        return advertisement;
    }
}
