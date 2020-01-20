package me.shijunjie.test.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TestGetContent {
    private static final String rootUrl = "https://m.5200.net";


    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        String url = rootUrl + "/18_18146/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
            String firstChapterUrl = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd").get(0).getElementsByTag("a").attr("href");
            url = rootUrl + firstChapterUrl;
            String content = getText(url, stringBuilder, "/18_18146/");
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }


        System.out.println(doc);
    }

    private static String getText(String chapterUrl, StringBuilder stringBuilder, String link) throws IOException {
        System.out.println("getText:" + chapterUrl);
        Document doc = Jsoup.connect(chapterUrl).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        String nextLink = doc.getElementsByClass("Readpage_down js_page_down").get(0).attr("href");
        String nextUrl = rootUrl + nextLink;
        String innerHtml = doc.getElementsByClass("Readarea ReadAjax_content").get(0).html();
        String innerText = innerHtml.replaceAll("<br>", "");
        stringBuilder.append(innerText);
        stringBuilder.append("\n\n\n\n");
        if(!link.equals(nextLink)) {
            getText(nextUrl, stringBuilder, link);
        }
        return stringBuilder.toString();
    }
}
