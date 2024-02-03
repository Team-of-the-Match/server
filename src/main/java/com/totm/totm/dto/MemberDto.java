package com.totm.totm.dto;

import com.totm.totm.entity.Authority;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class MemberDto {

//    @Data
//    public static class Response {
//
//        private String username;
//        private String nickname;
//        private String name;
//        private String phoneNumber;
//        private Authority authority;
//
//        public Response(Member member) {
//            this.username = member.getUsername();
//            this.nickname = member.getNickname();
//            this.name = member.getName();
//            this.phoneNumber = member.getPhoneNumber();
//            this.authority = member.getAuthority();
//        }
//    }
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
    public static class UserResponseDto {
        private Long id;
        private String username;
        private String nickname;
        private String name;
        private String phoneNumber;
        private MemberStatus memberStatus;
        private LocalDateTime createdDate;

        public UserResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.nickname = member.getNickname();
            this.name = member.getName();
            this.phoneNumber = member.getPhoneNumber();
            this.memberStatus = member.getMemberStatus();
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
