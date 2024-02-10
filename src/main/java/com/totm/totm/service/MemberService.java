package com.totm.totm.service;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import com.totm.totm.exception.DuplicatedMemberException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PasswordsNotEqualException;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.totm.totm.dto.MemberDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = { MethodArgumentNotValidException.class })
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    로그인 관련은 JWT 와 함께, 마지막 접속 날짜 갱신 필요
//    @Transactional
//    public MemberResponseDto loginManager(String username, String password) {
//        Optional<Member> findMember = memberRepository.findByUsernameAndPasswordAndAuthority(username, password, Authority.MANAGER, MemberStatus.NORMAL);
//
//        MemberResponseDto
//    }

    // 고객 계정 조회
    public Page<UserResponseDto> findUser(String nickname, Pageable pageable) {
        Page<Member> findMembers = memberRepository.findMembersByNicknameContainingAndAuthority(nickname, Authority.USER, pageable);
        return findMembers.map(UserResponseDto::new);
    }

    // 고객 계정 정지
    @Transactional
    public void changeStopDeadline(Long id) throws MemberNotFoundException {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            findMember.get().changeStopDeadline(LocalDateTime.now().plusDays(7));
        }
        else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    // 관리자 계정 조회
    public Page<ManagerResponseDto> findManager(String name, Pageable pageable) {
        return memberRepository.findMembersByNameContainingAndAuthority(name, Authority.MANAGER, pageable).map(ManagerResponseDto::new);
    }

    // 관리자 계정 추가
    @Transactional
    public void addManager(AddManagerRequestDto manager) throws DuplicatedMemberException {
        if(duplicated(manager.getUsername(), Authority.MANAGER)) throw new DuplicatedMemberException("중복된 회원 존재");
        Member member = new Member(manager.getUsername(), passwordEncoder.encode(manager.getPassword()), null, manager.getName(), manager.getPhoneNumber(), Authority.MANAGER);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteManager(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean duplicated(String username, Authority authority) {
        Optional<Member> member = memberRepository.findByUsernameAndAuthority(username, authority);
        return member.isPresent();
    }

    public Integer today() {
        return memberRepository.countMembersByLastConnectedDateAndAuthority(LocalDate.now(), Authority.USER);
    }

    @Transactional
    public void resetPassword(Long id) throws MemberNotFoundException {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            findMember.get().changePassword(passwordEncoder.encode("123456789a"));
        }
        else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(request.getId());
        if(findMember.isPresent()) {
            if(passwordEncoder.matches(request.getOldPassword(), findMember.get().getPassword()))
                findMember.get().changePassword(passwordEncoder.encode(request.getNewPassword()));
            else throw new PasswordsNotEqualException("기존 비밀번호가 틀렸습니다.");
        }
        else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }
}
