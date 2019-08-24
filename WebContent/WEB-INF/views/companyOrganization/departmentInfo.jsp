<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!--
<a id="refreshJbsxBox2lineTree" rel="jbsxBox2lineTree" target="ajax" href="${contextPath}/productLine/tree" style="display:none;"></a>
--> 
      <div class="main-wrap">
         <!--  <div class="searchBar">
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
          </div>  --> 
          <table class="table table-striped" id="table" data-field="departments" data-single-select="true"  data-url="${contextPath }/companyOrganization/companyInfoData/${id}">
            <thead>
              <tr>
				<th data-field="Number" width="22" data-align="center">序号</th>
				<th data-field="sn" width="100">部门编号</th>
				<th data-field="name" width="100">部门名称</th>
				<th data-field="principal" width="100">负责人</th>
				<th data-field="phone" width="100">电话</th>
				<th data-field="floor" width="100">部门位置</th>
              </tr>
            </thead>
          </table>

      </div>

  <!-- Modal 
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
</script> -->
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
