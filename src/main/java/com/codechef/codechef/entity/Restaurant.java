package com.codechef.codechef.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chef_no;
    @Column(length = 10)
    private String chef_name;
    @Column(length = 20)
    private String chef_nickname;
    @Column(length = 20)
    private String res_name;
    @Column(length = 100)
    private String main_image;
    @Column(length = 100)
    private String banner_image;
    @Column(length = 12)
    private String res_phone_no;
    @Column(length = 100)
    private String address;
    @Column(length = 100)
    private String biz_hour;
    @Column(length = 10)
    private String category;
    @Column(length = 10)
    private String local_area;
    @Column(length = 1000)
    private String res_explain;
    @Column(length = 2000)
    private String res_mpa;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<TimeSlot> timeSlotList = new ArrayList<>();
}
