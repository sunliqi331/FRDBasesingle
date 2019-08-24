<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>角色管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath}/styles/treeTable/themes/default/treeTable.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/keta/css/keta.css" rel="stylesheet" type="text/css" />
</head>
<body scroll="no">
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li>角色管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 角色查询条件
            </div>
            <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath }/management/security/role/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">角色名称</label>
                 <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name }"/>
              </div>
              <button type="submit" class="btn btn-info  btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
              <shiro:hasPermission name="Role:view">
                <a class="btn btn-default1 btn-tool" target="dialog" rel="view" data-target="table" rel="roleView" href="${contextPath }/management/security/role/view/{slt_uid}">
                  <i class="fa fa-eye"></i>
                  <span>查看角色</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="Role:save">
                <a class="btn btn-default1 btn-tool" target="dialog" rel="save" refresh="true" data-target="table" rel="roleCreate" href="${contextPath }/management/security/role/create">
                  <i class="fa fa-plus"></i>
                  <span>新增角色</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="Role:edit">
                <a class="btn btn-default1 btn-tool" target="dialog" rel="update" refresh="true" data-target="table" rel="roleUpdatae" href="${contextPath }/management/security/role/update/{slt_uid}">
                  <i class="fa fa-pencil"></i>
                  <span>编辑角色</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="Role:delete">
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="Role_navTab" callback="deleteCallback" href="${contextPath }/management/security/role/delete/{slt_uid}" title="确认要删除该角色?">
                  <i class="fa fa-remove"></i> 
                  <span>删除角色</span>
                </a>
              </shiro:hasPermission>
<%--               <shiro:hasPermission name="Role:assign"> --%>
<%--                 <a class="btn btn-default1 btn-tool" target="dialog" rel="assign" refresh="true" data-target="table" rel="Role_navTab" href="${contextPath }/management/security/role/assign/{slt_uid}"> --%>
<!--                   <span>分配权限</span> -->
<!--                 </a> -->
<%--               </shiro:hasPermission> --%>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="roles" data-checkbox-header="false" data-single-select="true" data-url="${contextPath }/management/security/role/data">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"></th>
                <th data-field="name" width="200">角色名称</th>
                <th data-field="description">描述</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
  <div id="wait" style="display: none;">请稍等</div>
<!-- Modal -->
<script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
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
<c:set var="ModuleTitle" value="Role"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.blockUI.js"></script>
<script src="${contextPath}/styles/treeTable/jquery.treeTable.js" type="text/javascript"></script>
<script src="${contextPath}/styles/keta/js/keta.js" type="text/javascript"></script>
<script type="text/javascript">

</script>
</body>
</html>