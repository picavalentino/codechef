package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByReservationNo(Long reservationNo);


    @Query(value = "SELECT r.chef_no FROM Reservation r WHERE r.mem_no = :memNo AND r.visit_ox = false", nativeQuery = true)
    List<Long> findChefNosByMemNo(@Param("memNo") Long memNo);

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
            @Param("memNo") Long memNo,
            @Param("chefNo") Long chefNo);

    // memNo로 방문 예정 예약을 오래된 날짜 순으로 가져오는 쿼리 (visitOx가 false인 경우만)
    @Query("SELECT r FROM Reservation r WHERE r.member.memNo = :memNo AND r.visitOx = false ORDER BY r.reservationDate ASC")
    List<Reservation> findEarliestReservationsByMemberMemNo(@Param("memNo") Long memNo);

    // memNo로 방문 완료 예약을 최근 날짜 순으로 가져오는 쿼리 (visitOx가 true인 경우만) - 리뷰 정보 가져옴
    @Query("SELECT r FROM Reservation r WHERE r.member.memNo = :memNo AND r.visitOx = true ORDER BY r.reservationDate DESC")
    List<Reservation> findTop2ByMemberMemNoAndVisitOxTrueOrderByReservationDateDesc(@Param("memNo") Long memNo);

    // 이전에 방문한 적이 있는지 확인
    @Query("SELECT r FROM Reservation r WHERE r.restaurant.chefNo = :chefNo AND r.member.memNo = :memNo AND reservation_date < NOW()")
    List<Reservation> visitOxFind(Long chefNo, Long memNo);
}
