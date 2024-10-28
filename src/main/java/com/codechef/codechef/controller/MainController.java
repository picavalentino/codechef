package com.codechef.codechef.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/login")
    public String login() {
        return "/codechef/login";
    }

    @GetMapping("/insert")
    public String insert() {
        return "/codechef/join";
    }

    @PostMapping("/insert")
    public String insertMember() {
        return "/codechef/main";
    }

    // 방문예약 리스트 페이지
    @GetMapping("/visit-expected")
    public String visitExpected() {
        return "/codechef/visit_expected";
    }

    // 방문완료 리스트 페이지
    @GetMapping("/visit-completion")
    public String visitCompletion() {
        return "/codechef/visit_completion";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage() {
        return "/codechef/mypage";
    }

    //리뷰 보기 페이지
    @GetMapping("/reviewView")
    public String reviewView() {
        return "/codechef/reviewView";
    }

    // 프로필 수정 페이지
    @GetMapping("/profileEdit")
    public String profileEdit(){
        return "/codechef/profileEdit";
    }

}
