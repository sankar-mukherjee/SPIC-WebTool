/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = init;

var context = new (window.AudioContext || window.webkitAudioContext)();
var microphone;
var analyser;
var javascriptNode;
var myVad;

function gotAudio(stream) {

    microphone = context.createMediaStreamSource(stream);

    // setup a javascript node
    javascriptNode = context.createScriptProcessor(512, 1, 1);
    // connect to destination, else it isn't called
    javascriptNode.connect(context.destination);

    // setup an analyzer
    analyser = context.createAnalyser();
    analyser.smoothingTimeConstant = 0.99; // 0.3;
    analyser.fftSize = 512;

    // analyser.maxDecibels = -20;
    // analyser.minDecibels = -60;

    // connect up the nodes
    microphone.connect(analyser);
    analyser.connect(javascriptNode);

    myVad = new vad(analyser);

    // when the javascript node is called
    // we use information from the analyzer node
    // to draw the volume
    javascriptNode.onaudioprocess = function () {

        // get the average for the first channel
        var array = new Uint8Array(analyser.frequencyBinCount);
        analyser.getByteFrequencyData(array);
        myVad.energyMonitor();
        myVad.iterate();

    };
}

function logError(error) {
    console.log(error.name + ": " + error.message);
}

function init() {
    if (navigator.getUserMedia) { // W3C
        navigator.getUserMedia({audio: true}, gotAudio, logError);
    } else if (navigator.webkitGetUserMedia) { // WebKit
        navigator.webkitGetUserMedia({audio: true}, gotAudio, logError);
    } else if (navigator.mozGetUserMedia) { // Mozilla
        navigator.mozGetUserMedia({audio: true}, gotAudio, logError);
    }
}