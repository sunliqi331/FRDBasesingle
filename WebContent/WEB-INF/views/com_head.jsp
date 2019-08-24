<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.its.common.shiro.ShiroUser" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${contextPath}/styles/validationEngine/css/validationEngine.jquery.css" rel="stylesheet" type="text/css" media="screen"/>
<!--external css-->
<link href="${contextPath }/styles/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="${contextPath }/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link href="${contextPath }/styles/assets/bootstrap-table/bootstrap-table.css" rel="stylesheet" type="text/css" />
<link href="${contextPath }/styles/assets/sweetalert/sweetalert.css" rel="stylesheet" type="text/css" />
<!-- Custom styles for this template -->
<%
HttpSession s = request.getSession(); 
Object obj = s.getAttribute("shiroUser");
if(obj != null){
	ShiroUser shiroUser = (ShiroUser)obj;
if(shiroUser.getCompanyid() != null){ %>
<link href="${contextPath }/styles/css/style-mes.css" rel="stylesheet" />
<link href="${contextPath }/styles/css/style-responsive-mes.css" rel="stylesheet" />
<%}else{ %>
<link href="${contextPath }/styles/css/style.css" rel="stylesheet" />
<link href="${contextPath }/styles/css/style-responsive.css" rel="stylesheet" />
<%} }%>



<link href="${contextPath }/styles/img/favicon.ico" type="image/x-icon" rel="icon" />
<!-- For the use of the company's management page -->
<link rel="stylesheet" href="${contextPath }/styles/css/companypublic.css"/>
<!-- For the use of the registedlist page -->
<link href="${contextPath }/styles/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" />
<!-- datapicker css  -->
<link href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<!--[if IE]>
<link href="${contextPath}/styles/dwz/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
<![endif]-->
<!--[if lte IE 9]>
<script src="${contextPath}/styles/dwz/js/speedup.js" type="text/javascript"></script>
<![endif]-->
<script src="${contextPath}/js/jquery.min.js" type="text/javascript"></script>
