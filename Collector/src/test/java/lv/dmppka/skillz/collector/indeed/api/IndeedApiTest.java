package lv.dmppka.skillz.collector.indeed.api;

import lv.dmppka.skillz.collector.http.DocumentFetcher;
import lv.dmppka.skillz.collector.indeed.IndeedUriBuilder;
import lv.dmppka.skillz.collector.indeed.dom.IndeedDomParser;
import lv.dmppka.skillz.common.Country;
import lv.dmppka.skillz.common.model.JobAdvertisement;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class IndeedApiTest {

    private IndeedUriBuilder uriBuilder;
    private DocumentFetcher documentFetcher;
    private IndeedDomParser domParser;

    private IndeedApi indeedApi;

    @Before
    public void setUp() {
        uriBuilder = mock(IndeedUriBuilder.class);
        documentFetcher = mock(DocumentFetcher.class);
        domParser = mock(IndeedDomParser.class);

        indeedApi = new IndeedApi(uriBuilder, documentFetcher, domParser);
    }

    @Test
    public void getJobAdvertisements_shouldReturnEmptyArrayIfFailedToGetJobList() throws IOException {
        doReturn("dummy-url.com").when(uriBuilder).buildJobListUrl("Java", Country.SPAIN, 1);
        doThrow(IOException.class).when(documentFetcher).fetch("dummy-url.com");

        List<JobAdvertisement> advertisements = indeedApi.getJobAdvertisements("Java", Country.SPAIN, 1);

        assertThat(advertisements.size(), equalTo(0));
    }

    @Test
    public void getJobAdvertisements_shouldReturnJobAdvertisements() throws IOException {
        doReturn("dummy-url.com").when(uriBuilder).buildJobListUrl("Java", Country.SPAIN, 1);

        Document jobListDocument = mock(Document.class);
        doReturn(jobListDocument).when(documentFetcher).fetch("dummy-url.com");

        doReturn(asList(
                "dummy-link1.com",
                "dummy-link2.com",
                "dummy-link3.com"
        )).when(domParser).getAdvertisementUrls(jobListDocument);

        Document dummyDocument1 = mock(Document.class);
        doReturn(dummyDocument1).when(documentFetcher).fetch("dummy-link1.com");

        doThrow(IOException.class).when(documentFetcher).fetch("dummy-link2.com");

        Document dummyDocument3 = mock(Document.class);
        doReturn(dummyDocument3).when(documentFetcher).fetch("dummy-link3.com");

        doReturn("title1").when(domParser).getTitle(dummyDocument1);
        doReturn("description1").when(domParser).getDescription(dummyDocument1);
        doReturn("companyName1").when(domParser).getCompanyName(dummyDocument1);

        doReturn("title3").when(domParser).getTitle(dummyDocument3);
        doReturn("description3").when(domParser).getDescription(dummyDocument3);
        doReturn("companyName3").when(domParser).getCompanyName(dummyDocument3);

        List<JobAdvertisement> advertisements = indeedApi.getJobAdvertisements("Java", Country.SPAIN, 1);

        assertThat(advertisements.size(), equalTo(2));
        assertThat(advertisements.get(0).getTitle(), equalTo("title1"));
        assertThat(advertisements.get(0).getDescription(), equalTo("description1"));
        assertThat(advertisements.get(0).getCompany(), equalTo("companyName1"));
        assertThat(advertisements.get(1).getTitle(), equalTo("title3"));
        assertThat(advertisements.get(1).getDescription(), equalTo("description3"));
        assertThat(advertisements.get(1).getCompany(), equalTo("companyName3"));
    }
}