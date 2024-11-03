package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Restaurant;
import com.codechef.codechef.entity.Review;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long reservationNo;
    private Date reservationDate;
    private int memberCount;
    private Boolean reviewOx;
    private Boolean visitOx;
    private Member member;
    private Restaurant restaurant;
    private Review review;

    public static ReservationDto fromEntity(Reservation reservation) {
        return new ReservationDto(
                reservation.getReservationNo(),
                reservation.getReservationDate(),
                reservation.getMemberCount(),
                reservation.getReviewOx(),
                reservation.getVisitOx(),
                reservation.getMember(),
                reservation.getRestaurant(),
                reservation.getReview()
        );
    }
}
