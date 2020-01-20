package me.shijunjie.test.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CommonTest {

    private static final String rootUrl = "https://m.5200.net";

    @Test
    public void testGetChapters() {
        String url = rootUrl + "/18_18146/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
            Elements chapterElements = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd");
            for(int index = 0; index < chapterElements.size(); index++) {
                System.out.println(chapterElements.get(index).text());
            }
            String nextLink = doc.getElementsByClass("right").get(0).getElementsByTag("a").get(0).attr("href");
            System.out.println(doc);
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }
    }

    @Test
    public void testLineRead() {
        String url = rootUrl + "/18_18146/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
            String contentLink = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd").get(0).getElementsByTag("a").get(0).attr("href");
            readContent(contentLink);
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }
    }

    public String readContent(String link) throws IOException {
        String url = rootUrl + link;
        Document doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        String innerHtml = doc.getElementById("chaptercontent").html();
        return innerHtml;
    }

}
