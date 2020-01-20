package me.shijunjie.worm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(Map<String,String> map) {
        map.put("hello", "你好");
        return "test";
    }

}
