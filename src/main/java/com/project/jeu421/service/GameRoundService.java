package com.project.jeu421.service;

import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GameRoundEntity;
import com.project.jeu421.entity.PlayerEntity;

public interface GameRoundService {

    void createGameRound(GameRoundEntity gameRound);

    void updateGamePlayer(GameEntity game, PlayerEntity player, int dice1, int dice2, int dice3);

}
