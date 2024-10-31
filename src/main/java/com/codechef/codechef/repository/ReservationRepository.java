package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByReservationNo(Long reservationNo);


    @Query(value = "SELECT r.chef_no FROM Reservation r WHERE r.mem_no = :memNo AND r.visit_ox = false", nativeQuery = true)
    List<Long> findChefNosByMemNo(@Param("memNo") Long memNo);
}
