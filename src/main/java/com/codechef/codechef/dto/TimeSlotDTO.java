package com.codechef.codechef.dto;

import com.codechef.codechef.entity.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDTO {
    private Long timeNo;
    private String day;
    private String time;
    private Boolean available;
    private Long chefNo;

    public static TimeSlotDTO fromEntity(TimeSlot timeSlot) {
        return new TimeSlotDTO(
                timeSlot.getTimeNo(),
                timeSlot.getDay(),
                timeSlot.getTime(),
                timeSlot.getAvailable(),
                timeSlot.getRestaurant().getChefNo()
        );
    }
}
