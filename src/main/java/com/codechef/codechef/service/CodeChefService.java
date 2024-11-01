package com.codechef.codechef.service;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CodeChefService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    MemberRepository memberRepository;

    public void joinMember(MemberDto memberDTO) {
        Boolean isUser = memberRepository.existsByEmail(memberDTO.getEmail());
        if(isUser) {
            return;
        }

        Member data = new Member();
        data.setEmail(memberDTO.getEmail());
        data.setPassword(bCryptPasswordEncoder.encode(memberDTO.getPassword()));
        data.setPasswordCheck(memberDTO.getPasswordCheck());
        data.setNickname(memberDTO.getNickname());
        data.setPhoneNo(memberDTO.getPhoneNo());

        memberRepository.save(data);
    }


    public boolean isEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }
}
