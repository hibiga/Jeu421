package com.project.jeu421.websockets;

public class FinishTurnRerollPhaseChargeMessage {
    private String host;
    private String playerName;
    private int playerNameIndex;
    private String playerNameNext;
    private int turn;
    private int score;
    private String scoreRerollList;
    private String playerTokenList;
    private String rerollLowestScoreIndexes;
    private String rerollHighestScoreIndexes;
    private int phase;
    private int nbRound;
    private int pot;
    private int isReroll;
    private int tokenToTake;
    private String playersReroll;
    private String gameName;
    private String notSkipRerollPlayerIndex;
    private String type;

    public String getHost() {
        return host;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerNameIndex() {
        return playerNameIndex;
    }

    public String getPlayerNameNext() {
        return playerNameNext;
    }

    public int getTurn() {
        return turn;
    }

    public int getScore() {
        return score;
    }

    public String getScoreRerollList() {
        return scoreRerollList;
    }

    public String getPlayerTokenList() {
        return playerTokenList;
    }

    public int getPhase() {
        return phase;
    }

    public int getNbRound() {
        return nbRound;
    }

    public int getPot() {
        return pot;
    }

    public int getIsReroll() {
        return isReroll;
    }

    public String getType() {
        return type;
    }

    public int getTokenToTake() {
        return tokenToTake;
    }

    public String getPlayersReroll() {
        return playersReroll;
    }

    public String getRerollLowestScoreIndexes() {
        return rerollLowestScoreIndexes;
    }

    public String getRerollHighestScoreIndexes() {
        return rerollHighestScoreIndexes;
    }

    public String getGameName() {
        return gameName;
    }

    public String getNotSkipRerollPlayerIndex() {
        return notSkipRerollPlayerIndex;
    }
}
