package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chefNo")
    private Restaurant restaurant;
}
