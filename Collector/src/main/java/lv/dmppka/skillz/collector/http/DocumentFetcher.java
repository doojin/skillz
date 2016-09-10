package lv.dmppka.skillz.collector.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DocumentFetcher {

    public Document fetch(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
