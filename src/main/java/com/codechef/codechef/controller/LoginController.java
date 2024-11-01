package com.codechef.codechef.controller;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.service.CodeChefService;
import groovy.util.logging.Log;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@lombok.extern.slf4j.Slf4j
@Controller
@Slf4j
public class LoginController {
    private final CodeChefService codeChefService;

    public LoginController(CodeChefService codeChefService) {
        this.codeChefService = codeChefService;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "/codechef/login";
    }

    // 로그인 에러메시지 삭제
    @PostMapping("/clear-error-message")
    @ResponseBody
    public void clearErrorMessage(HttpSession session) {
        session.removeAttribute("errorMessage");
    }

    // 회원가입 페이지
    @GetMapping("/join")
    public String join() {
        return "/codechef/join";
    }

    // 회원가입 저장
    @PostMapping("/joinMember")
    public String joinMember(MemberDto memberDTO) {
        codeChefService.joinMember(memberDTO);
        return "/codechef/main";
    }

    // 이메일 중복체크 기능
    @GetMapping("/checkEmail")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email") String email) {
        boolean isDuplicated = codeChefService.isEmailDuplicated(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicated", isDuplicated);
        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/main";
    }
}
