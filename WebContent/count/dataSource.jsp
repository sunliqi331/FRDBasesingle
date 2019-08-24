<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>设备列表</title>
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
        <li><i class="fa fa-puzzle-piece"></i> 设备列表</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
    <div class="searchBar">
          <form class="form-inline" method="post" action="../../../company/data2" data-target="table" onsubmit="return navTabSearch(this)">
            <div class="form-group">
               <label for="inputText" class="searchtitle">工件编号</label>
               <input type="text" class="form-control searchtext" id="inputText" placeholder="开始" name="search_LIKE_legalperson" value=""/>
               <input type="text" class="form-control searchtext" id="inputText" placeholder="结束" name="search_LIKE_legalperson" value=""/>
            </div>
            <button type="submit" class="btn btn-info btn-search">搜索</button>
          </form>
        </div>
          <table class="table table-striped" id="table" data-field="driverList" data-checkbox-header="false" data-single-select="true" data-url="../../../company/data2">
          <thead>
            <tr>
              <th data-checkbox="true" width="22"></th>
              <th data-field="id" width="100">序号</th>
              <th data-field="sm" width="100">工件编号</th>
              <th data-field="name" width="100">生产日期</th>
              <th data-field="companyname" >产线</th>
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
  <c:set var="ParentTitle" value="Driver" />
  <c:set var="ModuleTitle" value="driverList" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>