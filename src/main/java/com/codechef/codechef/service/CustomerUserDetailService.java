package com.codechef.codechef.service;

import com.codechef.codechef.dto.CustomerUserDetails;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CustomerUserDetailService implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private HttpSession session; // HttpSession 주입

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member userdata = memberRepository.findByEmail(email);
        if (!ObjectUtils.isEmpty(userdata)) {
            // 로그인 성공 시 세션에 memNo 저장
            session.setAttribute("loggedInUser", userdata.getMemNo()); // memNo를 세션에 저장

            return new CustomerUserDetails(userdata);
        }
        return null;
    }
}
