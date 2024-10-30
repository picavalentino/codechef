package com.codechef.codechef.service;

import com.codechef.codechef.dto.ReviewCreateDTO;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.ReservationRepository;
import com.codechef.codechef.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
            String imageUrl = s3Service.uploadFile(dto.getReviewImage());

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
        reviewRepository.deleteById(reviewNo);
        log.info("리뷰가 삭제되었습니다: {}", reviewNo);
    }
}
