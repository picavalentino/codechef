package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // 식당 3개 랜덤 출력
    @Query(value = "SELECT * FROM Restaurant ORDER BY RANDOM() limit 3" , nativeQuery = true)
    List<Restaurant> getRandRestaurant();

    // 키워드 혹은 카테고리 별 식당 조회
    Page<Restaurant> findByCategoryContainsAndResNameContains(String category, String keyword, Pageable pageable);

    // chefNo로 식당 조회 메서드
    Restaurant findByChefNo(Long chefNo);
}
