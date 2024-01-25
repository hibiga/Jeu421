var socket = new SockJS('/game_websocket');
stompClient = Stomp.over(socket);
$(document).ready(function () {

    // This function will do the websocket connection so that the players can send and receive messages
    function connect() {

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);

            if (typeof currentPlayerUsername !== 'undefined') {
                stompClient.send("/app/create_room_websocket", {},
                    JSON.stringify({
                        'playerName': currentPlayerUsername,
                        'room': window.location.href.split("/").at(-1)
                    }));
            }

            stompClient.subscribe('/topic/messages', function (messageOutput) {
                msg = JSON.parse(messageOutput.body);
                // If a new player connect websocket it will send a message with type online to all others players
                if (msg.type === "online" && window.location.href.includes("create_game")) {
                    addOnlineUserToList(msg.username, currentPlayerUsername)
                }
                if (msg.type === "offline" && window.location.href.includes("create_game")) {
                    removeUserFromOnlineList(msg.username)
                    // removeUserFromRoom(msg.username)
                }
                if (msg.type === "invite" && frame.headers["user-name"] === msg.invitedPlayer) {
                    $("#accept").prop('value', msg.host + "_" + msg.invitedPlayer);
                    document.getElementById("dialog").showModal();
                }
                if (msg.type === "confirmation") {
                    addPlayerToRoom(msg.invitedPlayer, currentPlayerUsername)
                    if (!playersInRoom.includes(msg.invitedPlayer)) {
                        playersInRoom.push(msg.invitedPlayer)
                    }
                }
                if (msg.type === "start_game" && playersInRoom.includes(currentPlayerUsername)) {
                    let gameName = msg.gameName;
                    // window.location.replace("/game_interface/" + msg.host);
                    window.location.replace("/game_interface/" + gameName);
                }

                if (msg.type === "roll_dice") {
                    $('#dice1').html(msg.dice1);
                    $('#dice2').html(msg.dice2);
                    $('#dice3').html(msg.dice3);
                }

                if (msg.type === "finish_turn_phase_charge") {
                    turn = msg.turn;
                    playerToRoll = msg.playerNameNext;
                    player_to_roll_index = msg.playerNameNextIndex;
                    scoreList = JSON.parse(msg.scoreList);
                    playerTokenList = JSON.parse(msg.playerTokenList);
                    numberOfRoll = 0;
                    isReroll = msg.isReroll;
                    previousNumberOfRoll = msg.previousNumberOfRoll;
                    startPhaseDecharge = msg.startPhaseDecharge;
                    turnCounter = msg.turnCounter;
                    winnerIndex = msg.winnerIndex;

                    // show game info in sidebar each time players finish their turn
                    let phaseId = "";
                    let phaseTitle = "";
                    if (phase === 0) {
                        phaseId = "charge_" + nbRound;
                        phaseTitle = "Charge " + nbRound + ":";
                    } else {
                        phaseId = "decharge_" + nbRound;
                        if (startPhaseDecharge) {
                            phaseTitle = "Start Decharge " + nbRound + ":";
                        } else {
                            phaseTitle = "Decharge " + nbRound + ":";
                        }
                    }
                    if ($("#" + phaseId).length === 0) {
                        $("<dt id=" + phaseId + ">" + phaseTitle + "</dt>").insertAfter("#game_info_detail");
                    }
                    if ($("#" + phaseId + "_" + msg.playerName).length === 0) {
                        $("<dd id=" + phaseId + "_" + msg.playerName + ">" + msg.playerName + ": " + scoreList[msg.playerNameIndex] + "</dd>").insertAfter("#" + phaseId);
                    }

                    if (isReroll === 0) {
                        scoreRerollList = [];
                        // if last player have rolled then reset the score list
                        // if (msg.playerNameIndex === playersInGame.length - 1) {
                        if (msg.turnCounter === playersInGame.length) {
                            scoreList = new Array(playersInGame.length).fill(0);
                        }
                        phase = msg.phase;
                        nbRound = msg.nbRound;
                        pot = msg.pot;

                        // show value of pot
                        $('#pot').html("Pot: " + pot);

                    } else if (isReroll === 1) {
                        $('#reroll').html("REROLL!!!!");
                        playersReroll = JSON.parse(msg.playersReroll);
                        tokenToTake = msg.tokenToTake;
                    }

                    // show the token of every player
                    for (let i = 0; i < playerTokenList.length; i++) {
                        $('#player' + i + '_token').html(playerTokenList[i]);
                    }
                    let dice1Select = $("#dice1_select")
                    let dice2Select = $("#dice2_select")
                    let dice3Select = $("#dice3_select")


                    // reset the values of the dices to 0
                    $('#dice1').html(0);
                    $('#dice2').html(0);
                    $('#dice3').html(0);

                    dice1Select.attr('checked', true);
                    dice2Select.attr('checked', true);
                    dice3Select.attr('checked', true);


                    // reset turnCounter
                    if (turnCounter === playersInGame.length) {
                        turnCounter = 0;
                    }

                    if (playerToRoll === currentPlayerUsername) {
                        if (msg.phase === 1 && msg.startPhaseDecharge === 0) {
                            dice1Select.removeAttr("disabled");
                            dice2Select.removeAttr("disabled");
                            dice3Select.removeAttr("disabled");
                        }
                        $("#roll").removeAttr("disabled");
                    }

                    if (winnerIndex !== 99) {
                        $("<dt>" + playersInGame[winnerIndex] + " won!!!!" + "</dt>").insertAfter("#game_info_detail");
                        $("#roll").attr('disabled', true);
                        $("#finish_turn").attr('disabled', true);

                    }


                }

                if (msg.type === "finish_turn_reroll_phase_charge") {

                    playerToRoll = msg.playerNameNext;
                    turn = msg.turn;
                    scoreRerollList = JSON.parse(msg.scoreRerollList)
                    playerTokenList = JSON.parse(msg.playerTokenList);
                    player_to_roll_index = msg.playerNameNextIndex;
                    phase = msg.phase;
                    nbRound = msg.nbRound;
                    pot = msg.pot;
                    isReroll = msg.isReroll;
                    tokenToTake = msg.tokenToTake;
                    winnerIndex = msg.winnerIndex;
                    notSkipRerollPlayerIndex = JSON.parse(msg.notSkipRerollPlayerIndex);
                    let phaseId = "";
                    let phaseTitle = "";
                    if (phase === 0) {
                        phaseId = "charge_" + nbRound;
                        phaseTitle = "Charge " + nbRound + ":";
                    } else {
                        phaseId = "decharge_" + nbRound;
                        phaseTitle = "Decharge " + nbRound + ":";
                    }

                    $("<dd id=reroll_" + turn + "_" + phaseId + "_" + msg.playerName + ">" + msg.playerName + "(reroll): " + scoreRerollList[msg.playerNameIndex] + "</dd>").insertAfter("#" + phaseId);

                    if (isReroll === 0) {
                        scoreRerollList = []
                        playersReroll = [];
                        notSkipRerollPlayerIndex = [];
                        nbRound += 1;
                        $('#reroll').html("");
                        $('#pot').html("Pot: " + pot);

                    }
                    // show the token of every player
                    for (let i = 0; i < playerTokenList.length; i++) {
                        $('#player' + i + '_token').html(playerTokenList[i]);
                    }

                    if (playerToRoll === currentPlayerUsername) {
                        $("#roll").removeAttr("disabled");
                    }
                    $('#dice1').html(0);
                    $('#dice2').html(0);
                    $('#dice3').html(0);

                    if (winnerIndex !== 99) {
                        $("<dt>" + playersInGame[winnerIndex] + " won!!!!" + "</dt>").insertAfter("#game_info_detail");
                    }
                }

            });
        });
    }

    connect();
})


function inviteToGame(currentPlayerUsername, invitedPlayer) {
    stompClient.send("/app/game_websocket", {},
        JSON.stringify({
            'host': currentPlayerUsername,
            'invitedPlayer': invitedPlayer,
            'type': "invite"
        }));
}
