package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeNo;
    @Column(length = 10)
    private String day;
    @Column(length = 10)
    private String time;
    @ColumnDefault("false")
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;
}
