package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // chefNo로 리뷰를 페이징하여 가져오는 쿼리
    @Query("SELECT r FROM Review r WHERE r.restaurant.chefNo = :chefNo")
    Page<Review> findByRestaurantChefNo(@Param("chefNo") Long chefNo, Pageable pageable);

    // memNo로 리뷰를 페이징하여 가져오는 쿼리
    @Query("SELECT r FROM Review r WHERE r.member.memNo = :memNo")
    Page<Review> findByMembermemNo(@Param("memNo")Long memNo, Pageable pageable);

    // chefNo, memNo 조건으로 검색
    List<Review> findByRestaurant_ChefNoAndMember_MemNo(Long chefNo, Long memNo);
}
