package com.project.jeu421.websockets;

import com.project.jeu421.entity.PlayerEntity;

import java.util.List;
import java.util.Objects;

public class Room {
    private List<String> listPlayers;
    private String hostUsername;
    private String gameName;


    public Room(final String host, final List<String> invitedPlayer) {

        this.hostUsername = host;
        this.listPlayers = invitedPlayer;
    }


    public List<String> getListPlayers() {
        return listPlayers;
    }

    public void setListPlayers(List<String> listPlayers) {
        this.listPlayers = listPlayers;
    }

    public void updateListPlayer(String player, String update_type) {
        if (Objects.equals(update_type, "add")) {
            this.listPlayers.add(player);
        } else {
            this.listPlayers.remove(player);
        }
    }


    public String getHostUsername() {
        return hostUsername;
    }

    public void setHostUsername(String hostUsername) {
        this.hostUsername = hostUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
