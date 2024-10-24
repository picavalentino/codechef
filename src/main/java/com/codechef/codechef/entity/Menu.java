package com.codechef.codechef.entity;

import jakarta.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menu_no;
    @Column(length = 50)
    private String menu_name;
    @Column(length = 100)
    private String menu_image;
    @Column(length = 100)
    private String menu_price;
    @Column(length = 500)
    private String menu_explain;

    @ManyToOne
    @JoinColumn(name = "chef_no")
    private Restaurant restaurant;
}
