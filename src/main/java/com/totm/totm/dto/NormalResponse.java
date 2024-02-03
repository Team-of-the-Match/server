package com.totm.totm.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class NormalResponse<T> {
    private final int status;
    private final T data;

    @Builder
    public NormalResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
