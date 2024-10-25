package com.codechef.codechef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    // test용
    @GetMapping("/test")
    public String test() {
        return "/codechef/test";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String main() {
        return "/codechef/main";
    }

    // 검색 페이지
    @GetMapping("/search")
    public String search() {
        return "/codechef/search";
    }

    // 상세 페이지
    @GetMapping("/detail")
    public String detail() {
        return "/codechef/detail";
    }

    // 리뷰 작성 페이지
    @GetMapping("/review-create")
    public String reviewCreate() {
        return "/codechef/reviewCreate";
    }
}
