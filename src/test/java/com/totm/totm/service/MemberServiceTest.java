package com.totm.totm.service;

import com.totm.totm.entity.Member;
import com.totm.totm.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 멤버_이름변경() {
        long f = 0;
        int testNum = 10000;
        for(int i = 0; i < testNum; i++) {
            Member member = new Member("jakey111" + i, "14234242", "13" + i, false);
            memberRepository.save(member);
            if(i == 0) f = member.getId();
        }

        em.flush();

        for (long i = f; i < f + testNum; i++) {
            Optional<Member> findMember = memberRepository.findById(i);
            if(findMember.isEmpty()) continue;
            findMember.get().changePassword("134243242423");
        }
    }
}