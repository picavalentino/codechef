package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "memNo")
    private Member member;

    @OneToOne
    @JoinColumn(name = "reservationNo")
    private Reservation reservation;
}
