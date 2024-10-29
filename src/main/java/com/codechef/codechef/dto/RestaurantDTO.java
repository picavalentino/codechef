package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private Long chef_no;
    private String chef_name;
    private String chef_nickname;
    private String res_name;
    private String main_image;
    private String banner_image;
    private String res_phone_no;
    private String address;
    private String biz_hour;
    private String category;
    private String local_area;
    private String res_explain;
    private String res_mpa;
    private List<ReviewDTO> reviews = new ArrayList<>();

    // 리뷰 맛+분위기+서비스 전체 평점
    private String reviewRating;

    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getChef_no(),
                restaurant.getChef_name(),
                restaurant.getChef_nickname(),
                restaurant.getRes_name(),
                restaurant.getMain_image(),
                restaurant.getBanner_image(),
                restaurant.getRes_phone_no(),
                restaurant.getAddress(),
                restaurant.getBiz_hour(),
                restaurant.getCategory(),
                restaurant.getLocal_area(),
                restaurant.getRes_explain(),
                restaurant.getRes_mpa(),
                restaurant.getReviewList().stream().map(x->ReviewDTO.fromEntity(x)).toList(),
                // 리뷰 총합, 평점 구하기
                reviewRating(restaurant.getReviewList())
        );
    }

    private static String reviewRating(List<Review> reviewList) {
        // 해당 컬럼의 전체 합을 구하고, 평점 구하기
        int moodTotal = reviewList.stream().mapToInt(Review::getMood_point).sum();
        int serveTotal = reviewList.stream().mapToInt(Review::getServe_point).sum();
        int tasteTotal = reviewList.stream().mapToInt(Review::getTaste_point).sum();

        double avg = (moodTotal+serveTotal+tasteTotal)/(reviewList.size()*3.0);
        return String.format("%.1f", avg);
    }
}
