<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 利用jstl标签格式化请求的参数
	等同于传统的做法：request.setCharecterEncoding("UTF-8");
 -->
<fmt:requestEncoding value="UTF-8"/>
<title>param.jsp</title>
</head>
<body>
<h2>EL表达式隐藏对象param 和 paramValues的案列</h2>
<!-- 通过el标签取出传的参数的值 -->
姓名：${param.username }<br>
密码：${param.password }<br>
性别：${param.sex }<br>
年龄：${param.old }<br>
爱好：<c:forEach var="habit" items="${paramValues.habit}">
		${habit}
	</c:forEach>
<br>
<c:set var="test_name" value="test_value" scope="request"></c:set>
<c:set var="session_name" value="session_value" scope="session"></c:set>
<a href="pageContext.jsp?name=abc&password=46546">连接pageContext.jsp</a>
</body>
</html>