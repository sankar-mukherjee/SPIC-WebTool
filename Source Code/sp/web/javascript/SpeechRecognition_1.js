
window.onload = init;

var analyser;
var filter;
var audioCtx;
var source;
var myVad;

function init() {
    navigator.getUserMedia = (navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia);

    audioCtx = new (window.AudioContext || window.webkitAudioContext)();

    var stream;

    analyser = audioCtx.createAnalyser();
    //analyser.minDecibels = -90;
    //analyser.maxDecibels = -10;
    //analyser.smoothingTimeConstant = 0.85;
    filter = audioCtx.createBiquadFilter();

    if (navigator.getUserMedia) {
        console.log('getUserMedia supported.');
        navigator.getUserMedia({audio: true},
        // Success callback
        function (stream) {
            source = audioCtx.createMediaStreamSource(stream);
            filter.type = 2; // band-pass filter.
            filter.frequency.value = 170; // 80-260 hz center 90
            filter.Q.value = 0.94;
            //source.connect(filter);
            source.connect(analyser);
            visualize();
        },
                // Error callback
                        function (err) {
                            console.log('The following gUM error occured: ' + err);
                        }
                );
            } else {
        console.log('getUserMedia not supported on your browser!');
    }
}

var I = 0;
var energy_threshold_pos=2*1e-8;
var energy_threshold_neg=0.5*1e-8;
var voiceTrend = 0;
var voiceTrendMax = 12;
var voiceTrendMin = -10;
var voiceTrendStart = 5;
var voiceTrendEnd = -5;
var signal;


function visualize() {
    analyser.fftSize = 2048;
    var bufferLength = analyser.fftSize;
    var dataArray = new Uint8Array(bufferLength);

    function draw() {

        drawVisual = requestAnimationFrame(draw);
        analyser.getByteTimeDomainData(dataArray);

        myVad = new vad(analyser);

        document.getElementsByName('Energy')[0].value = myVad.getEnergy();
        document.getElementsByName('EnergyB')[0].value = myVad.getEnergyB();
        document.getElementsByName('EnergyC')[0].value = myVad.getEnergyC();
        document.getElementsByName('EnergyD')[0].value = myVad.getEnergyD();
        document.getElementsByName('Frequency')[0].value = myVad.getFrequency();
        document.getElementsByName('SFM')[0].value = myVad.getSFM();
        document.getElementsByName('energyMonitor')[0].value = myVad.energyMonitor();
        document.getElementsByName('iterate')[0].value = myVad.iterate();

        if (myVad.energyMonitor() > 0) {
            document.getElementsByName('Energy1')[0].value = myVad.getEnergy();
            document.getElementsByName('EnergyB1')[0].value = myVad.getEnergyB();
            document.getElementsByName('EnergyC1')[0].value = myVad.getEnergyC();
            document.getElementsByName('EnergyD1')[0].value = myVad.getEnergyD();
            document.getElementsByName('Frequency1')[0].value = myVad.getFrequency();
            document.getElementsByName('SFM1')[0].value = myVad.getSFM();
            document.getElementsByName('energyMonitor1')[0].value = myVad.energyMonitor();
            document.getElementsByName('iterate1')[0].value = myVad.iterate();
        }
        
        if (myVad.energyMonitor() > 0) {
            I = I + myVad.energyMonitor();
        }
        
        signal = myVad.energyMonitor();
        if (signal > energy_threshold_pos) {
            voiceTrend = (voiceTrend + 1 > voiceTrendMax) ? voiceTrendMax : voiceTrend + 1;
        } else if (signal < -energy_threshold_neg) {
            voiceTrend = (voiceTrend - 1 < voiceTrendMin) ? voiceTrendMin : voiceTrend - 1;
        } else {
            // voiceTrend gets smaller
            if (voiceTrend > 0) {
                voiceTrend--;
            } else if (voiceTrend < 0) {
                voiceTrend++;
            }
        }

        var start = false, end = false;
        if (voiceTrend > voiceTrendStart) {
            // Start of speech detected
            document.getElementsByName('ww')[0].value = voiceTrend;

            start = true;
        } else if (voiceTrend < voiceTrendEnd) {
            // End of speech detected
            document.getElementsByName('ww')[0].value = 'voiceTrend';
            end = true;
        }


        document.getElementsByName('xx')[0].value = I;

    }
    ;
    window.setTimeout(draw(), 3000);
}