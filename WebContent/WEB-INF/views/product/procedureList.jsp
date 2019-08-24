<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="searchBar">
<div class="search_header">
<i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 工序查询条件
           </div>
           <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/procedure/procedureData/${productId}" data-target="mesProductProcedureTable" onsubmit="return navTabSearch(this)">
    <div class="form-group">
      <label for="inputText" class="searchtitle">工序号</label> <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_procedurenum" value="${param.search_LIKE_procedurenum}"/>
    </div>
    <button type="submit" class="btn btn-info btn-search1">搜索</button>
  </form>
  </div>
</div>
<div class="driver_info"> 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品编号:</label> ${product.productnum}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品名称:</label> ${product.name}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">产品型号:</label> ${product.modelnum}
              </div>
            </form>
            </div>
<div id="toolBar">
  <div class="btn-group pull-left clearfix" style="margin-bottom: 10px;">
  <shiro:hasPermission name="Product:saveProcedure">
    <a class="btn btn-default1" target="dialog" rel="create_procedure" refresh="true" href="${contextPath}/procedure/addprocedure/${productId}"> 
    <i class="fa fa-plus"></i> 
    <span>添加工序</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="Product:editProcedure">
    <a class="btn btn-default1" target="dialog" data-target="mesProductProcedureTable" refresh="true" rel="edit_procedure" href="${contextPath}/procedure/findProcedureById/{slt_uid}?pagename=editProcedure"> 
    <i class="fa fa-pencil"></i> 
      <span>修改工序</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="Product:deleteProcedure">
    <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="mesProductProcedureTable" rel="ids" href="${contextPath }/procedure/deleteProcedureById" title="确认要删除?"> 
    <i class="fa fa-remove"></i> 
    <span>删除工序</span>
    </a>
    </shiro:hasPermission>
    <shiro:hasPermission name="Product:showProcedure">
    <a class="btn btn-default1 btn-tool" target="dialog" data-target="mesProductProcedureTable"  rel="ids" href="${contextPath }/procedure/findProcedureById/{slt_uid}?pagename=viewProcedure"> 
    <i class="fa fa-eye"></i> 
    <span>查看工序信息</span>
    </a>
    </shiro:hasPermission>
    <shiro:hasPermission name="Product:viewProperty">
    <a class="btn btn-default1" target="page" data-target="mesProductProcedureTable" rel="view_procedure_property" href="${contextPath }/procedure/ProcedurePropertyList/{slt_uid}?pagename=ProcedurePropertyList">
    <i class="fa fa-cogs"></i> 
    <span>配置工序属性</span>
    </a>
    </shiro:hasPermission>
  </div>
</div>
<br />
<table class="table table-striped" id="mesProductProcedureTable"
  data-field="mesProductProcedure"
  data-url="${contextPath}/procedure/procedureData/${productId}">
  <thead>
    <tr>
         <th data-field="Number" width="2%" data-align="center">序号</th>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
      <th data-field="procedurenum" width="100">工序号</th>
      <th data-field="procedurename" width="100">工序名称</th>
      <th data-field="mesProduct.name" width="100">所属产品</th>
      <th data-field="nextprocedurename" width="100">步骤序号</th>
      <th data-field="customername" width="100">客户名称</th>
<!--       <th data-field="ops" width="100">ops</th> -->
      <!-- <th data-field="partname" width="100">零件名称</th> -->
      <!-- <th data-field="customerpicturenum" width="100">客户图号</th>
      <th data-field="versiondate" width="100">版本日期</th>
      <th data-field="swpicturenum" width="100">森威图号</th>
      <th data-field="drivermodel" width="100">设备名称</th>
      <th data-field="drivernum" width="100">设备编号</th> -->
      <!-- <th data-field="jipname" width="100">夹具名称</th>
      <th data-field="jippicturenum" width="100">夹具图号</th>
      <th data-field="operationnum" width="100">操作指导书编号</th>
      <th data-field="operationversion" width="100">操作指导书版本</th> -->
      <!-- <th data-field="materialnum" width="100">材料牌号</th> -->
      <!-- <th data-field="materialstandards" width="100">材料规格</th>
      <th data-field="weightsmaterials" width="100">下料重量</th> -->
      <!-- <th data-field="blankshapesize" width="100">毛坯外形尺寸</th>
      <th data-field="makecase" width="100">每坯可制作数</th>
      <th data-field="reservation" width="100">周转装具</th> -->
      <th data-field="workshop" width="100">生产车间</th>
      <!-- <th data-field="remarks" width="100">备注</th> -->
    </tr>
  </thead>
</table>
<!-- Modal -->
<script type="text/template" id="dialogTemp">
  <div class="modal fade " tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
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
    $.table.init('mesProductProcedureTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesProductProcedureTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesProductProcedure[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer" style="margin-top:15px">
  <button type="button" id="procedureBtn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
$("#procedureBtn").click(function(){
	$.table.setCurrent("table");
})
</script>