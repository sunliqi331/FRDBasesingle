<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<a id="refreshJbsxBox2factoryTree" rel="jbsxBox2factoryTree" target="ajax" href="${contextPath}/company/tree" style="display:none;"></a>
        <div class="">
          <div class="searchBar">
          <div class="search_header">
              <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 工厂查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" dynamic-url="parentid" action="${contextPath }/company/data3/${parentid}" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">工厂名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_companyname" value="${param.search_LIKE_companyname}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
          </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="Factory:save">
              <a class="btn btn-default1 btn-tool" target="dialog" rel="create_factory" refresh="true" dynamic-url="parentid" mask="true" href="${contextPath }/company/addFactory/${parentid}?pagename=addFactoryinfo">
                <i class="fa fa-pencil"></i> 
                <span>添加工厂</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Factory:edit">
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" refresh="true" rel="update_factory" href="${contextPath }/company/findFactoryByid/{slt_uid}?pagename=editFactoryinfo">
                <i class="fa fa-pencil"></i> 
                <span>编辑工厂</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Factory:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" href="${contextPath }/company/deleteCompanyinfo" title="确认要删除?">
              <%-- <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids" 
              callback="deleteReloadRel2Factory" href="${contextPath }/company/deleteCompanyinfo/{slt_uid}" title="确认要删除?">  --%>
              <i class="fa fa-remove"></i> 
                <span>删除工厂</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
    <table class="table table-striped" id="table" data-field="factory" data-url="${contextPath }/company/data3/${parentid}">
      <thead>
        <tr>
          <th data-field="Number" width="2%" data-align="center">序号</th>
          <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox">
          </th>
          <th data-field="companyname" width="100">工厂名称</th>
          <th data-field="address" width="100">工厂地址</th>
          <th data-field="startdate" width="100">成立日期</th>
        </tr>
      </thead>
<!--       <tbody> -->
<%--         <c:forEach var="item" items="${companyinfos}"> --%>
<%--           <tr target="slt_uid" rel="${item.id}"> --%>
<!--             <th data-checkbox="true" width="22"> -->
<!--               <input class="cbr checkboxCtrl" type="checkbox" group="ids"> -->
<!--             </th> -->
<%--             <td><a href="${contextPath}/company/list/${item.id}" target="ajax" rel="jbsxBox2factoryList">${item.companyname}</a></td> --%>
<%--             <td>${item.companyname}</td> --%>
<%--             <td>${item.address}</td> --%>
<!--           </tr> -->
<%--         </c:forEach> --%>
<!--       </tbody> -->
    </table>
    </div>