package com.codechef.codechef.service;

import com.codechef.codechef.dto.ReservationDto;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public void insertReservationInfo(Long reservation_no, LocalDateTime reservationDate, int memberCount, boolean reviewOx, boolean visitOx, Long memNo, Long chefNo) {
        reservationRepository.insertReservationInfo(reservation_no, reservationDate, memberCount, reviewOx, visitOx, memNo, chefNo);
    }

    public List<Long> getChefNosByMemNo(Long memNo) {
        return reservationRepository.findChefNosByMemNo(memNo);
    }

    // memNo로 방문 예정 예약 2개 가져오기(오래된 날짜 순, visitOx가 false인 경우만)
    public List<ReservationDto> getEarliestReservations(Long memNo) {
        List<Reservation> reservations = reservationRepository.findEarliestReservationsByMemberMemNo(memNo);
        return reservations.stream()
                .map(ReservationDto::fromEntity) // ReservationDto로 변환하는 메서드 필요
                .limit(2) // 최대 2개로 제한
                .collect(Collectors.toList());
    }

    // memNo로 방문 완료 예약 2개 가져오기(최근 날짜 순, visitOx가 true인 경우만)
    public List<ReservationDto> getLatestReservations(Long memNo) {
        List<Reservation> reservations = reservationRepository.findTop2ByMemberMemNoAndVisitOxTrueOrderByReservationDateDesc(memNo);
        return reservations.stream()
                .map(ReservationDto::fromEntity) // ReservationDto로 변환하는 메서드 필요
                .limit(2) // 최대 2개로 제한
                .collect(Collectors.toList());
    }

    public List<ReservationDto> visitOxFind(Long chefNo, Long memNo) {
        List<Reservation> reservations = reservationRepository.visitOxFind(chefNo, memNo);
        if(ObjectUtils.isEmpty(reservations)){
            return Collections.emptyList();
        }
        return reservations.stream()
                .map(x->ReservationDto.fromEntity(x)).toList();
    }

    public Long maxReservationNo() {
        return reservationRepository.maxReservationNo();
    }
}
