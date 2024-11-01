package com.codechef.codechef.service;

import com.codechef.codechef.dto.TimeSlotDTO;
import com.codechef.codechef.entity.TimeSlot;
import com.codechef.codechef.repository.TimeSlotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Service
public class TimeSlotService {
    @Autowired
    TimeSlotRepository timeSlotRepository;

    // 예약가능한 시간 가져오기
    public List<TimeSlotDTO> findTimeSlotByChefNo(Long chef_no, String koreanDayOfWeek) {
        List<TimeSlot> timeSlots = timeSlotRepository.getTimeSlotByChefNo(chef_no, koreanDayOfWeek);
        if(ObjectUtils.isEmpty(timeSlots)){
            return Collections.emptyList();
        }
        return timeSlots.stream().map(x->TimeSlotDTO.fromEntity(x)).toList();
    }

    public void availableCheck(Long chefNo, String selectTime, String dayOfWeek) {
        try {
            timeSlotRepository.availableCheck(chefNo, selectTime, dayOfWeek);
        } catch (Exception e) {
            System.err.println("Error occurred while updating availability: " + e.getMessage());
        }
    }

//    public List<TimeSlotDTO> selectTest(Long chefNo, String selectTime, String dayOfWeek) {
//        List<TimeSlot> timeSlots = timeSlotRepository.selectTest(chefNo, selectTime, dayOfWeek);
//        if(ObjectUtils.isEmpty(timeSlots)){
//            return Collections.emptyList();
//        }
//        return timeSlots.stream().map(x->TimeSlotDTO.fromEntity(x)).toList();
//    }
}
