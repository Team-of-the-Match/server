package com.totm.totm.controller;

import com.totm.totm.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        return ResponseEntity.ok(managerService.login(request));
    }

    @PatchMapping("/refresh")
    public ResponseEntity refresh(String username, HttpServletRequest request) {
        return ResponseEntity.ok(managerService.refresh(username, request));
    }

    @GetMapping("/managers")
    public ResponseEntity findManagers(String name, Pageable pageable) {
        return ResponseEntity.ok(managerService.findManagers(name, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity addManager(@Valid @RequestBody AddManagerRequestDto request) {
        managerService.addManager(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        managerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicated-username")
    public ResponseEntity duplicatedUsername(String username) {
        return ResponseEntity.ok(managerService.duplicatedUsername(username));
    }

    @PatchMapping("/reset-password/{id}")
    public ResponseEntity resetPassword(@PathVariable("id") Long id) {
        managerService.resetPassword(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/change-password/{id}")
    public ResponseEntity changePassword(@PathVariable("id") Long id, @Valid @RequestBody ChangePasswordRequestDto request) {
        managerService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }
}
