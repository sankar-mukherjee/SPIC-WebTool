<%-- 
    Document   : selfauth
    Created on : Feb 9, 2015, 11:50:53 PM
    Author     : Sank
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="admin.*" %>
<jsp:useBean id="admin" class="admin.Admini" scope="session"/>
<jsp:setProperty name="admin" property="*"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/selfSpeechRecognition.js"></script>
        <script type="text/javascript" src="javascript/vad.js"></script>
        <title>Self Test</title>
    </head>
    <%

        String name = request.getParameter("ip");

        String sp;
        int t = 0;
        ArrayList<String> data = (ArrayList<String>) session.getAttribute("D");
        if (name != null && name != " ") {
            data = admin.dominnoSetup(name);
            sp = admin.getwords(data, name);
            session.setAttribute("IP", name);
            t = data.size();
        } else {

            System.out.println(data);
            data = admin.dominnoUpdate(data);
            sp = admin.getwords(data, (String) session.getAttribute("IP"));
            session.setAttribute("IP", (String) session.getAttribute("IP"));
            t = (Integer) session.getAttribute("t");
        }
        session.setAttribute("D", data);
        session.setAttribute("t", t);

        int c = t - data.size();
        String tmp[] = sp.split(",");

        if (tmp[1].equals("XXX")) {
            tmp[1] = "";
        }
        int y = 0;

        int x = (Math.random() < 0.5) ? 0 : 1;
        if (x == 0) {
            y = 1;
        }

        String nc = "";
        if (c == 1) {
            nc = "New Trial";
        } else {
            nc = "";
        }
    %>
    <body style="background-color: white">

        <progress style="position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;" value="<%=c%>" max="<%=t%>"></progress>
        <table border="1" style="width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;">            
            <tbody> 
                <tr><td>                        
                        <table border="0" align="center" style="width: 90%;text-align: center; font-size: xx-large">
                            <tbody>
                                <tr style="height: 100px;">
                                    <td><%out.print(tmp[x]);%></td>

                                </tr>
                                <tr style="height: 100px;">
                                    <td><%out.print(tmp[y]);%></td>
                                </tr>                                
                            </tbody>
                        </table>
                    </td></tr>
            </tbody>
        </table>
        <a href="selfauth.jsp">Restart</a>
    </body>

</html>
