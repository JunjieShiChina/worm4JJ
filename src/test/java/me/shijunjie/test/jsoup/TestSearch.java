package me.shijunjie.test.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSearch {



    public static void main(String[] args) throws Exception {
        Map<String, String> bookNameAndLinks = new HashMap<>();
        Document doc = Jsoup.connect("https://m.5200.net/s.php?ie=gbk&s=5256649918672294880&q=重生").header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        Elements bookinfos = doc.getElementsByClass("bookinfo");
        bookinfos.forEach(bookinfo -> {
            String bookName = bookinfo.getElementsByTag("a").get(0).text();
            String link = bookinfo.getElementsByTag("a").get(0).attr("href");
            String author = bookinfo.getElementsByClass("author").text();
            String curChapters = bookinfo.getElementsByClass("update").get(0).getElementsByTag("a").get(0).text();
            bookNameAndLinks.put(bookName, link);
        });
        System.out.println(bookNameAndLinks);
    }







}
