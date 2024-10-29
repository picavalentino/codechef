package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDTO {
    private Long reviewNo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날짜 형식 지정
    private Date date;
    private int tastePoint;
    private int moodPoint;
    private int servePoint;
    private String contents;
    private Reservation reservation;
    private Member member;
    private Restaurant restaurant;
    private MultipartFile reviewImage;

    //dto -> entity
    public static Review fromDto(ReviewCreateDTO dto) {
        Review review = new Review();
        review.setReviewNo(dto.getReviewNo());
        review.setDate(dto.getDate());
        review.setTastePoint(dto.getTastePoint());
        review.setMoodPoint(dto.getMoodPoint());
        review.setServePoint(dto.getServePoint());
        review.setContents(dto.getContents());
        review.setReservation(dto.getReservation());
        review.setMember(dto.getMember());
        review.setRestaurant(dto.getRestaurant());
        return review;
    }
}
