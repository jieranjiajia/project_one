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
<title>paramContext.jsp</title>
</head>
<body>
<h2>EL隐藏对象pageContext案列</h2>
<!-- 通过el标签取出传的参数的值 -->
取得请求的参数字符串：${pageContext.request.queryString }<br/>
取得请求的URL：${pageContext.request.requestURL }<br>
服务的application名称：${pageContext.request.contextPath }<br>
HTTP请求的方式：${pageContext.request.method }<br>
用户的名称：${pageContext.request.remoteUser }<br>
用户的IP：${pageContext.request.remoteAddr }
${pageContext.request.authType }<br>
请求的编码：${pageContext.request.characterEncoding }<br>
contentLength：${pageContext.request.contentLength }<br>
contentType：${pageContext.request.contentType }<br>
sessionId：${pageContext.session.id }<br>
sessionValues：${pageContext.session.valueNames }<br>
主机端的服务信息：${pageContext.servletContext.serverInfo }<br>
<c:set var="request_test" value="request_test_value" scope="request"></c:set>
从request取出的数据：${requestScope.test_name }<br>
request的数据：${requestScope.request_test }<br>
session中的数据：${sessionScope.session_name }
</body>
</html>