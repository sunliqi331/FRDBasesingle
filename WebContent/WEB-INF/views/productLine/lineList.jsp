<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
 <a id="refreshJbsxBox2lineTree" rel="jbsxBox2lineTree" target="ajax" href="${contextPath}/productLine/tree" style="display:none;"></a> 
        <div class="">
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 产线查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" dynamic-url="parentid" action="${contextPath }/productline/productlineData/${parentid}" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">产线名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_linename" value="${param.search_LIKE_linename}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
              <input type="hidden" id="tree" name="treeId" value="${parentid}">
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="Productline:save">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" rel="create_productLine" dynamic-url="parentid" mask="true" href="${contextPath}/productline/addLine/${parentid}">
                <i class="fa fa-plus"></i> 
                <span>添加产线</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Productline:edit">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" data-target="table" rel="update_productLine" href="${contextPath}/productline/findById/{slt_uid}?pagename=editLine">
                <i class="fa fa-pencil"></i> 
                <span>编辑产线</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Productline:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" callback="deleteReloadRel2Factory" href="${contextPath }/productline/deleteMesProductline" title="确认要删除?"> 
              <i class="fa fa-remove"></i> 
                <span>删除产线</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Productline:viewDriver">
              <a class="btn btn-default1 btn-tool" id="driver" target="page" data-target="table" rel=finddrivers href="${contextPath}/productline/driverList/{slt_uid}"> 
                <i class="fa fa-cogs"></i> 
                <span>分配设备</span>
              </a>
              </shiro:hasPermission>
            </div>
          </div>
    <table class="table table-striped" id="table" data-field="MesProductlines" data-url="${contextPath }/productline/productlineData/${parentid}">
      <thead>
        <tr>
             <th data-field="Number" width="2%" data-align="center">序号</th>
          <th data-checkbox="true" width="22">
          <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
           <th data-field="linesn" width="100">产线编号</th>
           <th data-field="linename" width="100">产线名称</th>
           <th data-field="mesdriver" width="100">关联设备</th>
           <th data-field="companyinfo.companyname" width="100">关联工厂</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="item" items="${mesProductline}">
          <tr target="slt_uid" rel="${item.id}">
            <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
            </th>
            <td><a href="${contextPath}/productline/list/${item.id}" target="ajax" rel="jbsxBox2lineList">${item.linesn}</a></td>
            <td>${item.linename}</td>
            <td>${item.linesn}</td>
            <td>${item.mesdriver}</td>
            <td>${item.companyinfo.companyname}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    </div>
    <script src="${contextPath}/js/uikit.pageswitch.js"></script>