package lv.dmppka.skillz.collector;

import lv.dmppka.skillz.collector.indeed.api.IndeedApi;
import lv.dmppka.skillz.common.Country;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        IndeedApi indeedApi = context.getBean(IndeedApi.class);
        indeedApi.getJobAdvertisements("Java", Country.SPAIN, 1).forEach(System.out::println);
    }
}
