<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>公司信息组织架构</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<%-- zTree --%>
<link href="${contextPath}/styles/ztree/css/zTreeStyle.css" rel="stylesheet" type="text/css" media="screen"/>

<script type="text/javascript">
	$(document).ready(function(){
		$.table.init('factoryinfo',{});
		$.table.init('userinfo',{});
		$.table.init('departmentinfo',{});
		$("#userInfoTable").hide();
		$("#userInfoLabel").html("");
		$("#factoryInfoLabel").html("工厂信息");
		$("#departmentInfoLabel").html("部门信息");
	});

</script>

</head>
<body scroll="no">
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li>公司信息组织架构</li>
      </ol>
      <div class="main-wrap fold-wrap">
        <div class="tree-list" layoutH="5" id="jbsxBox2lineTree" >
          <c:import url="/companyOrganization/tree"/>
        </div>
        <div class="collapse-trigger">
          <div class="collapse-trigger-inner">
            <span class="fa fa-outdent"></span>
          </div>
        </div>
        <div class="main-body" layoutH="0" id="jbsxBox2lineList" class="unitBox">
          <div class="main-wrap">
          <div id="factoryInfoTable">
          <b><span id="factoryInfoLabel"></span></b>
          <!-- 工厂表 -->
          <table class="table table-striped" id="factoryinfo" data-field="factoryinfos" data-single-select="true" data-url="${contextPath }/companyOrganization/factoryInfoData/${id}">
            <thead>
              <tr>
                <th data-field="Number" width="22" data-align="center">序号</th>
                <th data-field="companyname" width="100">工厂名称</th>
                <th data-field="address" width="100">工厂地址</th>
                <th data-field="startdate" width="100">成立日期</th>
              </tr>
            </thead>
          </table><br>
          </div>
          <div id="departmentInfoTable">
          <b><span id="departmentInfoLabel"></span></b>
          <!-- 部门表 -->
          <table class="table table-striped" id="departmentinfo" data-field="departmentinfos" data-single-select="true"  data-url="${contextPath }/companyOrganization/departmentInfoData/${id}">
            <thead>
              <tr>
				<th data-field="Number" width="22" data-align="center">序号</th>
				<th data-field="sn" width="100">部门编号</th>
				<th data-field="name" width="100">部门名称</th>
				<th data-field="principal" width="100">负责人</th>
				<th data-field="phone" width="100">电话</th>
				<th data-field="floor" width="100">部门位置</th>
              </tr>
            </thead>
          </table><br>
          </div>
          <div id="userInfoTable">
          <b><span id="userInfoLabel"></span></b>
          <!-- 人员表 -->
           <%-- data-url="${contextPath }/companyOrganization/userInfoData/${id}" --%>
          <table class="table table-striped" id="userinfo" data-field="userinfos" data-single-select="true">
            <thead>
              <tr>
                <th data-field="Number" width="22" data-align="center">序号</th>
                <th data-field="username" width="100">用户名</th>
                <th data-field="realname" width="100">真实姓名</th>
                <th data-field="phone" width="100">电话</th>
                <th data-field="position" width="100">职位</th>
                <th data-field="email" width="100">邮箱地址</th>
              </tr>
            </thead>
          </table><br>
          </div>
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
  <c:set var="ParentTitle" value="Company"/>
  <c:set var="ModuleTitle" value="CompanyOrganization"/>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/styles/ztree/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
    <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>
