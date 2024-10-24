package com.codechef.codechef.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long time_no;
    @Column(length = 10)
    private String day;
    @Column(length = 10)
    private String time;
    @ColumnDefault("false")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "chef_no")
    private Restaurant restaurant;
}
