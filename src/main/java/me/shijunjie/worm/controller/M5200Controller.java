package me.shijunjie.worm.controller;

import me.shijunjie.worm.bean.BookInfo;
import me.shijunjie.worm.bean.BookSearchRequest;
import me.shijunjie.worm.bean.CommonResponse;
import me.shijunjie.worm.service.M5200Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/5200")
public class M5200Controller {

    @Autowired
    private M5200Service m5200Service;

    @ResponseBody
    @PostMapping("/search")
    public CommonResponse<List<BookInfo>> search(@RequestBody BookSearchRequest bookSearchRequest) {
        return CommonResponse.success(m5200Service.search(bookSearchRequest));
    }

    @GetMapping("/downloadTxt")
    public void downloadTxt(HttpServletResponse response, String bookName, String link) {
        String fileName = bookName + ".txt";
        response.setContentType("text/plain");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ServletOutputStream outputStream = null;
        BufferedOutputStream buffer = null;

        try {
            outputStream = response.getOutputStream();
            buffer = new BufferedOutputStream(outputStream);
            m5200Service.getBookContent(link, buffer);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @GetMapping("/listChapters")
    public CommonResponse<List<String>> listChapters(String link) {
        return CommonResponse.success(m5200Service.getChapters(link));
    }

    @GetMapping("/lineRead")
    public String lineRead(String link, Map<String,String> map) {
        Map<String, String> resultMap = m5200Service.getContentHtml(link);
        map.put("title", resultMap.get("title"));
        map.put("contentHtml", resultMap.get("contentHtml"));
        return "content";
    }

}
