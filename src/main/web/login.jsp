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
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        <p><input type="text" name="username"></p>
        <p><input type="text" name="password"></p>
        <p><input type="submit"></p>
    </form>
</body>
</html>
