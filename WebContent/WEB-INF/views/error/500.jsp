<%@page import="java.io.StringWriter"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.its.common.utils.SecurityUtils"%>

<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%response.setStatus(200);%>

<%
	String errorMsg = null;
	Throwable ex = null;
	if (request.getAttribute("javax.servlet.error.exception") != null) {
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	} else {
		ex = exception;
	}

	//记录日志
	if (ex != null) {
		Logger logger = LoggerFactory.getLogger("500.jsp");
		
		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		errorMsg = stringWriter.toString();
		
		logger.error(errorMsg);
	}

%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>500 - 系统内部错误</title>
	<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>

<body style="background: #fff; text-align: center;">
<%@ include file="/WEB-INF/views/include.header.jsp"%>
<div id="sidebar"  class="nav-collapse" style="background: transparent;box-shadow: none;">
  <!--logo start-->
  <div class="sidebar-header t-center">
    <img class="text-logo" src="${contextPath}/styles/img/ytran-logo-login.png" width="220" alt="" >
  </div>
  </div>
<div class="container">
 

	<div style=" text-align: center;  margin-top:80px "><img src="${contextPath}/styles/img/500.jpg" class="img-responsive" style="display: inline;margin-top:5% " /><br/>
	  
	</div>
	<div><a href="<c:url value="/management/index"/>" class="btn btn-index" target="_top" style="color:#fff; margin-top: 15px;">返回首页</a></div>
</div>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>
