function loadTableData(item) {
    const table = document.getElementById("table_body");
    let row = table.insertRow();
    let gameName = row.insertCell(0);
    gameName.innerHTML = item.gameName;
    let playerName = row.insertCell(1);
    playerName.innerHTML = item.playerName;
    let nbJetCharge = row.insertCell(2);
    nbJetCharge.innerHTML = item.nbJetCharge;
    let nbJetDecharge = row.insertCell(3);
    nbJetDecharge.innerHTML = item.nbJetDecharge;

}

$(document).ready(function () {
    for (let i = 0; i < Object.keys(gameHistoryString).length; i++) {
        loadTableData(gameHistoryString[i])
    }
})