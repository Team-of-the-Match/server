package com.totm.totm.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document
@Getter
public class AbroadFootballPrediction {

    @Id
    private String id;

    private Long memberId;

    private List<Prediction> predictions;

    private List<Integer> scores;

    @DocumentReference
    private AbroadFootballGame abroadFootballGame;

    public AbroadFootballPrediction(Long memberId, List<Prediction> predictions, AbroadFootballGame abroadFootballGame) {
        this.memberId = memberId;
        this.predictions = predictions;
        this.abroadFootballGame = abroadFootballGame;
        abroadFootballGame.getAbroadFootballPredictions().add(this);
    }
}
