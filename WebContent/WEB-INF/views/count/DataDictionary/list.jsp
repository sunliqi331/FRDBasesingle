<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>数据字典</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="icon-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
      <li>数据字典</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 字典查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/DataDictionary/data/0" data-target="table" onsubmit="return navTabSearch(this)">
             <div class="form-group">
               <label for="inputText" class="searchtitle">名称</label>
               <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}">
            </div>
            <button type="submit" class="btn btn-info  btn-search1">搜索</button>
          </form>
          </div>
        </div>
      <div id="toolBar">
          <div class="btn-group pull-left">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" rel="create"  href="${contextPath }/DataDictionary/create">
                <i class="fa fa-plus"></i>
                <span>添加字典</span>
              </a>
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" refresh="true" rel="update" href="${contextPath }/DataDictionary/update/{slt_uid}">
                <i class="fa fa-pencil"></i>
                <span>编辑字典</span>
              </a>
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids" href="${contextPath }/DataDictionary/delete/{slt_uid}" title="确认要删除?">
                    <i class="fa fa-remove"></i> 
                    <span>删除字典</span>
                </a>
               <a class="btn btn-default1 btn-tool" target="page" refresh="true" data-target="table" href="${contextPath }/DataDictionary/view/{slt_uid}">
                    <i class="fa fa-book"></i>
                    <span>配置词典</span>
                </a>
        </div>
          </div>
           <table class="table table-striped" id="table" data-field="DataDictionary" data-single-select="true" data-url="${contextPath }/DataDictionary/data/0">
          <thead>
            <tr target="slt_uid" rel="${item.id}">
                 <th data-field="Number" width="2%" data-align="center">序号</th>

              <th data-checkbox="true" width="22">
                <!-- <input class="cbr checkboxCtrl" type="checkbox" group="ids" > -->
              </th>
              <th data-field="name" width="100">名称</th>
               <c:choose>
                 <c:when test="${dictionaryType == 'THEME' }">
                   <th data-field="value" >描述</th>
                 </c:when>
                 <c:otherwise>
                   <th>内容</th>
                 </c:otherwise>
                 </c:choose>
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
<c:set var="ParentTitle" value="System"/>
<c:set var="ModuleTitle" value="Dictionary"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>