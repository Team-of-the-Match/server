package com.totm.totm.service;

import com.totm.totm.dto.MemberDto;
import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.MemberStatus;
import com.totm.totm.exception.DuplicatedMemberException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = { MethodArgumentNotValidException.class })
public class MemberService {

    private final MemberRepository memberRepository;

//    로그인 관련은 JWT와 함께, 마지막 접속 날짜 갱신 필요
//    @Transactional
//    public MemberResponseDto loginManager(String username, String password) {
//        Optional<Member> findMember = memberRepository.findByUsernameAndPasswordAndAuthority(username, password, Authority.MANAGER, MemberStatus.NORMAL);
//
//        MemberResponseDto
//    }

    // 고객 계정 조회
    public Page<MemberDto.UserResponseDto> findUser(String username, Pageable pageable) {
        Page<Member> findMembers = memberRepository.findMembersByUsernameContainingAndAuthority(username, Authority.USER, pageable);
        return findMembers.map(MemberDto.UserResponseDto::new);
    }

    // 고객 계정 정지/정지 해제
    @Transactional
    public void changeMemberStatusToStopOrNormal(Long id) throws MemberNotFoundException {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            if(findMember.get().getMemberStatus() == MemberStatus.STOPPED) findMember.get().changeMemberStatus(MemberStatus.NORMAL);
            else findMember.get().changeMemberStatus(MemberStatus.STOPPED);
        }
        else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    // 관리자 계정 조회
    public Page<MemberDto.ManagerResponseDto> findManager(String name, Pageable pageable) {
        Page<Member> findMembers = memberRepository.findMembersByNameContainingAndAuthority(name, Authority.MANAGER, pageable);
        return findMembers.map(MemberDto.ManagerResponseDto::new);
    }

    // 관리자 계정 추가
    @Transactional
    public void addManager(MemberDto.AddManagerRequestDto manager) throws DuplicatedMemberException {
        if(duplicated(manager.getUsername(), Authority.MANAGER)) throw new DuplicatedMemberException("중복된 회원 존재");
        Member member = new Member(manager.getUsername(), manager.getPassword(), null, manager.getName(), manager.getPhoneNumber(), Authority.MANAGER, MemberStatus.NORMAL);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteManager(Long id) throws MemberNotFoundException {
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()) memberRepository.delete(member.get());
        else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    public boolean duplicated(String username, Authority authority) {
        Optional<Member> member = memberRepository.findByUsernameAndAuthority(username, authority);
        return member.isPresent();
    }
}
