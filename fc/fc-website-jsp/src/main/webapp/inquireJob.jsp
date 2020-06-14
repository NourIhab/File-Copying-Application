<%-- 
    Document   : inquireJob
    Created on : Dec 31, 2019, 2:43:31 PM
    Author     : nour.ihab
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
            }
            th {
                text-align: left;
            }
            table#t01 tr:nth-child(even) {
                background-color: #eee;
            }
            table#t01 tr:nth-child(odd) {
                background-color: #fff;
            }
            table#t01 th {
                background-color: black;
                color: white;
            }
            #myProgress {
                background-color: #ddd;
            }

            #myBar {
                height: 30px;
                background-color: #4CAF50;
                text-align: center;
                line-height: 30px;
                color: white;
            }

        </style>

        <title>Inquire Job</title>
        <script>
            function timedRefresh(timeoutPeriod) {

                setTimeout("location.reload(true);", timeoutPeriod);
            }

            window.onload = timedRefresh(10000);

        </script>

    </head>
    <body>
        <h1 align="center">Welcome to the Inquire Job page</h1>
        <br><br>
        <h3> Please Enter the Job Id</h3><br>
        <form action="inquireJob" method="post">
            Job ID: <input type="number" name="jobID" value=${it.Job_Id}><br><br>
            <input type="submit" value="Inquire">
        </form>
        <br><br><br>
        <c:if test="${it != null}">
            <div id="myProgress">
                <div id="myBar" style= "width: ${it.Copy_Percentatge}%">${it.Copy_Percentatge}%</div>
            </div>
            <br><br><br>
            <h1 align="center"> Welcome to the Response Page</h1><br><br>

            <table id="t01" align="center">
                <caption>Job Fields</caption>
                <tr>
                    <th>Job Fields</th>
                    <th>Job Values</th>          
                </tr>
                <tr>
                    <td>Import Method</td>
                    <td>${it.Import_Method}</td>
                </tr>
                <tr>
                    <td>Job Status</td>
                    <td>${it.Status}</td>
                </tr>
                <tr>
                    <td>Job Name</td>
                    <td>${it.Job_Name}</td>
                </tr>
                <tr>
                    <td>Source File</td>
                    <td>${it.Source_File}</td>
                </tr>
                <tr>
                    <td>Destination File</td>
                    <td>${it.Destniation_File}</td>
                </tr>
                <tr>
                    <td>Max Speed</td>
                    <td>${it.Max_Speed}</td>
                </tr>
                <tr>
                    <td>Current Coping Speed</td>
                    <td>${it.Current_Speed}</td>
                </tr>

                <tr>
                    <td>Copying Percentage</td>
                    <td>${it.Copy_Percentatge}</td>

                </tr>
                <</div>
            <tr>
                <td>Owner</td>
                <td>${it.Owner}</td>
            </tr>
            <tr>
                <td>File Size</td>
                <td>${it.File_Size}</td>
            </tr>
            <tr>
                <td>Fail Reason</td>
                <td>${it.Fail_Reason}</td>
            </tr>
            <tr>

                <td>Creation Date</td>
                <td>${it.Creation_Date}</td>
            </tr>
        </table>
    </c:if>

</body>
</html>
