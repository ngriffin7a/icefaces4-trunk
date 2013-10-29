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

<html>
<head>JSP Logout Page</head>
<body>

<% session.invalidate(); %>

<p>This JSP page has invalidated the session. Using the back button and trying to interact with the page should
    give a ViewExpiredException and redirect to the error-page configured in the web-xml (viewExpired.xhtml).</p>

<a href="index.jsp">Go back to the start page.</a>

</body>
</html>