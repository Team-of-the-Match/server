package com.totm.totm.dto;

import com.totm.totm.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class MemberDto {

    @Data
    public static class LoginRequestDto {
        @NotBlank(message = "이메일은 빈값일 수 없습니다.")
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        private String password;
    }

    @Data
    public static class SignUpRequestDto {

        @NotBlank(message = "이메일 빈값일 수 없습니다.")
        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", message = "올바른 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String password;

        @NotBlank(message = "닉네임은 빈값일 수 없습니다.")
        private String nickname;
    }

    @Data
    public static class ResetPasswordRequestDto {
        @NotBlank(message = "비밀번호는 빈값일 수 없습니다.")
        @Length(min = 8, max = 30, message = "비밀번호는 최소 8자 이상, 30자 이하여야 합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "비밀번호는 최소 하나의 문자, 숫자, 특수 문자로 이루어져야합니다.")
        private String password;
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
    public static class FindMembersResponseDto {
        private Long id;
        private String email;
        private String nickname;
        private LocalDateTime stopDeadline;
        private LocalDateTime createdDate;

        public FindMembersResponseDto(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.nickname = member.getNickname();
            this.stopDeadline = member.getStopDeadline();
            this.createdDate = member.getCreatedDate();
        }
    }
}
