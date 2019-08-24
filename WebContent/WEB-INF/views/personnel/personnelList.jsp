<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<a id="refreshJbsxBox2personnelTree" rel="jbsxBox2personnelTree" target="ajax" href="${contextPath}/personnel/tree" style="display:none;"></a>
    <div class="searchBar">
    <div class="search_header">
    <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 人员查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" dynamic-url="parentDepartmentId" action="${contextPath }/personnel/data/${parentDepartmentId}" data-target="table"  onsubmit="return navTabSearch(this)">
            <div class="form-group">
               <label for="inputText" class="searchtitle">用户名</label>
               <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_username" value="${param.search_LIKE_username}"/>
            </div>
            <button type="submit" class="btn btn-info btn-search1">搜索</button>
          </form>
          </div>
        </div>
  <div id="toolBar">
    <div class="btn-group pull-left">
    <shiro:hasPermission name="Personnel:save">
        <a class="btn btn-default1" target="dialog" refresh="true" rel="invite" dynamic-url="parentDepartmentId" mask="true" href="${contextPath }/personnel/personnelInvite/${parentDepartmentId}?pagename=personnelInvite">
          <i class="fa fa-plus"></i> <span>邀请人员</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:delete">
        <a class="btn btn-default1" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/personnel/deleteUserById" title="确认要删除?">
          <i class="fa fa-remove"></i> <span>删除人员</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:viewPosition">
        <a class="btn btn-default1" target="page" data-target="table"  rel="position"  href="${contextPath }/personnel/positionList/{slt_uid}">
          <i class="fa fa-user"></i> <span>分配职位</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:viewDepartment">
        <a class="btn btn-default1" target="page" data-target="table"  rel="department"  href="${contextPath }/personnel/departmentList/{slt_uid}">
          <i class="fa fa-sitemap"></i> <span>分配部门</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:assign">
        <a class="btn btn-default1" target="dialog" refresh="true" data-target="table" rel="createRole" href="${contextPath }/personnel/lookup2create/userCompanyRole/{slt_uid}">
          <i class="fa fa-group"></i> 
          <span>分配公司角色</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:assign">
        <a class="btn btn-default1" target="dialog" refresh="true" data-target="table" rel="deleteRole" href="${contextPath }/personnel/lookup2delete/userCompanyRole/{slt_uid}">
          <i class="fa fa-reply-all"></i> 
          <span>撤销公司角色</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:transfer">
        <a class="btn btn-default1" target="dialog" refresh="true" data-target="table" rel="transferPermission" href="${contextPath }/personnel/inputPhone/{slt_uid}" title="确认要转移权限?">
          <i class="fa fa-exchange"></i> 
          <span>转移权限</span>
        </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Personnel:transfer">
        <a class="btn btn-default1" target="page" refresh="true" data-target="table" rel="transferPermissionRecord" href="${contextPath }/personnel/transferPermissionRecord">
          <i class="fa fa-list-ul"></i> 
          <span>权限转移记录</span>
        </a>
        </shiro:hasPermission>
    </div>
  </div>
  <table class="table table-striped" id="table" data-field="user" data-url="${contextPath }/personnel/data">
    <thead>
      <tr>
           <th data-field="Number" width="2%" data-align="center">序号</th>
        <th data-checkbox="true" width="22">
         <input class="cbr checkboxCtrl" type="checkbox" group="ids">
        </th>
              <th data-field="username" width="100">用户名</th>
              <th data-field="realname" width="100">真实姓名</th>
              <th data-field="phone" width="100">电话</th>
              <th data-field="email" width="100">邮箱地址</th>
              <th data-field="position" width="100">职位</th>
              <th data-field="department" width="100">所属部门</th>
              <th data-field="companyRole" width="100">公司角色</th>
      </tr>
    </thead>
  </table>
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
<c:set var="ModuleTitle" value="Personnel"/>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>