package com.project.jeu421.service;

import com.project.jeu421.dao.GameDAO;
import com.project.jeu421.dao.PlayerDAO;
import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gameService")

public class GameServiceImpl implements GameService {
    @Autowired
    GameDAO gameDAO;

    @Override
    public void createGame(GameEntity game) {
        gameDAO.save(game);
    }

    @Override
    public void updateGame(String nomGame, int num, int phase) {
        GameEntity gameToUpdate = gameDAO.findByNomGame(nomGame);
        if (phase == 0) {
            gameToUpdate.setNumCharge(num);
        } else {
            gameToUpdate.setNumDecharge(num);
        }
        gameDAO.save(gameToUpdate);
    }

    @Override
    public GameEntity getGameByGameName(String gamename) {
        return gameDAO.findByNomGame(gamename);
    }
}
