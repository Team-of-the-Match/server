package com.totm.totm.controller;

import com.totm.totm.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.NotificationDto.AddNotificationRequestDto;
import static com.totm.totm.dto.NotificationDto.ModifyNotificationRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/post")
    public ResponseEntity post(@Valid @RequestBody AddNotificationRequestDto request) {
        notificationService.post(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    public ResponseEntity findNotifications(@RequestParam(required = false) Long lastId) {
        return ResponseEntity.ok(notificationService.findNotifications(lastId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotification(id));
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity modifyNotification(@PathVariable Long id, @Valid @RequestBody ModifyNotificationRequestDto request) {
        notificationService.modifyNotification(id, request);
        return ResponseEntity.ok().build();
    }
}
