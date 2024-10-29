package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPassword(String password);
}
