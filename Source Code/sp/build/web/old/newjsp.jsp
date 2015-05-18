<%-- 
    Document   : refresh
    Created on : 15 oct. 2014, 17:17:47
    Author     : mukherjee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Normal Page</title>    
        <script type="text/javascript">

            var I = [];
            navigator.getUserMedia = (navigator.getUserMedia ||
                    navigator.webkitGetUserMedia ||
                    navigator.mozGetUserMedia ||
                    navigator.msGetUserMedia);

            var audioCtx = new (window.AudioContext || window.webkitAudioContext)();
            var voiceSelect = document.getElementById("voice");
            var source;
            var stream;

            var analyser = audioCtx.createAnalyser();
            //analyser.minDecibels = -90;
            //analyser.maxDecibels = -10;
            //analyser.smoothingTimeConstant = 0.85;
            var filter = audioCtx.createBiquadFilter();

            if (navigator.getUserMedia) {
                console.log('getUserMedia supported.');
                navigator.getUserMedia({audio: true},
                // Success callback
                function (stream) {
                    source = audioCtx.createMediaStreamSource(stream);
                    filter.type = 2; // band-pass filter.
                    filter.frequency.value = 170; // 80-260 hz center 90
                    filter.Q.value = 0.94;
                    source.connect(filter);
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

            function visualize() {
                analyser.fftSize = 1024;
                var bufferLength = analyser.fftSize;
                var dataArray = new Uint8Array(bufferLength);


                var d = 'S';
                function draw() {
                    drawVisual = requestAnimationFrame(draw);
                    analyser.getByteTimeDomainData(dataArray);

                    for (var i = 1; i < bufferLength; i++) {
                        I[i] = dataArray[i] + '\t';
                    }
                    document.getElementsByName('all')[0].value = I;
                    var diff;
                    var dataArray1 = Math.abs(dataArray);

                    for (var i = 1; i < bufferLength; i++) {
                        diff = dataArray1[i] - dataArray1[i - 1];
                        if (diff < 0) {
                            document.getElementsByName('onp')[0].value = d;
                            d = 'P';
                            break;
                        } else {
                            d = 'X';
                        }
                    }

                }
                ;
                window.setTimeout(draw(), 5000);
            }


        </script>
    </head>
    <body>


        <input type="text" name="rms" value="" style="width: 90%"/> 
        <input type="text" name="onp" value="" style="width: 90%"/>        
        <table border="1" style="width: 500px;height: 300px; position:fixed; margin-left:-50px; margin-top:-50px; top:20%;left:5%;">            
            <tbody> 
                <tr><td>                        
                        <table border="0" align="center" style="width: 90%;text-align: center; font-size: xx-large">
                            <tbody>
                                <tr style="height: 100px;">
                                    <td><textarea name="all" rows="45" cols="210">
                                        </textarea></td>                            
                                </tr>
                            </tbody>
                        </table>
                    </td></tr>
            </tbody>
        </table>
    </body>
</html>
