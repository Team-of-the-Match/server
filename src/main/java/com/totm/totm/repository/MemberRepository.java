package com.totm.totm.repository;

import com.totm.totm.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Page<Member> findMembersByNicknameContainsOrderByCreatedDateDesc(String nickname, Pageable pageable);

    int countByLastConnectedDate(LocalDate localDate);
}
