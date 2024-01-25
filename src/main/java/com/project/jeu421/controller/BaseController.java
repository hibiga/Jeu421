package com.project.jeu421.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jeu421.dao.GamePlayerDAO;
import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.PlayerEntity;
import com.project.jeu421.service.GamePlayerService;
import com.project.jeu421.service.PlayerService;
import com.project.jeu421.websockets.GameHistory;
import com.project.jeu421.websockets.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.project.jeu421.websockets.WebSocketEventListener.onlinePlayersUsername;
import static com.project.jeu421.websockets.WebsocketController.rooms;

@Controller
public class BaseController {
    @RequestMapping("/")
    public String welcome() {
        return "index";
    }

    @Autowired
    PlayerService playerService;

    @Autowired
    GamePlayerDAO gamePlayerDAO;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        PlayerEntity player = new PlayerEntity();
        model.addAttribute("player", player);
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(PlayerEntity player) {
        playerService.createPlayer(player);
        return "register_success";
    }

    @GetMapping("/my_account")
    public String myAccountPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        PlayerEntity player = playerService.getPlayerByUsername(user_name);
        model.addAttribute("player", player);
        return "my_account";
    }

    @GetMapping("/my_account/edit")
    public String showEditForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        PlayerEntity player = playerService.getPlayerByUsername(user_name);
        model.addAttribute("player", player);
        return "edit_form";
    }

    @PostMapping("/my_account/process_edit")
    public String showEdit(PlayerEntity player) {
        playerService.updatePlayerInfo(player);
        return "my_account";
    }

    @GetMapping("/game_menu")
    public String menuPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPlayerUsername = authentication.getName();
        model.addAttribute("currentPlayerUsername", currentPlayerUsername);
        return "game_menu";
    }

    @GetMapping("/my_account/game_history")
    public String listLoggedInUsers(Locale locale, Model model) throws JsonProcessingException {
        List<Object> principals = sessionRegistry.getAllPrincipals();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPlayerUsername = authentication.getName();

        PlayerEntity player = playerService.getPlayerByUsername(currentPlayerUsername);
        List<GamePlayerEntity> gamePlayerEntity1 = gamePlayerDAO.findByPlayer(player);
        HashMap<String, GameHistory> gameHistoryList = new HashMap<>();
        for (GamePlayerEntity gamePlayer : gamePlayerEntity1) {
            GameEntity game1 = gamePlayer.getGame();
            List<GamePlayerEntity> gamePlayerEntity2 = gamePlayerDAO.findByGame(game1);
            for (int j = 0; j < gamePlayerEntity2.size(); j++) {
                GameEntity game2 = gamePlayerEntity2.get(j).getGame();
                PlayerEntity player2 = gamePlayerEntity2.get(j).getPlayer();
                String playerName2 = player2.getUsername();
                String gameName = game2.getNomGame();
                int numCharge = gamePlayerEntity2.get(j).getNbJetCharge();
                int numDecharge = gamePlayerEntity2.get(j).getNbJetDecharge();
                GameHistory gameHistory = new GameHistory(gameName, playerName2, numCharge, numDecharge);
                gameHistoryList.put(String.valueOf(j), gameHistory);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String gameHistoryString = objectMapper.writeValueAsString(gameHistoryList);
        model.addAttribute("gameHistoryString", gameHistoryString);
        return "game_history";
    }

    @GetMapping("/create_game/{room_id}")
    public String createGamePage(Model model, @PathVariable("room_id") String roomId) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPlayerUsername = authentication.getName();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(onlinePlayersUsername);
        String playersInRoom = "[]";
        if (rooms.get(roomId) != null) {
            playersInRoom = objectMapper.writeValueAsString(rooms.get(roomId).getListPlayers());
        }
        model.addAttribute("onlinePlayersUsername", jsonString);
        model.addAttribute("currentPlayerUsername", currentPlayerUsername);
        model.addAttribute("playersInRoom", playersInRoom);

        return "create_game";
    }

    @RequestMapping(value = "/process_create_game", method = RequestMethod.POST)
    public @ResponseBody
    String processCreateGame(@RequestBody String hostname) {
        hostname = (hostname.substring(0, hostname.length() - 1));
        return "game_interface/" + hostname;
    }

    @GetMapping("/game_interface/{game_name}")
    public String gameInterface(Model model, @PathVariable("game_name") String gameName) throws Exception {
        String[] parts = gameName.split("_");
        String roomId = parts[0];
        Room room = rooms.get(roomId);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(room.getListPlayers());
        String host = room.getHostUsername();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPlayerUsername = authentication.getName();

        model.addAttribute("playersInGame", jsonString);
        model.addAttribute("roomHost", host);
        model.addAttribute("currentPlayerUsername", currentPlayerUsername);
        model.addAttribute("gameName", gameName);
        return "game_interface";
    }

    @GetMapping("/chat")
    public String testsocket(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        model.addAttribute("username", user_name);
        return "chat";
    }


}
