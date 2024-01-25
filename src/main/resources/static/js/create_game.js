function addOnlineUserToList(username, currentPlayerUsername) {
    if (username === currentPlayerUsername) {
        document.getElementById("online_users").innerHTML += "<li id='online_" + username + "'><button role='invite_button' data-user=" + username + " disabled>Invite</button>\n" + username + "</li>";
    } else {
        document.getElementById("online_users").innerHTML += "<li id='online_" + username + "' ><button role='invite_button' data-user=" + username + ">Invite</button>\n" + username + "</li>";
    }
}

function removeUserFromOnlineList(username) {
    $('li#online_' + username).remove();

}

function addPlayerToRoom(username, currentPlayerUsername) {
    if (username !== currentPlayerUsername) {
        document.getElementById("game_room").innerHTML += "<li id='inroom_" + username + "'>" + username + "</li>";
    }
}

function removeUserFromRoom(username) {
    $('li#inroom_' + username).remove();
}

$(document).ready(function () {
    // Everytime the create_game page is loaded it will renew the online player list
    for (let username of onlinePlayersUsername) {
        addOnlineUserToList(username, currentPlayerUsername)
    }
    for (let playername of playersInRoom) {
        addPlayerToRoom(playername, currentPlayerUsername)
    }

    // When the host invite others to the game the dialog will be displayed to the player who is invited
    // and when invited player choose to accept or deny the invitation the close event of dialog below will be triggered
    document.getElementById("dialog").addEventListener('close', (event) => {
        let invited_name;
        let host_name;
        let invitation_info;
        if (event.target.returnValue !== 0) {
            invitation_info = event.target.returnValue;
            host_name = invitation_info.split("_")[0]
            invited_name = invitation_info.split("_")[1]
            stompClient.send("/app/game_websocket", {},
                JSON.stringify({
                    'value': "add",
                    'host': host_name,
                    'invitedPlayer': invited_name,
                    'type': "confirmation"
                }));

            window.location.replace("/create_game/" + host_name);
        } else {
            stompClient.send("/app/game_websocket", {},
                JSON.stringify({
                    'value': "delete",
                    'type': "confirmation"
                }));
        }
    })

    // When the host click the invite button next to the name of others players the button click event below will be triggered
    // it will call the inviteToGame function in connect_websocket.js file
    // the first param is the name of the host
    // the second param is the name of the invited player
    $(document).on('click', "[role='invite_button']", function (event) {
        invitedPlayer = event.target.dataset.user;
        inviteToGame(currentPlayerUsername, invitedPlayer);
    });

    // When the host click create game button
    $("#create_game").on('click', function (event) {
        $.ajax({
            type: "POST",
            url: "/process_create_game",
            dataType: "text",
            data: currentPlayerUsername,
            success: function (data) {
                console.log("SUCCESS");
                stompClient.send("/app/in_game_websocket", {},
                    JSON.stringify({
                        'host': currentPlayerUsername,
                        'playersInRoom': JSON.stringify(playersInRoom),
                        'type': "start_game"
                    }));
            },
        })
    })
})

