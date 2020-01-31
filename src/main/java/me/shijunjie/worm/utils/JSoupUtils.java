package me.shijunjie.worm.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JSoupUtils {
    public static Document connect(String url) {
        Document document = null;
        int retryCnt = 0;
        while(retryCnt < 3) {
            try {
                document = Jsoup.connect(url).header("Sec-Fetch-Mode", "cors").timeout(4000).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36").get();
                return document;
            } catch (IOException e) {
                retryCnt ++;
                if(retryCnt > 2) {
                    throw new RuntimeException("连接失败：" + e.getMessage());
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
        return document;
    }
}
