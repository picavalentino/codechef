package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNo;
    private Date reservationDate;
    private int memberCount;
    @ColumnDefault("false")
    private Boolean reviewOx;
    @ColumnDefault("false")
    private Boolean visitOx;

    @ManyToOne
    @JoinColumn(name = "memNo")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;

    @OneToOne(mappedBy = "reservation")
    private Review review;

}
