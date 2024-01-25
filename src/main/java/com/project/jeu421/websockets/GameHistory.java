package com.project.jeu421.websockets;

import java.util.List;
import java.util.Objects;

public class GameHistory {
    private String gameName;
    private String playerName;
    private float nbJetCharge;
    private float nbJetDecharge;

    public GameHistory(String gameName, String playerName, float nbJetCharge, float nbJetDecharge) {

        this.gameName = gameName;
        this.playerName = playerName;
        this.nbJetCharge = nbJetCharge;
        this.nbJetDecharge = nbJetDecharge;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public float getNbJetCharge() {
        return nbJetCharge;
    }

    public void setNbJetCharge(float nbJetCharge) {
        this.nbJetCharge = nbJetCharge;
    }

    public float getNbJetDecharge() {
        return nbJetDecharge;
    }

    public void setNbJetDecharge(float nbJetDecharge) {
        this.nbJetDecharge = nbJetDecharge;
    }
}
