<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
    <div class="searchBar">
    <div class="search_header">
    <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 网关测点查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/mesPointGateway/mesPointsData/${pointGatewayId}" data-target="mesDriverPointsTable" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle" >测点名称</label>
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_mesPoints.name" value="${param.search_LIKE_mesPoints.name}">
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
        </div>
        <div class="driver_info"> 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">网关名称:</label> ${pointGaqteway.name}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">MAC地址:</label> ${pointGaqteway.mac}
              </div>
            </form>
            </div>
        <div id="toolBar" class="clearfix" style=" margin-bottom: -6px ">
          <div class="btn-group pull-left">
          		<shiro:hasPermission name="gateway:savePoint">
                <a class="btn btn-default1" target="dialog" refresh="true" href="${contextPath}/mesPointGateway/addMesPoint/${pointGatewayId}">
                  <i class="fa fa-plus"></i> 
                  <span>添加网关测点</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateway:editPoint">
                <a class="btn btn-default1" target="dialog" data-target="mesDriverPointsTable" refresh="true" href="${contextPath }/mesPointGateway/findMesPointById/{id}">
                  <i class="fa fa-pencil"></i> 
                  <span>修改网关测点</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="gateway:deletePoint">
                <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="mesDriverPointsTable" rel="ids" href="${contextPath }/mesPointGateway/deleteMesPointById" title="确认要删除?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除网关测点</span>
                </a>
                </shiro:hasPermission>
          </div>
        </div>
        <table class="table table-striped" id="mesDriverPointsTable" data-field="mesDriverPoints" data-url="${contextPath }/mesPointGateway/mesPointsData/${pointGatewayId}">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22"><input
                class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
              <th data-field="mesPoints.name" width="100">测点名称</th>
              <th data-field="mesPoints.mesPointGateway.name" width="100">所属网关</th>
              <!-- <th data-field="codekey" width="100">codekey</th> -->
              <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
              <th data-field="mesPoints.datatype" width="100">数据类型</th>
              <th data-field="mesPoints.units" width="100">单位</th>
            </tr>
          </thead>
        </table> 
<!-- Modal -->
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer" style="margin-top:15px;">
  <button type="button" id="mesPointBtn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
$("#mesPointBtn").click(function(){
	$.table.setCurrent("table");
})
</script>
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
    $.table.init('mesDriverPointsTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesDriverPointsTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesDriverPoints[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>