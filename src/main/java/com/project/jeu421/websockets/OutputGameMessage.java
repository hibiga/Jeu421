package com.project.jeu421.websockets;

public class OutputGameMessage {
    private String host;
    private String playersInRoom;
    private String type;
    private String gameName;
    private String time;
    private String playerToRoll;

    public OutputGameMessage(final String host, final String playersInRoom, final String type, final String time, final String gameName, final String playerToRoll) {

        this.host = host;
        this.playersInRoom = playersInRoom;
        this.type = type;
        this.time = time;
        this.gameName = gameName;
        this.playerToRoll = playerToRoll;
    }

    public OutputGameMessage() {

    }

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

    public String getTime() {
        return time;
    }

    public String getPlayerToRoll() {
        return playerToRoll;
    }
}
