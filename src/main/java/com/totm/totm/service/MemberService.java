package com.totm.totm.service;

import com.totm.totm.component.JwtTokenProvider;
import com.totm.totm.dto.TokenResponseDto;
import com.totm.totm.entity.Member;
import com.totm.totm.exception.*;
import com.totm.totm.repository.CommentRepository;
import com.totm.totm.repository.LikesRepository;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import static com.totm.totm.dto.MemberDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = { MethodArgumentNotValidException.class })
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional(noRollbackFor = { MemberUnconfirmedException.class }, rollbackFor = { MessagingException.class })
    public TokenResponseDto login(LoginRequestDto request) throws MessagingException {
        Optional<Member> findMember = memberRepository.findByEmail(request.getEmail());
        if(findMember.isPresent()) {
            if(!passwordEncoder.matches(request.getPassword(), findMember.get().getPassword()))
                throw new PasswordNotEqualException("이메일 또는 비밀번호가 틀렸습니다.");
//            if(!findMember.get().isConfirmed()) {
//                int confirmationValue = new Random().nextInt(888888) + 111111;
//
//                try {
//                    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//                    mimeMessageHelper.setTo(request.getEmail());
//                    mimeMessageHelper.setSubject("[Team of the Match] 인증 코드");
//                    mimeMessageHelper.setText("아래의 인증번호를 입력해주세요." + "\n" + "\n" + confirmationValue);
//
//                    javaMailSender.send(mimeMessage);
//                } catch(MessagingException e) {
//                    throw new MessagingException("메일 전송에 실패했습니다.");
//                }
//
//                redisTemplate.opsForValue().set(
//                        request.getEmail(),
//                        confirmationValue,
//                        Duration.ofMinutes(4)
//                );
//                throw new MemberUnconfirmedException("unconfirmed");
//            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            String accessToken = jwtTokenProvider.createAccessToken(authentication, findMember.get().getId(), findMember.get().getNickname(), "member");
            String refreshToken = jwtTokenProvider.createRefreshToken();

            redisTemplate.opsForValue().set(findMember.get().getEmail(), refreshToken, Duration.ofDays(7L));

            return new TokenResponseDto(accessToken, refreshToken);
        } else throw new MemberNotFoundException("이메일 또는 비밀번호가 틀렸습니다.");
    }

    @Transactional
    public TokenResponseDto refresh(String email, HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if(StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        String savedToken = (String) redisTemplate.opsForValue().get(email);
        if(refreshToken.equals(savedToken)) {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없음"));

            String accessToken = jwtTokenProvider.createNewAccessToken(member.getEmail(), member.getId(), member.getNickname(), "member");
            String newRefreshToken = jwtTokenProvider.createRefreshToken();

            redisTemplate.opsForValue().set(email, newRefreshToken, Duration.ofDays(7L));

            return new TokenResponseDto(accessToken, newRefreshToken);
        } else
            throw new TokenNotFoundException("다시 로그인 후 시도해주세요.");
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

    @Transactional
    public void confirmationForLogin(String email, String value) {
        Integer s = (Integer) redisTemplate.opsForValue().get(email);
        if(s == null) throw new ValueExpiredException("인증번호가 만료되었습니다. 다시 로그인 후 인증해주세요.");
        if(!s.toString().equals(value)) throw new ValueNotEqualException("인증번호가 일치하지 않습니다.");

        Optional<Member> findMember = memberRepository.findByEmail(email);
        if(findMember.isPresent()) {
            findMember.get().confirm();
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
    }

    @Transactional(rollbackFor = { MessagingException.class })
    public void sendEmail(String email) throws MessagingException {
        int confirmationValue = new Random().nextInt(888888) + 111111;
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[Team of the Match] 인증 코드");
            mimeMessageHelper.setText("아래의 인증번호를 입력해주세요." + "\n" + confirmationValue);

            javaMailSender.send(mimeMessage);
        } catch(MessagingException e) {
            throw new MessagingException("메일 전송에 실패했습니다.");
        }
        redisTemplate.opsForValue().set(
                email,
                confirmationValue,
                Duration.ofMinutes(4)
        );
    }

    public void confirmationForPassword(String email, String value) {
        Integer s = (Integer) redisTemplate.opsForValue().get(email);
        if(s == null) throw new ValueExpiredException("인증번호가 만료되었습니다. 다시 로그인 후 인증해주세요.");
        if(!s.toString().equals(value)) throw new ValueNotEqualException("인증번호가 일치하지 않습니다.");
    }
}
