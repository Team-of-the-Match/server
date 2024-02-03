package com.totm.totm.controller;

import com.totm.totm.dto.MemberDto;
import com.totm.totm.dto.NormalResponse;
import com.totm.totm.entity.Authority;
import com.totm.totm.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@ResponseBody
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/user/find")
    public ResponseEntity findUser(@RequestParam("username") String username, Pageable pageable) {
        Page<MemberDto.UserResponseDto> result = memberService.findUser(username, pageable);
        return ResponseEntity
                .status(200)
                .body(NormalResponse.builder().status(200).data(result).build());
    }

    @GetMapping("/manager/find")
    public ResponseEntity findManager(@RequestParam("name") String name, Pageable pageable) {
        Page<MemberDto.ManagerResponseDto> result = memberService.findManager(name, pageable);
        return ResponseEntity
                .status(200)
                .body(NormalResponse.builder().status(200).data(result).build());
    }

    @PostMapping("/manager/add")
    public ResponseEntity addManager(@Valid @RequestBody MemberDto.AddManagerRequestDto manager) {
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
        memberService.changeMemberStatusToStopOrNormal(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/manager/duplicate")
    public ResponseEntity managerDuplicated(@RequestParam("username") String username) {
        System.out.println(username);
        return ResponseEntity
                .status(200)
                .body(NormalResponse.builder()
                        .status(200)
                        .data(memberService.duplicated(username, Authority.MANAGER))
                        .build());
    }

}
