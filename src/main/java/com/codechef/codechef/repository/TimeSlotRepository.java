package com.codechef.codechef.repository;

import com.codechef.codechef.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // 예약가능 시간 정보 가져오기
    @Query(value = "SELECT * FROM time_slot WHERE chef_no = :chefNo AND day = :koreanDayOfWeek", nativeQuery = true)
    List<TimeSlot> getTimeSlotByChefNo(@Param("chefNo") Long chefNo, @Param("koreanDayOfWeek") String koreanDayOfWeek);
}
