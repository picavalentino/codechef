package com.codechef.codechef.dto;

import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Reservation;
import com.codechef.codechef.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long memNo;
    private String email;
    private String password;
    private String passwordCheck;
    private String phoneNo;
    private String nickname;
    private byte[] memImage;

    public static MemberDto fromEntity(Member member){
        return new MemberDto(
                member.getMemNo(),
                member.getEmail(),
                member.getPassword(),
                member.getPasswordCheck(),
                member.getPhoneNo(),
                member.getNickname(),
                member.getMemImage()
        );
    }

    public static Member fromDto(MemberDto dto){
        return new Member(
                dto.getMemNo(),
                dto.getEmail(),
                dto.getPhoneNo(),
                dto.getNickname(),
                dto.memImage,
                dto.password,
                dto.passwordCheck
        );
    }
}
