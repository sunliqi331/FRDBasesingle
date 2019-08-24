<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <a id="refreshJbsxBox2departmentTree" rel="jbsxBox2departmentTree" target="ajax" href="${contextPath}/department/tree" style="display:none;"></a>
          <div class="">
          <div class="searchBar">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
              <i class="fa fa-search"></i> 部门查询条件
            </div>
            <div class="ishidden" >
            <form class="form-inline" method="post" dynamic-url="parentDepartmentId" mask="true" action="${contextPath }/department/data" data-target="table"  onsubmit="return navTabSearch(this)">
                <div class="form-group">
                <label for="inputText" class="searchtitle">部门名称</label>
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}">
                </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
        </div>
          <div id="toolBar" class="clearfix" >
            <div class="btn-group pull-left clearfix">
            <shiro:hasPermission name="Department:save">
            <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create" dynamic-url="parentDepartmentId" mask="true" href="${contextPath }/department/create">
                <i class="fa fa-plus"></i> 
                <span>添加部门</span>
            </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="Department:edit">
            <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="update" href="${contextPath }/department/findDepartmentByid/{slt_uid}?pagename=update">
                <i class="fa fa-pencil"></i> 
                <span>编辑部门</span>
             </a>
             </shiro:hasPermission>
             <shiro:hasPermission name="Department:delete">
             <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" href="${contextPath }/department/delete" title="确认要删除?">
            <%-- <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids" 
            callback="deleteReloadRel2Department" href="${contextPath }/department/delete/{slt_uid}" title="确认要删除?"> --%>
                <i class="fa fa-remove"></i> 
                <span>删除部门</span>
            </a>
            </shiro:hasPermission>
        </div>
       </div>
       
    <table class="table table-striped" id="table" data-field="departments" data-url="${contextPath }/department/data">
      <thead>
        <tr>
        
        <th data-field="Number" width="22" data-align="center">序号</th>
		  <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox">
          </th>
          <th data-field="sn" width="100">部门编号</th>
          <th data-field="name" width="100">部门名称</th>
          <th data-field="principal" width="100">负责人</th>
          <th data-field="phone" width="100">电话</th>
          <th data-field="floor" width="100">部门位置</th>
          <th data-field="parentInfo" width="100">部门所属</th>
        </tr>
      </thead>
     <%--  <tbody>
        <c:forEach var="item" items="${departments}">
          <tr target="slt_uid" rel="${item.id}">
            <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
            </th>
            <td><a href="${contextPath}/department/list/${item.id}" target="ajax" rel="jbsxBox2departmentList">${item.name}</a></td>
            <td>${item.sn}</td>
            <td>${item.name}</td>
            <td>${item.principal}</td>
            <td>${item.phone}</td>
            <td>${item.floor}</td>
          </tr>
        </c:forEach>
      </tbody> --%>
    </table>
    </div>
    