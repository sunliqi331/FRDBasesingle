<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="dwz" uri="http://www.ketayao.com/dwz"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
<!--
$(document).ready(function(){
  $.table.init('lookupDataCtl',{}, function(data){
	  var $p = $('#lookupDataCtl').find('tbody');
	  $('input[type="checkbox"]',$p).each(function(i){
		  var $this = $(this);
		  var rpid = ${param['rp.id'] };
		  $this.val("{'dataControl.id':'"+ data.dataControls[i].id + "','dataControl.name':'" + data.dataControls[i].name + "','rolePermission.id':'" + rpid + " '}");
	  });
  });
});
//-->
</script>
<div class="pageContent">  
<form method="post" action="${contextPath }/management/security/role/lookup/data?rp.id=${param['rp.id'] }&prefix=${param.prefix }" data-target="lookupDataCtl" onsubmit="return navTabSearch(this)">
  <div class="pageHeader">
    <div class="searchBar">
      <ul class="searchContent">
        <li>
          <label>名称：</label>
          <input type="text" name="search_LIKE_name" value="${param.search_LIKE_name}"/>      
        </li>
      </ul>
      <div class="subBar">
        <ul>
          <li><button type="submit" class="btn btn-default"><i class="icon-search"></i> 搜索</button></li>
          <li><button type="reset" class="btn btn-default"><i class="icon-trash"></i> 清空</button></li>
          <li> <button type="button" multLookup="${param.prefix }rolePermissionDataControls[]" class="btn btn-default"><i class="icon-mail-reply-all"></i> 选择带回</button>
               <input type="hidden" value="{'rolePermission.id':'${param['rp.id'] }'}"></li>
        </ul>
      </div>
    </div>
  </div>
</form>
  <table id="lookupDataCtl" class="table" data-url="${contextPath }/management/security/role/lookup/data?rp.id=${param['rp.id'] }&prefix=${param.prefix }" data-select-item-name="${param.prefix }rolePermissionDataControls[]" data-field="dataControls" targetType="dialog" width="100%">
    <thead>
      <tr>
        <th data-checkbox="true" width="22"></th>
        <th data-field="name" width="200">名称</th>
        <th data-field="control" width="400">条件</th>
        <th data-field="description">描述</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="item" items="${dataControls}">
      <tr>
        <td><input type="checkbox" name="${param.prefix }rolePermissionDataControls[]" value="{'dataControl.id':'${item.id }', 'rolePermission.id':'${param['rp.id'] }', 'dataControl.name':'${item.name }'}"></td>
        <td data-field="name">${item.name}</td>
        <td data-field="control">${item.control}</td>
        <td data-field="description">${item.description}</td>
      </tr>
      </c:forEach>
    </tbody>
  </table>
</div>