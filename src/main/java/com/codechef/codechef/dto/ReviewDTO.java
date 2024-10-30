package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewNo;
    private String reviewImage;
    private Date date;
    private int tastePoint;
    private int moodPoint;
    private int servePoint;
    private String contents;
    private Member member;
    private String myReviewRating;
    private Restaurant restaurant;

    public static ReviewDTO fromEntity(Review review){
        // 평균 점수 계산
        double avg = (review.getTastePoint() + review.getMoodPoint() + review.getServePoint()) / 3.0;

        // 평균 점수를 문자열로 변환
        String myReviewRating = String.format("%.1f", avg);

        return new ReviewDTO(
                review.getReviewNo(),
                review.getReviewImage(),
                review.getDate(),
                review.getTastePoint(),
                review.getMoodPoint(),
                review.getServePoint(),
                review.getContents(),
                review.getMember(),
                myReviewRating,
                review.getRestaurant()
        );
    }
}
