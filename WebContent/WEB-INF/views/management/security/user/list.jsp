<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
	
		//$.table.init("companyTable",{columns:orignalColumns});
		
	});

</script>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>用户管理</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
            <i class="fa fa-search"></i> 用户查询条件
          </div>
          <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/management/security/user/data" data-target="table" onsubmit="return navTabSearch(this)">
             <div class="form-group">
               <label for="inputText" class="searchtitle">用户名</label>
               <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_username">
            </div>
             <div class="form-group">
               <label for="inputText" class="searchtitle">手机号</label>
               <input type="tel" class="form-control searchtext" id="inputPhone" name="search_LIKE_phone" maxlength="11"
               onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false">
            </div>
            <button type="submit" class="btn btn-info  btn-search1">搜索</button>
          </form>
          </div>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
            <shiro:hasPermission name="User:save">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="ture" data-target="table" rel="create" mask="true" href="${contextPath }/management/security/user/create">
                <i class="fa fa-plus"></i>
                <span>添加用户</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="User:edit:User拥有的资源">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="ture"
              		data-target="table" rel="update" mask="true" 
              		href="${contextPath }/management/security/user/update/{slt_uid}">
                <i class="fa fa-pencil"></i>
                <span>编辑用户</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="User:delete:User拥有的资源">
<!--             <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table"  -->
<!--             			rel="ids" callback="deleteCallback"  -->
<%--             			href="${contextPath }/management/security/user/delete/{slt_uid}" title="确认要删除?"> --%>
<!--                 <i class="fa fa-remove"></i>  -->
<!--                 <span>删除用户</span> -->
<!--               </a> -->
              <a class="btn btn-default1 btn-tool" target="dialog"  refresh="ture" data-target="table" href="${contextPath }/management/security/user/preUpdateStatus/{slt_uid}">
                <i class="fa fa-refresh"></i> 
                <span>修改用户状态</span>
              </a>
            </shiro:hasPermission>
             <c:set var="currentUser">
    		 <shiro:principal type="java.lang.String"/>
    		 </c:set>
             <c:if test="${currentUser == 'manage' }">
            <shiro:hasPermission name="User:reset:User拥有的资源">
               <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table"  
               	href="${contextPath }/management/security/user/reset/password/{slt_uid}" title="确认重置密码为123456?"> 
                 <i class="fa fa-unlock-alt"></i>  
               <span>重置密码</span> 
              </a> 
            </shiro:hasPermission>
               </c:if>
            <shiro:hasPermission name="User:assign">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="createRole" href="${contextPath }/management/security/user/lookup2create/userRole/{slt_uid}">
                <i class="fa fa-group"></i> 
                <span>分配用户角色</span>
              </a>
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="deleteRole" href="${contextPath }/management/security/user/lookup2delete/userRole/{slt_uid}">
                <i class="fa fa-reply-all"></i> 
                <span>撤销用户角色</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="User:assign">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="createRole" href="${contextPath }/management/security/user/lookup2create/userCompanyRole/{slt_uid}">
                <i class="fa fa-group"></i> 
                <span>分配公司角色</span>
              </a>
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="deleteRole" href="${contextPath }/management/security/user/lookup2delete/userCompanyRole/{slt_uid}">
                <i class="fa fa-reply-all"></i> 
                <span>撤销公司角色</span>
              </a>
            </shiro:hasPermission>
          </div>
        </div>
        <table class="table table-striped" id="table" data-field="users" data-checkbox-header="false" data-single-select="true" data-url="${contextPath }/management/security/user/data">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
<!--                 <input class="cbr checkboxCtrl" type="checkbox" group="ids"> -->
              </th>
              <th data-field="username" width="100">用户名</th>
              <th data-field="realname" width="100">姓名</th>
              <th data-field="phone" width="120">手机号</th>
              <th data-field="email" width="150">邮箱</th>
     <!--          <th data-field="organize" width="150">所在组织</th>-->
             <!--  <th data-field="roles" width="150">角色</th>  -->
              <th data-field="status" width="60">账户状态</th>
              <th data-field="companyinfo" width="60">公司名</th>
              <th data-field="usersRole" width="60">用户角色</th>
              <!-- <th data-field="companyRole" width="60">公司角色</th> -->
              <th data-field="createTime" width="200">创建时间</th>
              
            </tr>
          </thead>
        </table>  
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
<c:set var="ParentTitle" value="Security"/>
<c:set var="ModuleTitle" value="User"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>