package com.project.jeu421.dao;

import com.project.jeu421.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerDAO")
public interface PlayerDAO extends JpaRepository<PlayerEntity, Long> {
    PlayerEntity findByUsername(String username);
}
