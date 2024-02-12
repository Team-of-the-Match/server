package com.totm.totm.service;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import com.totm.totm.exception.DuplicatedMemberException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PasswordsNotEqualException;
import com.totm.totm.repository.CommentRepository;
import com.totm.totm.repository.LikesRepository;
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
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final PasswordEncoder passwordEncoder;

    public void loginUser(LoginRequestDto request) {
        Optional<Member> findMember = memberRepository.findByUsernameAndAuthority(request.getUsername(), Authority.USER);
        if(findMember.isPresent()) {
            if(passwordEncoder.matches(request.getPassword(), findMember.get().getPassword())) return;
            else throw new MemberNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        } else throw new MemberNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
    }

    public void loginManager(LoginRequestDto request) {
        Optional<Member> findMember = memberRepository.findByUsernameAndAuthority(request.getUsername(), Authority.MANAGER);
        if(findMember.isPresent()) {
            if(passwordEncoder.matches(request.getPassword(), findMember.get().getPassword())) return;
            else throw new MemberNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
        } else throw new MemberNotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
    }

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

    @Transactional
    public void addUser(AddUserRequestDto request) {
        if(duplicatedUsername(request.getUsername(), Authority.USER)) throw new DuplicatedMemberException("중복된 회원 존재");
        if(duplicatedNickname(request.getNickname(), Authority.USER)) throw new DuplicatedMemberException("중복된 닉네임 존재");
        if(duplicatedPhoneNumber(request.getPhoneNumber(), Authority.USER)) throw new DuplicatedMemberException("중복된 전화번호 존재");
        Member member = new Member(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getNickname(), request.getName(), request.getPhoneNumber(), Authority.USER);
        memberRepository.save(member);
    }

    // 관리자 계정 추가
    @Transactional
    public void addManager(AddManagerRequestDto request) throws DuplicatedMemberException {
        if(duplicatedUsername(request.getUsername(), Authority.MANAGER)) throw new DuplicatedMemberException("중복된 회원 존재");
        Member member = new Member(request.getUsername(), passwordEncoder.encode(request.getPassword()), null, request.getName(), request.getPhoneNumber(), Authority.MANAGER);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteManager(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean duplicatedUsername(String username, Authority authority) {
        Optional<Member> member = memberRepository.findByUsernameAndAuthority(username, authority);
        return member.isPresent();
    }

    public boolean duplicatedNickname(String nickname, Authority authority) {
        Optional<Member> member = memberRepository.findByNicknameAndAuthority(nickname, authority);
        return member.isPresent();
    }

    public boolean duplicatedPhoneNumber(String phoneNumber, Authority authority) {
        Optional<Member> member = memberRepository.findByPhoneNumberAndAuthority(phoneNumber, authority);
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

    public String findUsername(FindUsernameRequestDto request) {
        Optional<Member> findMember = memberRepository.findUsernameByNameAndPhoneNumberAndAuthority(request.getName(), request.getPhoneNumber(), Authority.USER);
        if(findMember.isPresent()) {
            return findMember.get().getUsername();
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    @Transactional
    public void resetNewPassword(ResetNewPasswordRequestDto request) {
        Optional<Member> findMember = memberRepository.findMemberByUsernameAndNameAndPhoneNumberAndAuthority(request.getUsername(), request.getName(), request.getPhoneNumber(), Authority.USER);
        if(findMember.isPresent()) {
            findMember.get().changePassword(passwordEncoder.encode(request.getPassword()));
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    @Transactional
    public void deleteUser(Long id) {
        postRepository.bulkPostMember(id);
        commentRepository.bulkCommentMember(id);
        likesRepository.bulkLikesMember(id);
        memberRepository.deleteById(id);
    }

    @Transactional
    public void changePhoneNumber(Long id, ChangePhoneNumberRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            findMember.get().changePhoneNumber(request.getPhoneNumber());
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }
}
