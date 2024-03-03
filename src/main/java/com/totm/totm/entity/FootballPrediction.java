package com.totm.totm.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document
@Getter
public class FootballPrediction {

    @Id
    private String id;

    private Long memberId;

    private List<Prediction> predictions;

    private List<Integer> scores;

    @DocumentReference
    private FootballGame footballGame;

    public FootballPrediction(Long memberId, List<Prediction> predictions, FootballGame footballGame) {
        this.memberId = memberId;
        this.predictions = predictions;
        this.footballGame = footballGame;
        footballGame.getFootballPredictions().add(this);
    }
}
