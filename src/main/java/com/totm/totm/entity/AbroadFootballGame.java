package com.totm.totm.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
public class AbroadFootballGame {

    @Id
    private String id;

    private String gameDate;

    private List<Match> matches;

    private boolean closed;

    private boolean scoreUpdated;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @DocumentReference(lazy = true)
    private List<AbroadFootballPrediction> abroadFootballPredictions = new ArrayList<>();

    public AbroadFootballGame(String gameDate, List<Match> matches, boolean closed, boolean scoreUpdated) {
        this.gameDate = gameDate;
        this.matches = matches;
        this.closed = closed;
        this.scoreUpdated = scoreUpdated;
    }
}
