<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
      + path + "/";
%>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>注册用户</title>
<%--   <script src="${contextPath}/js/jquery.js"></script> --%>
  
<%--   <link href="${contextPath }/styles/css/style.css" rel="stylesheet" /> --%>
  <%@ include file="/WEB-INF/views/com_head.jsp"%>
  <link rel="stylesheet" href="${contextPath}/styles/css/index.css" type="text/css">
  
</head>
<script>
jQuery(document).ready(function(){
  jQuery("#formID").validationEngine();
    
//   $("#username").keyup(function(){
// 		$("#divOfUsername").find("div.parentFormformID").remove();
// 	  if($("#username").val()!=""&&$.trim($("#username").val()) != ''){
// 		  ajaxTodo("${contextPath}/register/checkUsername/" + $("#username").val(), function(data) {
// 			    $.each(data, function(idx, item) {
// 			      if (item != 1) {
// 			        $("#divOfUsername").find("div.parentFormformID").remove();
// 			        $("#username").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 该用户已注册<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
// 			        $("#register").attr("disabled", "true");
// 			        } else {
// 			          $("#divOfUsername").find("div.parentFormformID").remove();
// 			          $("#register").removeAttr("disabled");
// 			          }
// 			      });
// 			    });
// 	    }
// 	  });
  $("#phone").keyup(function(){
	  $("#divOfPhone").find("div.parentFormformID").remove();
	  if($("#phone").val().length == 11){
		  ajaxTodo("${contextPath}/register/checkPhone/"+$("#phone").val(), function(data) {
			    $.each(data, function(idx, item) {
			      if (item != 1) {
			    	  if(item == 2){
			    		  $("#divOfPhone").find("div.parentFormformID").remove();
					        $("#phone").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 手机号不正确<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
					        $("#send").attr("disabled", "true");
 					        $("#register").attr("disabled", "true");
			    	  }else{
					        $("#divOfPhone").find("div.parentFormformID").remove();
					        $("#phone").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 该手机号已注册<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
					        $("#send").attr("disabled", "true");
 					        $("#register").attr("disabled", "true");
			    	  }
			        } else {
			          $("#divOfPhone").find("div.parentFormformID").remove();
			          if(countdown == 60){
			          $("#send").removeAttr("disabled");
			          }
			          $("#register").removeAttr("disabled");
			          }
			      });
			    });
	    }else{
	    	$("#send").attr("disabled", "true");
	    }
  });
  $("#send").click(function(){
	 if($("#phone").val().length == 11) {
		 if(isNaN($("#phone").val())){
			 return;
		 }else{
	  			ajaxTodo("${contextPath}/register/sendMessage/" + $("#phone").val(),function(){
				  });
		 }
	 }
  });
//    $("#code").keyup(function(){
// 	   $("#divOfCode").find("div.parentFormformID").remove();
// 	  if($(this).val() != ""&&$.trim($(this).val()) != '') {
// 		  ajaxTodo("${contextPath}/register/checkCode/"+$("#code").val(), function(data) {
// 			    $.each(data, function(idx, item) {
// 			      if (item != 1) {
// 			        $("#divOfCode").find("div.parentFormformID").remove();
// 			        $("#code").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 验证码不正确<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
// 			        $("#register").attr("disabled", "true");
// 			        } else {
// 			          $("#divOfCode").find("div.parentFormformID").remove();
// 			          $("#register").removeAttr("disabled");
// 			          }
// 			      });
// 			    });
// 	  }
//   }); 
  }); 
</script>
<script type="text/javascript"> 
	var countdown = 60;
	function settime(val) {
		if($("#phone").val().length != 11 && countdown == 60){
			return;
		}
		if(isNaN($("#phone").val())){
			 return;
		 }else{
		if (countdown == 0) {
			val.removeAttribute("disabled");
			val.value = "发送验证码";
			countdown = 60;
			return;
			
		} else {
			val.setAttribute("disabled", true);
			val.value = "重新发送(" + countdown + ")";
			countdown--;
		}
		 }
		setTimeout(function() {
			settime(val)
		}, 1000)
	}
</script>
<body style="background: #18293b;width:100%; text-align:center\0/;">

  <div class="content">   
	 <div class="login_head clearfix">
	   <div class="container" style="padding-top: 0px;">
	     <div class="logo"><a href="${contextPath}/index"><div class="logo_img"></div></a></div>
	     <div class="login_head_right"><a href="${contextPath}/login">已有 FRD 帐号？立即登录 >></a></div>  
	   </div>
	 </div>  
  <div class="container ">
    <div class="register-bg">
	    <form method="post" action="${contextPath }/register/success" id="formID" name="registForm" class="form-horizontal" enctype="multipart/form-data" onsubmit="return validateCallback(this, callback, confirmMsg)"  >
	    <div class=" account-set">
	        <div class="form-group">
	          <div class="reg_title title1"><div class="fl">账户设置</div><span>请设置你的会员名和密码用于登录</span></div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户名</label>
	          <div id="divOfUsername" class="col-sm-6">
	            <input id="username" name="username" type="text" class="form-control input-medium input_text validate[custom[onlyNumAndLet],required]" maxlength="25" 
	            onKeyPress="if (event.keyCode ==32) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>设置登录密码</label>
	          <div id="divOfPw1" class="col-sm-6">
	            <input name="password" type="password" id="pw1" class="form-control input-medium input_text validate[required,minSize[6],maxSize[16]]" maxlength="16" 
	            onKeyPress="if (event.keyCode ==32) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>重复输入密码</label>
	          <div id="divOfPw2" class="col-sm-6">
	            <input name="passwordagain" type="password" id="pw2" class="form-control input-medium input_text validate[required,equals[pw1]]" maxlength="16" 
	            onKeyPress="if (event.keyCode ==32) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>手机号码</label>
	          <div id="divOfPhone" class="col-sm-6">
	            <input name="phone" id="phone" type="tel" class="form-control input-medium input_text validate[required,maxSize[11],minSize[11]]" maxlength="11"
	            onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>手机验证码</label>
	          <div id="divOfCode" class="col-sm-4">
	            <input type="text" id="code" name="code" class="form-control input-medium input_text validate[required]" maxlength="6"
	            onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
	          </div>
              <input type="button" class="btn btn-default" id="send" value="发送验证码" disabled="disabled" onclick="settime(this)"  style="color:#333"/>
	        </div>
	        
	<!--         <div class="form-group">
	        <span id="worning"> </span>
	        </div> -->
	        </div>
	        <div class="basic-info">
	        <div class="form-group">
	          <div class="reg_title"><div class="fl">基本信息</div><span>请输入您真实的个人信息</span></div>
<!-- 	          <label class="control-label col-sm-r">基本信息</label> -->
<!-- 	          <label class="control-label col-sm-l" >请输入您真实的个人信息</label> -->
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>邮箱</label>
	          <div id="divOfEmail" class="col-sm-6">
	            <input type="text" id="email" name="email" class="form-control input-medium validate[custom[email],maxSize[128],required] required input_text" 
	            onKeyPress="if (event.keyCode ==32) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4">真实姓名</label>
	          <div class="col-sm-6">
	            <input name="realname" id="realname" type="text" class="form-control input-medium input_text validate[]" maxlength="16"
	            onKeyPress="if (event.keyCode ==32) event.returnValue=false"/>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4">性别</label>
	          <div class="col-sm-6" style="margin-top:7px">
	            <input id="sex" name="sex" class="validate[]" type="radio" value="男"/>&nbsp;男&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
	            <input id="sex" name="sex" class="validate[]" type="radio" value="女"/>&nbsp;女
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>所在地(省)</label>
	          <div class="col-sm-6">
	            <select name="province" id="province" class="form-control input-medium input_text "/></select>
	          </div>
	        </div>
	        <div class="form-group">
	          <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>所在地(市)</label>
	          <div class="col-sm-6">
	            <select name="city" id="city"  class="form-control input-medium input_text "/></select>
	          </div>
	        </div>
	<!--         <div class="form-group"> -->
	<!--           <label class="control-label col-sm-4">所在地(县):<span class="require">&nbsp&nbsp*</span></label> -->
	<!--           <div class="col-sm-5"> -->
	<!--             <select name="location_a" id="location_a" class="form-control input-medium"/></select> -->
	<!--           </div> -->
	<!--         </div> -->
	        <script type="text/javascript" src="${contextPath}/js/region_select.js"></script>
	        <script type="text/javascript">
	          new PCAS('province', 'city', '', '');
	        </script> 
	        <div class="form-group">
	          <label class="control-label col-sm-4"></label>
	          <div id="divOfCheck" class="col-sm-6">
	            <input id="read" name="read" type="checkbox" class="validate[required]" value="yes" style="float:left;"/><span style="color: #767575; font-size:12px; line-height:20px ">&nbsp;<a href="${contextPath}/index/read" target="_blank">已阅读《无锡悦创服务协议》</a></span>
	          </div>
	        </div>
	        <div class="form-group">
	            <div class="col-sm-6 col-sm-offset-4">
	              <input type="button" id="register" class="btn reg_button" value="确定" ></input>
	            </div>
	        </div>
	        </div>
	    </form>
    </div>
  </div>
        <div class="footer">
            <p style="color:#bebdbd; padding:0 15px">Copyright &copy 2016 无锡悦创智能科技有限公司 苏ICP备17038704号</p>
        </div>
</div>
<div class="clear"></div>
 <div class="foot_menu_fixed">
     <ul>
	   <li><a href="${contextPath}/index" class="first"><i class="fa fa-home"></i>首页</a></li>
	   <li><a href="${contextPath}/index/about"><i class="fa fa-list"></i>关于我们</a></li>
	   <li><a href="${contextPath}/index/showProduct"><i class="fa fa-cog"></i>产品中心</a></li>
	   <li><a href="${contextPath}/index/contact"><i class="fa fa-phone"></i>联系我们</a></li>
	 </ul>
   
   </div>
<c:set var="ParentTitle" value="Company" />
<c:set var="ModuleTitle" value="CompanyRegister" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>

</body>
</html>
<script type="text/javascript">

var submitStatus1 = new Array();
var submitStatus2 = new Array();
var submitStatus3 = new Array();
function checkValue(){
	if($("#username").val()!=""&&$.trim($("#username").val()) != ''){
    	 ajaxTodo("${contextPath}/register/checkUsername/"+$("#username").val(), function(data) {
            checkData(data,$("#username"),"用户名不可重复",$("#divOfUsername"),$("#formID"),submitStatus1,"username");
        });
      }
  };
function checkValue1(){
	if($("#username").val()!=""&&$.trim($("#username").val()) != ''){
    	 ajaxTodo("${contextPath}/register/checkUsername/"+$("#username").val(), function(data) {
            checkData1(data,$("#username"),"用户名不可重复",$("#divOfUsername"),$("#formID"));
        });
      }
  };
 function checkValue2(){
	if($("#email").val()!=""&&$.trim($("#email").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkEmail?email="+$("#email").val(), function(data) {
            checkData(data,$("#email"),"邮箱不可重复",$("#divOfEmail"),$("#formID"),submitStatus2,"email");
        });
      }
  };
function checkValue3(){
	if($("#email").val()!=""&&$.trim($("#email").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkEmail?email="+$("#email").val(), function(data) {
            checkData1(data,$("#email"),"邮箱不可重复",$("#divOfEmail"),$("#formID"));
        });
      }
  }; 
 function checkValue4(){
	if($("#code").val()!=""&&$.trim($("#code").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkCode/"+$("#code").val(), function(data) {
            checkData(data,$("#code"),"验证码不正确",$("#divOfCode"),$("#formID"),submitStatus3,"code");
        });
      }
  };
function checkValue5(){
	if($("#code").val()!=""&&$.trim($("#code").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkCode/"+$("#code").val(), function(data) {
            checkData1(data,$("#code"),"验证码不正确",$("#divOfCode"),$("#formID"));
        });
      }
  }; 
  $("#username").blur(checkValue);
  $("#email").blur(checkValue2);
  $("#code").blur(checkValue4);

$("#register").click(function(){
	$("#divOfUsername").find("div.parentFormformID").remove();
	$("#divOfPw1").find("div.parentFormformID").remove();
	$("#divOfPw2").find("div.parentFormformID").remove();
	$("#divOfPhone").find("div.parentFormformID").remove();
	$("#divOfEmail").find("div.parentFormformID").remove();
	$("#divOfCode").find("div.parentFormformID").remove();
	$("#divOfCheck").find("div.parentFormformID").remove();
var username = $("#username").val();
var password = $("#pw1").val();
var password2 = $("#pw2").val();
var phone = $("#phone").val();
var email = $("#email").val();
var realname = $("#realname").val();
var sex = $('input:radio:checked').val();
var province = $("#province").val();
var city = $("#city").val();
var code = $("#code").val();
var chk = document.getElementById("read");
if(username == ""){
     $("#username").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(password == ""){
     $("#pw1").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(password2 == ""){
     $("#pw2").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(phone == ""){
     $("#phone").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(email == ""){
     $("#email").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(code == ""){
     $("#code").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(chk.checked == false){
     $("#read").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 请阅读后勾选此处</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
}
if(username != ""&& password != ""&& phone !="" && email !="" && code != "" && chk.checked == true){
	if(submitStatus1.length>0){
    		checkValue1();
 }else if(submitStatus2.length>0){
	    	checkValue3();
 }else if(submitStatus3.length>0){
	 		checkValue5();
 }else{
	add_submit();
	$.ajax({
		 contentType : "application/json;charset=utf-8",
		   type : 'GET',
		   url : "${contextPath }/register/register",
// 		   data : "username="+username+"&password="+password+"&phone="+phone+"&email="+email+"&realname="+realname+"&sex="+sex+"&province="+province+"&city="+city+"",
 		   data : {"username":username,"password":password,"phone":phone,"email":email,"realname":realname,"sex":sex,"province":province,"city":city},
		   dataType : "json",
		   success : function(data) {
			   $.each(data, function(idx, item) {
				    if (item == true){
				    	remove_submit();
				    	document.getElementById("formID").reset();
				    	window.location.href = "${contextPath }/register/success";
				    }
				    if(item == false){
				    	remove_submit();
				    	swal("注册失败","请检查填写的信息","error");
				    }
			   });
		   },
		   error : function(data) {
			   remove_submit();
			   document.getElementById("formID").reset();
			   window.location.href = "${contextPath }/register/error";
			   },
	});
 }
}
});

//提交注册信息后显示加载中的图片
function add_submit(){
	$("body").addClass("Submit_on").prepend("<div class='submit_body'><img src='${contextPath}/js/submit.gif'/></div>"); 
 }
 function remove_submit(){
	 $("body").removeClass("Submit_on"); 
 }
</script>