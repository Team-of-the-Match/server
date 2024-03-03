package com.totm.totm.service;

import com.totm.totm.entity.Member;
import com.totm.totm.entity.score.*;
import com.totm.totm.exception.DuplicatedEmailException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PasswordNotEqualException;
import com.totm.totm.repository.CommentRepository;
import com.totm.totm.repository.LikesRepository;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import com.totm.totm.repository.score.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
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
    private final FootballScoreRepository footballScoreRepository;
    private final BaseballScoreRepository baseballScoreRepository;
    private final BasketballScoreRepository basketballScoreRepository;
    private final AbroadFootballScoreRepository abroadFootballScoreRepository;
    private final AbroadBasketballScoreRepository abroadBasketballScoreRepository;

    @Transactional
    public void login(LoginRequestDto request) {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if(findMember.isPresent()) {
            if(!passwordEncoder.matches(request.getPassword(), findMember.get().getPassword()))
                throw new PasswordNotEqualException("이메일 또는 비밀번호가 틀렸습니다.");
            if(findMember.get().getLastConnectedDate() == null) {
                FootballScore footballScore = new FootballScore(findMember.get(), LocalDate.now().getYear(), 0);
                BaseballScore baseballScore = new BaseballScore(findMember.get(), LocalDate.now().getYear(), 0);
                BasketballScore basketballScore = new BasketballScore(findMember.get(), LocalDate.now().getYear(), 0);
                AbroadFootballScore abroadFootballScore = new AbroadFootballScore(findMember.get(), LocalDate.now().getYear(), 0);
                AbroadBasketballScore abroadBasketballScore = new AbroadBasketballScore(findMember.get(), LocalDate.now().getYear(), 0);
                footballScoreRepository.save(footballScore);
                baseballScoreRepository.save(baseballScore);
                basketballScoreRepository.save(basketballScore);
                abroadFootballScoreRepository.save(abroadFootballScore);
                abroadBasketballScoreRepository.save(abroadBasketballScore);
            } else if(findMember.get().getLastConnectedDate().getYear() != LocalDate.now().getYear()) {
                FootballScore footballScore = new FootballScore(findMember.get(), LocalDate.now().getYear(), 0);
                BaseballScore baseballScore = new BaseballScore(findMember.get(), LocalDate.now().getYear(), 0);
                BasketballScore basketballScore = new BasketballScore(findMember.get(), LocalDate.now().getYear(), 0);
                AbroadFootballScore abroadFootballScore = new AbroadFootballScore(findMember.get(), LocalDate.now().getYear(), 0);
                AbroadBasketballScore abroadBasketballScore = new AbroadBasketballScore(findMember.get(), LocalDate.now().getYear(), 0);
                footballScoreRepository.save(footballScore);
                baseballScoreRepository.save(baseballScore);
                basketballScoreRepository.save(basketballScore);
                abroadFootballScoreRepository.save(abroadFootballScore);
                abroadBasketballScoreRepository.save(abroadBasketballScore);
            }
            findMember.get().setLastConnectedDateToday();
        } else throw new MemberNotFoundException("이메일 또는 비밀번호가 틀렸습니다.");
    }

    @Transactional
    public void signUp(SignUpRequestDto request) {
        if(duplicatedEmail(request.getEmail())) throw new DuplicatedEmailException("중복된 이메일입니다.");
        if(duplicatedNickname(request.getNickname())) throw new DuplicatedEmailException("중복된 닉네임입니다.");

        Member member = new Member(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getNickname(), false);
        memberRepository.save(member);
    }

    public boolean duplicatedEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.isPresent();
    }

    public boolean duplicatedNickname(String nickname) {
        Optional<Member> findMember = memberRepository.findByNickname(nickname);
        return findMember.isPresent();
    }

    @Transactional
    public void resetPassword(Long id, ResetPasswordRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
                findMember.get().changePassword(passwordEncoder.encode(request.getPassword()));
        } else throw new MemberNotFoundException("해당 멤버가 존재하지 않습니다.");
    }

    @Transactional
    public void changePassword(Long id, ChangePasswordRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            if(passwordEncoder.matches(request.getOldPassword(), findMember.get().getPassword())) {
                findMember.get().changePassword(passwordEncoder.encode(request.getNewPassword()));
            } else throw new PasswordNotEqualException("기존 패스워드가 일치하지 않습니다.");
        } else throw new MemberNotFoundException("해당 멤버가 존재하지 않습니다.");
    }

    @Transactional
    public void delete(Long id) {
        postRepository.bulkPostMember(id);
        commentRepository.bulkCommentMember(id);
        likesRepository.bulkLikesMember(id);
        memberRepository.deleteById(id);
    }

    public Page<FindMembersResponseDto> findMembers(String nickname, Pageable pageable) {
        return memberRepository.findMembersByNicknameContainsOrderByCreatedDateDesc(nickname, pageable)
                .map(FindMembersResponseDto::new);
    }

    @Transactional
    public void stopMember(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            findMember.get().stopMember();
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없습니다.");
    }

    public int today() {
        return memberRepository.countByLastConnectedDate(LocalDate.now());
    }
}
