package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;
    @Column(length = 1000)
    private String reviewImage;
    private Date date;
    private int tastePoint;
    private int moodPoint;
    private int servePoint;
    @Column(length = 2000)
    private String contents;
    @Column(length = 500)
    private String reviewUpImgName;
    @Column(length = 500)
    private String reviewDownImgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memNo")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservationNo")
    private Reservation reservation;
}
