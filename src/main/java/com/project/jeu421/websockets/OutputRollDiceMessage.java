package com.project.jeu421.websockets;

public class OutputRollDiceMessage {
    private String playerName;
    private int dice1;
    private int dice2;
    private int dice3;
    private String type;

    public OutputRollDiceMessage(final String playerName, final int dice1, final int dice2, final int dice3, final String type) {

        this.playerName = playerName;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.dice3 = dice3;
        this.type = type;
    }

    public OutputRollDiceMessage() {

    }

    public String getPlayerName() {
        return playerName;
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

    public String getType() {
        return type;
    }
}
