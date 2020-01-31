package me.shijunjie.worm.controller;

import me.shijunjie.worm.bean.BookInfo;
import me.shijunjie.worm.bean.BookSearchRequest;
import me.shijunjie.worm.bean.CommonResponse;
import me.shijunjie.worm.service.ZonghengService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/zongheng")
public class ZonghengController {

    @Autowired
    private ZonghengService zonghengService;

    @ResponseBody
    @PostMapping("/search")
    public CommonResponse<List<BookInfo>> search(@RequestBody BookSearchRequest bookSearchRequest) {
        return CommonResponse.success(zonghengService.search(bookSearchRequest));
    }

    @ResponseBody
    @GetMapping("/listChapters")
    public CommonResponse<List<String>> listChapters(String link) {
        return CommonResponse.success(zonghengService.getChapters(link));
    }
/*

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



    @GetMapping("/lineRead")
    public String lineRead(String link, Map<String,String> map) {
        Map<String, String> resultMap = m5200Service.getContentHtml(link);
        map.put("contentHtml", resultMap.get("contentHtml"));
        String prevLink = "/5200/readContent?link="+resultMap.get("prevLink");
        String nextLink = "/5200/readContent?link="+resultMap.get("nextLink");
        if(!prevLink.endsWith("html")) {
            prevLink = "#";
        }
        if(!nextLink.endsWith("html")) {
            nextLink = "#";
        }
        map.put("prevLink", prevLink);
        map.put("nextLink", nextLink);
        return "content";
    }

    @GetMapping("/readContent")
    public String readContent(String link, Map<String,String> map) {
        Map<String, String> resultMap = m5200Service.readContent(link);
        map.put("contentHtml", resultMap.get("contentHtml"));
        String prevLink = "/5200/readContent?link="+resultMap.get("prevLink");
        String nextLink = "/5200/readContent?link="+resultMap.get("nextLink");
        if(!prevLink.endsWith("html")) {
            prevLink = "#";
        }
        if(!nextLink.endsWith("html")) {
            nextLink = "#";
        }
        map.put("prevLink", prevLink);
        map.put("nextLink", nextLink);
        return "content";
    }
*/

}
