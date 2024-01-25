package com.project.jeu421.websockets;

public class GameMessage {
    private String host;
    private String playersInRoom;
    private String type;
    private String gameName;
    private String playerToRoll;

    public String getHost() {
        return host;
    }

    public String getPlayersInRoom() {
        return playersInRoom;
    }

    public String getType() {
        return type;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerToRoll() {
        return playerToRoll;
    }
}
