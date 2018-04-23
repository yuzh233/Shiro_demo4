<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: yu_zh
  Date: 2018/4/22
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%-- ScendLoginRealm中放入认证信息的是字符串，所以不需要用对象访问 --%>
welcome!<shiro:principal></shiro:principal>
</body>
</html>
