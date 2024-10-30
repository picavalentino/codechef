package com.codechef.codechef.service;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMember(MemberDto dto) {
        memberRepository.save(MemberDto.fromDto(dto));
    }

    public Member getMemberByMemNo(Long memNo) {
        // mem_no로 회원 정보 조회
        return memberRepository.findById(memNo).orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memNo));
    }

    public void updateMemberInfo(Long memNo, String password, String passwordCheck, String phoneNo, String nickname, byte[] profileImage) {
        Member member = memberRepository.findById(memNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memNo));

        member.setPassword(password);
        member.setPasswordCheck(passwordCheck);
        member.setPhoneNo(phoneNo);
        member.setNickname(nickname);
        member.setMemImage(profileImage);


        memberRepository.save(member);
    }

    public Member getMemberWithMemNoOne() {
        return memberRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
    }
}
