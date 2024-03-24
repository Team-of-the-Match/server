package com.totm.totm.entity;

import lombok.Getter;

@Getter
public class Match {
    private String id;

    private Long match;

    private int home;

    private int draw;

    private int away;

    public Match(Long match, int home, int draw, int away) {
        this.match = match;
        this.home = home;
        this.draw = draw;
        this.away = away;
    }
}
