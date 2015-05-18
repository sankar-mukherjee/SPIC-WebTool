
window.onload = init;

var context;
var source;
var analyser;
var buffer;
var audioBuffer;
var energy_threshold_pos = 2 * 1e-8;
var energy_threshold_neg = 0.5 * 1e-8;
var voiceTrend = 0;
var voiceTrendMax = 10;
var voiceTrendMin = -10;
var voiceTrendStart = 5;
var voiceTrendEnd = -5;
var signal;
var analyserView1;

//var mediaRecorder = new MediaRecorder(stream);
function error() {
    alert('Stream generation failed.');
    finishJSTest();
}

function getUserMedia(dictionary, callback) {
    try {
        navigator.webkitGetUserMedia(dictionary, callback, error);
    } catch (e) {
        alert('webkitGetUserMedia threw exception :' + e);
        finishJSTest();
    }
}

function gotStream(stream) {
    s = stream;

    // mediaRecorder.start();
    initAudio(stream);
    window.requestAnimationFrame(draw);

}

function init() {
    getUserMedia({audio: true}, gotStream);
}

function initAudio(stream) {
    context = new webkitAudioContext();

    analyser = context.createAnalyser();
    analyser.fftSize = 2048;
    var mediaStreamSource = context.createMediaStreamSource(stream);
    mediaStreamSource.connect(analyser);
    window.requestAnimationFrame(draw);
}

if (!window.requestAnimationFrame) {
    window.requestAnimationFrame = (function () {

        return window.webkitRequestAnimationFrame ||
                window.mozRequestAnimationFrame || // comment out if FF4 is slow (it caps framerate at ~30fps: https://bugzilla.mozilla.org/show_bug.cgi?id=630127)
                window.oRequestAnimationFrame ||
                window.msRequestAnimationFrame ||
                function (/* function FrameRequestCallback */ callback, /* DOMElement Element */ element) {

                    window.setTimeout(callback, 3000);

                };
    })();
}

function draw() {
    var buffer = new Uint8Array(analyser.fftSize);
    analyser.getByteTimeDomainData(buffer);
//    var rms = 0;
//    for (var i = 0; i < buffer.length; i++) {
//        rms += buffer[i] * buffer[i];
//    }
//    rms /= buffer.length;
//    rms = Math.sqrt(rms);

    myVad = new vad(analyser);
//    if (myVad.getSFM() <= -7) {
//    //if (rms >= 130) {
//       // mediaRecorder.stop();
//        window.setTimeout(window.location.reload(), 500);
//    }

//    signal = myVad.energyMonitor();
//    if (signal > energy_threshold_pos) {
//        voiceTrend = (voiceTrend + 1 > voiceTrendMax) ? voiceTrendMax : voiceTrend + 1;
//    } else if (signal < -energy_threshold_neg) {
//        voiceTrend = (voiceTrend - 1 < voiceTrendMin) ? voiceTrendMin : voiceTrend - 1;
//    } else {
//        // voiceTrend gets smaller
//        if (voiceTrend > 0) {
//            voiceTrend--;
//        } else if (voiceTrend < 0) {
//            voiceTrend++;
//        }
//    }
//
//    var start = false, end = false;
//    if (voiceTrend > voiceTrendStart) {
//        // Start of speech detected
//        window.setTimeout(window.location.reload());
//        start = true;
//    } else if (voiceTrend < voiceTrendEnd) {
//        // End of speech detected
//        end = true;
//    }

    window.requestAnimationFrame(draw);
}
