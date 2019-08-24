<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%response.setStatus(200);%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>404 - 页面不存在</title>
  <link href="${contextPath }/styles/css/style.css" rel="stylesheet" />
  <%@ include file="/WEB-INF/views/com_head.jsp"%>
<style>
 .btn-index{
  background-color: #51ace1;
  border-color: #4d9ece;
  padding:5px 30px;
  font-family: "microsoft yahei", Arial, sans-serif;
  border-radius: 0px;
}
@media (max-width: 480px) {
  .btn-index{
    padding:3px 15px ;
  }
}

</style>
  
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
 

	<div style=" text-align: center;  margin-top:80px "><img src="${contextPath}/styles/img/404.jpg" class="img-responsive" style="display: inline;margin-top:5% " /><br/>
	  
	</div>
<%--      	<%
	HttpSession ss = request.getSession(); 
	Object objs = ss.getAttribute("shiroUser");
	if(obj != null){
		ShiroUser shiroUser = (ShiroUser)obj;
	if(shiroUser.getCompanyid() == null){ %> --%>
	
	<div><a href="<c:url value="/management/index"/>" class="btn btn-index" target="_top" style="color:#fff; margin-top: 15px;">返回平台</a></div>
<%-- 	<%}else{
			long cid=shiroUser.getCompanyid();	
		%>
	   
	<div><a href="<c:url value="/management/index?companyId=<%  shiroUser.getCompanyid();%>"/>" class="btn btn-index" target="_top" style="color:#fff; margin-top: 15px;">返回公司</a></div>   
	<%}}%> --%>

</div>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script text="javascript/text">
$("a").click(function(){
	var companyid=session.getCompanyid
	if(companyid!=null){
		var msg=session.getCompanyid
		$("c:url").set(value="/management/index?companyId="+companyid)
	}
})
</script>
</body>
</html>