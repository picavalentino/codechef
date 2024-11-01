package com.codechef.codechef.repository;

import com.codechef.codechef.dto.ReservationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.codechef.codechef.entity.Reservation;


import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByReservationNo(Long reservationNo);


    // memNo에 따른 예약 목록을 페이징 처리하여 조회
    Page<Reservation> findByMemberMemNo(Long memNo, Pageable pageable);

    Page<Reservation> findByMemberMemNoAndVisitOxTrue(Long memNo, Pageable pageable);



    @Query(value = "SELECT r.chef_no FROM Reservation r WHERE r.mem_no = :memNo AND r.visit_ox = false", nativeQuery = true)
    List<Long> findChefNosByMemNo(@Param("memNo") Long memNo);

    // memNo로 방문 예정 예약을 오래된 날짜 순으로 가져오는 쿼리 (visitOx가 false인 경우만)
    @Query("SELECT r FROM Reservation r WHERE r.member.memNo = :memNo AND r.visitOx = false ORDER BY r.reservationDate ASC")
    List<Reservation> findEarliestReservationsByMemberMemNo(@Param("memNo") Long memNo);

    // memNo로 방문 완료 예약을 최근 날짜 순으로 가져오는 쿼리 (visitOx가 true인 경우만) - 리뷰 정보 가져옴
    @Query("SELECT r FROM Reservation r WHERE r.member.memNo = :memNo AND r.visitOx = true ORDER BY r.reservationDate DESC")
    List<Reservation> findTop2ByMemberMemNoAndVisitOxTrueOrderByReservationDateDesc(@Param("memNo") Long memNo);

}



