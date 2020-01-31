package me.shijunjie.worm.service;

import me.shijunjie.worm.bean.BookInfo;
import me.shijunjie.worm.bean.BookSearchRequest;

import java.io.BufferedOutputStream;
import java.util.List;
import java.util.Map;

public interface ZonghengService {

    List<BookInfo> search(BookSearchRequest bookSearchRequest);

    void getBookContent(String link, BufferedOutputStream buffer);

    List<String> getChapters(String link);

    Map<String,String> getContentHtml(String link);

    Map<String, String> readContent(String link);
}
