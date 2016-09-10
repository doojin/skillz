package lv.dmppka.skillz.collector.indeed.dom;

import org.apache.commons.codec.Charsets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IndeedDomParserTest {

    private IndeedDomParser domParser;

    @Before
    public void setUp() {
        domParser = new IndeedDomParser();
    }

    @Test
    public void getAdvertisementUrls() throws IOException {
        Document document = Jsoup.parse(
                this.getClass().getClassLoader().getResourceAsStream("indeed.com.advertisement.list.html"),
                Charsets.UTF_8.name(),
                "http://www.indeed.es");

        List<String> advertisementUrls = domParser.getAdvertisementUrls(document);
        assertThat(advertisementUrls.size(), equalTo(1));

        assertThat(
                advertisementUrls.get(0),
                equalTo("http://www.indeed.es/cmp/DSI-ERITIA/jobs/Analista-programador-J2EE-javascript-475a1321b42236d4?r=1&fccid=c907e6692197ab75"));
    }

    @Test
    public void getTitle() throws IOException {
        Document document = Jsoup.parse(
                this.getClass().getClassLoader().getResourceAsStream("indeed.com.advertisement.example.html"),
                Charsets.UTF_8.name(),
                "http://www.indeed.es");

        assertThat(domParser.getTitle(document), equalTo("Analista/Programador J2EE y JavaScript"));
    }

    @Test
    public void getDescription() throws IOException {
        Document document = Jsoup.parse(
                this.getClass().getClassLoader().getResourceAsStream("indeed.com.advertisement.example.html"),
                Charsets.UTF_8.name(),
                "http://www.indeed.es");

        assertThat(domParser.getDescription(document), equalTo("summary"));
    }

    @Test
    public void getCompanyName() throws IOException {
        Document document = Jsoup.parse(
                this.getClass().getClassLoader().getResourceAsStream("indeed.com.advertisement.example.html"),
                Charsets.UTF_8.name(),
                "http://www.indeed.es");

        assertThat(domParser.getCompanyName(document), equalTo("DSI ERITIA"));
    }
}