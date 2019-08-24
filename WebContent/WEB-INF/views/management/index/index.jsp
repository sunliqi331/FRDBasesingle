<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>FRDBase PDM System</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/css/AdminLTE.css" rel="stylesheet" type="text/css" />

</head>
<body >
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content container">
    <div class="main-body" style="overflow:auto; padding-bottom:0;">
      <div class="row">
        <!--userinfo start-->
        <div class="col-lg-12" >
          <!-- Widget: user widget style 1 -->
          <div class="box box-widget widget-user personnel" style=" border:1px solid #efeeee; ">
            <!-- Add the bg color to the header using any of the bg-* classes -->
                <%
        HttpSession ss = request.getSession(); 
        Object objs = ss.getAttribute("shiroUser");
        if(obj != null){
            ShiroUser shiroUser = (ShiroUser)obj;
        if(shiroUser.getCompanyid() == null){ %>
            <div class="widget-user-header " style="background: #f0f0f0;" >
            <div class="widget-user-desc" style=" font-size:16px;color:#666;line-height: 36px; "><i class="fa fa-info" style="color:#51ace1"></i> 平台管理员信息</div>
            <%}else{ %>
            <div class="widget-user-header "  style="background: #f0f0f0;">
             <div class="widget-user-desc" style=" font-size:16px;color:#666;line-height: 36px; "><i class="fa fa-info" style="color:#51ace1"></i> PDM SYSTEM管理员信息</div>
              <%}}%>
              
            </div>
<!--             <div class="widget-user-image"> -->
<%--             <c:choose> --%>
<%--               <c:when test="${bool}"> --%>
<%-- 				  <img src="${pic}" class="img-circle"/> --%>
<%-- 			    </c:when> --%>
<%-- 			    <c:otherwise> --%>
<%-- 			     <img src="${contextPath}/styles/img/avatar-mini.jpg" class="img-circle"/> --%>
<%-- 			    </c:otherwise> --%>
<%-- 			</c:choose> --%>
<%-- <%-- 			<span class="user_name">&nbsp;${login_user.realname }</span> --%> 
<!-- <!-- 			<br> -->
<%-- <%--             <a href="${contextPath }/register/updatePic" class="btn-info btn-rounded btn-person"><i class="fa fa-photo" style="font-size: 14px"></i>&nbsp;修改头像</a> --%> 
<!--             </div> -->
<!--             <br> -->
<!--             <br> -->
<%--             <h3 class="text-center" style="margin-top: 40px;"><span class="user_name">${login_user.realname }</span></h3> --%>
            <div class="box-footer" style=" border-top:none; padding-top: 15px">
              <div class="row">
                <div class="col-sm-4 border-right">
                   <dl> 
                      <dd>登录名称：<span class="unit" style="word-wrap: break-word;">${login_user.username }</span></dd>
                    </dl>
                    <dl>
                      <dd>真实名字：<span class="unit">${login_user.realname }</span></dd>
                    </dl>
                    <dl>
                      <dd>手机号码：<span class="unit">${login_user.phone }</span></dd>
                    </dl>
                   
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-4 border-right">
                	<dl>
                      <dd>E-Mail：<span class="unit" style="word-wrap: break-word;">${login_user.email }</span></dd>
                    </dl>
                 	<dl>
                      <dd>可用状态：<span class="unit">${(login_user.status == "enabled")? "可用":"不可用"}</span></dd>
                    </dl>
                    <c:if test="${position == null }">
                	<dl class="index_dl">
                      <dd>&nbsp;</dd>
                    </dl>
                    </c:if>
                    <c:if test="${position != null }">
                	<dl>
                      <dd>职位：<span class="unit">${position}</span></dd>
                    </dl>
                    </c:if>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-4">
                 	<dl>
                      <dd>创建时间：<span class="unit"><fmt:formatDate value="${login_user.createTime}" pattern="yyyy-MM-dd"/></span></dd>
                    </dl>
                    <dl >
                      <dd >上次登录时间：<span class="unit">${last_login_time }</span></dd>
                    </dl>
                  <a    href="${contextPath }/register/personalinfo" class="btn-rounded btn-person"><i class="fa fa-eye" style="font-size: 14px"></i>&nbsp;&nbsp;查看个人信息</a>
                  <!-- /.description-block -->
                </div>
                </div>
                </div>
                </div>
                </div>
                </div>

	

          	<%
// 		HttpSession ss = request.getSession(); 
// 		Object objs = ss.getAttribute("shiroUser");
		if(obj != null){
			ShiroUser shiroUser = (ShiroUser)obj;
		if(shiroUser.getCompanyid() == null){ %>
      <div class="col-lg-12" style="padding:0">
        <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
          <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
            
            <h6 class="panel-title"  style=" border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;" >总资产 </h6>
          </div>
          
          <div class="panel-body" style="padding-bottom:0;">
          <div class="row" >
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-aqua">
            <div class="inner">
              <h3>${tcp}</h3>

              <p>公司总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-diamond"></i>
            </div>
<!--             <a href="#" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a> -->
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-green">
            <div class="inner">
              <h3>${tuser}</h3>

              <p>用户总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-user"></i>
            </div>
<!--             <a href="#" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a> -->
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-yellow">
            <div class="inner">
              <h3>${tmd}</h3>

              <p>设备总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-desktop"></i>
            </div>
<!--             <a href="#" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a> -->
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-red">
            <div class="inner">
              <h3>${tmp}</h3>

              <p>测点总量</p>
            </div>
            <div class="fa1">
              <i class="fa fa-lightbulb-o"></i>
            </div>
<!--             <a href="#" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a> -->
          </div>
        </div>
        </div>
       </div>
        </div>
           </div>
            <div class="col-sm-6" style="padding-left:0;padding-top:0;padding-bottom:0">
           <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
            <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
<!--               <span class="pull-right" style="font-size: 12px;"><a href="#">查看更多>></a></span> -->
              <h6 class="panel-title"  style=" border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;" >最新设备数据告警 </h6>
            </div>
            <div class="panel-body" style="padding-bottom:0;">
<table class="table table-striped table-hover index_table" id="table" data-field="mesDriverAlarms" data-url="${contextPath}/MesDriverAlarm/data">
    <thead>
        <tr>
            <th style="" data-field="mesDriver.name" tabindex="0">设备名称</th>
            <th style="" data-field="mesPoints.name" tabindex="0">测点名称</th>
        	<th style="" data-field="companyname" tabindex="0">公司</th>
            <th style="" data-field="updatetime" tabindex="0">时间</th>
            <th style="" data-field="tstatecode" tabindex="0">状态</th>
            <th style="" data-field="errorchangevalue" tabindex="0">超出范围</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${mesDriverAlarms != null}">
    <c:forEach var="p" items="${mesDriverAlarms}" begin="0" end="4">
        <tr data-index="0" data-uniqueid="59">
            <td style="">${p.mesDriver.name}</td>
            <td style="">${p.mesPoints.name}</td>
            <td style="">${p.companyname}</td>
            <td style="">${p.updatetime}</td>
            <td style="">${p.tstatecode}</td>
            <td style="">${p.errorchangevalue}</td>
        </tr>
    </c:forEach>
    </c:if>
    <c:if test="${mesDriverAlarms == null}">
  <tr><td colspan="6" class="text-center">  暂无数据!</td></tr>
    </c:if>
    </tbody>
</table>
            </div>
          </div>
         </div>
          <div class="col-sm-6 indexTable2" style="padding-top:0;padding-bottom:0">
           <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
            <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
<!--               <span class="pull-right" style="font-size: 12px;"><a href="#">查看更多>></a></span> -->
              <h6 class="panel-title"  style=" border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;" >最新产品数据告警 </h6>
            </div>
            <div class="panel-body" style="padding-bottom:0;">
<table class="table table-striped table-hover index_table" id="ttable" data-field="mesProductAlarms" data-url="${contextPath}/MesDriverAlarm/data">
    <thead>
        <tr>
            <th style="" data-field="productsn" tabindex="0">产品编号</th>
            <th style="" data-field="mesProcedureProperty.mesProductProcedure.mesProduct.name" tabindex="0">产品名称</th>
        	<th style="" data-field="companyname" tabindex="0">公司</th>
            <th style="" data-field="updatetime" tabindex="0">时间</th>
            <th style="" data-field="tstatecode" tabindex="0">状态</th>
            <th style="" data-field="errorchangevalue" tabindex="0">超出范围</th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${mesProductAlarms != null}">
    <c:forEach var="p" items="${mesProductAlarms}" begin="0" end="4">
        <tr data-index="0" data-uniqueid="59">
            <td style="">${p.productsn}</td>
            <td style="">${p.mesProcedureProperty.mesProductProcedure.mesProduct.name}</td>
         	<td style="">${p.companyname}</td>
            <td style="">${p.updatetime}</td>
            <td style="">${p.tstatecode}</td>
            <td style="">${p.errorchangevalue}</td>
        </tr>
    </c:forEach>
    </c:if>
    <c:if test="${mesProductAlarms == null}">
 <tr><td colspan="6" class="text-center">  暂无数据!</td></tr>
    </c:if>
    </tbody>
</table>
            </div>
          </div>
         </div>
		<%}else{ %>
		<div class="col-lg-12" style="padding:0">
          <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
          <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
            <h6 class="panel-title"  style="border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;"><a class="text-primary"> ${companyinfo.companyname}</a>：公司资产
            </h6>
          </div>
		<div class="panel-body" style="padding-bottom:0;">
	   <div class="row" >
        <div class="col-lg-4 col-sm-6">
          <!-- small box -->
          <div class="small-box bg-green">
            <div class="inner">
              <h3>${Fids}</h3>

              <p>工厂总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-flag"></i>
            </div>
            <a href="${contextPath}/company/tree_list" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-4 col-sm-6">
          <!-- small box -->
          <div class="small-box bg-aqua">
            <div class="inner">
              <h3>${pln}</h3>

              <p>产线总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-bars"></i>
            </div>
            <a href="${contextPath}/productline/tree_list" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-4 col-sm-6">
          <!-- small box -->
          <div class="small-box bg-yellow">
            <div class="inner">
              <h3>${mdn}</h3>

              <p>设备总数</p>
            </div>
            <div class="fa1">
              <i class="fa fa-desktop"></i>
            </div>
            <a href="${contextPath}/driver/driverList" class="small-box-footer">查看详细 <i class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div>  
       </div>
       </div>
        </div>
           </div>
            <div class="col-sm-6" style="padding-left:0;padding-top:0;padding-bottom:0">
           <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
            <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
              <shiro:hasPermission name="MesDriverAlarm:show">
              <span class="pull-right" style="font-size: 12px;"><a href="${contextPath}/MesDriverAlarm/MesDriverAlarmList">查看更多>></a></span>
              </shiro:hasPermission>
              <h6 class="panel-title"  style=" border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;" >最新设备数据告警 </h6>
            </div>
            <div class="panel-body" style="padding-bottom:0;">
<table class="table table-striped table-hover index_table" id="table" data-field="mesDriverAlarms" data-url="${contextPath}/MesDriverAlarm/data">
    <thead>
        <tr>
            <th style="" data-field="mesDriver.name" tabindex="0">设备名称</th>
            <th style="" data-field="mesPoints.name" tabindex="0">测点名称</th>
        	<th style="" data-field="companyname" tabindex="0">公司</th>
            <th style="" data-field="updatetime" tabindex="0">时间</th>
            <th style="" data-field="tstatecode" tabindex="0">状态</th>
            <th style="" data-field="errorchangevalue" tabindex="0">超出范围</th>
        </tr>
    </thead>
    <tbody>
    <c:choose>
    <c:when test="${mesDriverAlarms != null}">
    <c:forEach var="p" items="${mesDriverAlarms}" begin="0" end="4">
        <tr data-index="0" data-uniqueid="59">
            <td style="">${p.mesDriver.name}</td>
            <td style="">${p.mesPoints.name}</td>
        	<td style="">${p.companyname}</td>
            <td style="">${p.updatetime}</td>
            <td style="">${p.tstatecode}</td>
            <td style="">${p.errorchangevalue}</td>
        </tr>
    </c:forEach>
    </c:when>
    <c:otherwise>
  <tr><td colspan="6" class="text-center">  暂无数据!</td></tr>
    </c:otherwise>
    </c:choose>
    </tbody>
</table>
            </div>
          </div>
         </div>
          <div class="col-sm-6 indexTable2" style="padding-top:0;padding-bottom:0">
           <div class="panel bk-bg-white" style="box-shadow:none;margin-bottom:0">
            <div class="panel-heading bk-bg-white" style="border-color: #e8e3e3; padding: 10px 5px">
            <shiro:hasPermission name="MesProductAlarm:show">
              <span class="pull-right" style="font-size: 12px;"><a href="${contextPath}/MesProductAlarm/MesProductAlarmList">查看更多>></a></span>
              </shiro:hasPermission>
              <h6 class="panel-title"  style=" border-left:3px solid #00c0ef; padding-left:8px;font-family: '微软雅黑',Arial, sans-serif;" >最新产品数据告警 </h6>
            </div>
            <div class="panel-body" style="padding-bottom:0;">
<table class="table table-striped table-hover index_table" id="ttable" data-field="mesProductAlarms" data-url="${contextPath}/MesDriverAlarm/data">
    <thead>
        <tr>
            <th style="" data-field="productsn" tabindex="0">产品编号</th>
            <th style="" data-field="mesProcedureProperty.mesProductProcedure.mesProduct.name" tabindex="0">产品名称</th>
         	<th style="" data-field="companyname" tabindex="0">公司</th>
            <th style="" data-field="updatetime" tabindex="0">时间</th>
            <th style="" data-field="tstatecode" tabindex="0">状态</th>
            <th style="" data-field="errorchangevalue" tabindex="0">超出范围</th>
        </tr>
    </thead>
    <tbody>
    <c:choose>
    <c:when test="${mesProductAlarms != null}">
    <c:forEach var="p" items="${mesProductAlarms}" begin="0" end="4">
        <tr data-index="0" data-uniqueid="59">
            <td style="">${p.productsn}</td>
            <td style="">${p.mesProcedureProperty.mesProductProcedure.mesProduct.name}</td>
			<td style="">${p.companyname}</td>
            <td style="">${p.updatetime}</td>
            <td style="">${p.tstatecode}</td>
            <td style="">${p.errorchangevalue}</td>
        </tr>
    </c:forEach>
    </c:when>
    <c:otherwise>
 <tr><td colspan="6" class="text-center">  暂无数据!</td></tr>
    </c:otherwise>
    </c:choose>
    </tbody>
</table>
            </div>
          </div>
         </div>
        <%}}%>
        </div>
      <!--table end-->
      </div>
      <div id="footer" class="foot">Copyright &copy; 2015-2016, pactera.com, All Rights Reserve.</div>
    </div>
  </div>
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

<script src="${contextPath}/js/bootstrap.min.js"></script>
<script src="${contextPath}/js/uikit.cbr.js"></script>
<script src="${contextPath}/js/core.min.js"></script>
<script src="${contextPath}/styles/assets/sweetalert/sweetalert-dev.js"></script>
<script src="${contextPath}/js/uikit.todolist.js"></script>
<script src="${contextPath}/js/jquery.nicescroll.min.js"></script>
<script src="${contextPath}/js/jquery.nicescroll.plus.js"></script>
<script src="${contextPath}/js/common.js"></script>
<script>
//   	$(document).ready(function(){
  		 
//   	});
</script>
</body>
</html>