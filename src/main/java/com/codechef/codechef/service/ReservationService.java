package com.codechef.codechef.service;

import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation getReservationById(Long reservationNo) {
        return reservationRepository.findById(reservationNo).orElseThrow(() ->
                new IllegalArgumentException("Reservation not found with ID: " + reservationNo));
    }

    public void insertReservationInfo(LocalDateTime reservationDate, int memberCount, boolean reviewOx, boolean visitOx, Long memNo, Long chefNo) {
        reservationRepository.insertReservationInfo(reservationDate, memberCount, reviewOx, visitOx, memNo, chefNo);
    }

    public List<Long> getChefNosByMemNo(Long memNo) {
        return reservationRepository.findChefNosByMemNo(memNo);
    }
}
