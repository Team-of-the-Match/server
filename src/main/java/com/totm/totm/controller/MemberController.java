package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto request) {
        memberService.login(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
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

    @GetMapping("/today")
    public ResponseEntity today() {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.today()).build());
    }
}
