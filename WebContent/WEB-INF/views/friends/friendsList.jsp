<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <a id="refreshJbsxBox2friendTree" rel="jbsxBox2friendTree" target="ajax" href="${contextPath}/friends/tree" style="display:none;"></a>
  <div class="">
        <div class="searchBar">
        
          <form class="form-inline" method="post" action="${contextPath }/friends/friendPageData" 
          			data-target="table" onsubmit="return navTabSearch(this)">
             <div class="form-group">
               <label for="inputText" class="searchtitle">公司名称</label>
                <input type="hidden" class="form-control searchtext" 
               		id="inputText" name="search_EQ_status" value="1">
               <input type="text" class="form-control searchtext" maxlength="20"
               		id="inputText" name="search_LIKE_companyname">
            </div>
            <button type="submit" class="btn btn-info  btn-search">搜索</button>
          </form>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
           <shiro:hasPermission name="User:edit:User拥有的资源">
              <a class="btn btn-default1 btn-tool" target="dialog" 
              		data-target="table" rel="update" mask="true" 
              		href="${contextPath }/company/findCompanyByid/{slt_uid}?pagename=showRegistedCompanyinfo">
                <i class="icon-pencil"></i>
                <span>编辑公司</span>
              </a>
            </shiro:hasPermission>
            <shiro:hasPermission name="User:delete:User拥有的资源">
            <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" 
            			rel="ids" 
            			href="${contextPath }/company/deleteCompanyinfo/{slt_uid}" title="确认要删除?">
                <i class="icon-remove"></i> 
                <span>删除公司</span>
              </a>
            </shiro:hasPermission>
            
            
            
          </div>
        </div>
        <table class="table table-striped" id="table" data-field="companyinfos" data-url="${contextPath }/company/registeddata?search_EQ_status=1">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
             <th data-field="companyname" width="100">公司名称</th>
              <th data-field="legalperson" width="100">法人</th>
              <th data-field="businesstype" width="100">行业类型</th>
              <th data-field="startdate" width="130">公司创建时间</th>
            </tr>
          </thead>
        </table>  
      </div>
