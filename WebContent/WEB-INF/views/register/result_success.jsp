<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>注册成功</title>
<link rel="stylesheet"
	href="${contextPath}/styles/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${contextPath}/styles/css/bootstrapValidator.css" />
<link rel="stylesheet" href="${contextPath}/styles/css/index.css"
	type="text/css">
</head>
<body style="background:#18293b ! important">
	<div class="header">
	<div class="login_head clearfix">
		<div class="container">
			<div class="row">
				<div style="float: left;"> <img src="${contextPath }/styles/img/ytran-logo-login.png"> </div>
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
				<span><img class="ok" src="${contextPath }/styles/img/round_ok.png"
					style="width: 75%; height: 50%" /></span>
			</div>
			<div class="pull-left">
				<p class="reviewone">恭喜您已注册成功！<input type="text" id="getting" style="border:0px;"></p>
                <p class="reviewone"><a href="${contextPath}/login" style="font-size:18px;text-decoration:underline;">如果画面没有跳转，请点此链接</a></p>
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
<script type="text/javascript">
$(function(){
	var count = 6;
	var btn = $("#getting");
	setInterval(function(){
        count--;  
        if (count > 0){  
        	btn.val(count+'秒后跳转到登录页面');
        }else {  
            window.location.href = "${contextPath }/managemanet/index";  
        }  
    }, 1000);
});
</script>