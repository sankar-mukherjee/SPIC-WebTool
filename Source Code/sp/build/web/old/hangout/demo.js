
function showParticipants() {
    var participants = gapi.hangout.getParticipants();

    var retVal = '<p>Participants: </p><ul>';

    for (var index in participants) {
        var participant = participants[index];

        if (!participant.person) {
            retVal += '<li>A participant not running this app</li>';
        }
        retVal += '<li>' + participant.person.displayName + '</li>';
    }

    retVal += '</ul>';

    var div = document.getElementById('participantsDiv');

    div.innerHTML = retVal;
}

function readTextFile(file)
{
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if (rawFile.readyState === 4)
        {
            if (rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                alert(allText);
            }
        }
    }
    rawFile.send(null);
}

function prepareAPP() {
//    readTextFile("file:///https://147.94.196.248/sp/old/hangout/Trial53");
    readTextFile("file:///C:/Users/mukherjee/Desktop/sp/data/Trial53");
}


function init() {
    // When API is ready...                                                         
    gapi.hangout.onApiReady.add(
            function (eventObj) {
                if (eventObj.isApiReady) {
                   // prepareAPP();
                    document.getElementById('showParticipants')
                            .style.visibility = 'visible';
                }
            });
}

// Wait for gadget to load.                                                       
gadgets.util.registerOnLoadHandler(init);