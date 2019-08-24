<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>子系统列表</title>
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
        <li><i class="fa fa-puzzle-piece"></i> 子系统管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
            <form class="form-inline" method="post" action="${contextPath }/subsys/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">子系统名称</label>&nbsp&nbsp&nbsp&nbsp 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_sysname">
              </div>
              <button type="submit" class="btn btn-info btn-search">搜索</button>
            </form>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
              <shiro:hasPermission name="User:edit">
                <a class="btn btn-default1" target="dialog" data-target="table" rel="create" mask="true" href="${contextPath }/subsys/create?pagename=create"> 
                <i class="fa fa-plus"></i> 
                <span>新增子系统</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="User:edit">
                <a class="btn btn-default1" target="dialog" data-target="table" rel="update" href="${contextPath }/subsys/showOne/{slt_uid}?pagename=update">
                  <i class="fa fa-pencil"></i> 
                  <span>修改子系统</span>
                </a>
              </shiro:hasPermission>
              <shiro:hasPermission name="User:delete:User拥有的资源">
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids" callback="deleteCallback" href="${contextPath }/subsys/delete/{slt_uid}" title="确认要删除?">
                  <i class="fa fa-remove"></i> 
                  <span>删除子系统</span>
                </a>
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="subsysteminfos"
            data-url="${contextPath }/subsys/data">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"><input
                  class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="sysname" width="100">子系统名称</th>
                <th data-field="price" width="100">价格</th>
                <th data-field="url" width="100">URL地址</th>
                <th data-field="description" width="130">子系统说明</th>
                <th data-field="picname" width="130">图标名称</th>
                <th data-field="status" width="130">状态</th>
                <th data-field="limit" width="130">公司创建时间</th>
                <th data-field="defaultrole" width="130">默认角色</th>
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
  <c:set var="ParentTitle" value="SubSystem" />
  <c:set var="ModuleTitle" value="SubSystemList" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>