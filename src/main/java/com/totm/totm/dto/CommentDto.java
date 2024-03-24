package com.totm.totm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.totm.totm.entity.Comment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentDto {

    @Data
    public static class AddCommentRequestDto {
        @Min(value = 1, message = "멤버 아이디는 1보다 커야합니다.")
        private Long memberId;

        @Min(value = 1, message = "게시글 아이디는 1보다 커야합니다.")
        private Long postId;

        @NotBlank(message = "댓글은 빈칸일 수 없습니다.")
        private String comment;
    }

    @Data
    public static class DeleteCommentRequestDto {
        @Min(value = 1, message = "댓글 아이디는 1보다 커야 합니다.")
        private Long commentId;

        @Min(value = 1, message = "게시글 아이디는 1보다 커야 합니다.")
        private Long postId;
    }

    @Data
    public static class CommentResponseDto {
        private Long commentId;
        private Long memberId;
        private String nickname;
        private String comment;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public CommentResponseDto(Comment comment) {
            this.commentId = comment.getId();
            if(comment.getMember() == null) {
                this.memberId = null;
                this.nickname = null;
            } else {
                this.memberId = comment.getMember().getId();
                this.nickname = comment.getMember().getNickname();
            }
            this.comment = comment.getComment();
            this.createdDate = comment.getCreatedDate();
        }
    }

    @Data
    public static class ReportedCommentResponseDto {
        private Long commentId;
        private Long memberId;
        private String nickname;
        private String comment;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime lastModifiedDate;

        public ReportedCommentResponseDto(Comment comment) {
            this.commentId = comment.getId();
            if(comment.getMember() == null) {
                this.memberId = null;
                this.nickname = null;
            } else {
                this.memberId = comment.getMember().getId();
                this.nickname = comment.getMember().getNickname();
            }
            this.comment = comment.getComment();
            this.lastModifiedDate = comment.getLastModifiedDate();
        }
    }
}
