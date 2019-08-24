<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
	response.setStatus(200);
//alertMsg.error("用户权限不足。");
%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>403 - 用户权限不足</title>
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
 

	<div style=" text-align: center;  margin-top:80px "><img src="${contextPath}/styles/img/403.jpg" class="img-responsive" style="display: inline;margin-top:5% " /><br/>
	  
	</div>
	<div><a href="<c:url value="/management/index"/>" class="btn btn-index" target="_top" style="color:#fff; margin-top: 15px;">返回首页</a></div>
</div>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>