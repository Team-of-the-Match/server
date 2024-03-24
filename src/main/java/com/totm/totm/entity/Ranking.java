package com.totm.totm.entity;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
public class Ranking {

    private String email;

    private String nickname;

    public Ranking(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
