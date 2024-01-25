package com.project.jeu421.service;

import com.project.jeu421.dao.GameDAO;
import com.project.jeu421.dao.GamePlayerDAO;
import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gamePlayerService")

public class GamePlayerServiceImpl implements GamePlayerService {

    @Autowired
    GamePlayerDAO gamePlayerDAO;

    @Override
    public void createGamePlayer(GamePlayerEntity gamePlayer) {
        gamePlayerDAO.save(gamePlayer);
    }

    @Override
    public void updateGamePlayer(GameEntity game, PlayerEntity player, int nbJet, int phase, int status) {
        GamePlayerEntity gamePlayerToUpdate = gamePlayerDAO.findByGameAndPlayer(game, player);
        if (phase == 0) {
            gamePlayerToUpdate.setNbJetCharge(nbJet);
        } else if (phase == 1) {
            gamePlayerToUpdate.setNbJetDecharge(nbJet);
            gamePlayerToUpdate.setStatus(status);
        } else {
            gamePlayerToUpdate.setStatus(status);
        }

        gamePlayerDAO.save(gamePlayerToUpdate);
    }
}
