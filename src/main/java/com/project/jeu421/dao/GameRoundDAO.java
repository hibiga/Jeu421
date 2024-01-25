package com.project.jeu421.dao;

import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GameRoundEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoundDAO extends JpaRepository<GameRoundEntity, Long> {

    GameRoundEntity findByGameAndPlayer(GameEntity game, PlayerEntity player);
}
