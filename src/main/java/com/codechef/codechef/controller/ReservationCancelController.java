package com.codechef.codechef.controller;

import com.codechef.codechef.repository.ReservationRepository;
import com.codechef.codechef.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReservationCancelController {
    @Autowired
    ReservationService reservationService;

    @PostMapping("/cancel-reservation")
    public ResponseEntity<String> cancelReservation(@RequestBody Map<String, Long> requestData) {
        Long reservationNo = requestData.get("reservationNo");
        boolean isDeleted = reservationService.deleteReservation(reservationNo);

        if (isDeleted) {
            return ResponseEntity.ok("예약이 취소되고 삭제되었습니다.");
        } else {
            return ResponseEntity.status(400).body("예약 취소 및 삭제에 실패했습니다.");
        }
    }

}
