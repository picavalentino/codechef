package com.codechef.codechef.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memNo;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 20)
    private String passwordCheck;
    @Column(length = 20, nullable = false)
    private String phoneNo;
    @Column(length = 20, nullable = false)
    private String nickname;
    @Column(columnDefinition = "bytea")
    private byte[] memImage;
    @Column(length = 500)
    private String memImageUpName;
    @Column(length = 500)
    private String memImageDownName;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>();

    public Member(Long memNo, String email, String phoneNo, String nickname, byte[] memImage, String password, String passwordCheck) {
    }
}
