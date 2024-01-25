package com.project.jeu421.websockets;

public class OutputFinishTurnRerollPhaseChargeMessage {
    private String host;
    private String playerName;
    private int playerNameIndex;
    private String playerNameNext;
    private int playerNameNextIndex;
    private int turn;
    private int score;
    private String scoreRerollList;
    private String playerTokenList;
    private int phase;
    private int nbRound;
    private int pot;
    private int isReroll;
    private int tokenToTake;
    private int winnerIndex;
    private String notSkipRerollPlayerIndex;
    private String type;

    public OutputFinishTurnRerollPhaseChargeMessage(String host, String playerName, int playerNameIndex, String playerNameNext, int playerNameNextIndex, int turn, int score, String scoreRerollList, String playerTokenList, int phase, int nbRound, int pot, int isReroll, int tokenToTake, int winnerIndex, String notSkipRerollPlayerIndex, String type) {
        this.host = host;
        this.playerName = playerName;
        this.playerNameIndex = playerNameIndex;
        this.playerNameNext = playerNameNext;
        this.playerNameNextIndex = playerNameNextIndex;
        this.turn = turn;
        this.score = score;
        this.scoreRerollList = scoreRerollList;
        this.playerTokenList = playerTokenList;
        this.phase = phase;
        this.nbRound = nbRound;
        this.pot = pot;
        this.isReroll = isReroll;
        this.tokenToTake = tokenToTake;
        this.winnerIndex = winnerIndex;
        this.notSkipRerollPlayerIndex = notSkipRerollPlayerIndex;
        this.type = type;
    }

    public OutputFinishTurnRerollPhaseChargeMessage() {

    }

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

    public int getPlayerNameNextIndex() {
        return playerNameNextIndex;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }

    public String getNotSkipRerollPlayerIndex() {
        return notSkipRerollPlayerIndex;
    }
}
