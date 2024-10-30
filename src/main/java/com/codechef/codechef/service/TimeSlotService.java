package com.codechef.codechef.service;

import com.codechef.codechef.dto.TimeSlotDTO;
import com.codechef.codechef.entity.TimeSlot;
import com.codechef.codechef.repository.TimeSlotRepository;
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
    public List<TimeSlotDTO> findTimeSlotByChefNo(int chef_no, String koreanDayOfWeek) {
        List<TimeSlot> timeSlots = timeSlotRepository.getTimeSlotByChefNo(chef_no, koreanDayOfWeek);
        if(ObjectUtils.isEmpty(timeSlots)){
            return Collections.emptyList();
        }
        return timeSlots.stream().map(x->TimeSlotDTO.fromEntity(x)).toList();
    }
}
