package com.totm.totm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.totm.totm.entity.Manager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class ManagerDto {

    @Data
    public static class LoginRequestDto {
        @NotBlank(message = "아이디는 빈값일 수 없습니다.")
        @Length(min = 5, max = 20, message = "아이디는 5자 이상, 20자 이하여야 합니다.")
        private String username;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 5, max = 20, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        private String password;
    }

    @Data
    public static class AddManagerRequestDto {

        @NotBlank(message = "아이디는 빈값일 수 없습니다.")
        @Length(min = 5, max = 20, message = "아이디는 5자 이상, 20자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])[a-z0-9]+$", message = "아이디는 하나 이상의 소문자와 숫자로 이루어져야합니다.")
        private String username;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String password;

        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        private String name;

        @NotBlank(message = "전화번호는 빈값일 수 없습니다.")
        private String phoneNumber;

        public AddManagerRequestDto(String username, String password, String name, String phoneNumber) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }

    @Data
    public static class ChangePasswordRequestDto {
        @NotBlank(message = "기존 비밀번호는 빈값일 수 없습니다.")
        private String oldPassword;

        @NotBlank(message = "새 비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String newPassword;
    }

    @Data
    public static class FindManagersResponseDto {
        private Long id;
        private String username;
        private String name;
        private String phoneNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public FindManagersResponseDto(Manager manager) {
            this.id = manager.getId();
            this.username = manager.getUsername();
            this.name = manager.getName();
            this.phoneNumber = manager.getPhoneNumber();
            this.createdDate = manager.getCreatedDate();
        }
    }
}
