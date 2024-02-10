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

    @GetMapping("/user/find")
    public ResponseEntity findUser(String nickname, Pageable pageable) {
        Page<UserResponseDto> result = memberService.findUser(nickname, pageable);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(result).build());
    }

    @GetMapping("/manager/find")
    public ResponseEntity findManager(String name, Pageable pageable) {
        Page<ManagerResponseDto> result = memberService.findManager(name, pageable);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(result).build());
    }

    @PostMapping("/manager/add")
    public ResponseEntity addManager(@Valid @RequestBody AddManagerRequestDto manager) {
        memberService.addManager(manager);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data("success").build());
    }

    @DeleteMapping("/manager/delete/{id}")
    public ResponseEntity deleteManager(@PathVariable("id") Long id) {
        memberService.deleteManager(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data("success").build());
    }

    @PatchMapping("/user/stop/{id}")
    public ResponseEntity stopUser(@PathVariable("id") Long id) {
        memberService.changeStopDeadline(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data("success").build());
    }

    @GetMapping("/manager/duplicate")
    public ResponseEntity managerDuplicated(String username) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.duplicated(username, Authority.MANAGER))
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
                .body(NormalResponse.builder().status(200).data("success").build());
    }

    @PostMapping("/change/password")
    public ResponseEntity changePassword(@Valid @RequestBody ChangePasswordRequestDto request) {
        memberService.changePassword(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data("success").build());
    }

}
