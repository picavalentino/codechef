package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_no;
    @Column(length = 100)
    private String review_image;
    private Date date;
    private int taste_point;
    private int mood_point;
    private int serve_point;
    @Column(length = 1000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "chef_no")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "mem_no")
    private Member member;

    @OneToOne
    @JoinColumn(name = "reservation_no")
    private Reservation reservation;
}
