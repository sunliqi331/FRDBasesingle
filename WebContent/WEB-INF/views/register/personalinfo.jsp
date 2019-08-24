<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- <base href="${contextPath}"> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<title>个人信息</title>
<script src="${contextPath}/js/jquery.js"></script>
<script type="text/javascript">
$("#document").ready(function(){
	if($("#status").val() == "enabled"){
//		alert();
		$("#sendEmail").hide();
	}
});
</script>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>个人信息</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body" >
           <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding-left:5px">
				
				<h6 class="panel-title" style=" border-left:3px solid #00c0ef;; padding-left:8px"  >个人基本信息</h6>
			  </div>
			  <div class="row person_content">
			    <div class="col-sm-2">
			    <c:choose>
			    <c:when test="${bool}">
				  <img src="${pic}" class="img-rounded img-responsive"/>
			    </c:when>
			    <c:otherwise>
			     <img src="${contextPath}/styles/img/avatar-mini.jpg" class="img-rounded img-responsive"/>
			    </c:otherwise>
			    </c:choose>
				   <br/>
				  <a target="dialog" rel="updatePic" refresh="true" mask="true" href="${contextPath }/register/updatePic" class="btn-info btn-rounded btn-person"><i class="fa fa-photo" style="font-size: 14px"></i>&nbsp;修改头像</a>			
				</div>
				<div class="col-sm-5 border-right border-left">
				  <dl>
				    <dd>用户名：<span class="unit">${user.username}</span></dd>
					<dd>手机号码：<span class="unit">${user.phone}</span></dd>
					<dd>真实姓名：<span class="unit">${user.realname}</span></dd>
					<dd>性别：<span class="unit">${user.sex}</span></dd>
					<dd>所在地区：<span class="unit">${user.province}${user.city}</span></dd>
					
				  </dl>
				</div>
				<div class="col-sm-5">
				  <dl>
				    <dd>创建时间：<span class="unit">${user.createTime}</span></dd>
				    <dd>邮箱：<span class="unit">${user.email}</span></dd>
					<dd>当前状态：<span class="unit">${user.status}</span></dd>
					<dd>当前角色：<span class="unit">${rolename}</span></dd>
					<dd><a target="dialog" rel="updateInfo" refresh="true" mask="true"  href="${contextPath }/register/updateInfo" class="btn-info btn-rounded btn-person"><i class="fa fa-info" style="font-size: 14px"></i>&nbsp;修改信息</a></dd>
				  </dl>
				</div>
			    
			  </div>
			  <div class="person_safe text-center ">
			    <div class="pull-left person_margin"> 您当前的账号安全程度 </div>
			    
				  <div class=" progress pull-left"  style="margin-top: 5px; margin-bottom: 5px" >
                    <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                  </div>
             
				<div class="pull-left person_margin" >安全级别：<span class="text-warning">低</span><span style=" margin-left:20px">建议修改密码</span></div>
			  </div>
			  <div class="person_password clearfix">
			    <div class="col-sm-2 text-center"><b>登录密码</b></div>
				<div class="col-sm-5 ">安全性高的密码可以使帐号更安全。建议您定期更换密码，设置一个包含字母，符号或数字中至少两项且长度超过6位的密码。</div>
			    <div class="col-sm-4 " style="margin-top: 5px"><a target="dialog" refresh="true" rel="updatePassword"  mask="true" href="${contextPath }/register/updatePassword" class="btn-info btn-rounded btn-person"><i class="fa fa-key" style="font-size: 14px"></i>&nbsp;修改密码</a></div>
			  </div>
			  <div class="person_password">
			    <div class="col-sm-2 text-center"><b>手机号码</b></div>
				<div class="col-sm-5 ">您已绑定了手机<span class="text-success">${phone} </span>[您的手机为安全手机，可以找回密码，能用于登录]</div>
			    <div class="col-sm-4 " style="margin-top: 5px"><a target="dialog" refresh="true" rel="updatePhone"  mask="true" href="${contextPath }/register/updatePhone" class="btn-info btn-rounded btn-person"><i class="fa fa-mobile" style="font-size: 14px"></i>&nbsp;修改手机</a></div>
			  </div>
			  
          </div>
        </div>
      </div>
    </div>
<!-- Modal -->
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="myself" />
<c:set var="ModuleTitle" value="myInfo" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>