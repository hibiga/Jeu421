package com.project.jeu421.service;

import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.PlayerEntity;

public interface GamePlayerService {

    void createGamePlayer(GamePlayerEntity gamePlayer);

    void updateGamePlayer(GameEntity game, PlayerEntity player, int nb_jet, int phase, int status);

}
