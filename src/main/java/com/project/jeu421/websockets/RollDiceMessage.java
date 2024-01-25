package com.project.jeu421.websockets;

public class RollDiceMessage {
    private String playerName;
    private int dice1;
    private int dice2;
    private int dice3;
    private String type;

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


