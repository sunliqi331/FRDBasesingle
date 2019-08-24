<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <a id="refreshJbsxBox2organizationTree" rel="jbsxBox2organizationTree" target="ajax" href="${contextPath}/management/security/organization/tree" style="display:none;"></a>
  <div class="">
    <div class="searchBar">
      <div class="search_header">
      <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
        <i class="fa fa-search"></i> 组织查询条件
      </div>
      <div class="ishidden" >
      <form class="form-inline" method="post"  data-target="table" dynamic-url="parentOrganizationId" action="${contextPath}/management/security/organization/list/${parentOrganizationId}" onsubmit="return navTabSearch(this);">
        <div class="form-group">
           <label for="inputText" class="searchtitle">组织名称</label>
           <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name }"/>
        </div>
        <button type="submit" class="btn btn-info btn-search1">搜索</button>
      </form>
      </div>
    </div>
    <div id="toolBar">
      <div class="btn-group pull-left">
        <shiro:hasPermission name="Organization:save">
          <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="create" dynamic-url="parentOrganizationId" href="${contextPath}/management/security/organization/create/${parentOrganizationId}">
            <i class="fa fa-plus"></i>
            <span>新增组织</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Organization:edit">
          <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="update" href="${contextPath}/management/security/organization/update/{slt_uid}">
            <i class="fa fa-pencil"></i>
            <span>编辑组织</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Organization:delete">
          <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" callback="deleteReloadRel2Org" href="${contextPath}/management/security/organization/delete/{slt_uid}" title="确认要删除该组织?">
            <i class="fa fa-remove"></i> 
            <span>删除组织</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Organization:assign">
          <a class="btn btn-default btn-tool" target="dialog" refresh="true" data-target="table" rel="createRole" href="${contextPath}/management/security/organization/lookup2create/organizationRole/{slt_uid}">
            <i class="fa fa-group"></i> 
            <span>分配角色</span>
          </a>
          <a class="btn btn-default btn-tool" target="dialog" refresh="true" data-target="table" rel="deleteRole" href="${contextPath}/management/security/organization/lookup2delete/organizationRole/{slt_uid}">
            <i class="fa fa-reply-all"></i> 
            <span>撤销角色</span>
          </a>
        </shiro:hasPermission>
      </div>
    </div>
    <table class="table table-striped" id="table" data-field="organizations" data-checkbox-header="false" data-single-select="true" data-url="${contextPath }/management/security/organization/data/${parentOrganizationId}">
      <thead>
        <tr>
             <th data-field="Number" width="2%" data-align="center">序号</th>
          <th data-checkbox="true" width="22"></th>
          <th data-field="name" width="100">名称</th>
          <th data-field="role" width="100">拥有角色</th>
          <th data-field="priority" width="200">优先级</th>
          <th data-field="description" width="120">描述</th>
          <th data-field="parent_name" width="120">父组织</th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
