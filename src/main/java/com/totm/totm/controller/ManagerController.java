package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.ManagerDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(managerService.login(request)).build());
    }

    @PatchMapping("/refresh")
    public ResponseEntity refresh(String username, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(managerService.refresh(username, request)).build());
    }

    @GetMapping("/managers")
    public ResponseEntity findManagers(String name, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(managerService.findManagers(name, pageable)).build());
    }

    @PostMapping("/add")
    public ResponseEntity addManager(@Valid @RequestBody AddManagerRequestDto request) {
        managerService.addManager(request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        managerService.delete(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/duplicated-username")
    public ResponseEntity duplicatedUsername(String username) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(managerService.duplicatedUsername(username))
                        .build());
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity resetPassword(@PathVariable("id") Long id) {
        managerService.resetPassword(id);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity changePassword(@PathVariable("id") Long id, @Valid @RequestBody ChangePasswordRequestDto request) {
        managerService.changePassword(id, request);
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
