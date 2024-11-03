package com.codechef.codechef.controller;

import com.codechef.codechef.dto.*;
import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.dto.RestaurantDTO;
import com.codechef.codechef.dto.ReviewCreateDTO;
import com.codechef.codechef.dto.ReviewDTO;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.ReservationRepository;
import com.codechef.codechef.repository.RestaurantRepository;
import com.codechef.codechef.service.*;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codechef.codechef.dto.VisitCompletionDTO.calculateReviewRating;


@Controller
@Slf4j
public class MainController {
    // 서비스 연결
    private final ReservationService reservationService;
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final TimeSlotService timeSlotService;

    public MainController(ReservationService reservationService, ReviewService reviewService, MemberService memberService, ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, TimeSlotService timeSlotService) {
        this.reservationService = reservationService;
        this.reviewService = reviewService;
        this.memberService = memberService;
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.timeSlotService = timeSlotService;
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
    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("randLists", restaurantService.getRandLists());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String nickname = memberService.getNicknameByEmail(email);
        model.addAttribute("nickname", nickname);
        return "/codechef/main";
    }

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
        log.info("=================================" + reservation.getReservationDate());
        log.info("=================================" + reservation.getRestaurant().getResName());
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
        return "redirect:/mypage";
    }


    // 방문예약 리스트 페이지
    @GetMapping("/visitExpected")
    public String visitExpected(Model model, @PageableDefault(page = 0, size = 6, sort = "reservationDate") Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long memNo = memberService.getMemNoByEmail(email);
        Page<VisitExpectedDto> visitExpectedPage = reservationService.findReservationDataByMemNo(memNo, pageable);
        model.addAttribute("visitExpected", visitExpectedPage.getContent());
        model.addAttribute("page", visitExpectedPage);
//        List<Long> chefNos = reservationService.getChefNosByMemNo(memNo);
//        List<Restaurant> restaurants = restaurantService.getRestaurantsByChefNos(chefNos);
//        model.addAttribute("restaurants", restaurants);
        return "/codechef/visit_expected";
    }


    // 방문완료 리스트 페이지
    @GetMapping("/visit-completion")
    public String visitCompletion() {
        return "/codechef/visit_completion";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        Long memNo = (Long) session.getAttribute("loggedInUser"); // 세션에서 로그인한 사용자 ID 가져오기
        if (memNo != null) {
            // memNo로 사용자 정보를 가져오기
            Member member = memberService.getMemberByMemNo(memNo); // 서비스에서 회원 정보 조회
            //프사 가져오기
            String imageData = null;
            if (member.getMemImage() != null) {
                imageData = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(member.getMemImage());
            }
            // memNo로 최신 리뷰 2개 가져오기
            List<ReviewDTO> latestReviews = memberService.getLatestReviews(memNo);
            // memNo로 방문 예정 예약 2개 가져오기(오래된 날짜 순)
            List<ReservationDto> earliestReservations = reservationService.getEarliestReservations(memNo);
            // memNo로 방문 완료 예약 2개 가져오기(최근 날짜 순)
            List<ReservationDto> latestReservations = reservationService.getLatestReservations(memNo);

            model.addAttribute("member", member);               // 회원 정보
            model.addAttribute("imageData", imageData);
            model.addAttribute("latestReviews", latestReviews); // 최신 리뷰 추가
            model.addAttribute("earliestReservations", earliestReservations); // 방문 예정 예약 2개 가져오기
            model.addAttribute("latestReservations", latestReservations); // 방문 완료 예약 2개 가져오기
        }
        return "/codechef/mypage";
    }

    //리뷰 보기 페이지 - 내 리뷰 보기 페이지
    @GetMapping("/review-my")
    public String reviewViewMy(@RequestParam("memNo") Long memNo,
                               @PageableDefault(size = 5) Pageable pageable,
                               Model model) {
        // memNo로 멤버 정보를 가져옵니다.
        MemberReviewDTO memberReviewDTO = memberService.getMemberByMemNo2(memNo);
        // 리뷰를 페이징하여 가져옵니다.
        Page<ReviewDTO> reviewsPage = memberService.getReviewsByMember(memNo, pageable);


        model.addAttribute("member", memberReviewDTO);
        log.info("====================="+memberReviewDTO.getReviewRating());
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
    public String getProfileEdit(@RequestParam("memNo") Long memNo,
            Model model, HttpSession session) {
//        // 로그인된 상태로만 접근 가능하도록 확인
        Long loggedInUserId = (Long) session.getAttribute("loggedInUser");
       if (loggedInUserId == null) {
            return "redirect:/login";
        }
//
//        // mem_no로 회원 정보 조회
        Member member = memberService.getMemberByMemNo(memNo);
           model.addAttribute("member", member);

        // mem_no가 1인 회원 정보 조회
        MemberDto dto = MemberDto.fromEntity(memberService.getMemberByMemNo(memNo));
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
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passwordCheck", required = false) String passwordCheck,
            @RequestParam(value = "phoneNo", required = false) String phoneNo,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            Model model) {

        try {
            byte[] imageBytes = null;
            if (profileImage != null && !profileImage.isEmpty()) {
                // 프로필 이미지 처리
                imageBytes = profileImage.getBytes();
            }

            // 회원 정보 업데이트
            memberService.updateMemberInfo(memNo, password, passwordCheck, phoneNo, nickname, imageBytes);
            model.addAttribute("message", "회원 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/mypage";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "정보 업데이트 중 오류가 발생했습니다.");
            return "/codechef/profileEdit";
        }
    }


    @GetMapping("/visit-completion/{memNo}")
    public String visitCompletion(@PathVariable("memNo") Long memNo,
                                  Model model,
                                  @PageableDefault(page = 0, size = 5) Pageable pageable) {
        // 회원의 예약 목록 조회 (조인된 레스토랑 정보 포함)
        Page<Reservation> reservationsPage = reservationRepository.findByMemberMemNoAndVisitOxTrue(memNo, pageable);

        if (reservationsPage == null || reservationsPage.isEmpty()) {
            model.addAttribute("message", "예약 정보가 없습니다.");
            model.addAttribute("reservationsPage", Page.empty());
            return "/codechef/visit_completion";
        }

        // DTO로 변환 및 리뷰 평점 계산
        List<VisitCompletionDTO> visitCompletionDTOs = reservationsPage.getContent().stream().map(reservation -> {
            Review review = reservation.getReview();
            String reviewRating = "0.0";
            if (review != null) {
                ReviewDTO reviewDTO = ReviewDTO.fromEntity(review);
                reviewRating = calculateReviewRating(List.of(reviewDTO));
            }
            return VisitCompletionDTO.fromEntity(reservation, reviewRating);
        }).collect(Collectors.toList());

        // Page 객체로 변환
        Page<VisitCompletionDTO> visitCompletionDTOPage = new PageImpl<>(visitCompletionDTOs, pageable, reservationsPage.getTotalElements());

        // 페이지 블럭 처리
        int totalPage = visitCompletionDTOPage.getTotalPages();
        int currentPage = visitCompletionDTOPage.getNumber();
        List<Integer> pageNum = pagenationService.getPaginationBarNumber(currentPage, totalPage);

        // 모델에 값 추가
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("memberNo", memNo);
        model.addAttribute("reservationsPage", visitCompletionDTOPage);

        // 남은 요소가 5개 미만이라도 페이지에 표시되도록 처리
        model.addAttribute("totalElements", visitCompletionDTOPage.getTotalElements());
        model.addAttribute("hasContent", visitCompletionDTOPage.hasContent());

        return "/codechef/visit_completion";
    }

    @PostMapping("/deleteMember")
    public String deleteMember(Model model, HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();

            //// 사용자 삭제 부분 ////

            Long memNum = memberService.getMemNoByEmail(email);

            // reservation 테이블의 예약정보 삭제 (성공)
            reservationService.deleteReservationMemNum(memNum);

            // time_slot 테이블 예약정보 초기화
            LocalDateTime now = LocalDateTime.now();

            List<ReservationDto> reservationDtos = reservationService.getCurTimeAfter(now, memNum);

            reservationDtos.forEach(x-> {
                // Date를 Instant로 변환
                Instant instant = x.getReservationDate().toInstant();

                // Instant를 LocalDateTime으로 변환 (기본 타임존 사용)
                LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                int weekDay = getWeekOfMonth(localDateTime);

                // 요일 정보를 가져옴
                DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();

                // 요일을 한글로 변환
                String koreanDay = "";
                switch (dayOfWeek) {
                    case MONDAY:
                        koreanDay = "월";
                        break;
                    case TUESDAY:
                        koreanDay = "화";
                        break;
                    case WEDNESDAY:
                        koreanDay = "수";
                        break;
                    case THURSDAY:
                        koreanDay = "목";
                        break;
                    case FRIDAY:
                        koreanDay = "금";
                        break;
                    case SATURDAY:
                        koreanDay = "토";
                        break;
                    case SUNDAY:
                        koreanDay = "일";
                        break;
                }

                // DateTimeFormatter 정의
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                // 시분 형식으로 변환
                String formattedTime = localDateTime.format(formatter);

                String weekDayFormat = weekDay+"주"+koreanDay;

                timeSlotService.availableClear(x.getRestaurant().getChefNo(), weekDayFormat, formattedTime);
            });

            // member 테이블에 사용자 정보 삭제 (성공)
            memberService.deleteMember(email);

            // 인증 정보 제거
            SecurityContextHolder.clearContext();

            // 세션 무효화
            request.getSession().invalidate();
        }

        model.addAttribute("msg", "탈퇴처리가 완료 되었습니다.");
        model.addAttribute("url", "/");

        return "/codechef/alert";
    }

    private int getWeekOfMonth(LocalDateTime date) {
        // 주의 시작일을 월요일로 설정
        WeekFields weekFields = WeekFields.of(Locale.KOREA);
        return date.get(weekFields.weekOfMonth());
    }




}