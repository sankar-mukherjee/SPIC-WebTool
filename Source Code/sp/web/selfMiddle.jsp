<%
    String self_ip = request.getParameter("self_ip");
    String method = request.getParameter("method");

    if (method == null) {
        response.sendRedirect("selfauth.jsp");
    } else if (method.equals("Button")) {
        response.sendRedirect("selfButton.jsp?ip="+self_ip);
    } else if (method.equals("Voice")) {
        response.sendRedirect("selfVoice.jsp?ip="+self_ip);
    }
%>