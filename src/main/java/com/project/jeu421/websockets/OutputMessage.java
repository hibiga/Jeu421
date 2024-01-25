package com.project.jeu421.websockets;

public class OutputMessage {
    private String host;
    private String invitedPlayer;
    private String type;
    private String time;
    private String value;


    public OutputMessage(final String host, final String invitedPlayer, final String type, final String time) {

        this.host = host;
        this.invitedPlayer = invitedPlayer;
        this.type = type;
        this.time = time;
    }


    public OutputMessage(final String host, final String invitedPlayer, final String type, final String time, final String value) {

        this.host = host;
        this.invitedPlayer = invitedPlayer;
        this.type = type;
        this.time = time;
        this.value = value;
    }


    public OutputMessage() {

    }

    public String getHost() {
        return host;
    }

    public String getInvitedPlayer() {
        return invitedPlayer;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
