package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitCompletionDTO {
    private Long reservationNo;
    private String resName;
    private String mainImage;
    private Date reservationDate;
    private boolean reviewOx;
    private boolean visitOx;
    private String reviewRating;
    private Long memNo;


    // 필요에 따라 추가 생성자, 게터/세터 추가

    public static VisitCompletionDTO fromEntity(Reservation reservation, String reviewRating) {
        return new VisitCompletionDTO(
                reservation.getReservationNo(),
                reservation.getRestaurant().getResName(),
                reservation.getRestaurant().getMainImage(),
                reservation.getReservationDate(),
                reservation.getReviewOx(),
                reservation.getVisitOx(),
                reviewRating,
                reservation.getMember().getMemNo()
        );
    }

    public static String calculateReviewRating(List<ReviewDTO> reviewList) {
        // 리뷰 리스트가 비어 있는 경우 0으로 설정
        if (reviewList.isEmpty()) {
            return "0.0";
        }

        // 해당 컬럼의 전체 합을 구하고, 평점 구하기
        int moodTotal = reviewList.stream().mapToInt(ReviewDTO::getMoodPoint).sum();
        int serveTotal = reviewList.stream().mapToInt(ReviewDTO::getServePoint).sum();
        int tasteTotal = reviewList.stream().mapToInt(ReviewDTO::getTastePoint).sum();

        double avg = (moodTotal + serveTotal + tasteTotal) / (reviewList.size() * 3.0);
        return String.format("%.1f", avg);
    }
}

