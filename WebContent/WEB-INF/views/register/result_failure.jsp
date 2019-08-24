<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>注册失败</title>
<link rel="stylesheet"
	href="${contextPath}/styles/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${contextPath}/styles/css/bootstrapValidator.css" />
<link rel="stylesheet" href="${contextPath}/styles/css/index.css"
	type="text/css">
</head>
<body style="background:#18293b;">
	<div class="header">
	<div class="login_head clearfix">
		<div class="container">
			<div class="row">
				<div class="logo"> <img src="${contextPath }/styles/img/logo_login.png"> </div>
				<div class="login login_head_right">
					<ul class="clearfix">
						<li><a href="${contextPath }/index">返回首页</a></li>
						<li><a href="${contextPath }/login">立即登录</a></li>
					</ul>
				</div>
			</div>
		</div>
		</div>
	</div>
	<div class="container register-bg" style="margin-top: 10%">
		<div class="reviewinfo clearfix">
			<div class="pull-left">
				<span><img class="ok"
					src="${contextPath }/styles/img/error.png"
					style="width: 75%; height: 50%" /></span>
			</div>
			<div class="pull-left">
				<p class="reviewone">由于网络故障，注册失败！请重新注册！</p>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${contextPath}/js/jquery.js"></script>
	<script src="${contextPath}/js/jquery.stepy.js"></script>
	<script type="text/javascript" src="${contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript"
		src="${contextPath}/js/bootstrapValidator.js"></script>
</body>
</html>