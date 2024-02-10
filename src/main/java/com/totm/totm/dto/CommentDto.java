package com.totm.totm.dto;

import com.totm.totm.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentDto {

    @Data
    public static class ReportedCommentResponseDto {
        private Long commentId;
        private Long memberId;
        private String comment;
        private LocalDateTime lastModifiedDate;
        private String nickname;

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
