package com.project.jeu421.service;

import com.project.jeu421.dao.GamePlayerDAO;
import com.project.jeu421.dao.PlayerDAO;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    PlayerDAO playerDAO;

    @Autowired
    GamePlayerDAO gamePlayerDAO;


    @Override
    public void createPlayer(PlayerEntity player) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(encodedPassword);
        playerDAO.save(player);
    }

    @Override
    public PlayerEntity getPlayerByUsername(String username) {
        return playerDAO.findByUsername(username);
    }

    @Override
    public List<PlayerEntity> getAllPlayers() {
        return playerDAO.findAll();
    }

    @Override
    public void updatePlayerInfo(PlayerEntity player) {
        String username = player.getUsername();
        String password = player.getPassword();
        int age = player.getAge();
        String sexe = player.getSexe();
        String ville = player.getVille();

        PlayerEntity playerToUpdate = playerDAO.findByUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        playerToUpdate.setPassword(encodedPassword);
        playerToUpdate.setAge(age);
        playerToUpdate.setSexe(sexe);
        playerToUpdate.setVille(ville);
        playerDAO.save(playerToUpdate);
    }

    @Override
    public void updatePlayerStat(String username) {
        PlayerEntity playerToUpdate = playerDAO.findByUsername(username);
        Long playerId = playerToUpdate.getId();
        List<GamePlayerEntity> gamePlayerEntity = gamePlayerDAO.findByPlayer(playerToUpdate);
        float nbGame = gamePlayerEntity.size();
        float nbWin = 0;
        float totalNbJetCharge = 0;
        float totalNbJetDecharge = 0;
        for (GamePlayerEntity playerEntity : gamePlayerEntity) {
            int status = playerEntity.getStatus();
            float nbJetCharge = playerEntity.getNbJetCharge();
            float nbJetDecharge = playerEntity.getNbJetDecharge();
            if (status == 1) {
                nbWin += 1;
            }
            totalNbJetCharge += nbJetCharge;
            totalNbJetDecharge += nbJetDecharge;
        }
        float meanWin = nbWin / nbGame;
        float meanJetCharge = totalNbJetCharge / nbGame;
        float meanJetDecharge = totalNbJetDecharge / nbGame;
        playerToUpdate.setNbGame(nbGame);
        playerToUpdate.setNbWin(nbWin);
        playerToUpdate.setMeanWin(meanWin);
        playerToUpdate.setMeanJetCharge(meanJetCharge);
        playerToUpdate.setMeanJetDecharge(meanJetDecharge);
        playerDAO.save(playerToUpdate);
    }
}
