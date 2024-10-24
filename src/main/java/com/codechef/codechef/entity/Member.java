package com.codechef.codechef.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mem_no;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 20, nullable = false)
    private String phone_no;
    @Column(length = 20, nullable = false)
    private String nickname;
    @Column(length = 100)
    private String mem_image;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>();
}
