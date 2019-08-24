<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
 <%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
 <a id="refreshJbsxBox2lineTree"  rel="jbsxBox2lineTree" target="ajax"  href="${contextPath}/productLine/tree" style="display:none;"></a> 
<div class="">
<div class="searchBar">
  <div class="search_header search_driver">
  <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
   <i class="fa fa-search"></i> 设备查询条件
  </div>
  <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/productline/DriverData/${id}" data-target="mesDriversTable" onsubmit="return navTabSearch(this)">
  <input type="hidden" id="companyId" value="${companyId}">
  <input type="hidden" id="parentId" value="${parentId}">
    <div class="form-group">
                <label for="inputText" class="searchtitle">设备类型</label> 
                <select name="search_LIKE_mesDrivertype.typename" class="form-control searchtext" style="padding-right:100px">
                      <option></option>
                      <c:forEach var="p" items="${mdt }">
                        <option value="${p.typename }">${p.typename }</option>
                      </c:forEach>
                </select>
              </div>
              
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name"/>
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备编号</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_sn"/>
              </div>
    <button type="submit" class="btn btn-info btn-search1">搜索</button>
  </form>
  </div>
</div>
<div class="driver_info" style="margin-bottom: 0"> 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">产线名称:</label> ${line.linename}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">产线编号:</label> ${line.linesn}
              </div>
            </form>
            </div>
<div id="toolBar "  class="clearfix" >
    <div class="btn-group pull-left" style="margin-top: 10px;margin-bottom: -5px;">
    <shiro:hasPermission name="Productline:saveDriver">
       <a class="btn btn-default1 btn-tool" target="dialog" data-target="mesDriversTable" refresh="true" rel="addDrivers" href="${contextPath}/productline/addLineDriver/${lineId}" >
         <i class="fa fa-plus"></i> 
         <span>添加设备</span>
       </a>
       </shiro:hasPermission>
       <shiro:hasPermission name="Productline:deleteDriver">
       <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="mesDriversTable" rel="ids" href="${contextPath }/productline/deleteDriver" title="确认要移除该设备?"> 
         <i class="fa fa-remove"></i> 
         <span>删除设备</span>
       </a>
       </shiro:hasPermission>
    </div>
   
</div>

<table class="table table-striped" id="mesDriversTable" data-field="mesDrivers" data-url="${contextPath}/productline/DriverData/${id}">
  <thead>
    <tr>
         <th data-field="Number" width="2%" data-align="center">序号</th>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
      <th data-field="sn" width="100">设备编号</th>
      <th data-field="name" width="100">设备名称</th>
      <th data-field="mesDrivertype.typename" width="100">设备类型</th>
      <th data-field="mesProductline.linename" width="100">所属产线</th>
      <th data-field="companyinfo.companyname" width="100">所属公司</th>
    </tr>
  </thead>
</table>
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
<script type="text/javascript">
$("#jbsxBox2lineTree").click(function(){
	window.location.reload(); 
});
  $(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('mesDriversTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesDriversTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesDrivers[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<div class="modal-footer" style="margin-top:15px">
  <button type="button" id="Btn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
 <script type="text/javascript">
 $("#Btn").click(function(){
	$.table.setCurrent("table");
	$.table.refreshCurrent("${contextPath}/productline/productlineData/" + $("#companyId").val());
}); 

</script> 