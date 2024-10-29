package com.codechef.codechef.service;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeChefService {
    @Autowired
    MemberRepository memberRepository;

    public void joinMember(MemberDto memberDTO) {
        Member data = new Member();
        data.setEmail(memberDTO.getEmail());
        data.setPassword(memberDTO.getPassword()); // 자동으로 암호화해서 넣어줌
        data.setNickname(memberDTO.getNickname());
        data.setPhoneNo(memberDTO.getPhoneNo());

        memberRepository.save(data);
    }

    public void loginService(MemberDto memberDTO) {
        Boolean isUser = memberRepository.existsByEmail(memberDTO.getEmail());
        Boolean isPassword = memberRepository.existsByPassword(memberDTO.getPassword());

        if (isUser && isPassword) {
            return;
        };
    }
}
