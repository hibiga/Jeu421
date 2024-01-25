package com.project.jeu421.websockets;

public class OutputJoinMessage {
    private final String playerName;
    private final String roomName;
    private final String type;
    private final String time;


    public OutputJoinMessage(final String playerName, final String roomName, final String type, final String time) {

        this.playerName = playerName;
        this.roomName = roomName;
        this.type = type;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }
}
