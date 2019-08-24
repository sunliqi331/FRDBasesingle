<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>公司信息管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i> 
        <a href="${contextPath}/management/index">首页</a></li>
        <li>公司信息管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 公司查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath }/company/data2" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">状态</label> 
                <select name="search_LIKE_companystatus" class="form-control searchtext" style="padding-right: 50px">
                <option></option>
                <option value="1">已审核</option>
                <option value="3">无效</option>
                </select>
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">公司名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_companyname" value="${param.search_LIKE_companyname}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
            	<shiro:hasPermission name="CompanyRegisted:show">
              <a class="btn btn-default1" target="page" refresh="true" data-target="table" href="${contextPath }/company/myCompany/{slt_uid}?pagename=myselfCompany">
                <i class="fa fa-eye"></i> 
                <span>查看公司信息</span>
              </a> 
              </shiro:hasPermission>
		      <shiro:hasPermission name="CompanyRegisted:delete">      
 			 <a class="btn btn-default1 btn-tool" target="dialog" data-target="table"  href="${contextPath }/company/editStatus/{slt_uid}"> 
              <i class="fa fa-pencil"></i> 
              <span>修改公司状态</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="company" data-single-select="ture"  data-url="${contextPath }/company/data2">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
               <!--  <input class="cbr checkboxCtrl" type="checkbox" group="ids"> -->
                </th>
              
                <th data-field="companyname" width="100">公司名称</th>
                <th data-field="address" width="100">公司地址</th>
                <th data-field="companyemail" width="100">公司邮箱</th>
                <th data-field="username"width="100">创建人</th>
                <th data-field="legalperson" width="100">法人</th>
                <th data-field="businesstype" width="100">所属行业</th>
                <th data-field="infotype" width="100">类型</th>
                <th data-field="companystatus" width="130">状态</th>
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
  <c:set var="ModuleTitle" value="CompanyRegisted" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>