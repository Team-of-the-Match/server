package com.totm.totm.controller;

import com.totm.totm.component.JwtTokenProvider;
import com.totm.totm.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto request) throws MessagingException {

        return ResponseEntity.ok(memberService.login(request));
    }

    @PatchMapping("/refresh")
    public ResponseEntity refresh(String email, HttpServletRequest request) {
        return ResponseEntity.ok(memberService.refresh(email, request));
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequestDto request) {
        memberService.signUp(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequestDto request) {
        memberService.resetPassword(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDto request) {
        memberService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicated-email")
    public ResponseEntity duplicatedEmail(String email) {
        return ResponseEntity.ok(memberService.duplicatedEmail(email));
    }

    @GetMapping("/duplicated-nickname")
    public ResponseEntity duplicatedNickname(String nickname) {
        return ResponseEntity.ok(memberService.duplicatedNickname(nickname));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity findMembers(String nickname, Pageable pageable) {
        return ResponseEntity.ok(memberService.findMembers(nickname, pageable));
    }

    @PatchMapping("/stop/{id}")
    public ResponseEntity stopMember(@PathVariable Long id) {
        memberService.stopMember(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/confirmation-login")
    public ResponseEntity confirmationForLogin(String email, String value) {
        memberService.confirmationForLogin(email, value);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/send-email")
    public ResponseEntity sendEmail(String email) throws MessagingException {
        memberService.sendEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirmation-password")
    public ResponseEntity confirmationForPassword(String email, String value) {
        memberService.confirmationForPassword(email, value);
        return ResponseEntity.ok().build();
    }
}
