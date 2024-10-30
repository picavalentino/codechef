package com.codechef.codechef.service;

import com.codechef.codechef.dto.RestaurantDTO;
import com.codechef.codechef.dto.ReviewDTO;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.RestaurantRepository;
import com.codechef.codechef.repository.ReviewRepository;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class RestaurantService {
    @Autowired
    RestaurantRepository resRepository;

    private final ReviewRepository reviewRepository;

    public RestaurantService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 식당 3개 랜덤 조회
    public List<RestaurantDTO> getRandLists() {
        List<Restaurant> restaurants = resRepository.getRandRestaurant();
        if(ObjectUtils.isEmpty(restaurants)){
            return Collections.emptyList();
        }
        return restaurants.stream().map(x->RestaurantDTO.fromEntity(x)).toList();
    }

    // 카테고리별 or 키워드 검색, 페이지 처리
    public Page<RestaurantDTO> getResultLists(String category, String keyword, Pageable pageable) {
        Page<Restaurant> restaurants = resRepository.findByCategoryContainsAndResNameContains(category, keyword, pageable);
        if(ObjectUtils.isEmpty(restaurants)){
            return null;
        }
        return restaurants.map(x->RestaurantDTO.fromEntity(x));
    }

    // chefNo로 식당 정보 조회
    public RestaurantDTO getRestaurantByChefNo(Long chefNo) {
        Restaurant restaurant = resRepository.findByChefNo(chefNo);
        if (restaurant == null) {
            return null;
        }
        return RestaurantDTO.fromEntity(restaurant);
    }

    // chefNo로 리뷰 리스트를 페이징하여 가져오는 메서드
    public Page<ReviewDTO> getReviewsByRestaurant(Long chefNo, Pageable pageable) {
        // 리뷰를 페이징하여 가져오기
        Page<Review> reviews = reviewRepository.findByRestaurantChefNo(chefNo, pageable);
        return reviews.map(ReviewDTO::fromEntity); // ReviewDTO로 변환하여 반환
    }
}
