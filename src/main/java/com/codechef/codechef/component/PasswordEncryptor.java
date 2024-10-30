package com.codechef.codechef.component;

import com.codechef.codechef.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
//public class PasswordEncryptor implements CommandLineRunner {
//    @Autowired
//    private MemberRepository memberRepository; // 사용자 엔티티가 포함된 Repository
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        memberRepository.findAll().forEach(member -> {
//            if (!member.getPassword().startsWith("{bcrypt}")) {
//                String encryptedPassword = passwordEncoder.encode(member.getPassword());
//                member.setPassword(encryptedPassword);
//                memberRepository.save(member);
//            }
//        });
//    }
//}
