package com.codechef.codechef.controller;

import com.codechef.codechef.dto.ReservationDto;
import com.codechef.codechef.dto.RestaurantDTO;
import com.codechef.codechef.dto.ReviewDTO;
import com.codechef.codechef.dto.TimeSlotDTO;
import com.codechef.codechef.service.*;
import com.codechef.codechef.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
public class ReservationController {
    // 서비스 연결
    private final ReservationService reservationService;
    private final ReviewService reviewService;
    private final TimeSlotService timeSlotService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, ReviewService reviewService, TimeSlotService timeSlotService, MemberService memberService) {
        this.reservationService = reservationService;
        this.reviewService = reviewService;
        this.timeSlotService = timeSlotService;
        this.memberService = memberService;
    }

    @Autowired
    RestaurantService restaurantService;

    // 예약 페이지
    @GetMapping("/reservation")
    public String reservation(Model model,
                              @RequestParam(value = "month", required = false) Integer month,
                              @RequestParam(value = "year", required = false) Integer year,
                              @RequestParam(value = "chefNo") Long chefNo) {
        // test
//        timeSlotService.updateTest();

        LocalDate currentDate;

        if (month != null && year != null) {
            currentDate = LocalDate.of(year, month, 1);
        } else {
            currentDate = LocalDate.now();
            month = currentDate.getMonthValue();
        }

        RestaurantDTO restaurantDTO = restaurantService.getRestaurantByChefNo(chefNo);

        model.addAttribute("year", currentDate.getYear());
        model.addAttribute("month", month);
        model.addAttribute("monthName", Month.of(month).toString());
        model.addAttribute("days", DateUtil.daysInMonth(month, currentDate.getYear()));
        model.addAttribute("chefNo", chefNo);
        model.addAttribute("restaurantDTO", restaurantDTO);

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
    public String reservationPost(Model model,
                                  @RequestParam(value = "selectedDay") LocalDateTime reservationDate,
                                  @RequestParam(value = "numPeople") int memberCount,
                                  @RequestParam(value = "chefNo") Long chefNo,
                                  @RequestParam(value = "select_time") String select_time) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long memNo = memberService.getMemNoByEmail(email);

        Boolean review_ox = false;
        Boolean visit_ox = false;

        // 요일 정보를 가져오기
        String dayOfWeek = reservationDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);

        System.out.println("============================== reservationDate : "+reservationDate);
        System.out.println("============================== memberCount: "+memberCount);
        System.out.println("============================== chefNo : "+chefNo);
        System.out.println("============================== memNo : "+memNo);
        System.out.println("============================== select_time : "+select_time);
        System.out.println("============================== dayOfWeek : "+dayOfWeek);

        // 리뷰 작성했는지 확인
        List<ReviewDTO> reviewDTOS = reviewService.findByChefNoAndMemNo(chefNo, memNo);

        if (!reviewDTOS.isEmpty()) {
            review_ox = true;
        }

        // 이전에 방문했는지 확인
        List<ReservationDto> reservationDto = reservationService.visitOxFind(chefNo, memNo);

        if (!reservationDto.isEmpty()) {
            visit_ox = true;
        }

        Long reservation_no = reservationService.maxReservationNo();

        // 예약정보 데이터베이스에 저장
        reservationService.insertReservationInfo(++reservation_no, reservationDate, memberCount, review_ox, visit_ox, memNo, chefNo);

        // 예약 확인 체크
        timeSlotService.availableCheck(chefNo, select_time, dayOfWeek);

//        List<TimeSlotDTO> timeSlotDTOS = timeSlotService.selectTest(chefNo, select_time, dayOfWeek);
//        timeSlotDTOS.forEach(x-> System.out.println("============================ "+x));

        model.addAttribute("msg", "예약되었습니다.");
        model.addAttribute("url", "/visitExpected");

        return "/codechef/alert";
    }
}
