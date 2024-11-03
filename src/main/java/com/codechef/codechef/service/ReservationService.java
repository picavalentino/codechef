package com.codechef.codechef.service;

import com.codechef.codechef.dto.VisitExpectedDto;
import com.codechef.codechef.dto.ReservationDto;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.List;


@Service
public class ReservationService {
    @PersistenceContext
    private EntityManager entityManager;

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


    public Page<VisitExpectedDto> findReservationDataByMemNo(Long memNo, Pageable pageable) {
        return reservationRepository.findReservationDataByMemNo(memNo, pageable);
    }

    @Transactional
    public boolean deleteReservation(Long reservationNo) {
        if (reservationRepository.existsById(reservationNo)) {
            reservationRepository.deleteById(reservationNo);
            return true;
        } else {
            return false;
        }
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

    public Page<Reservation> getReservationsByMemberNo(Long memNo, Pageable pageable) {

        return reservationRepository.findByMemberMemNo(memNo, pageable);
    }

    public List<ReservationDto> visitOxFind(Long chefNo, Long memNo) {
        List<Reservation> reservations = reservationRepository.visitOxFind(chefNo, memNo);
        if(ObjectUtils.isEmpty(reservations)){
            return Collections.emptyList();
        }
        return reservations.stream()
                .map(x->ReservationDto.fromEntity(x)).toList();
    }



    // 예약날짜가 현재날짜보다 지났으면 visitOx 값을 자동으로 방문완료(true)로 전환
    @Transactional
    @Scheduled(fixedRate = 1800000)  // 30분마다 실행
    public void updateExpiredReservations() {
        Date now = new Date(); // 현재 날짜와 시간

        // reservationDate가 현재 날짜 이전인 항목들을 조회
        List<Reservation> expiredReservations = reservationRepository.findByReservationDateBeforeAndVisitOxFalse(now);

        // 만료된 예약의 visitOx를 true로 업데이트
        for (Reservation reservation : expiredReservations) {
            reservation.setVisitOx(true);
            reservationRepository.save(reservation); // 업데이트 후 저장
        }
    }
    public Long maxReservationNo() {
        return reservationRepository.maxReservationNo();
    }

    public void deleteReservationMemNum(Long memNum) {
        reservationRepository.deleteReservationMemNum(memNum);
    }

    public List<ReservationDto> getCurTimeAfter(LocalDateTime now, Long memNum) {
        List<Reservation> reservations = reservationRepository.getCurTimeAfter(now, memNum);
        if(ObjectUtils.isEmpty(reservations)){
            return Collections.emptyList();
        }
        return reservations.stream()
                .map(x->ReservationDto.fromEntity(x)).toList();
    }
}
