<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>产品管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>

<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li><a href="${contextPath}/product/productList">产品管理</a></li>  
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 产品查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/product/productData" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left clearfix" >
            <shiro:hasPermission name="Product:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_product" href="${contextPath}/product/addProduct" >
                <i class="fa fa-plus"></i> 
                <span>添加产品</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:edit">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="edit_product" href="${contextPath}/product/findById/{slt_uid}" >
                <i class="fa fa-pencil"></i> 
                <span>修改产品</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/product/deleteProduct" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>删除产品</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:viewProcedure">             
              <a class="btn btn-default1" id="work" title="配置产品工序" target="page" data-target="table"  rel="view_procedure" href="${contextPath }/procedure/procedureList/{slt_uid}?pagename=procedureList">
                <i class="fa fa-cogs"></i>
                	<span>配置产品工序</span>
              </a>
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="mesProducts" data-url="${contextPath}/product/productData">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                 <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
<!--                		<th data-field="productnum" width="100">产品编号</th> -->
	                <th data-field="name" width="100">产品名称</th>
               		<th data-field="modelnum" width="100">产品型号</th>
                <!-- <th data-field="companyinfo.companyname" width="100">所属工厂</th> -->
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
  <c:set var="ParentTitle" value="Production" />
  <c:set var="ModuleTitle" value="Product" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>