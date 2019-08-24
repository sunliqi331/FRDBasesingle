<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>部门管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<%-- zTree --%>
<link href="${contextPath}/styles/ztree/css/zTreeStyle.css" rel="stylesheet" type="text/css" media="screen"/>
</head>
<body scroll="no">
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
        <li>部门管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body" id="jbsxBox2departmentList" class="unitBox">
          <c:import url="/department/list"/>
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
  <c:set var="ModuleTitle" value="Department"/>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/styles/ztree/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
</body>
</html>
