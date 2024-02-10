package com.totm.totm.repository;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testAuditing() {
        Member member = new Member("jakey1110", "1417", "간절한사람", "유창현", "010-4715-4652", Authority.MANAGER);

        memberRepository.save(member);
        Long id = member.getId();

        Member findMember = memberRepository.findById(id).get();

        System.out.println("Id = " + findMember.getId());
        System.out.println("CreatedDate = " + findMember.getCreatedDate());
    }
}