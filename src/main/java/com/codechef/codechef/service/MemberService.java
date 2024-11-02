package com.codechef.codechef.service;

import com.codechef.codechef.dto.MemberDto;
import com.codechef.codechef.dto.MemberReviewDTO;
import com.codechef.codechef.dto.ReviewDTO;
import com.codechef.codechef.entity.Member;
import com.codechef.codechef.entity.Review;
import com.codechef.codechef.repository.MemberRepository;
import com.codechef.codechef.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, ReviewRepository reviewRepository) {
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
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

        member.setPassword(bCryptPasswordEncoder.encode(password));
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

    //memNo로 Member정보 조회 - Review
    public MemberReviewDTO getMemberByMemNo2(Long memNo) {
        Member member = memberRepository.findByMemNo(memNo);
        if (member == null) {
            return null;
        }
        return MemberReviewDTO.fromEntity(member);
    }
    // memNo로 리뷰 리스트를 페이징하여 가져오는 메서드
    public Page<ReviewDTO> getReviewsByMember(Long memNo, Pageable pageable) {
        // 리뷰를 페이징하여 가져오기
        Page<Review> reviews = reviewRepository.findByMembermemNo(memNo, pageable);
        return reviews.map(ReviewDTO::fromEntity); // ReviewDTO로 변환하여 반환
    }

    public String getNicknameByEmail(String email) {
        return memberRepository.findNicknameByEmail(email);
    }

    public Long getMemNoByEmail(String email) {
        return memberRepository.findMemNoByEmail(email);
    }

    // 사용자의 최신 리뷰 2개 가져오기
    public List<ReviewDTO> getLatestReviews(Long memNo) {
        // 사용자의 리뷰를 최신 순으로 가져오기 (최대 2개)
        return reviewRepository.findTop2ByMemberMemNoOrderByDateDesc(memNo).stream()
                .map(ReviewDTO::fromEntity)
                .limit(2) // 최대 2개로 제한
                .collect(Collectors.toList());
    }

    public void deleteMember(String email) {
        memberRepository.deleteMember(email);
    }
}
