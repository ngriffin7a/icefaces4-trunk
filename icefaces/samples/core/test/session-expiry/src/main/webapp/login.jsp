<%--
  ~ Copyright 2004-2013 ICEsoft Technologies Canada Corp.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the
  ~ License. You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an "AS
  ~ IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
  ~ express or implied. See the License for the specific language
  ~ governing permissions and limitations under the License.
  --%>

<%if ("POST".equals(request.getMethod())) {
        response.setContentType("text/xml");%>
<partial-response><error><error-name>class org.icefaces.application.SessionExpiredException</error-name><error-message>Session has expired</error-message></error></partial-response>
<%} else {%>
<head>
    <title>Login</title>
</head>
<body>
<div>enter the user name and password of any user specified as being in 'manager-gui' role in tomcat-users.xml</div>
<form method="POST" action="j_security_check">
    <table>
        <tbody>
        <tr>
            <th style="text-align: right;">user:</th>
            <td><input type="text" name="j_username"></td>
        </tr>
        <tr>
            <th style="text-align: right;">password:</th>
            <td><input type="password" name="j_password"></td>
        </tr>
        <tr>
            <td></td>
            <td style="float: right;"><input type="submit" value="Login"/></td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>
<%}%>
