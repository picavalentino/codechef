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
    @Query(value = "SELECT * FROM Restaurant ORDER BY RAND() limit 3" , nativeQuery = true)
    List<Restaurant> getRandRestaurant();

    // 키워드 혹은 카테고리 별 식당 조회
    // Column명에 언더바(_) 포함된 경우, Query Method 사용 불가
    @Query(value = "SELECT * FROM Restaurant WHERE category LIKE %:category% AND res_name LIKE %:keyword% ORDER BY res_name ASC", nativeQuery = true)
    Page<Restaurant> findByLists(@Param("category")String category, @Param("keyword")String keyword, Pageable pageable);
}
