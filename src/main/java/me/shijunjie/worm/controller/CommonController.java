package me.shijunjie.worm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommonController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/5200")
    public String to5200() {
        return "5200";
    }

    @RequestMapping("/zongheng")
    public String toZongHeng() {
        return "zongheng";
    }
}
