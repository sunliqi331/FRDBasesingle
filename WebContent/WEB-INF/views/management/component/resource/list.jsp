<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>资源管理</title>
<link href="${contextPath }/styles/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" />
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>资源管理</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar">
        <div class="search_header">
        <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 资源查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/management/component/resource/data" data-target="resourceTable" onsubmit="return navTabSearch(this)">
            <div class="form-group">
              <label for="inputText" class="searchtitle">资源名称：</label>
              <input type="text" class="form-control searchtext" name="search_EQ_name" />
            </div>
            <button type="submit" class="btn btn-info  btn-search">搜索</button>
          </form>
          </div>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
            <shiro:hasPermission name="Resource:upload">
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="resourceTable" rel="upload2File" close="close2upload" refresh="true" href="${contextPath }/management/component/resource/upload?storeType=file">
                <i class="fa fa-upload"></i>
                <span>上传资源到文件系统</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="Resource:upload">
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="resourceTable" rel="upload2Db" close="close2upload" refresh="true" href="${contextPath }/management/component/resource/upload?storeType=db">
                <i class="fa fa-upload"></i>
                <span>上传资源到数据库</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="Resource:edit">
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="resourceTable" rel="update" href="${contextPath }/management/component/resource/update/{slt_uid}">
                <i class="fa fa-pencil"></i>
                <span>编辑资源</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="Resource:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="resourceTable" rel="ids" href="${contextPath }/management/component/resource/delete" title="确认要删除选定资源?">
                <i class="fa fa-remove"></i>
                <span>删除资源</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="Resource:download">
              <a class="btn btn-default1 btn-tool" target="download" data-target="resourceTable" rel="ids" url="${contextPath }/management/component/resource/download/">
                <i class="fa fa-download"></i>
                <span>下载</span>
              </a>
            </shiro:hasPermission>
          </div>
        </div>
        <table class="table table-striped" id="resourceTable" data-field="resources" data-url="${contextPath }/management/component/resource/data">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
              <th data-field="name">名称</th>
              <th data-field="type">文件类型</th>
              <th data-field="size">长度</th>
              <th data-field="uuid">uuid(下载标识)</th>
              <th data-field="storeType">存储类型</th>
              <th data-field="uploadTime">上传时间</th>
            </tr>
          </thead>
          <tbody>
          </tbody>
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
<script type="text/javascript">
$(document).ready(function($){
  $.table.init('resourceTable',{toolbar:'#toolBar'}, function(data){
    var $p = $('#resourceTable').find('tbody');
    $('tr[data-uniqueid]',$p).each(function(i){
      var $this = $(this);
      var item = data.resources[i];
      $this.attr('url', item.storeType + '/' + item.uuid);
    });
  });
});
</script>
<c:set var="ParentTitle" value="Component"/>
<c:set var="ModuleTitle" value="Resource"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/styles/uploadify/scripts/jquery.uploadify.js"></script>
</body>
</html>