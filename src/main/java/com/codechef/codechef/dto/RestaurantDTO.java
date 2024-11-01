package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.ReservationRepository;
import com.codechef.codechef.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private Long chefNo;
    private String chefName;
    private String chefNickname;
    private String resName;
    private String mainImage;
    private String bannerImage;
    private String resPhoneNo;
    private String address;
    private String bizHour;
    private String closedDay;
    private String category;
    private String localArea;
    private String resExplain;
    private String resMap;
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<MenuDTO> menus = new ArrayList<>();

    // 리뷰 맛+분위기+서비스 전체 평점
    private String reviewRating;


    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getChefNo(),
                restaurant.getChefName(),
                restaurant.getChefNickname(),
                restaurant.getResName(),
                restaurant.getMainImage(),
                restaurant.getBannerImage(),
                restaurant.getResPhoneNo(),
                restaurant.getAddress(),
                restaurant.getBizHour(),
                restaurant.getClosedDay(),
                restaurant.getCategory(),
                restaurant.getLocalArea(),
                restaurant.getResExplain(),
                restaurant.getResMap(),
                restaurant.getReviewList().stream().map(x->ReviewDTO.fromEntity(x)).toList(),
                restaurant.getMenuList().stream().map(x->MenuDTO.fromEntity(x)).toList(),
                // 리뷰 총합, 평점 구하기
                reviewRating(restaurant.getReviewList())
        );
    }

    private static String reviewRating(List<Review> reviewList) {
        // 해당 컬럼의 전체 합을 구하고, 평점 구하기
        int moodTotal = reviewList.stream().mapToInt(Review::getMoodPoint).sum();
        int serveTotal = reviewList.stream().mapToInt(Review::getServePoint).sum();
        int tasteTotal = reviewList.stream().mapToInt(Review::getTastePoint).sum();

        double avg = (moodTotal+serveTotal+tasteTotal)/(reviewList.size()*3.0);
        return String.format("%.1f", avg);
    }
    // getters and setters

    private static String reviewCompletionRating(List<ReviewDTO> reviewList) {
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
