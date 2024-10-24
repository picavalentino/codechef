package com.codechef.codechef.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_no;
    private Date reservation_date;
    private int member_count;
    @ColumnDefault("false")
    private Boolean review_ox;
    @ColumnDefault("false")
    private Boolean visit_ox;

    @ManyToOne
    @JoinColumn(name = "mem_no")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chef_no")
    private Restaurant restaurant;
}
