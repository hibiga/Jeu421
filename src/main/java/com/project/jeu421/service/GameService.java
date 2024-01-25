package com.project.jeu421.service;

import com.project.jeu421.entity.GameEntity;

public interface GameService {
    void createGame(GameEntity game);

    void updateGame(String nomGame, int num, int phase);

    GameEntity getGameByGameName(String gamename);

}
