package com.project.jeu421.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.jeu421.entity.GameEntity;
import com.project.jeu421.entity.GamePlayerEntity;
import com.project.jeu421.entity.GameRoundEntity;
import com.project.jeu421.entity.PlayerEntity;
import com.project.jeu421.service.GamePlayerService;
import com.project.jeu421.service.GameRoundService;
import com.project.jeu421.service.GameService;
import com.project.jeu421.service.PlayerService;
import org.hibernate.mapping.Join;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

import static com.project.jeu421.websockets.WebSocketEventListener.onlinePlayersUsername;

@Controller
public class WebsocketController {
    public static HashMap<String, Room> rooms = new HashMap<String, Room>();

    @Autowired
    GameService gameService;

    @Autowired
    PlayerService playerService;

    @Autowired
    GameRoundService gameRoundService;

    @Autowired
    GamePlayerService gamePlayerService;

    @MessageMapping("/game_websocket")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String type = message.getType();

        if (Objects.equals(type, "invite")) {
            String host = message.getHost();
            String invitedPlayer = message.getInvitedPlayer();

            if (rooms.containsKey(host)) {
                Room room = rooms.get(host);
                if (!room.getListPlayers().contains(invitedPlayer)) {
                    room.updateListPlayer(invitedPlayer, "add");
                }
            } else {
                List<String> invitedPlayers = new ArrayList<>();
                invitedPlayers.add(host);
                Room room = new Room(host, invitedPlayers);
                rooms.put(host, room);
            }
            return new OutputMessage(host, invitedPlayer, type, time);

        } else if (Objects.equals(type, "confirmation")) {
            String host = message.getHost();
            String invitedPlayer = message.getInvitedPlayer();
            String value = message.getValue();

            if (rooms.containsKey(host)) {
                Room room = rooms.get(host);
                if (!room.getListPlayers().contains(invitedPlayer)) {
                    room.updateListPlayer(invitedPlayer, value);
                }
                return new OutputMessage(host, invitedPlayer, type, time);

            }
        }
        return new OutputMessage();
    }


    @MessageMapping("/in_game_websocket")
    @SendTo("/topic/messages")
    public OutputGameMessage send(GameMessage gameMessage) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String type = gameMessage.getType();

        if (Objects.equals(type, "start_game")) {
            String host = gameMessage.getHost();
            String playersInRoom = gameMessage.getPlayersInRoom();

            GameEntity game = new GameEntity();
            String gameName = host + "_" + time;
            game.setNomGame(gameName);
            gameService.createGame(game);

            Room room = rooms.get(host);

            for (int i = 0; i < room.getListPlayers().size(); i++) {
                String pName = room.getListPlayers().get(i);
                PlayerEntity player = playerService.getPlayerByUsername(pName);

                GamePlayerEntity gamePlayer = new GamePlayerEntity();
                gamePlayer.setGame(game);
                gamePlayer.setPlayer(player);
                gamePlayer.setNbJetCharge(0);
                gamePlayer.setNbJetDecharge(0);
                gamePlayer.setStatus(0);
                gamePlayerService.createGamePlayer(gamePlayer);
            }
            room.setGameName(gameName);
            return new OutputGameMessage(host, playersInRoom, type, time, gameName, host);

        }
        return new OutputGameMessage();
    }

    @MessageMapping("/create_room_websocket")
    @SendTo("/topic/messages")
    public OutputJoinMessage send(JoinMessage joinMessage) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String roomName = joinMessage.getRoom();
        String playerName = joinMessage.getPlayerName();
        if (!rooms.containsKey(playerName)) {
            List<String> invitedPlayers = new ArrayList<>();
            invitedPlayers.add(playerName);
            Room room = new Room(playerName, invitedPlayers);
            rooms.put(playerName, room);
        }
        return new OutputJoinMessage(playerName, playerName, "join", time);
    }

    @MessageMapping("/roll_dice_websocket")
    @SendTo("/topic/messages")
    public OutputRollDiceMessage send(RollDiceMessage rollDiceMessage) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String playerName = rollDiceMessage.getPlayerName();

        int dice1 = rollDiceMessage.getDice1();
        int dice2 = rollDiceMessage.getDice2();
        int dice3 = rollDiceMessage.getDice3();
        String type = rollDiceMessage.getType();

        return new OutputRollDiceMessage(playerName, dice1, dice2, dice3, type);
    }

    @MessageMapping("/finish_turn_phase_charge_websocket")
    @SendTo("/topic/messages")
    public OutputFinishTurnMessage send(FinishTurnMessage finishTurnMessage) throws Exception {
        String host = finishTurnMessage.getHost();
        String playerName = finishTurnMessage.getPlayerName();
        int playerNameIndex = finishTurnMessage.getPlayerNameIndex();
        String playerNameNext = finishTurnMessage.getPlayerNameNext();
        int numberOfRoll = finishTurnMessage.getNumberOfRoll();
        int turn = finishTurnMessage.getTurn();
        int dice1 = finishTurnMessage.getDice1();
        int dice2 = finishTurnMessage.getDice2();
        int dice3 = finishTurnMessage.getDice3();
        int score = finishTurnMessage.getScore();
        String scoreList = finishTurnMessage.getScoreList();
        String playerTokenList = finishTurnMessage.getPlayerTokenList();
        int phase = finishTurnMessage.getPhase();
        int nbRound = finishTurnMessage.getNbRound();
        int pot = finishTurnMessage.getPot();
        String type = finishTurnMessage.getType();
        int highestScore = finishTurnMessage.getHighestScore();
        int lowestScore = finishTurnMessage.getLowestScore();
        String lowestScoreIndexes = finishTurnMessage.getLowestScoreIndexes();
        String highestScoreIndexes = finishTurnMessage.getHighestScoreIndexes();
        int isReroll = finishTurnMessage.getIsReroll();
        int previousNumberOfRoll = finishTurnMessage.getPreviousNumberOfRoll();
        int startPhaseDecharge = finishTurnMessage.getStartPhaseDecharge();
        int turnCounter = finishTurnMessage.getTurnCounter();
//        String gameName = finishTurnMessage.getGameName();
        int winnerIndex = 99;

        previousNumberOfRoll = numberOfRoll;
        ObjectMapper mapper = new ObjectMapper();
        int[] lowestScoreIndexesArr = mapper.readValue(lowestScoreIndexes, int[].class);
        int[] highestScoreIndexesArr = mapper.readValue(highestScoreIndexes, int[].class);
        int[] playerTokenListArr = mapper.readValue(playerTokenList, int[].class);


//        Create record in game round table
        Room room = rooms.get(host);
        String gameName = room.getGameName();
        createRecordInGameRoundTable(room, playerName, dice1, dice2, dice3, nbRound, phase, numberOfRoll);
        int tokenToTake = 0;

//        When all player have rolled the dices
//        if (playerNameIndex == room.getListPlayers().size() - 1) {
        if (turnCounter == room.getListPlayers().size()) {
            previousNumberOfRoll = 3;
            if (phase == 0) {
//                calculate the token that lose player has to take
                if (pot > highestScore) {
                    pot = pot - highestScore;
                    tokenToTake = highestScore;
                } else {
                    tokenToTake = pot;
                    pot = 0;
                }

//                check if is there any players have same lowest score
                if (lowestScoreIndexesArr.length > 1) {
                    if (isReroll == 0) {
                        isReroll = 1;
                        turn = 0;
                    }
                    int playerNameNextIndex = turn % lowestScoreIndexesArr.length;
                    playerNameNext = room.getListPlayers().get(lowestScoreIndexesArr[playerNameNextIndex]);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String playersReroll = objectMapper.writeValueAsString(lowestScoreIndexesArr);
                    return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, playersReroll, isReroll, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, 99, type);
                }
//                if there is no player has same score then apply the token to the player that have lowest score
                playerTokenListArr[lowestScoreIndexesArr[0]] += tokenToTake;
            } else if (phase == 1) {
                if (highestScoreIndexesArr.length > 1) {
//                    If there are more than 1 winner
                    if (isReroll == 0) {
                        isReroll = 1;
                        turn = highestScoreIndexesArr[0];
                    }
                    tokenToTake = highestScore;
                    int playerNameNextIndex = turn % highestScoreIndexesArr.length;
                    playerNameNext = room.getListPlayers().get(highestScoreIndexesArr[playerNameNextIndex]);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String playersReroll = objectMapper.writeValueAsString(highestScoreIndexesArr);
                    return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, playersReroll, isReroll, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, winnerIndex, type);
                } else {
//                    if there is only one winner
                    int winnerToken = playerTokenListArr[highestScoreIndexesArr[0]];
                    tokenToTake = Math.min(highestScore, winnerToken);

//                    if this is the start of phase decharge to choose the player to start
                    if (startPhaseDecharge == 1) {
                        startPhaseDecharge = 0;
                        turn = highestScoreIndexesArr[0];
                        int playerNameNextIndex = turn % room.getListPlayers().size();
                        playerNameNext = room.getListPlayers().get(playerNameNextIndex);
                        nbRound += 1;
                        return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, "", 0, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, winnerIndex, type);
                    } else {
//                        if it's a normal round
                        playerTokenListArr[highestScoreIndexesArr[0]] = playerTokenListArr[highestScoreIndexesArr[0]] - tokenToTake;

//                        found the final winner if player token equal 0
                        if (playerTokenListArr[highestScoreIndexesArr[0]] == 0) {
                            winnerIndex = highestScoreIndexesArr[0];
                        }
                        if (lowestScoreIndexesArr.length == 1) {
//                            if there is only one lowest score
//                            1. case 1 highest score, 1 lowest score
                            playerTokenListArr[lowestScoreIndexesArr[0]] = playerTokenListArr[lowestScoreIndexesArr[0]] + tokenToTake;
                            ObjectMapper objectMapper = new ObjectMapper();
                            playerTokenList = objectMapper.writeValueAsString(playerTokenListArr);

//                            set player that has lowest score as next player to roll
                            turn = lowestScoreIndexesArr[0];
                            int playerNameNextIndex = turn % room.getListPlayers().size();
                            playerNameNext = room.getListPlayers().get(playerNameNextIndex);

                            if (winnerIndex != 99) {
//                          update game table when game is finish
                                gameService.updateGame(gameName, nbRound, phase);
                                GameEntity game = gameService.getGameByGameName(gameName);
//                          update gamePlayer table when game is finish
                                for (int i = 0; i < room.getListPlayers().size(); i++) {
                                    int status = 0;
                                    if (winnerIndex == i) {
                                        status = 1;
                                    }
                                    int nbJet = playerTokenListArr[i];
                                    String pname = room.getListPlayers().get(i);
                                    PlayerEntity player = playerService.getPlayerByUsername(pname);
                                    gamePlayerService.updateGamePlayer(game, player, nbJet, phase, status);

//                              update player table when game is finish
                                    playerService.updatePlayerStat(pname);
                                }
                            }

                            nbRound += 1;
                            return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, "", 0, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, winnerIndex, type);
                        } else {
//                            if there are more than one loser
                            int b = 0;
                        }
                    }
                }
                if (lowestScoreIndexesArr.length > 1 && startPhaseDecharge == 0) {
                    if (isReroll == 0) {
                        isReroll = 1;
                        turn = 0;
                    }
                    int playerNameNextIndex = turn % lowestScoreIndexesArr.length;
                    playerNameNext = room.getListPlayers().get(lowestScoreIndexesArr[playerNameNextIndex]);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String playersReroll = objectMapper.writeValueAsString(lowestScoreIndexesArr);
                    return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, playersReroll, isReroll, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, winnerIndex, type);
                }

            }

//        if value of pot is 0 then move to phase decharge

            nbRound += 1;
            if (pot == 0 && phase == 0) {
//                update game table after phase charge
                GameEntity game = gameService.getGameByGameName(gameName);
                gameService.updateGame(gameName, nbRound, phase);
//                update gamePlayer table after phase charge
                for (int i = 0; i < room.getListPlayers().size(); i++) {
                    int nbJet = playerTokenListArr[i];
                    String pname = room.getListPlayers().get(i);
                    PlayerEntity player = playerService.getPlayerByUsername(pname);
                    gamePlayerService.updateGamePlayer(game, player, nbJet, phase, 0);
                }

                phase = 1;
                nbRound = 1;
            }
        }


        int playerNameNextIndex = turn % room.getListPlayers().size();
        ObjectMapper objectMapper = new ObjectMapper();
        playerTokenList = objectMapper.writeValueAsString(playerTokenListArr);

        return new OutputFinishTurnMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, scoreList, playerTokenList, numberOfRoll, turn, phase, nbRound, pot, "", 0, tokenToTake, previousNumberOfRoll, startPhaseDecharge, turnCounter, winnerIndex, type);
    }


    @MessageMapping("/finish_turn_reroll_phase_charge_websocket")
    @SendTo("/topic/messages")
    public OutputFinishTurnRerollPhaseChargeMessage send(FinishTurnRerollPhaseChargeMessage finishTurnRerollPhaseChargeMessage) throws Exception {
        String host = finishTurnRerollPhaseChargeMessage.getHost();
        String playerName = finishTurnRerollPhaseChargeMessage.getPlayerName();
        int playerNameIndex = finishTurnRerollPhaseChargeMessage.getPlayerNameIndex();
        String playerNameNext = finishTurnRerollPhaseChargeMessage.getPlayerNameNext();
        int turn = finishTurnRerollPhaseChargeMessage.getTurn();
        int score = finishTurnRerollPhaseChargeMessage.getScore();
        String scoreRerollList = finishTurnRerollPhaseChargeMessage.getScoreRerollList();
        String playerTokenList = finishTurnRerollPhaseChargeMessage.getPlayerTokenList();
        String rerollLowestScoreIndexes = finishTurnRerollPhaseChargeMessage.getRerollLowestScoreIndexes();
        String rerollHighestScoreIndexes = finishTurnRerollPhaseChargeMessage.getRerollHighestScoreIndexes();
        int phase = finishTurnRerollPhaseChargeMessage.getPhase();
        int nbRound = finishTurnRerollPhaseChargeMessage.getNbRound();
        int pot = finishTurnRerollPhaseChargeMessage.getPot();
        int isReroll = finishTurnRerollPhaseChargeMessage.getIsReroll();
        int tokenToTake = finishTurnRerollPhaseChargeMessage.getTokenToTake();
        String type = finishTurnRerollPhaseChargeMessage.getType();
        String playersReroll = finishTurnRerollPhaseChargeMessage.getPlayersReroll();
        String notSkipRerollPlayerIndex = finishTurnRerollPhaseChargeMessage.getNotSkipRerollPlayerIndex();
//        String gameName = finishTurnRerollPhaseChargeMessage.getGameName();

        ObjectMapper mapper = new ObjectMapper();
        int[] rerollLowestScoreIndexesArr = mapper.readValue(rerollLowestScoreIndexes, int[].class);
        int[] rerollHighestScoreIndexesArr = mapper.readValue(rerollHighestScoreIndexes, int[].class);
        int[] playersRerollArr = mapper.readValue(playersReroll, int[].class);
        int[] playerTokenListArr = mapper.readValue(playerTokenList, int[].class);
        int[] notSkipRerollPlayerIndexArr = mapper.readValue(notSkipRerollPlayerIndex, int[].class);
        int winnerIndex = 99;

        if (playerNameIndex == playersRerollArr.length - 1) {
            if (phase == 0) {
                if (rerollLowestScoreIndexesArr.length == 1) {
                    isReroll = 0;
                    pot = pot - tokenToTake;
                    playerTokenListArr[playersRerollArr[rerollLowestScoreIndexesArr[0]]] += tokenToTake;
                    turn = 0;
                    Room room = rooms.get(host);
                    String gameName = room.getGameName();
                    int playerNameNextIndex = 0;
                    playerNameNext = room.getListPlayers().get(0);
                    playerTokenList = mapper.writeValueAsString(playerTokenListArr);

                    if (pot == 0) {
//                        update game table after phase charge
                        gameService.updateGame(gameName, nbRound, phase);
                        GameEntity game = gameService.getGameByGameName(gameName);
                        // update gamePlayer table after phase charge
                        for (int i = 0; i < room.getListPlayers().size(); i++) {
                            int nbJet = playerTokenListArr[i];
                            String pname = room.getListPlayers().get(i);
                            PlayerEntity player = playerService.getPlayerByUsername(pname);
                            gamePlayerService.updateGamePlayer(game, player, nbJet, phase, 0);
                        }
                        phase = 1;
                        nbRound = 0;
                    }
                    return new OutputFinishTurnRerollPhaseChargeMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, turn, score, scoreRerollList, playerTokenList, phase, nbRound, pot, isReroll, tokenToTake, 99, notSkipRerollPlayerIndex, type);
                } else {
                    if (!Arrays.equals(rerollHighestScoreIndexesArr, rerollLowestScoreIndexesArr)) {
//                        notSkipRerollPlayerIndexArr = rerollHighestScoreIndexesArr;
                        notSkipRerollPlayerIndexArr = rerollLowestScoreIndexesArr;
                    }
                }
            } else if (phase == 1) {
                if (rerollHighestScoreIndexesArr.length == 1) {
                    Room room = rooms.get(host);
                    String gameName = room.getGameName();

//                    if after reroll, found the winner
                    int winnerToken = playerTokenListArr[playersRerollArr[rerollHighestScoreIndexesArr[0]]];
                    tokenToTake = Math.min(tokenToTake, winnerToken);
                    playerTokenListArr[playersRerollArr[rerollHighestScoreIndexesArr[0]]] -= tokenToTake;

//                        found the final winner if player token equal 0
                    if (playerTokenListArr[playersRerollArr[rerollHighestScoreIndexesArr[0]]] == 0) {
//                      update game table when game is finish
                        winnerIndex = playersRerollArr[rerollHighestScoreIndexesArr[0]];
                    }
                    if (rerollLowestScoreIndexesArr.length == 1) {
//                        if after reroll found the loser
                        isReroll = 0;
                        playerTokenListArr[playersRerollArr[rerollLowestScoreIndexesArr[0]]] += tokenToTake;
                        turn = playersRerollArr[rerollLowestScoreIndexesArr[0]];
//                        set loser as the player to roll first next round
                        playerNameNext = room.getListPlayers().get(playersRerollArr[rerollLowestScoreIndexesArr[0]]);
                        int playerNameNextIndex = turn % room.getListPlayers().size();
                        playerTokenList = mapper.writeValueAsString(playerTokenListArr);

                        if (winnerIndex != 99) {
                            gameService.updateGame(gameName, nbRound, phase);
                            GameEntity game = gameService.getGameByGameName(gameName);
//                       update gamePlayer table when game is finish
                            for (int i = 0; i < room.getListPlayers().size(); i++) {
                                int status = 0;
                                if (winnerIndex == i) {
                                    status = 1;
                                }
                                int nbJet = playerTokenListArr[i];
                                String pname = room.getListPlayers().get(i);
                                PlayerEntity player = playerService.getPlayerByUsername(pname);
                                gamePlayerService.updateGamePlayer(game, player, nbJet, phase, status);

//                          update player table when game is finish
                                playerService.updatePlayerStat(pname);
                            }
                        }
                        return new OutputFinishTurnRerollPhaseChargeMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, turn, score, scoreRerollList, playerTokenList, phase, nbRound, pot, isReroll, tokenToTake, winnerIndex, notSkipRerollPlayerIndex, type);

                    } else {
//                        found winner but not yet found the loser
                        int playerNameNextIndex = turn % playersRerollArr.length;
                        if (notSkipRerollPlayerIndexArr.length > 0) {
                            notSkipRerollPlayerIndex = mapper.writeValueAsString(notSkipRerollPlayerIndexArr);
                            for (int z = 0; z < 99; z++) {
//                                if (playerNameNextIndex != notSkipRerollPlayerIndexArr[z]) {
                                int finalPlayerNameNextIndex = playerNameNextIndex;
                                if (!Arrays.stream(notSkipRerollPlayerIndexArr).anyMatch(i -> i == finalPlayerNameNextIndex)) {
                                    turn += 1;
                                    playerNameNextIndex = turn % playersRerollArr.length;
                                    playerNameNext = room.getListPlayers().get(playersRerollArr[playerNameNextIndex]);
                                } else {
                                    break;
                                }
                            }
                        }

//                        playerNameNext = room.getListPlayers().get(playerNameNextIndex);
                        return new OutputFinishTurnRerollPhaseChargeMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, turn, score, scoreRerollList, playerTokenList, phase, nbRound, pot, isReroll, tokenToTake, winnerIndex, notSkipRerollPlayerIndex, type);
                    }
//                    turn = 0;
//                    int playerNameNextIndex = 0;
//                    playerNameNext = room.getListPlayers().get(0);
//                    playerTokenList = mapper.writeValueAsString(playerTokenListArr);
//
//                    return new OutputFinishTurnRerollPhaseChargeMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, turn, score, scoreRerollList, playerTokenList, phase, nbRound, pot, isReroll, tokenToTake, winnerIndex, type);
                } else {
                    //if not yet found the winner
                    int b = 0;
                }
            }

        }
        int playerNameNextIndex = turn % playersRerollArr.length;
        if (notSkipRerollPlayerIndexArr.length > 0) {
            notSkipRerollPlayerIndex = mapper.writeValueAsString(notSkipRerollPlayerIndexArr);
            for (int z = 0; z < 99; z++) {
//                if (playerNameNextIndex != notSkipRerollPlayerIndexArr[z]) {
                int finalPlayerNameNextIndex = playerNameNextIndex;
                if (!Arrays.stream(notSkipRerollPlayerIndexArr).anyMatch(i -> i == finalPlayerNameNextIndex)) {
                    turn += 1;
                    playerNameNextIndex = turn % playersRerollArr.length;
                    Room room = rooms.get(host);
                    playerNameNext = room.getListPlayers().get(playersRerollArr[playerNameNextIndex]);
                } else {
                    break;
                }
            }
        }


        return new OutputFinishTurnRerollPhaseChargeMessage(host, playerName, playerNameIndex, playerNameNext, playerNameNextIndex, turn, score, scoreRerollList, playerTokenList, phase, nbRound, pot, isReroll, tokenToTake, winnerIndex, notSkipRerollPlayerIndex, type);
    }


    public void createRecordInGameRoundTable(Room room, String playerName, int dice1, int dice2, int dice3, int nbRound, int phase, int numberOfRoll) {
        String gameName = room.getGameName();
        PlayerEntity player = playerService.getPlayerByUsername(playerName);
        GameEntity game = gameService.getGameByGameName(gameName);
        GameRoundEntity gameRoundEntity = new GameRoundEntity();
        gameRoundEntity.setDiceValue1(dice1);
        gameRoundEntity.setDiceValue2(dice2);
        gameRoundEntity.setDiceValue3(dice3);
        gameRoundEntity.setNbRound(nbRound);
        gameRoundEntity.setPhase(phase);
        gameRoundEntity.setRelancement(numberOfRoll);
        gameRoundEntity.setGame(game);
        gameRoundEntity.setPlayer(player);
        gameRoundService.createGameRound(gameRoundEntity);
    }


}