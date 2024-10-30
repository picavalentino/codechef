package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByReservationNo(Long reservationNo);

    // 예약정보 저장
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO reservation (reservation_date, member_count, review_ox, visit_ox, mem_no, chef_no) " +
            "VALUES (:reservationDate, :memberCount, :reviewOx, :visitOx, :memNo, :chefNo)", nativeQuery = true)
    void insertReservationInfo(
            @Param("reservationDate") LocalDateTime reservationDate,
            @Param("memberCount") int memberCount,
            @Param("reviewOx") boolean reviewOx,
            @Param("visitOx") boolean visitOx,
            @Param("memNo") int memNo,
            @Param("chefNo") Long chefNo);
}
