<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<div class="searchBar">
<div class="search_header">
  <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 设备类型属性查询条件
           </div>
           <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/drivertype/driverTypePropertyData/${mesDrivertype.id}" data-target="mesDrivertypePropertysTable" onsubmit="return navTabSearch(this)">
    <div class="form-group">
      <label for="inputText" class="searchtitle" style="width: 110px;">设备类型属性名</label>
      <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_propertyname" value="${param.search_LIKE_propertyname}" />
    </div>
    <button type="submit" class="btn btn-info btn-search1">搜索</button>
  </form>
  </div>
</div>
<div class="driver_info" style="margin-bottom: 0"> 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备类型名称:</label> ${mesDrivertype.typename}
              </div>
            </form>
            </div>
<div id="toolBar" >
<div class="btn-group pull-left clearfix" style="margin-bottom: 10px;margin-top:10px;">
	<shiro:hasPermission name="driverType:saveProperty">
    <a class="btn btn-default1" target="dialog" data-target="mesDrivertypePropertysTable" refresh="true" rel="create_prop" href="${contextPath}/drivertype/addDriverAttribute/${mesDrivertypeId}?pagename=addDriverAttribute"> 
        <i class="fa fa-plus"></i> 
        <span>添加类型属性</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="driverType:editProperty">
    <a class="btn btn-default1" target="dialog" data-target="mesDrivertypePropertysTable" refresh="true" rel="edit_prop" href="${contextPath}/drivertype/findTypePropertyById/{slt_uid}">
        <i class="fa fa-pencil"></i> 
        <span>修改类型属性</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="driverType:deleteProperty">
    <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="mesDrivertypePropertysTable" rel="ids" href="${contextPath }/drivertype/deleteDriverTypeProperty" title="确认要删除?"> 
        <i class="fa fa-remove"></i> 
        <span>删除类型属性</span>
    </a>
    </shiro:hasPermission>
  </div>
  </div>
<table class="table table-striped" id="mesDrivertypePropertysTable" data-field="mesDrivertypePropertys" data-url="${contextPath}/drivertype/driverTypePropertyData/${mesDrivertype.id}">
  <thead>
    <tr>
         <th data-field="Number" width="2%" data-align="center">序号</th>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids">
      </th>
      <th data-field="propertykeyid" width="100">设备类型属性号</th>
      <th data-field="propertyname" width="100">设备类型属性名</th>
      <!-- <th data-field="datatype" width="100">数据类型</th>
      <th data-field="units" width="100">单位</th> -->
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
    $.table.init('mesDrivertypePropertysTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesDrivertypePropertysTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesDrivertypePropertys[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer">
  <button type="button" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>