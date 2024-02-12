package com.totm.totm.repository;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 계정 조회
    Page<Member> findMembersByNicknameContainingAndAuthority(String nickname, Authority authority, Pageable pageable);

    Page<Member> findMembersByNameContainingAndAuthority(String username, Authority authority, Pageable pageable);

    Optional<Member> findByUsernameAndAuthority(String username, Authority authority);

    Optional<Member> findByNicknameAndAuthority(String nickname, Authority authority);

    Optional<Member> findByPhoneNumberAndAuthority(String phoneNumber, Authority authority);

    Integer countMembersByLastConnectedDateAndAuthority(LocalDate date, Authority authority);

    Optional<Member> findUsernameByNameAndPhoneNumberAndAuthority(String name, String phoneNumber, Authority authority);

    Optional<Member> findMemberByUsernameAndNameAndPhoneNumberAndAuthority(String username, String name, String PhoneNumber, Authority authority);
}
