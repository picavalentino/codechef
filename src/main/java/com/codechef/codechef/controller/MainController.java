package com.codechef.codechef.controller;

import com.codechef.codechef.dto.*;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.service.*;
import com.codechef.codechef.util.DateUtil;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MainController {
    // 서비스 연결
    private final ReservationService reservationService;
    private final ReviewService reviewService;
    private final TimeSlotService timeSlotService;
    private final MemberService memberService;

    public MainController(ReservationService reservationService, ReviewService reviewService, TimeSlotService timeSlotService, MemberService memberService) {
        this.reservationService = reservationService;
        this.reviewService = reviewService;
        this.timeSlotService = timeSlotService;
        this.memberService = memberService;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String nickname = memberService.getNicknameByEmail(email);
        model.addAttribute("nickname", nickname);
        return "/codechef/main";
    }

    // 카테고리별 or 키워드 검색, 페이지 처리
    @GetMapping("/search")
    public String search(@RequestParam("category") String category, @RequestParam("keyword") String keyword,
                         Model model, @PageableDefault(page = 0, size = 6, sort = "resName") Pageable pageable) {
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
    public String detail(@RequestParam("chefNo") Long chefNo, Model model) {
        // 식당 상세 조회
        model.addAttribute("restaurant", restaurantService.getRestaurantByChefNo(chefNo));
        return "/codechef/detail";
    }

    //리뷰 보기 페이지 - 식당 별 리뷰 보기 페이지
    @GetMapping("/review-restaurant")
    public String reviewViewRes(@RequestParam("chefNo") Long chefNo,
                                @PageableDefault(size = 5) Pageable pageable,
                                Model model) {
        // chefNo로 식당 정보를 가져옵니다.
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantByChefNo(chefNo);
        // 리뷰를 페이징하여 가져옵니다.
        Page<ReviewDTO> reviewsPage = restaurantService.getReviewsByRestaurant(chefNo, pageable);

        model.addAttribute("restaurant", restaurantDTO);
        model.addAttribute("reviews", reviewsPage.getContent());
        model.addAttribute("reviewsPage", reviewsPage);  // 페이지 정보 추가

        int totalPage = reviewsPage.getTotalPages();
        int currentPage = reviewsPage.getNumber();

        // 페이지 블럭 처리
        List<Integer> pageNum = pagenationService.getPaginationBarNumber(currentPage, totalPage);
        model.addAttribute("pageNum", pageNum);

        return "/codechef/reviewViewRes";
    }

    // 리뷰 작성 페이지
    @GetMapping("/review-create/{reservationNo}")
    public String reviewCreateView(@PathVariable("reservationNo") Long reservationNo, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationNo);
        model.addAttribute("reservation", reservation);
        model.addAttribute("member", reservation.getMember());
        model.addAttribute("restaurant", reservation.getRestaurant());
        log.info("================================="+reservation.getReservationDate());
        log.info("================================="+reservation.getRestaurant().getResName());
        return "/codechef/reviewCreate";
    }

    //리뷰 저장 페이지
    @PostMapping("/review-create")
    public String reviewCreate(@ModelAttribute ReviewCreateDTO dto) {
        try {
            reviewService.createReview(dto);
            reviewService.updateReviewOxByReservationNo(dto.getReservation().getReservationNo());
        } catch (Exception e) {
            log.error("리뷰 저장 중 오류 발생: {}", e.getMessage());
            // 오류 페이지로 리다이렉트할 수도 있습니다.
        }
        return "/codechef/mypage";
    }



    // 방문예약 리스트 페이지
    @GetMapping("/visitExpected")
    public String visitExpected(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long memNo = memberService.getMemNoByEmail(email);
        List<Long> chefNos = reservationService.getChefNosByMemNo(memNo);
        List<Restaurant> restaurants = restaurantService.getRestaurantsByChefNos(chefNos);
        model.addAttribute("restaurants", restaurants);
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

    //리뷰 보기 페이지 - 내 리뷰 보기 페이지
    @GetMapping("/review-my")
    public String reviewViewMy(@RequestParam("memNo") Long memNo,
                               @PageableDefault(size = 5) Pageable pageable,
                               Model model) {
        // memNo로 식당 정보를 가져옵니다.
        MemberReviewDTO memberReviewDTO = memberService.getMemberByMemNo2(memNo);
        // 리뷰를 페이징하여 가져옵니다.
        Page<ReviewDTO> reviewsPage = memberService.getReviewsByMember(memNo, pageable);


        model.addAttribute("member", memberReviewDTO);
        model.addAttribute("reviews", reviewsPage.getContent());
        model.addAttribute("reviewsPage", reviewsPage);  // 페이지 정보 추가

        int totalPage = reviewsPage.getTotalPages();
        int currentPage = reviewsPage.getNumber();

        // 페이지 블럭 처리
        List<Integer> pageNum = pagenationService.getPaginationBarNumber(currentPage, totalPage);
        model.addAttribute("pageNum", pageNum);

        return "/codechef/reviewViewMy";
    }

    // 내 리뷰 보기 페이지 - 삭제
    @GetMapping("/review-my/delete/{reviewNo}")
    public String deleteReview(@PathVariable("reviewNo") Long reviewNo,
                               @RequestParam("memNo") Long memNo) {
        try {
            reviewService.deleteReview(reviewNo); // 서비스에서 리뷰 삭제 호출
        } catch (Exception e) {
            // 로그에 오류 메시지 기록 (옵션)
            log.error("리뷰 삭제 중 오류 발생: {}", e.getMessage());
        }
        return "redirect:/review-my?memNo=" + memNo; // memNo를 쿼리 파라미터로 추가
    }

    // 프로필 수정 페이지
    @GetMapping("/profileEdit")
    public String getProfileEdit(Model model, HttpSession session) {
//        // 로그인된 상태로만 접근 가능하도록 확인
//        Long loggedInUserId = (Long) session.getAttribute("loggedInUser");
//        if (loggedInUserId == null) {
//            return "redirect:/login";
//        }
//
//        // mem_no로 회원 정보 조회
//        Member member = memberService.getMemberByMemNo(memNo);
//        model.addAttribute("member", member);

        // mem_no가 1인 회원 정보 조회
        MemberDto dto = MemberDto.fromEntity(memberService.getMemberWithMemNoOne());
        System.out.println("회원 번호 (mem_no): " + dto.getMemNo());
        System.out.println("이메일 (email): " + dto.getEmail());
        System.out.println("닉네임 (nickname): " + dto.getNickname());
        System.out.println("전화번호 (phoneNo): " + dto.getPhoneNo());
        System.out.println("프로필 이미지 (memImage): " + dto.getMemImage());

        String imageData = null;
        if (dto.getMemImage() != null) {
            imageData = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(dto.getMemImage());
        }

        // 조회한 회원 정보를 모델에 추가하여 뷰로 전달
        log.info("###########################dto : " + dto.toString());
        model.addAttribute("member", dto);
        model.addAttribute("imageData", imageData);
        return "/codechef/profileEdit"; // 회원 정보 페이지로 이동 (member-info.html)

    }

    // 프로필 수정 처리
    @PostMapping("/profileEdit")
    public String updateMemberInfo(
            HttpSession session,
            @RequestParam("memNo") Long memNo,
            @RequestParam(value ="password", required = false) String password,
            @RequestParam(value ="passwordCheck", required = false) String passwordCheck,
            @RequestParam(value ="phoneNo", required = false) String phoneNo,
            @RequestParam(value ="nickname", required = false) String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            Model model) {

        try {
            byte[] imageBytes = null;
            if (profileImage != null && !profileImage.isEmpty()) {
                // 프로필 이미지 처리
                imageBytes = profileImage.getBytes();
            }

            // 회원 정보 업데이트
            memberService.updateMemberInfo(memNo, password,passwordCheck, phoneNo, nickname, imageBytes);
            model.addAttribute("message", "회원 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/mypage";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "정보 업데이트 중 오류가 발생했습니다.");
            return "/codechef/profileEdit";
        }
    }

    // 예약 페이지
    @GetMapping("/reservation")
    public String reservation(Model model,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "chefNo") Long chefNo) {
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
        model.addAttribute("chefNo", chefNo);

        return "/codechef/reservation";
    }

    // 다음달 이동 기능
    @GetMapping("/reservation/ajax")
    @ResponseBody
    public Map<String, Object> reservationAjax(@RequestParam(value = "month") int month,
                                               @RequestParam(value = "year") int year) {
        LocalDate currentDate = LocalDate.of(year, month, 1);

        Map<String, Object> response = new HashMap<>();
        response.put("year", currentDate.getYear());
        response.put("month", Month.of(currentDate.getMonthValue()).toString());
        response.put("days", DateUtil.daysInMonth(currentDate.getMonthValue(), currentDate.getYear()));
        response.put("week", currentDate.getDayOfWeek());

        return response;
    }

    // 날짜일 선택 기능
    @GetMapping("/reservation/timeSlot")
    public ResponseEntity<Map<String, String>> getTimeSlot(@RequestParam("chef_no") Long chefNo,
                                                           @RequestParam("koreanDayOfWeek") String koreanDayOfWeek) {
        Map<String, String> response = new HashMap<>();

        List<TimeSlotDTO> timeSlotDTOS = timeSlotService.findTimeSlotByChefNo(chefNo, koreanDayOfWeek);

        response.put("timeSlotDTOS", timeSlotDTOS.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservation")
    public String reservationPost(@RequestParam(value = "chefNo") Long chefNo,
                                  @RequestParam(value = "selectedDay") String selectedDay,
                                  @RequestParam(value = "numPeople") int numPeople,
                                  @RequestParam(value = "select_time") String select_time) {

        System.out.println("============================== "+chefNo);
        System.out.println("============================== "+selectedDay);
        System.out.println("============================== "+numPeople);
        System.out.println("============================== "+select_time);

//        reservationService.insertReservationInfo(numPeople, chefNo, selectedDay)

        return "/codechef/reservation";
    }
}
