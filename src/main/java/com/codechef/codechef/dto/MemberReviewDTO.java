package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReviewDTO {
    private Long memNo;
    private String email;
    private String nickname;
    private List<ReviewDTO> reviews = new ArrayList<>();
    // 리뷰 맛+분위기+서비스 전체 평점
    private String reviewRating;

    public static MemberReviewDTO fromEntity(Member member) {
        return new MemberReviewDTO(
                member.getMemNo(),
                member.getEmail(),
                member.getNickname(),
                member.getReviewList().stream().map(x->ReviewDTO.fromEntity(x)).toList(),
                // 리뷰 총합, 평점 구하기
                reviewRating(member.getReviewList())
        );
    }

    private static String reviewRating(List<Review> reviewList) {
        if (reviewList.isEmpty()) {
            return " "; // 리스트가 비어 있을 경우 기본값 반환
        }
        // 해당 컬럼의 전체 합을 구하고, 평점 구하기
        int moodTotal = reviewList.stream().mapToInt(Review::getMoodPoint).sum();
        int serveTotal = reviewList.stream().mapToInt(Review::getServePoint).sum();
        int tasteTotal = reviewList.stream().mapToInt(Review::getTastePoint).sum();

        double avg = (moodTotal+serveTotal+tasteTotal)/(reviewList.size()*3.0);
        return String.format("%.1f", avg);
    }
}
