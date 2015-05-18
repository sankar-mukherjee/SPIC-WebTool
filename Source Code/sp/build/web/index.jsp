<%@ page import="java.io.*, java.net.*" %>
<% response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1 
    response.setHeader("Pragma", "no-cache"); //HTTP 1.0 
    response.setDateHeader("Expires", 0); //prevents caching at the proxy server  
%>
<HTML>
<%
    try {
        int character;
        Socket socket = new Socket("127.0.0.1", 8765);

        OutputStream outSocket = socket.getOutputStream();
        BufferedReader inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //InputStream inSocket = socket.getInputStream();
        String str = request.getRemoteAddr() + "\n";
        byte buffer[] = str.getBytes();
        outSocket.write(buffer);

        while ((character = inSocket.read()) != -1) {
            out.print((char) character);
        }

        socket.close();
    } catch (java.net.ConnectException e) {
%>
<h1 style="alignment-adjust: auto">Server is not running</h1>
<%
    }
%>

</HTML>