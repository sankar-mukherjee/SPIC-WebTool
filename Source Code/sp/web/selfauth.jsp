<%-- 
    Document   : selfauth
    Created on : Feb 9, 2015, 11:50:53 PM
    Author     : Sank
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Self Authorization</title>
    </head>
    <body>
        <form action="selfMiddle.jsp" method="get" >
            <table border="1" style="width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%; text-align: center">           
                <tbody>
                    <tr>
                        <td>
                            <h1>Select and submit</h1>  

                            <table border="0" width="95%" style="text-align: center">
                                <tbody>
                                    <tr>
                                        <td>
                                            <input type="radio" name="method" value="Button" />Button
                                            <input type="radio" name="method" value="Voice" />Voice Detection
                                            <input type="hidden" name="self_ip" value="<%out.print(request.getRemoteAddr());%>" />
                                        </td>
                                    </tr>                                    
                                </tbody>
                            </table>

                        </td>
                    </tr>
                    <tr>                                
                        <td><input type="submit" value="Submit" name="submit" /></td>
                    </tr>
                </tbody>
            </table>
        </form>

    </body>
</html>
