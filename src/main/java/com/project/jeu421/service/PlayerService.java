package com.project.jeu421.service;

import com.project.jeu421.entity.PlayerEntity;

import java.util.List;

public interface PlayerService {
    void createPlayer(PlayerEntity player);

    PlayerEntity getPlayerByUsername(String username);

    List<PlayerEntity> getAllPlayers();

    void updatePlayerInfo(PlayerEntity player);

    void updatePlayerStat(String username);

}
