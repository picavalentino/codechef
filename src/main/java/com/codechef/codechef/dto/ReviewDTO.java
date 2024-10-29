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
    private Long reviewNo;
    private String reviewImage;
    private Date date;
    private int tastePoint;
    private int moodPoint;
    private int servePoint;
    private String contents;

    public static ReviewDTO fromEntity(Review review){
        return new ReviewDTO(
                review.getReviewNo(),
                review.getReviewImage(),
                review.getDate(),
                review.getTastePoint(),
                review.getMoodPoint(),
                review.getServePoint(),
                review.getContents()
        );
    }
}
