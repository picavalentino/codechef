package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefNo;
    @Column(length = 10)
    private String chefName;
    @Column(length = 20)
    private String chefNickname;
    @Column(length = 20)
    private String resName;
    @Column(length = 500)
    private String mainImage;
    @Column(length = 500)
    private String bannerImage;
    @Column(length = 20)
    private String resPhoneNo;
    @Column(length = 100)
    private String address;
    @Column(length = 200)
    private String bizHour;
    @Column(length = 20)
    private String closedDay;
    @Column(length = 10)
    private String category;
    @Column(length = 10)
    private String localArea;
    @Column(length = 1000)
    private String resExplain;
    @Column(length = 2000)
    private String resMap;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<TimeSlot> timeSlotList = new ArrayList<>();
}
