package com.totm.totm.dto;

import com.totm.totm.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class PostDto {

    @Data
    public static class AddPostRequestDto {

        @NotNull(message = "멤버 아이디가 필요합니다.")
        private Long memberId;

        @NotBlank(message = "제목을 입력해야합니다.")
        @Length(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용을 입력해야합니다.")
        private String content;
    }

    @Data
    public static class ReportedPostResponseDto {

        private Long postId;
        private Long memberId;
        private String title;
        private String content;
        private LocalDateTime lastModifiedDate;
        private String nickname;

        public ReportedPostResponseDto(Post post) {
            this.postId = post.getId();
            if(post.getMember() == null) {
                this.memberId = null;
                this.nickname = null;
            } else {
                this.memberId = post.getMember().getId();
                this.nickname = post.getMember().getNickname();
            }
            this.title = post.getTitle();
            this.content = post.getContent();
            this.lastModifiedDate = post.getLastModifiedDate();
        }
    }
}
