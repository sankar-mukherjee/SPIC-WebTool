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
        <script type="text/javascript" src="../javascript/SpeechRecognition_1.js"></script>
        <script type="text/javascript" src="../javascript/vad.js"></script>
    </head>
    <body>

        <input type="text" name="ww" value="" style="width: 90%"/> 

        <input type="text" name="xx" value="" style="width: 90%"/> 
        <input type="text" name="onp" value="" style="width: 90%"/>        
        <table border="1">            
            <tbody> 
                <tr>
                    <td> <h1>Normal</h1>                
                        <table border="0" align="center" >
                            <tbody>
                                <tr>
                                    <td>Energy<textarea name="Energy" rows="5" cols="20">
                                        </textarea></td> 
                                    <td>EnergyB<textarea name="EnergyB" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                                <tr>
                                    <td>EnergyC<textarea name="EnergyC" rows="5" cols="20">
                                        </textarea></td>
                                    <td>EnergyD<textarea name="EnergyD" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                                <tr>
                                    <td>Frequency<textarea name="Frequency" rows="5" cols="20">
                                        </textarea></td>
                                    <td>SFM<textarea name="SFM" rows="5" cols="20">
                                        </textarea></td>
                                </tr> 
                                <tr>
                                    <td>energyMonitor<textarea name="energyMonitor" rows="5" cols="20">
                                        </textarea></td>
                                    <td>iterate<textarea name="iterate" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>

                    <td>    <h1>Speech</h1>                   
                        <table border="0" align="center">
                            <tbody>
                                <tr>
                                    <td>Energy<textarea name="Energy1" rows="5" cols="20">
                                        </textarea></td> 
                                    <td>EnergyB<textarea name="EnergyB1" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                                <tr>
                                    <td>EnergyC<textarea name="EnergyC1" rows="5" cols="20">
                                        </textarea></td>
                                    <td>EnergyD<textarea name="EnergyD1" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                                <tr>
                                    <td>Frequency<textarea name="Frequency1" rows="5" cols="20">
                                        </textarea></td>
                                    <td>SFM<textarea name="SFM1" rows="5" cols="20">
                                        </textarea></td>
                                </tr> 
                                <tr>
                                    <td>energyMonitor<textarea name="energyMonitor1" rows="5" cols="20">
                                        </textarea></td>
                                    <td>iterate<textarea name="iterate1" rows="5" cols="20">
                                        </textarea></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</html>
