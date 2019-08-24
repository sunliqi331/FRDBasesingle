<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>工厂列表</title>
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
        <li><i class="fa fa-puzzle-piece"></i> 工厂列表</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
            <form class="form-inline" method="post" action="${contextPath }/company/data3" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">工厂名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_companyname" value="${param.search_LIKE_companyname}" />
              </div>
              <button type="submit" class="btn btn-info btn-search">搜索</button>
            </form>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" rel="create_factory" href="${contextPath }/company/addFactory?pagename=addFactoryinfo">
                <i class="fa fa-pencil"></i> 
                <span>添加工厂</span>
              </a> 
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="update_factory" href="${contextPath }/company/findFactoryByid/{slt_uid}?pagename=editFactoryinfo">
                <i class="fa fa-pencil"></i> 
                <span>编辑工厂</span>
              </a> 
              <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids" href="${contextPath }/company/deleteCompanyinfo/{slt_uid}" title="确认要删除?"> 
              <i class="fa fa-remove"></i> 
                <span>删除工厂</span>
              </a> 
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="factory" data-checkbox-header="false" data-single-select="true" data-url="${contextPath }/company/data3">
            <thead>
              <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"></th>
                <th data-field="companyname" width="100">工厂名称</th>
                <th data-field="address" width="100">工厂地址</th>
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
  <c:set var="ParentTitle" value="Company" />
  <c:set var="ModuleTitle" value="Factory" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>