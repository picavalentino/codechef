package com.codechef.codechef.service;

import com.codechef.codechef.dto.TimeSlotDTO;
import com.codechef.codechef.entity.TimeSlot;
import com.codechef.codechef.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    // 예약가능한 시간 가져오기
    public List<TimeSlotDTO> findTimeSlotByChefNo(int chef_no) {
        List<TimeSlot> timeSlots = reservationRepository.getTimeSlotByChefNo(chef_no);
        if(ObjectUtils.isEmpty(timeSlots)){
            return Collections.emptyList();
        }
        return timeSlots.stream().map(x->TimeSlotDTO.fromEntity(x)).toList();
    }
}
