package com.project.jeu421.dao;

import com.project.jeu421.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameDAO")
public interface GameDAO extends JpaRepository<GameEntity, Long> {

    GameEntity findByNomGame(String nomGame);

}
