package com.codechef.codechef.entity;

import jakarta.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuNo;
    @Column(length = 50)
    private String menuName;
    @Column(length = 500)
    private String menuImage;
    @Column(length = 100)
    private String menuPrice;
    @Column(length = 500)
    private String menuExplain;

    @ManyToOne
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;
}
