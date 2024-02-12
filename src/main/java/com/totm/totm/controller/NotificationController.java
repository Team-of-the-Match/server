package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.NotificationDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/add")
    public ResponseEntity addNotification(@Valid @RequestBody AddNotificationRequestDto request) {
        notificationService.addNotification(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/find")
    public ResponseEntity findNotifications(Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(notificationService.findNotifications(pageable)).build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getNotification(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(notificationService.getNotification(id)).build());
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity modifyNotification(@PathVariable Long id, @Valid @RequestBody ModifyNotificationRequestDto request) {
        notificationService.modifyNotification(id, request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
