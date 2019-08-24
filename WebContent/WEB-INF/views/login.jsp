<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<title>Pactera - FRDBaseSystem</title>
<link href="${contextPath}/styles/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="${contextPath}/styles/css/reset.css" type="text/css">
<link href="${contextPath}/styles/css/index0.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css" />


<!-- form验证 -->
<link rel="stylesheet" href="${contextPath}/styles/validationEngine/css/validationEngine.jquery.css" type="text/css"/>
<script src="${contextPath}/js/jquery.js" type="text/javascript"></script>
<script src="${contextPath}/styles/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/styles/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>
<script>
    jQuery(document).ready(function(){
        jQuery("#formID").validationEngine();
    });
    jQuery(document).ready(function(){
      $("#captcha").click(function(){
        $(this).attr("src", "${contextPath}/Captcha.jpg?time=" + new Date());
        return false;
      });
      
    });

</script>

<%--  <script src="${contextPath}/js/login-scripts.js" type="text/javascript" charset="utf-8" ></script> --%>

</head>

<body style="background:#18293b">
  <div id="login">
  <!--头部-->
	  <div class="login_head clearfix">
	    <div class="container">
	      <div class="logo fl"><a href="${contextPath}/homepage/homepage"><img src="${contextPath}/styles/img/logo_login.png"  /></a><span>登录</span></div>
	      <div class="login_head_right fr"><a href="http://www.y-tran.com/register/infoFillPage">没有 FRD 帐号？立即注册 >></a></div>  
	    </div>
	  </div>
	<div class="login_center">
    <div class="container">
      <div class="login_bg1">
        <div class="login_title">登录</div>
        <form method="post" action="${contextPath}/login" id="formID">
           <input placeholder="用户名"   name="username" class="validate[required] login_input form-control input_text" id="username" value="${username }" style="border-radius: 0;"></input> 
           <input placeholder="密码" name="password" class="validate[] login_input form-control input_text" id="password" type="password" style="border-radius: 0;"></input>
           <!--         </div> -->
          <div class="captcha">           
              <input type="text" id="captcha_key"  name="captcha_key" class="login_input validate[maxSize[4]] input_text" size="6" style="width:40%; margin-right:5px"/>
              <span><img src="${contextPath}/Captcha.jpg" alt="点击刷新验证码" width="85" height="34" id="captcha"/></span>
            
          </div>
           <div class="longin_password clearfix" >
             <span class="fr"><a href="javascript:toggleBox('forgotPwd')">忘记密码？</a></span>
             <input type="checkbox" id="rememberMe" name="rememberMe" class="login_check fl" ></input>&nbsp;记住密码
           </div>
           <input type="submit"  class="login_button" value="登录"></input>
           <div class="login_reg"><a href="${contextPath}/register/infoFillPage" >没有 FRD 帐号？立即注册 >></a></div>
        </form>
      
      </div>
    
    <div class="copy">©2013 Pactera Technology International Ltd. All rights reserved.  京公网安备11010802016198号</div>
    </div>
  
  </div>

  </div>
  <script type="javascript">
    $(document).ready(function($){
	  if (navigator.userAgent.toLowerCase().indexOf("chrome") >= 0)
{
 var _interval = window.setInterval(function () {
 var autofills = $('input:-webkit-autofill');
 if (autofills.length > 0) {
  window.clearInterval(_interval); // 停止轮询
  autofills.each(function() {
  var clone = $(this).clone(true, true);
  $(this).after(clone).remove();
  });
 }
 }, 20);
}
	}
  
  </script>
</body>
</html>