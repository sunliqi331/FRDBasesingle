<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="searchBar">
<div class="search_header">
<i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 部门查询条件
           </div>
           <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/personnel/departmentData/${userId}" data-target="departmentTable" onsubmit="return navTabSearch(this)">
  <input type="hidden" id="companyId" value="${companyId }">
    <div class="form-group">
      <label for="inputText" class="searchtitle">部门</label> <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_department.name" value="${param.search_LIKE_department.name}"/>
    </div>
    <button type="submit" class="btn btn-info btn-search1">搜索</button>
  </form>
  </div>
</div>
<div class="driver_info"> 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">用户名:</label> ${user.username}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">真实姓名:</label> ${user.realname}
              </div>
            </form>
            </div>
<div id="toolBar">
  <div class="btn-group pull-left clearfix" style="margin-bottom: 10px;">
  <shiro:hasPermission name="Personnel:saveDepartment">
    <a class="btn btn-default1" target="dialog" rel="add_department" refresh="true" href="${contextPath}/personnel/addDepartment/${userId}"> 
    <i class="fa fa-plus"></i> 
    <span>添加部门</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="Personnel:deleteDepartment">
    <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="departmentTable" rel="ids" href="${contextPath }/personnel/deleteDepartmentById" title="确认要删除?"> 
    <i class="fa fa-remove"></i> 
    <span>删除部门</span>
    </a>
    </shiro:hasPermission>
  </div>
</div>
<br />
<table class="table table-striped" id="departmentTable"
  data-field="department"
  data-url="${contextPath}/personnel/departmentData/${userId}">
  <thead>
    <tr>
         <th data-field="Number" width="2%" data-align="center">序号</th>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
 <!--      <th data-field="id" width="100">序号</th> -->
      <!-- <th data-field="userid" width="100">人员</th> -->
      <th data-field="department.name" width="100">部门</th>
      <th data-field="department.parentInfo" width="100">所属</th>
      
<!--       <th data-field="" width="100">部门描述</th> -->
    </tr>
  </thead>
</table>
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
  $(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('departmentTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#departmentTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.department[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer" style="margin-top:15px">
  <button type="button" id="Btn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
$("#Btn").click(function(){
	$.table.setCurrent("table");
	$.table.refreshCurrent("${contextPath}/personnel/data");
})
</script>