<%-- 
    Document   : processOnlineJob
    Created on : Dec 31, 2019, 2:44:26 PM
    Author     : nour.ihab
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Process Online Job</title>
    </head>
    <body>
        <h2 align="center">Welcome to the Process Online Job</h2>
        <br><br>
        <form method="post">
            Source File: <input type="text" name="sourceFile"><br><br>
            Destination File: <input type="text" name="destinationFile"><br><br>
            Max Speed: <input type="number" name="maxSpeed"><br><br>
            Job Name: <input type="text" name="jobName"><br><br>
            <input type="submit" value="Process">
        </form>
    </body>
</html>
