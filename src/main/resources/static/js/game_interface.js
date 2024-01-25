function randomIntFromInterval(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min)
}

function getAllIndexes(arr, val) {
    let indexes = [];

    for (let index = 0; index < arr.length; index++) {
        if (arr[index] === val) {
            indexes.push(index);
        }
    }
    return indexes
}

$(document).ready(function () {
    let combinationScore = {
        '[1,2,4]': 10,
        '[1,1,1]': 7,
        '[1,1,6]': 6,
        '[6,6,6]': 6,
        '[1,1,5]': 5,
        '[5,5,5]': 5,
        '[1,1,4]': 4,
        '[4,4,4]': 4,
        '[1,1,3]': 3,
        '[3,3,3]': 3,
        '[1,1,2]': 2,
        '[2,2,2]': 2,
        '[4,5,6]': 2,
        '[3,4,5]': 2,
        '[2,3,4]': 2,
        '[1,2,3]': 2,
        '[1,2,2]': 2,
    }

    for (let i = 0; i < playersInGame.length; i++) {
        let player_index = i
        let player_name = playersInGame[i]
        $('#player' + player_index).html(player_name);
        $('#player' + player_index + "_token").html(playerTokenList[i]);
    }

    let dice1 = 0
    let dice2 = 0
    let dice3 = 0
    let rollButton = $("#roll");

    if (playerToRoll === currentPlayerUsername) {
        rollButton.removeAttr("disabled")
    }

    rollButton.on("click", function (event) {
        // number of time roll button clicked
        numberOfRoll += 1;
        if (phase === 0) {
            // in phase charge player can't reroll the dices
            if (isReroll === 0) {
                dice1 = randomIntFromInterval(1, 6);
                dice2 = randomIntFromInterval(1, 6);
                dice3 = randomIntFromInterval(1, 6);
            } else if (isReroll === 1) {
                dice1 = randomIntFromInterval(1, 6);
                dice2 = 0;
                dice3 = 0;
            }
            rollButton.attr('disabled', true);
        } else if (phase === 1) {
            if (isReroll === 0 && startPhaseDecharge === 0) {
                if ($('#dice1_select').is(':checked')) {
                    dice1 = randomIntFromInterval(1, 6);
                }
                if ($('#dice2_select').is(':checked')) {
                    dice2 = randomIntFromInterval(1, 6);
                }
                if ($('#dice3_select').is(':checked')) {
                    dice3 = randomIntFromInterval(1, 6);
                }
            } else if (isReroll === 1 || startPhaseDecharge === 1) {
                dice1 = randomIntFromInterval(1, 6);
                dice2 = 0;
                dice3 = 0;
                rollButton.attr('disabled', true);
            }
            // in phase decharge player can choose which dice to reroll
            if (numberOfRoll >= previousNumberOfRoll) {
                rollButton.attr('disabled', true);
            }
        }

        stompClient.send("/app/roll_dice_websocket", {},
            JSON.stringify({
                'playerName': playerToRoll,
                'dice1': dice1,
                'dice2': dice2,
                'dice3': dice3,
                'type': "roll_dice"
            }));
        $("#finish_turn").removeAttr("disabled")
    });

    $("#finish_turn").on("click", function (event) {
        $("#roll").attr('disabled', true);
        $("#finish_turn").attr('disabled', true);
        let lowestScoreIndexes = [];
        let highestScoreIndexes = [];
        let highestScore = 0;
        let lowestScore = 0;

        // calculate the score from combination
        let score = 0
        if (isReroll === 0) {
            turnCounter += 1;
            score = combinationScore[JSON.stringify([dice1, dice2, dice3].sort())];
            if (typeof score === 'undefined') {
                // if combination not in list set score to 1
                score = 1;
            }
            if (phase === 1 && startPhaseDecharge === 1) {
                score = dice1;
            }
        } else if (isReroll === 1) {
            score = dice1;
        }

        turn += 1;
        if (phase === 0) {
            if (isReroll === 0) {
                if (scoreList.length < playersInGame.length) {
                    scoreList.push(score);
                } else {
                    scoreList[player_to_roll_index] = score;
                }
                // get the next player to roll the dices
                let playerToRollNextIndex = turn % playersInGame.length;
                let playerToRollNext = playersInGame[playerToRollNextIndex];

                // if this turn is the turn of last player
                // if (player_to_roll_index === playersInGame.length - 1) {
                if (turnCounter === playersInGame.length) {
                    // get the highest score and lowest score of players
                    highestScore = Math.max.apply(null, scoreList)
                    lowestScore = Math.min.apply(null, scoreList)
                    lowestScoreIndexes = getAllIndexes(scoreList, lowestScore)
                    highestScoreIndexes = getAllIndexes(scoreList, highestScore)
                }

                stompClient.send("/app/finish_turn_phase_charge_websocket", {},
                    JSON.stringify({
                        'host': roomHost,
                        'playerName': playerToRoll,
                        'playerNameIndex': player_to_roll_index,
                        'playerNameNext': playerToRollNext,
                        'numberOfRoll': numberOfRoll,
                        'turn': turn,
                        'dice1': dice1,
                        'dice2': dice2,
                        'dice3': dice3,
                        'score': score,
                        'scoreList': JSON.stringify(scoreList),
                        'highestScore': highestScore,
                        'lowestScore': lowestScore,
                        'lowestScoreIndexes': JSON.stringify(lowestScoreIndexes),
                        'highestScoreIndexes': JSON.stringify(highestScoreIndexes),
                        'playerTokenList': JSON.stringify(playerTokenList),
                        'phase': phase,
                        'nbRound': nbRound,
                        'pot': pot,
                        'isReroll': isReroll,
                        'previousNumberOfRoll': previousNumberOfRoll,
                        'startPhaseDecharge': startPhaseDecharge,
                        'turnCounter': turnCounter,
                        'type': "finish_turn_phase_charge"
                    }));
            } else if (isReroll === 1) {
                let rerollLowestScore = 0;
                let rerollLowestScoreIndexes = [];
                let rerollHighestScore = 0;
                let rerollHighestScoreIndexes = [];
                if (scoreRerollList.length < playersReroll.length) {
                    scoreRerollList.push(score);
                } else {
                    scoreRerollList[player_to_roll_index] = score;
                }

                if (player_to_roll_index === playersReroll.length - 1) {
                    // get the lowest score of players
                    rerollLowestScore = Math.min.apply(null, scoreRerollList)
                    rerollLowestScoreIndexes = getAllIndexes(scoreRerollList, rerollLowestScore)
                    rerollHighestScore = Math.max.apply(null, scoreRerollList)
                    rerollHighestScoreIndexes = getAllIndexes(scoreRerollList, rerollHighestScore)
                }

                let playerToRollNextIndex = turn % playersReroll.length;
                let playerToRollNext = playersInGame[playersReroll[playerToRollNextIndex]];
                stompClient.send("/app/finish_turn_reroll_phase_charge_websocket", {},
                    JSON.stringify({
                        'host': roomHost,
                        'playerName': playerToRoll,
                        'playerNameIndex': player_to_roll_index,
                        'playerNameNext': playerToRollNext,
                        'turn': turn,
                        'score': score,
                        'scoreRerollList': JSON.stringify(scoreRerollList),
                        'playerTokenList': JSON.stringify(playerTokenList),
                        'rerollLowestScoreIndexes': JSON.stringify(rerollLowestScoreIndexes),
                        'rerollHighestScoreIndexes': JSON.stringify(rerollHighestScoreIndexes),
                        'phase': phase,
                        'nbRound': nbRound,
                        'pot': pot,
                        'isReroll': isReroll,
                        'tokenToTake': tokenToTake,
                        'playersReroll': JSON.stringify(playersReroll),
                        'notSkipRerollPlayerIndex': JSON.stringify(notSkipRerollPlayerIndex),
                        'type': "finish_turn_reroll_phase_charge"
                    }));
            }
        } else if (phase === 1) {
            console.log("decharge");
            if (isReroll === 0) {
                if (scoreList.length < playersInGame.length) {
                    scoreList.push(score);
                } else {
                    scoreList[player_to_roll_index] = score;
                }

                let playerToRollNextIndex = turn % playersInGame.length;
                let playerToRollNext = playersInGame[playerToRollNextIndex];

                // if this turn is the turn of last player
                // if (player_to_roll_index === playersInGame.length - 1) {
                if (turnCounter === playersInGame.length) {
                    // get the highest score and lowest score of players
                    highestScore = Math.max.apply(null, scoreList)
                    lowestScore = Math.min.apply(null, scoreList)
                    lowestScoreIndexes = getAllIndexes(scoreList, lowestScore)
                    highestScoreIndexes = getAllIndexes(scoreList, highestScore)
                }

                stompClient.send("/app/finish_turn_phase_charge_websocket", {},
                    JSON.stringify({
                        'host': roomHost,
                        'playerName': playerToRoll,
                        'playerNameIndex': player_to_roll_index,
                        'playerNameNext': playerToRollNext,
                        'numberOfRoll': numberOfRoll,
                        'turn': turn,
                        'dice1': dice1,
                        'dice2': dice2,
                        'dice3': dice3,
                        'score': score,
                        'scoreList': JSON.stringify(scoreList),
                        'highestScore': highestScore,
                        'lowestScore': lowestScore,
                        'lowestScoreIndexes': JSON.stringify(lowestScoreIndexes),
                        'highestScoreIndexes': JSON.stringify(highestScoreIndexes),
                        'playerTokenList': JSON.stringify(playerTokenList),
                        'phase': phase,
                        'nbRound': nbRound,
                        'pot': pot,
                        'isReroll': isReroll,
                        'previousNumberOfRoll': previousNumberOfRoll,
                        'startPhaseDecharge': startPhaseDecharge,
                        'turnCounter': turnCounter,
                        'type': "finish_turn_phase_charge"
                    }));

            } else if (isReroll === 1) {
                console.log("reroll decharge");
                let rerollLowestScore = 0;
                let rerollLowestScoreIndexes = [];
                let rerollHighestScore = 0;
                let rerollHighestScoreIndexes = [];
                if (scoreRerollList.length < playersReroll.length) {
                    scoreRerollList.push(score);
                } else {
                    scoreRerollList[player_to_roll_index] = score;
                }

                if (player_to_roll_index === playersReroll.length - 1) {
                    // get the lowest score of players
                    rerollLowestScore = Math.min.apply(null, scoreRerollList)
                    rerollLowestScoreIndexes = getAllIndexes(scoreRerollList, rerollLowestScore)
                    rerollHighestScore = Math.max.apply(null, scoreRerollList)
                    rerollHighestScoreIndexes = getAllIndexes(scoreRerollList, rerollHighestScore)
                }

                let playerToRollNextIndex = turn % playersReroll.length;
                let playerToRollNext = playersInGame[playersReroll[playerToRollNextIndex]];

                stompClient.send("/app/finish_turn_reroll_phase_charge_websocket", {},
                    JSON.stringify({
                        'host': roomHost,
                        'playerName': playerToRoll,
                        'playerNameIndex': player_to_roll_index,
                        'playerNameNext': playerToRollNext,
                        'turn': turn,
                        'score': score,
                        'scoreRerollList': JSON.stringify(scoreRerollList),
                        'playerTokenList': JSON.stringify(playerTokenList),
                        'rerollLowestScoreIndexes': JSON.stringify(rerollLowestScoreIndexes),
                        'rerollHighestScoreIndexes': JSON.stringify(rerollHighestScoreIndexes),
                        'phase': phase,
                        'nbRound': nbRound,
                        'pot': pot,
                        'isReroll': isReroll,
                        'tokenToTake': tokenToTake,
                        'playersReroll': JSON.stringify(playersReroll),
                        'notSkipRerollPlayerIndex': JSON.stringify(notSkipRerollPlayerIndex),
                        'type': "finish_turn_reroll_phase_charge"
                    }));
            }

        }
    })

})




