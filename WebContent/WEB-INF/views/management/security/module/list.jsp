<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <a id="refreshJbsxBox2moduleTree" rel="jbsxBox2moduleTree" target="ajax" href="${contextPath}/management/security/module/tree" style="display:none;"></a>
  <div class="">
    <div class="searchBar">
      <div class="search_header">
      <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
        <i class="fa fa-search"></i> 模块查询条件
      </div>
      <div class="ishidden" >
      <form class="form-inline" method="post" data-target="table" dynamic-url="parentModuleId" action="${contextPath }/management/security/module/data/${parentModuleId}" onsubmit="return navTabSearch(this);">
        <div class="form-group">
          <label for="inputText" class="searchtitle">模块名称</label>
          <input type="number" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name }"/>
        </div>
        <button type="submit" class="btn btn-info btn-search1">搜索</button>
      </form>
      </div>
    </div>
    <div id="toolBar">
      <div class="btn-group pull-left">
        <shiro:hasPermission name="Module:save">
          <a class="btn btn-default1 btn-tool" target="dialog" rel="create" data-target="table" dynamic-url="parentModuleId" href="${contextPath }/management/security/module/create/${parentModuleId}">
            <i class="fa fa-plus"></i>
            <span>新增模块</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Module:edit">
          <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="update"  href="${contextPath }/management/security/module/update/{slt_uid}">
            <i class="fa fa-pencil"></i>
            <span>编辑模块</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Module:delete">
          <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" callback="deleteReloadRel2Module" href="${contextPath }/management/security/module/delete/{slt_uid}"  title="确认要删除该模块?">
            <i class="fa fa-remove"></i> 
            <span>删除模块</span>
          </a>
        </shiro:hasPermission>
        <shiro:hasPermission name="Module:view">
          <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="view" href="${contextPath }/management/security/module/view/{slt_uid}">
            <i class="fa fa-eye"></i>
            <span>查看模块</span>
          </a>
        </shiro:hasPermission>
      </div>
    </div>
    <table class="table table-striped" id="table" data-field="modules" data-checkbox-header="false" data-single-select="true" data-url="${contextPath }/management/security/module/data/${parentModuleId}">
      <thead>
        <tr>
             <th data-field="Number" width="2%" data-align="center">序号</th>
          <th data-checkbox="true" width="22"></th>
          <th data-field="name" width="100">名称</th>
          <th data-field="priority" width="100">优先级</th>
          <th data-field="sn" width="200">授权名称</th>
          <th data-field="parent_name" width="120">父模块</th>
          <th data-field="url" width="120">模块地址</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="item" items="${modules}">
          <tr target="slt_uid" rel="${item.id}">
            <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
            </th>
            <td><a href="${contextPath}/management/security/module/list/${item.id}" target="ajax" rel="jbsxBox2moduleList">${item.name}</a></td>
            <td>${item.priority}</td>
            <td>${item.sn}</td>
            <td>${item.parent.name}</td>
            <td>${item.url}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
