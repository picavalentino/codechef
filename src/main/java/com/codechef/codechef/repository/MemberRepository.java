package com.codechef.codechef.repository;

import com.codechef.codechef.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPassword(String password);

    Member findByEmail(String email);

    Member findByMemNo(Long memNo);

    @Query("SELECT m.nickname FROM Member m WHERE m.email = :email")
    String findNicknameByEmail(@Param("email") String email);

    @Query("SELECT m.memNo FROM Member m WHERE m.email = :email")
    Long findMemNoByEmail(@Param("email") String email);
}
