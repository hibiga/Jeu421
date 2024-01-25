package com.project.jeu421.service;

import com.project.jeu421.dao.GameRoundDAO;
import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.GameRoundEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameRoundService")

public class GameRoundServiceImpl implements GameRoundService {
    @Autowired
    GameRoundDAO gameRoundDAO;

    @Override
    public void createGameRound(GameRoundEntity gameRound) {
        gameRoundDAO.save(gameRound);
    }

    @Override
    public void updateGamePlayer(GameEntity game, PlayerEntity player, int dice1, int dice2, int dice3) {
        GameRoundEntity gameRoundToUpdate = gameRoundDAO.findByGameAndPlayer(game, player);
        gameRoundToUpdate.setDiceValue1(dice1);
        gameRoundToUpdate.setDiceValue2(dice2);
        gameRoundToUpdate.setDiceValue3(dice3);

        gameRoundDAO.save(gameRoundToUpdate);
    }
}
