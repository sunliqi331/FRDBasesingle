<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="searchBar">
<div class="search_header">
<i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 权限转移记录查询条件
           </div>
           <div class="ishidden" >
  <form class="form-inline" method="post" action="${contextPath}/personnel/transferPermissionRecordData" data-target="recordTable" onsubmit="return navTabSearch(this)">
    <div class="form-group">
      <label for="inputText" class="searchtitle">转移前人员</label> <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_userByFromuserid.realname" value="${param.search_LIKE_userByFromuserid.realname}"/>
    </div>
    <button type="submit" class="btn btn-info btn-search1">搜索</button>
  </form>
  </div>
</div>
<table class="table table-striped" id="recordTable" data-field="mesPermissionTransfer" data-url="${contextPath}/personnel/transferPermissionRecordData">
  <thead>
    <tr>
      <th data-field="Number" width="22" data-align="center">序号</th>
      <th data-field="userByFromuserid.realname" width="100">转移前人员</th>
      <th data-field="userByTouserid.realname" width="100">转移后人员</th>
      <th data-field="updatetime" width="100">转移日期</th>
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
    $.table.init('recordTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#recordTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesPermissionTransfer[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer">
  <button type="button" id="positionBtn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>