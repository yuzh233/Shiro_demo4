<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: yu_zh
  Date: 2018/4/20
  Time: 21:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
list page

<br>
<shiro:hasRole name="user"><a href="${pageContext.request.contextPath}/user.jsp"> to user page</a></shiro:hasRole>
<br>
<shiro:hasRole name="admin"><a href="${pageContext.request.contextPath}/admin.jsp"> to admin page</a></shiro:hasRole>
<br>
<a href="${pageContext.request.contextPath}/user/roleAnnoation">test roleAnnotation</a>
<br>
<a href="${pageContext.request.contextPath}/user/logout">登出</a>
</body>
</html>
