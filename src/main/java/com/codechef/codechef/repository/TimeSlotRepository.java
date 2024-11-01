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

    // 예약확인 체크
    @Transactional
    @Modifying
    @Query(value = "UPDATE time_slot SET available = true WHERE chef_no = :chefNo AND time = :selectTime AND day = :dayOfWeek", nativeQuery = true)
    void availableCheck(@Param("chefNo") Long chefNo, @Param("selectTime") String selectTime, @Param("dayOfWeek") String dayOfWeek);

    // 하루가 지나면 이전 예약 체크 해제
    @Transactional
    @Modifying
    @Query(value = "UPDATE time_slot SET available = false WHERE day = :dayOfWeekFormat", nativeQuery = true)
    void availableUpdate(@Param("dayOfWeekFormat") String dayOfWeekFormat);

//    @Transactional
//    @Modifying
//    @Query(value = "INSERT INTO time_slot (available, chef_no, day, time) SELECT available, chef_no, day, time FROM time_slot WHERE time_no < 1338 ORDER BY time_no", nativeQuery = true)
//    @Query(value = "UPDATE time_slot SET day = REPLACE(day, '1주', '6주') WHERE day LIKE '1주%' AND time_no > 9359", nativeQuery = true)
//    void updateTest();

//    @Query(value = "SELECT * FROM time_slot WHERE chef_no = :chefNo AND time = :selectTime AND day = :dayOfWeek", nativeQuery = true)
//    List<TimeSlot> selectTest(@Param("chefNo") Long chefNo, @Param("selectTime") String selectTime, @Param("dayOfWeek") String dayOfWeek);
}
