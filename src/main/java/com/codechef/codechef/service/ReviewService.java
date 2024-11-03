package com.codechef.codechef.service;

import com.codechef.codechef.dto.ReviewCreateDTO;
import com.codechef.codechef.dto.ReviewDTO;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.ReservationRepository;
import com.codechef.codechef.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final S3Service s3Service;

    public ReviewService(ReviewRepository reviewRepository, ReservationRepository reservationRepository, S3Service s3Service) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.s3Service = s3Service;
    }

    public void createReview(ReviewCreateDTO dto) {
        try {
            String imageUrl = null;

            // S3에 업로드할 파일이 있는지 체크
            if (dto.getReviewImage() != null && !dto.getReviewImage().isEmpty()) {
                // 파일이 선택된 경우
                imageUrl = s3Service.uploadFile(dto.getReviewImage());
            } else {
                // 파일이 선택되지 않은 경우 기본 이미지 URL 또는 null 설정
                imageUrl = null; // 기본 이미지 URL로 설정
            }

            Review review = ReviewCreateDTO.fromDto(dto);
            review.setReviewImage(imageUrl);
            review.setReviewUpImgName(dto.getReviewImage().getOriginalFilename());
            review.setReviewDownImgName(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));

            log.info("리뷰 저장 시도: {}", review);

            reviewRepository.save(review);
            log.info("리뷰 저장 성공");
        } catch (IOException e) {
            log.error("파일 업로드에 실패했습니다.", e);
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        } catch (Exception e) {
            log.error("리뷰 저장 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("리뷰 저장 중 오류 발생.", e);
        }
    }

    public void updateReviewOxByReservationNo(Long reservationNo) {
        // reservation_no로 Reservation 찾기
        Reservation reservation = reservationRepository.findByReservationNo(reservationNo);
        if (reservation != null) {
            // review_ox 값을 true로 변경
            reservation.setReviewOx(true);
            // 변경된 Reservation 저장
            reservationRepository.save(reservation);
        }
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewNo) {
        if (!reviewRepository.existsById(reviewNo)) {
            throw new RuntimeException("리뷰가 존재하지 않습니다.");
        }

        Review review = reviewRepository.findById(reviewNo).orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        //리뷰 삭제
        reviewRepository.deleteById(reviewNo);
        log.info("리뷰가 삭제되었습니다: {}", reviewNo);

        // 예약의 reviewOx 값을 false로 업데이트
        updateReservationReviewOx(review.getReservation());
    }

    // 예약의 reviewOx 값을 false로 업데이트하는 메서드
    private void updateReservationReviewOx(Reservation reservation) {
        if (reservation != null) {
            reservation.setReviewOx(false); // reviewOx 값을 false로 변경
            reservationRepository.save(reservation); // 변경사항 저장
            log.info("예약의 reviewOx가 false로 변경되었습니다: {}", reservation.getReservationNo());
        }
    }

    public List<ReviewDTO> findByChefNoAndMemNo(Long chefNo, Long memNo) {
        List<Review> reviews = reviewRepository.findByRestaurant_ChefNoAndMember_MemNo(chefNo, memNo);
        if(ObjectUtils.isEmpty(reviews)){
            return Collections.emptyList();
        }
        return reviews.stream()
                .map(ReviewDTO::fromEntity).toList();
    }
}
