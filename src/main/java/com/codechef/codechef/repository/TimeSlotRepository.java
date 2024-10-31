package com.codechef.codechef.repository;

import com.codechef.codechef.entity.TimeSlot;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // 예약가능 시간 정보 가져오기
    @Query(value = "SELECT * FROM time_slot WHERE chef_no = :chefNo AND day = :koreanDayOfWeek ORDER BY time_no", nativeQuery = true)
    List<TimeSlot> getTimeSlotByChefNo(@Param("chefNo") Long chefNo, @Param("koreanDayOfWeek") String koreanDayOfWeek);

    @Transactional
    @Modifying
    @Query(value = "UPDATE time_slot SET available = true WHERE chef_no = :chefNo AND time = :selectTime AND day = :dayOfWeek", nativeQuery = true)
    void availableCheck(@Param("chefNo") Long chefNo, @Param("selectTime") String selectTime, @Param("dayOfWeek") String dayOfWeek);

//    @Query(value = "SELECT * FROM time_slot WHERE chef_no = :chefNo AND time = :selectTime AND day = :dayOfWeek", nativeQuery = true)
//    List<TimeSlot> selectTest(@Param("chefNo") Long chefNo, @Param("selectTime") String selectTime, @Param("dayOfWeek") String dayOfWeek);
}
