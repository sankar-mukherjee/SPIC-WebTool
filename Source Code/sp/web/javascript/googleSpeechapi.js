/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


window.onload = init;
if (!('webkitSpeechRecognition' in window)) {
    alert('Install latest version of chrome');
} else {
    var recognition = new webkitSpeechRecognition();
    recognition.continuous = false;
    recognition.interimResults = false;

    recognition.onstart = function() {
        recognizing = true;

    };
    recognition.onend = function() {
        recognition.start();
        return;
    };
    recognition.onresult = function(event) {
        //alert("ddd");
        window.location.reload();
    };
}

function init() {
    recognition.start();
}


