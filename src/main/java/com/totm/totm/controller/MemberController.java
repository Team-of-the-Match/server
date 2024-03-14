package com.totm.totm.controller;

import com.totm.totm.component.JwtTokenProvider;
import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
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

        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.login(request)).build());
    }

    @PatchMapping("/refresh")
    public ResponseEntity refresh(String email, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.refresh(email, request)).build());
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequestDto request) {
        memberService.signUp(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequestDto request) {
        memberService.resetPassword(id, request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDto request) {
        memberService.changePassword(id, request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/duplicated-email")
    public ResponseEntity duplicatedEmail(String email) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.duplicatedEmail(email)).build());
    }

    @GetMapping("/duplicated-nickname")
    public ResponseEntity duplicatedNickname(String nickname) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.duplicatedNickname(nickname)).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/members")
    public ResponseEntity findMembers(String nickname, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.findMembers(nickname, pageable)).build());
    }

    @PatchMapping("/stop/{id}")
    public ResponseEntity stopMember(@PathVariable Long id) {
        memberService.stopMember(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/confirmation-login")
    public ResponseEntity confirmationForLogin(String email, String value) {
        memberService.confirmationForLogin(email, value);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/send-email")
    public ResponseEntity sendEmail(String email) throws MessagingException {
        memberService.sendEmail(email);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/confirmation-password")
    public ResponseEntity confirmationForPassword(String email, String value) {
        memberService.confirmationForPassword(email, value);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
