package com.totm.totm.dto;

import com.totm.totm.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class MemberDto {

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
    public static class AddUserRequestDto {

        @NotBlank(message = "아이디는 빈값일 수 없습니다.")
        @Length(min = 5, max = 20, message = "아이디는 5자 이상, 20자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[a-z])[a-z0-9]+$", message = "아이디는 하나 이상의 소문자와 숫자로 이루어져야합니다.")
        private String username;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String password;

        @NotBlank(message = "닉네임은 빈값일 수 없습니다.")
        private String nickname;

        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        private String name;

        @NotBlank(message = "전화번호는 빈값일 수 없습니다.")
        private String phoneNumber;
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
    }

    @Data
    public static class ChangePasswordRequestDto {
        @NotNull(message = "멤버 아이디가 필요합니다.")
        private Long id;

        @NotBlank(message = "기존 비밀번호는 빈값일 수 없습니다.")
        private String oldPassword;

        @NotBlank(message = "새 비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String newPassword;
    }

    @Data
    public static class FindUsernameRequestDto {
        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        private String name;

        @NotBlank(message = "전화번호는 빈값일 수 없습니다.")
        private String phoneNumber;
    }

    @Data
    public static class ResetNewPasswordRequestDto {
        @NotBlank(message = "아이디는 빈값일 수 없습니다.")
        @Length(min = 5, max = 20, message = "아이디는 5자 이상, 20자 이하여야 합니다.")
        private String username;

        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        private String name;

        @NotBlank(message = "전화번호는 빈값일 수 없습니다.")
        private String phoneNumber;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String password;
    }

    @Data
    public static class ChangePhoneNumberRequestDto {

        @NotBlank(message = "전화번호는 빈값일 수 없습니다.")
        private String phoneNumber;
    }

    @Data
    public static class UserResponseDto {
        private Long id;
        private String username;
        private String nickname;
        private String name;
        private String phoneNumber;
        private LocalDateTime stopDeadline;
        private LocalDateTime createdDate;

        public UserResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.nickname = member.getNickname();
            this.name = member.getName();
            this.phoneNumber = member.getPhoneNumber();
            this.stopDeadline = member.getStopDeadline();
            this.createdDate = member.getCreatedDate();
        }
    }

    @Data
    public static class ManagerResponseDto {
        private Long id;
        private String username;
        private String name;
        private String phoneNumber;
        private LocalDateTime createdDate;

        public ManagerResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.name = member.getName();
            this.phoneNumber = member.getPhoneNumber();
            this.createdDate = member.getCreatedDate();
        }
    }
}
