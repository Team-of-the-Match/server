package com.totm.totm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.totm.totm.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class NotificationDto {

    @Data
    public static class AddNotificationRequestDto {

        @NotBlank(message = "제목을 입력해야합니다.")
        @Length(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용을 입력해야합니다.")
        private String content;
    }
    @Data
    public static class ModifyNotificationRequestDto {

        @NotBlank(message = "제목을 입력해야합니다.")
        @Length(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용을 입력해야합니다.")
        private String content;
    }

    @Data
    public static class NotificationsResponseDto {

        private Long id;
        private String title;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public NotificationsResponseDto(Notification notification) {
            this.id = notification.getId();
            this.title = notification.getTitle();
            this.createdDate = notification.getCreatedDate();
        }
    }

    @Data
    public static class NotificationResponseDto {

        private Long id;
        private String title;
        private String content;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public NotificationResponseDto(Notification notification) {
            this.id = notification.getId();
            this.title = notification.getTitle();
            this.content = notification.getContent();
            this.createdDate = notification.getCreatedDate();
        }
    }
}
