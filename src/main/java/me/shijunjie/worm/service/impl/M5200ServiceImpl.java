package me.shijunjie.worm.service.impl;

import me.shijunjie.worm.bean.BookInfo;
import me.shijunjie.worm.bean.BookSearchRequest;
import me.shijunjie.worm.service.M5200Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class M5200ServiceImpl implements M5200Service {

    private static final String rootUrl = "https://m.5200.net";

    @Override
    public List<BookInfo> search(BookSearchRequest bookSearchRequest) {
        List<BookInfo> bookInfos = new ArrayList<>();
        String url = "https://m.5200.net/s.php?ie=gbk&s=5256649918672294880&q=" + bookSearchRequest.getBookName();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }
        Elements bookinfos = doc.getElementsByClass("bookinfo");
        bookinfos.forEach(bookinfo -> {
            String bookName = bookinfo.getElementsByTag("a").get(0).text();
            String link = bookinfo.getElementsByTag("a").get(0).attr("href");
            String author = bookinfo.getElementsByClass("author").text();
            String curChapters = bookinfo.getElementsByClass("update").get(0).getElementsByTag("a").get(0).text();
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookName(bookName);
            bookInfo.setLink(link);
            bookInfo.setAuthor(author);
            bookInfo.setCurChapters(curChapters);
            bookInfos.add(bookInfo);
        });
        return bookInfos;
    }

    @Override
    public void getBookContent(String link, BufferedOutputStream buffer) {
        String url = rootUrl + link;
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
            String firstChapterUrl = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd").get(0).getElementsByTag("a").attr("href");
            url = rootUrl + firstChapterUrl;
            getText(url, buffer, link);
            buffer.close();
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }
    }

    @Override
    public List<String> getChapters(String link) {
        List<String> chapters = new ArrayList<>();
        try {
            curPageChapters(link, link, chapters);
        } catch (IOException e) {
            throw new RuntimeException("连接失败");
        }
        return chapters;
    }

    @Override
    public Map<String,String> getContentHtml(String link) {
        Map<String,String> resultMap = new HashMap<>();
        String url = rootUrl + "/18_18146/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
            String contentLink = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd").get(0).getElementsByTag("a").get(0).attr("href");
            String title = doc.getElementsByTag("title").get(0).text();
            String content = readContent(contentLink);
            resultMap.put("title", title);
            resultMap.put("contentHtml", content);
        } catch (IOException e) {
            throw new RuntimeException("连接失败：" + e.getMessage());
        }
        return resultMap;
    }

    public String readContent(String link) throws IOException {
        String url = rootUrl + link;
        Document doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        String innerHtml = doc.getElementById("chaptercontent").html();
        return innerHtml;
    }

    private void curPageChapters(String link, String nextLink, List<String> chapters) throws IOException {
        String url = rootUrl + nextLink;
        Document doc = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        Elements chapterElements = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd");
        for(int index = 0; index < chapterElements.size(); index++) {
            chapters.add(chapterElements.get(index).text());
        }
        nextLink = doc.getElementsByClass("right").get(0).getElementsByTag("a").get(0).attr("href");
        if(!StringUtils.isEmpty(nextLink) && !link.equals(nextLink)) {
            curPageChapters(link, nextLink, chapters);
        }
    }

    private void getText(String chapterUrl, BufferedOutputStream buffer, String link) throws IOException {
        Document doc = Jsoup.connect(chapterUrl).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
        String nextLink = doc.getElementsByClass("Readpage_down js_page_down").get(0).attr("href");
        String nextUrl = rootUrl + nextLink;
        String innerHtml = doc.getElementsByClass("Readarea ReadAjax_content").get(0).html();
        String innerText = innerHtml.replaceAll("<br>", "") + "\n\n\n\n";
        buffer.write(innerText.getBytes("UTF-8"));
        buffer.flush();
        if(!link.equals(nextLink)) {
            getText(nextUrl, buffer, link);
        }
    }


}
