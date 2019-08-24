<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="searchBar">
<div class="search_header">
<i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 职位查询条件
           </div>
           <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/personnel/positionData/${userId}" data-target="positionTable" onsubmit="return navTabSearch(this)">
  <input type="hidden" id="companyId" value="${companyId }">
    <div class="form-group">
      <label for="inputText" class="searchtitle">职位</label> <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_mesCompanyPosition.positionname" value="${param.search_LIKE_mesCompanyPosition.positionname}"/>
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
  <shiro:hasPermission name="Personnel:savePosition">
    <a class="btn btn-default1" target="dialog" rel="add_position" href="${contextPath}/personnel/addPosition/${userId}"> 
    <i class="fa fa-plus"></i> 
    <span>添加职位</span>
    </a> 
    </shiro:hasPermission>
    <shiro:hasPermission name="Personnel:deletePosition">
    <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="positionTable" rel="ids" href="${contextPath }/personnel/deletePositionById" title="确认要删除?"> 
    <i class="fa fa-remove"></i> 
    <span>删除职位</span>
    </a>
    </shiro:hasPermission>
  </div>
</div>
<table class="table table-striped" id="positionTable"
  data-field="position"
  data-url="${contextPath}/personnel/positionData/${userId}">
  <thead>
    <tr>
         <th data-field="Number" width="2%" data-align="center">序号</th>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids">
      </th>
      <!-- <th data-field="user.name" width="100">人员</th> -->
      <th data-field="mesCompanyPosition.positionname" width="100">职位</th>
<!--       <th data-field="" width="100">职位描述</th> -->
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
    $.table.init('positionTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#positionTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.position[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer">
  <button type="button" id="positionBtn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
$("#positionBtn").click(function(){
	$.table.setCurrent("table");
	$.table.refreshCurrent("${contextPath}/personnel/data");
})
</script>