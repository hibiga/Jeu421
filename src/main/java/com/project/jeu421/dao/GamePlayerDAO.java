package com.project.jeu421.dao;

import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayerDAO extends JpaRepository<GamePlayerEntity, Long> {
    GamePlayerEntity findByGameAndPlayer(GameEntity game, PlayerEntity player);

    List<GamePlayerEntity> findByPlayer(PlayerEntity player);

    List<GamePlayerEntity> findByGame(GameEntity game);
}
