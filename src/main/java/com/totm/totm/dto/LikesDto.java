package com.totm.totm.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

public class LikesDto {

    @Data
    public static class LikeRequestDto {
        @Min(value = 1, message = "멤버 아이디는 1보다 커야 합니다.")
        private Long memberId;

        @Min(value = 1, message = "게시글 아이디는 1보다 커야 합니다.")
        private Long postId;
    }
}
