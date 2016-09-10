package lv.dmppka.skillz.collector.indeed;

import lv.dmppka.skillz.common.Country;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IndeedUriBuilderTest {

    private IndeedUriBuilder uriBuilder;

    @Before
    public void setUp() {
        uriBuilder = new IndeedUriBuilder();
    }

    @Test
    public void buildJobListUrl_forSpain() {
        String url = uriBuilder.buildJobListUrl("Java", Country.SPAIN, 2);
        assertThat(url, equalTo("http://www.indeed.es/ofertas?q=Java&l=Spain&start=10"));
    }

    @Test
    public void buildJobListUrl_forUnitedStates() {
        String url = uriBuilder.buildJobListUrl("Java", Country.US, 2);
            assertThat(url, equalTo("http://www.indeed.com/jobs?q=Java&l=United+States&start=10"));
    }
}