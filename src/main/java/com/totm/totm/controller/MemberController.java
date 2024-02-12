package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.entity.Authority;
import com.totm.totm.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @PostMapping("/user/login")
    public ResponseEntity loginUser(@Valid @RequestBody LoginRequestDto request) {
        memberService.loginUser(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PostMapping("/manager/login")
    public ResponseEntity loginManager(@Valid @RequestBody LoginRequestDto request) {
        memberService.loginManager(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/user/find")
    public ResponseEntity findUser(String nickname, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.findUser(nickname, pageable)).build());
    }

    @GetMapping("/manager/find")
    public ResponseEntity findManager(String name, Pageable pageable) {
        Page<ManagerResponseDto> result = memberService.findManager(name, pageable);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(result).build());
    }

    @PostMapping("/user/add")
    public ResponseEntity addUser(@Valid @RequestBody AddUserRequestDto request) {
        memberService.addUser(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PostMapping("/manager/add")
    public ResponseEntity addManager(@Valid @RequestBody AddManagerRequestDto request) {
        memberService.addManager(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @DeleteMapping("/manager/delete/{id}")
    public ResponseEntity deleteManager(@PathVariable("id") Long id) {
        memberService.deleteManager(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/user/stop/{id}")
    public ResponseEntity stopUser(@PathVariable("id") Long id) {
        memberService.changeStopDeadline(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/user/duplicate/username")
    public ResponseEntity userDuplicatedUsername(String username) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.duplicatedUsername(username, Authority.USER))
                        .build());
    }

    @GetMapping("/user/duplicate/nickname")
    public ResponseEntity userDuplicatedNickname(String nickname) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.duplicatedNickname(nickname, Authority.USER))
                        .build());
    }

    @GetMapping("/manager/duplicate")
    public ResponseEntity managerDuplicated(String username) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.duplicatedUsername(username, Authority.MANAGER))
                        .build());
    }

    @GetMapping("/user/today")
    public ResponseEntity today() {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.today()).build());
    }

    @PatchMapping("/manager/reset/{id}")
    public ResponseEntity resetPassword(@PathVariable("id") Long id) {
        memberService.resetPassword(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PostMapping("/change/password")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        memberService.changePassword(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PostMapping("/find/username")
    public ResponseEntity findUsername(@Valid @RequestBody FindUsernameRequestDto request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(memberService.findUsername(request)).build());
    }

    @PatchMapping("/reset/password")
    public ResponseEntity resetNewPassword(@Valid @RequestBody ResetNewPasswordRequestDto request) {
        memberService.resetNewPassword(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        memberService.deleteUser(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/change/phone-number/{id}")
    public ResponseEntity changePhoneNumber(@PathVariable Long id, @Valid @RequestBody ChangePhoneNumberRequestDto request) {
        memberService.changePhoneNumber(id, request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
