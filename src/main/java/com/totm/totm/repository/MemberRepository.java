package com.totm.totm.repository;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인
    Optional<Member> findByUsernameAndPasswordAndAuthorityAndMemberStatus(String username, String password, Authority authority, MemberStatus memberStatus);

    // 고객 계정 조회
    Page<Member> findMembersByUsernameContainingAndAuthority(String username, Authority authority, Pageable pageable);

    // 관리자 계정 조회
    Page<Member> findMembersByNameContainingAndAuthority(String name, Authority authority, Pageable pageable);

    Optional<Member> findByUsernameAndAuthority(String username, Authority authority);
}
