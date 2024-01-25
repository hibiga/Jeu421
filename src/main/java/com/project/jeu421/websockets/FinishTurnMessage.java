package com.project.jeu421.websockets;

public class FinishTurnMessage {
    private String host;
    private String playerName;
    private int playerNameIndex;
    private String playerNameNext;
    private int numberOfRoll;
    private int turn;
    private int dice1;
    private int dice2;
    private int dice3;
    private int score;
    private String scoreList;
    private String playerTokenList;
    private int phase;
    private int nbRound;
    private int pot;
    private String type;
    private int highestScore;
    private int lowestScore;
    private String lowestScoreIndexes;
    private String highestScoreIndexes;
    private int isReroll;
    private int previousNumberOfRoll;
    private int startPhaseDecharge;
    private String gameName;
    private String skipRerollPlayerIndex;
    private int turnCounter;

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

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public int getDice3() {
        return dice3;
    }

    public int getScore() {
        return score;
    }

    public String getScoreList() {
        return scoreList;
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

    public int getNumberOfRoll() {
        return numberOfRoll;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }

    public String getLowestScoreIndexes() {
        return lowestScoreIndexes;
    }

    public String getHighestScoreIndexes() {
        return highestScoreIndexes;
    }

    public String getPlayerTokenList() {
        return playerTokenList;
    }

    public int getIsReroll() {
        return isReroll;
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

    public String getGameName() {
        return gameName;
    }

    public String getSkipRerollPlayerIndex() {
        return skipRerollPlayerIndex;
    }
}
