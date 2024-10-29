package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long review_no;
    private String review_image;
    private Date date;
    private int taste_point;
    private int mood_point;
    private int serve_point;
    private String contents;

    public static ReviewDTO fromEntity(Review review){
        return new ReviewDTO(
                review.getReview_no(),
                review.getReview_image(),
                review.getDate(),
                review.getTaste_point(),
                review.getMood_point(),
                review.getServe_point(),
                review.getContents()
        );
    }
}
