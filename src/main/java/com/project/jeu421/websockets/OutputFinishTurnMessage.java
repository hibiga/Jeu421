package com.project.jeu421.websockets;

public class OutputFinishTurnMessage {
    private String host;
    private String playerName;
    private String playerNameNext;
    private int playerNameIndex;
    private int playerNameNextIndex;
    private int numberOfRoll;
    private int turn;
    private int phase;
    private int nbRound;
    private int pot;
    private String type;
    private String scoreList;
    private String playerTokenList;
    private String playersReroll;
    private int isReroll;
    private int tokenToTake;
    private int previousNumberOfRoll;
    private int startPhaseDecharge;
    private int turnCounter;
    private int winnerIndex;

    public OutputFinishTurnMessage(String host, String playerName, int playerNameIndex, String playerNameNext, int playerNameNextIndex, String scoreList, String playerTokenList, int numberOfRoll, int turn, int phase, int nbRound, int pot, String playersReroll, int isReroll, int tokenToTake, int previousNumberOfRoll, int startPhaseDecharge, int turnCounter, int winnerIndex, String type) {
        this.host = host;
        this.playerName = playerName;
        this.playerNameIndex = playerNameIndex;
        this.playerNameNext = playerNameNext;
        this.playerNameNextIndex = playerNameNextIndex;
        this.scoreList = scoreList;
        this.playerTokenList = playerTokenList;
        this.numberOfRoll = numberOfRoll;
        this.turn = turn;
        this.phase = phase;
        this.nbRound = nbRound;
        this.pot = pot;
        this.playersReroll = playersReroll;
        this.isReroll = isReroll;
        this.tokenToTake = tokenToTake;
        this.previousNumberOfRoll = previousNumberOfRoll;
        this.startPhaseDecharge = startPhaseDecharge;
        this.turnCounter = turnCounter;
        this.winnerIndex = winnerIndex;
        this.type = type;
    }

    public OutputFinishTurnMessage() {
    }

    public String getHost() {
        return host;
    }

    public String getPlayerNameNext() {
        return playerNameNext;
    }

    public int getTurn() {
        return turn;
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

    public String getType() {
        return type;
    }

    public String getScoreList() {
        return scoreList;
    }

    public int getNumberOfRoll() {
        return numberOfRoll;
    }

    public int getPlayerNameNextIndex() {
        return playerNameNextIndex;
    }

    public String getPlayerTokenList() {
        return playerTokenList;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerNameIndex() {
        return playerNameIndex;
    }

    public String getPlayersReroll() {
        return playersReroll;
    }

    public int getIsReroll() {
        return isReroll;
    }

    public int getTokenToTake() {
        return tokenToTake;
    }

    public int getPreviousNumberOfRoll() {
        return previousNumberOfRoll;
    }

    public int getStartPhaseDecharge() {
        return startPhaseDecharge;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public int getWinnerIndex() {
        return winnerIndex;
    }
}
