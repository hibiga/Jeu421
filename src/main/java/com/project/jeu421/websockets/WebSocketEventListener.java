package com.project.jeu421.websockets;

import com.project.jeu421.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.project.jeu421.websockets.WebsocketController.rooms;


@Component
public class WebSocketEventListener {
    public static ArrayList<String> onlinePlayersUsername = new ArrayList<String>();


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String current_player_name = Objects.requireNonNull(event.getUser()).getName();
        if (!onlinePlayersUsername.contains(current_player_name)) {
            onlinePlayersUsername.add(current_player_name);
            String json = "{\"username\":\"" + current_player_name + "\", \"type\":\"online\"}";
            messagingTemplate.convertAndSend("/topic/messages", json);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String current_player_name = Objects.requireNonNull(event.getUser()).getName();
        if (onlinePlayersUsername.contains(current_player_name)) {
            onlinePlayersUsername.remove(current_player_name);

//            if (rooms.containsKey(current_player_name)) {
//                rooms.remove(current_player_name);
//                for (Room room : rooms.values()) {
////                    System.out.println(room.getKey() + "/" + room.getValue());
//                    int a = 0;
//                }
//
//            }
            String json = "{\"username\":\"" + current_player_name + "\", \"type\":\"offline\"}";
            messagingTemplate.convertAndSend("/topic/messages", json);
        }
    }
}