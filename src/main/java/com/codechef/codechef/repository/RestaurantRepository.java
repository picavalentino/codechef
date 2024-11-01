package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // 식당 3개 랜덤 출력
    @Query(value = "SELECT * FROM Restaurant ORDER BY RANDOM() limit 3" , nativeQuery = true)
    List<Restaurant> getRandRestaurant();

    // 키워드 혹은 카테고리 별 식당 조회
    @Query("SELECT r FROM Restaurant r WHERE r.category LIKE %:category% AND " +
            "(r.resName LIKE %:keyword% OR r.chefName LIKE %:keyword% OR r.chefNickname LIKE %:keyword%)")
    Page<Restaurant> findByCategoryAndKeywordInFields(@Param("category") String category,
                                                      @Param("keyword") String keyword,
                                                      Pageable pageable);

//    Page<Restaurant> findByCategoryContainsAndResNameContains(String category, String keyword, Pageable pageable);

    // chefNo로 식당 조회 메서드
    Restaurant findByChefNo(Long chefNo);


    @Query("SELECT r FROM Restaurant r WHERE r.chefNo IN :chefNos")
    List<Restaurant> findByChefNos(@Param("chefNos") List<Long> chefNos);
}
