<%-- 
    Document   : index
    Created on : 14 oct. 2014, 15:25:20
    Author     : mukherjee
--%>

<%@page import="org.apache.tomcat.util.codec.binary.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            String tt = (String) session.getAttribute("fjyhyfj");
            if(tt!=null && tt.equals("1")){
                session.removeAttribute("fjyhyfj");
                response.setIntHeader("Refresh",1);                
            }

            ArrayList<String> sp1 = (ArrayList<String>) session.getAttribute("sp1");
            ArrayList<String> sp2 = (ArrayList<String>) session.getAttribute("sp2");

            //String client1 = request.getRemoteAddr();
            String ip1 = (String) session.getAttribute("ip1");
            String ip2 = (String) session.getAttribute("ip2");
            String client = request.getRemoteAddr();
            out.print(client);
            boolean B1 = false;
            boolean B2 = false;

        %>
        <div>
            <table border="1"  cellspacing="1" cellpadding="10" align="center" style="width: 90%;height: 300px">
                <thead>
                    <tr>
                        <th>Choose Words</th>
                    </tr>
                </thead>
                <form name="frm" method="get" action="refresh.jsp">
                    <tbody>
                        <%                                if (client.equals("147.94.197.76")) {
                                out.print("<tr><td>" + sp1.get(0) + "</td></tr>");
                                if (!sp1.get(0).equals("")) {
                                    out.print("<tr><td><input type='submit' name='submit' value='Submit'></td></tr>");
                                } else {
                                    out.print("<tr><td> Wait for other person </td></tr>");
                                }
                            } else if (client.equals("0:0:0:0:0:0:0:1")) {
                                out.print("<tr><td>" + sp2.get(0) + "</td></tr>");
                                if (!sp2.get(0).equals("")) {
                                    out.print("<tr><td><input type='submit' name='submit' value='Submit'></td></tr>");
                                } else {
                                    out.print("<tr><td> Wait for other person </td></tr>");
                                }
                            }
                            sp1.remove(0);
                            sp2.remove(0);
                            session.setAttribute("sp1", sp1);
                            session.setAttribute("sp2", sp2);
                            session.setAttribute("ip1", ip1);
                            session.setAttribute("ip2", ip2);
                            // session.setAttribute("i", i);
                            //response.setIntHeader("Refresh", 3);
                        %>
                    </tbody>
                </form>
            </table>
        </div>
    </body>
</html>
