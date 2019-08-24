<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>数据权限</title>
  <%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body scroll="no">
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li>数据权限</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 权限查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" action="${contextPath }/management/security/dataControl/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">权限名称</label>
                 <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}"/>
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
              <shiro:hasPermission name="DataControl:view">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="DataControl_view" href="${contextPath }/management/security/dataControl/view/{slt_uid}">
                  <i class="fa fa-eye"></i>
                  <span>查看权限</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="DataControl:save">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="DataControl_add" href="${contextPath }/management/security/dataControl/create">
                  <i class="fa fa-plus"></i>
                  <span>新增权限</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="DataControl:edit">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="DataControl_edit" href="${contextPath }/management/security/dataControl/update/{slt_uid}">
                  <i class="fa fa-pencil"></i>
                  <span>编辑权限</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="DataControl:delete">
                <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" callback="deleteCallback" href="${contextPath }/management/security/dataControl/delete" title="确认要删除选定数据权限?">
                  <i class="fa fa-remove"></i> 
                  <span>删除权限</span>
                </a>
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="dataControls" data-url="${contextPath }/management/security/dataControl/data">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="name" width="200">名称</th>
                <th data-field="control" width="400">条件</th>
                <th data-field="description" width="200">描述</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
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
        <div class="modal-body">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="Security"/>
<c:set var="ModuleTitle" value="DataControl"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>