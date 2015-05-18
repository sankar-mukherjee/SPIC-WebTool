<%-- 
    Document   : admin
    Created on : 24 oct. 2014, 15:34:12
    Author     : mukherjee
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="admin.*" %>
<jsp:useBean id="admin" class="admin.Admini" scope="session"/>
<jsp:setProperty name="admin" property="*"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ADMIN</title>
        <script type="text/javascript">
            window.onload = function () {
                document.getElementById("cur_logs").scrollTop = document.getElementById("cur_logs").scrollHeight;
                document.getElementById("stor_logs").scrollTop = document.getElementById("stor_logs").scrollHeight;
            }
        </script>
    </head>
    <body>
        <table border="1" style="width: 100%; text-align: center; alignment-adjust: auto">            
            <tbody>                
              <tr>
                    <td colspan="3">                        
                        <h3>Control the Experiment</h3>
                         <!-- <a style="float: left">
                            <form action="administrator" method="post">
                                <input type="submit" name="start_server" value="Start Server" />
                            </form>
                        </a>
                        <a style="float: right">
                            <form action="administrator" method="post">
                                <input type="submit" name="stop_server" value="Stop Server" />
                            </form>
                        </a> -->
                    </td>
                </tr>
                <tr>
                    <td>Current chains<br>                          
                        <textarea name="cur_list" rows="5" cols="15" readonly="readonly">
                            <%out.print(admin.getCurrentChains());%>
                        </textarea>
                    </td>
                    <td>
                        <h3>File Upload:</h3>
                        Select a file to upload: <br />
                        <form action="UploadServlet" method="post" enctype="multipart/form-data">
                            <input type="file" name="file" size="50" /><br />
                            <input type="submit" value="Upload File" />
                        </form>
                    </td>
                    <td colspan="2">                        
                        <form action="administrator" method="post">
                            <input type="text" name="filedel" value="" />
                            <input type="submit" name="b2" value="Write file name for delete" />
                        </form>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">                        
                        <h3>Logs</h3>
                    </td>
                </tr>
                <tr>                    
                    <td>Current Logs<br>                
                        <textarea name="cur_list" rows="3" cols="30" readonly="readonly">
                            <%out.print(admin.getCurrentlogs());%>
                        </textarea>
                    </td>
                    <td>                        
                        <form action="administrator" method="post">
                            <a><input type="submit" name="log" value="Get log backup" /></a>
                        </form></br>                        
                        <form action="admin.jsp" method="post">
                            <input type="text" name="showlog" value="" />
                            <input type="submit" name="b2" value="Show Log" />
                        </form>
                    </td>
                    <td colspan="2">                        
                        <form action="administrator" method="post">
                            <a><input type="submit" name="clearAlllog" value="Clear All pervious Logs" /></a>
                        </form> <br/>
                        <form action="administrator" method="post">
                            <a><input type="submit" name="clearCurlog" value="Clear Current Log" /></a>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td colspan="">Stored Log<br>
                        <a style="word-spacing: 40px;">Trial Computer Start Stop Word </a>
                        <textarea name="store_logs" rows="8" cols="100" readonly="readonly" id="stor_logs" style="text-align: left">
                            <%
                                String s = request.getParameter("showlog");
                                if (s != null && s != "") {
                                    out.print(admin.showLogs(s));
                                } else {
                                    out.print("Nothing to show");
                                }
                            %>
                        </textarea>
                    </td>
                    <td colspan="2">Current Log<br>
                        <a style="word-spacing: 40px;">Trial Computer Start Stop Word </a>
                        <textarea name="cur_logs" rows="8" cols="100" readonly="readonly" id="cur_logs" style="text-align: left">
                            <%out.print(admin.showCurrentlogs());%>
                        </textarea>
                    </td>

                </tr>
                <tr>
                    <td colspan="3">                        
                        <h3>Connected Computers</h3>
                    </td>
                </tr>
                <tr>
                    <td>Current Connected Computers<br>             
                        <textarea name="cur_list" rows="5" cols="30" readonly="readonly">
                            <%out.print(admin.getConectedIP());%>
                        </textarea>
                        <input type="submit" value="Refresh" name="Refresh" onclick="window.location.reload();"/>
                    </td>
                    <td>                        
                        <form action="administrator" method="post">           
                            <a><input type="submit" name="ip" value="Clear"/></a>
                        </form>
                    </td>
                    <td colspan="2">                        
                        <form action="administrator" method="post">
                            <a><input type="submit" name="restart" value="Restart Whole Experiment" style="float: right;height: 50px"/></a>
                        </form>
                    </td>
                </tr>
                
                <tr>
                    <td colspan="3">                        
                        <h3>Self test</h3>
                    </td>
                </tr>
                <tr>
                    <td>Current chains<br>                          
                        <textarea name="cur_list" rows="5" cols="15" readonly="readonly">
                            <%out.print(admin.getSelfCurrentChains());%>
                        </textarea>
                    </td>
                    <td>
                        <h3>File Upload:</h3>
                        Select a file to upload: <br />
                        <form action="UploadServlet" method="post" enctype="multipart/form-data">
                            <input type="file" name="file" size="50" /><br />
                            <input type="submit" value="Upload File" />
                        </form>
                    </td>
                    <td colspan="2">                        
                        <form action="administrator" method="post">
                            <input type="text" name="filedelself" value="" />
                            <input type="submit" name="b2" value="Write file name for delete" />
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>
</body>
</html>
