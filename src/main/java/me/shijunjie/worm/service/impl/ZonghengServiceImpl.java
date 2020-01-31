package me.shijunjie.worm.service.impl;

import me.shijunjie.worm.bean.BookInfo;
import me.shijunjie.worm.bean.BookSearchRequest;
import me.shijunjie.worm.service.M5200Service;
import me.shijunjie.worm.service.ZonghengService;
import me.shijunjie.worm.utils.JSoupUtils;
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
public class ZonghengServiceImpl implements ZonghengService {

    private static final String rootUrl = "https://m.5200.net";

    @Override
    public List<BookInfo> search(BookSearchRequest bookSearchRequest) {
        List<BookInfo> bookInfos = new ArrayList<>();
        String url = "http://search.zongheng.com/s?keyword=" + bookSearchRequest.getBookName();
        Document doc = JSoupUtils.connect(url);
        Elements bookinfos = doc.getElementsByClass("fl se-result-infos");
        bookinfos.forEach(bookinfo -> {
            String bookName = bookinfo.getElementsByClass("tit").get(0).text();
            String link = bookinfo.getElementsByClass("tit").get(0).getElementsByTag("a").get(0).attr("href");
            String author = bookinfo.getElementsByClass("bookinfo").get(0).getElementsByTag("a").get(0).text();
            String bookStatus = bookinfo.getElementsByClass("bookinfo").get(0).getElementsByTag("span").get(0).text();
            String totalWordNumber = bookinfo.getElementsByClass("bookinfo").get(0).getElementsByTag("span").get(1).text();
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookName(bookName);
            bookInfo.setLink(link);
            bookInfo.setAuthor(author);
            bookInfo.setBookStatus(bookStatus);
            bookInfo.setTotalWordNumber(totalWordNumber);
            bookInfos.add(bookInfo);
        });
        return bookInfos;
    }

    @Override
    public void getBookContent(String link, BufferedOutputStream buffer) {
        String url = rootUrl + link;
        Document doc = JSoupUtils.connect(url);
        try {
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
    public Map<String, String> getContentHtml(String link) {
        Map<String, String> resultMap = new HashMap<>();
        String url = rootUrl + "/18_18146/";
        Document doc = JSoupUtils.connect(url);
        String contentLink = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd").get(0).getElementsByTag("a").get(0).attr("href");
        String title = doc.getElementsByTag("title").get(0).text();
        Map<String, String> contentMap = readContent(contentLink);
        resultMap.put("title", title);
        resultMap.put("contentHtml", contentMap.get("contentHtml"));
        resultMap.put("prevLink", contentMap.get("prevLink"));
        resultMap.put("nextLink", contentMap.get("nextLink"));
        return resultMap;
    }

    @Override
    public Map<String, String> readContent(String link) {
        Map<String, String> resultMap = new HashMap<>();
        String url = rootUrl + link;
        Document doc = JSoupUtils.connect(url);
        String innerHtml = doc.getElementById("chaptercontent").html();
        String prevLink = doc.getElementById("pb_prev").attr("href");
        String nextLink = doc.getElementById("pb_next").attr("href");
        resultMap.put("contentHtml", innerHtml);
        resultMap.put("prevLink", prevLink);
        resultMap.put("nextLink", nextLink);
        return resultMap;
    }

    private void curPageChapters(String link, String nextLink, List<String> chapters) throws IOException {
        nextLink = readChapters(nextLink, chapters);
        while (!StringUtils.isEmpty(nextLink) && !link.equals(nextLink)) {
            nextLink = readChapters(nextLink, chapters);
        }
    }

    private String readChapters(String nextLink, List<String> chapters) throws IOException {
        String url = rootUrl + nextLink;
        Document doc = JSoupUtils.connect(url);
        Elements chapterElements = doc.getElementsByClass("book_last").get(1).getElementsByTag("dd");
        for (int index = 0; index < chapterElements.size(); index++) {
            chapters.add(chapterElements.get(index).text());
        }
        nextLink = doc.getElementsByClass("right").get(0).getElementsByTag("a").get(0).attr("href");
        return nextLink;
    }

    private void getText(String chapterUrl, BufferedOutputStream buffer, String link) throws IOException {
        String nextUrl = writeTxt(chapterUrl, buffer);
        while (!(rootUrl + link).equals(nextUrl)) {
            nextUrl = writeTxt(nextUrl, buffer);
        }
    }

    private String writeTxt(String chapterUrl, BufferedOutputStream buffer) throws IOException {
        Document doc = JSoupUtils.connect(chapterUrl);
        String nextLink = doc.getElementsByClass("Readpage_down js_page_down").get(0).attr("href");
        String nextUrl = rootUrl + nextLink;
        String innerHtml = doc.getElementsByClass("Readarea ReadAjax_content").get(0).html();
        String innerText = innerHtml.replaceAll("<br>", "") + "\n\n\n\n";
        buffer.write(innerText.getBytes("UTF-8"));
        buffer.flush();
        return nextUrl;
    }


}
