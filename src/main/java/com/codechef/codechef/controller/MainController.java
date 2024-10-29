package com.codechef.codechef.controller;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.dto.RestaurantDTO;
import com.codechef.codechef.service.CodeChefService;
import com.codechef.codechef.service.PagenationService;
import com.codechef.codechef.service.RestaurantService;
import com.codechef.codechef.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    // 서비스 연결
    private final CodeChefService codeChefService;

    public MainController(CodeChefService codeChefService) {
        this.codeChefService = codeChefService;
    }

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    PagenationService pagenationService;

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    // test용
    @GetMapping("/test")
    public String test() {
        return "/codechef/test";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String main(Model model) {
        // 식당 3개 랜덤 출력
        model.addAttribute("randLists", restaurantService.getRandLists());
        System.out.println(restaurantService.getRandLists());
        return "/codechef/main";
    }

    // 카테고리별 or 키워드 검색, 페이지 처리
    @GetMapping("/search")
    public String search(@RequestParam("category") String category, @RequestParam("keyword") String keyword,
                         Model model, @PageableDefault(page = 0, size = 6) Pageable pageable) {
        Page<RestaurantDTO> paging = restaurantService.getResultLists(category, keyword, pageable);
        model.addAttribute("resultLists", paging);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        int totalPage = paging.getTotalPages();
        int currentPage = paging.getNumber();

        // 페이지 블럭 처리
        List<Integer> pageNum = pagenationService.getPaginationBarNumber(currentPage, totalPage);
        model.addAttribute("pageNum", pageNum);

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

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "/codechef/login";
    }

//    @PostMapping("/loginMember")
//    public String login(MemberDTO memberDTO) {
//        codeChefService.loginservice(memberDTO);
//        return "redirect:/main";
//    }

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

    // 로그아웃
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!ObjectUtils.isEmpty(authentication)) {
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//        return "redirect:/main";
//    }

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

    // 예약 페이지
    @GetMapping("/reservation")
    public String reservation(Model model,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "year", required = false) Integer year) {
        LocalDate currentDate;

        if (month != null && year != null) {
            currentDate = LocalDate.of(year, month, 1);
        } else {
            currentDate = LocalDate.now();
            month = currentDate.getMonthValue();
        }

        model.addAttribute("year", currentDate.getYear());
        model.addAttribute("month", month);
        model.addAttribute("monthName", Month.of(month).toString());
        model.addAttribute("days", DateUtil.daysInMonth(month, currentDate.getYear()));

        return "/codechef/reservation";
    }

    @GetMapping("/reservation/ajax")
    @ResponseBody
    public Map<String, Object> reservationAjax(@RequestParam(value = "month") int month,
                                               @RequestParam(value = "year") int year) {
        LocalDate currentDate = LocalDate.of(year, month, 1);

        Map<String, Object> response = new HashMap<>();
        response.put("year", currentDate.getYear());
        response.put("month", Month.of(currentDate.getMonthValue()).toString());
        response.put("days", DateUtil.daysInMonth(currentDate.getMonthValue(), currentDate.getYear()));

        return response;
    }
}
