<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>告警邮件管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
</head>
<body scroll="no">
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>告警邮件管理</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
        <div class="searchBar search_driver">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 告警邮件管理查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/sendAlarm/Data" data-target="table" onsubmit="return navTabSearch(this)">
            <ul class="searchcontent">
              <li class="form-group">
                <label for="inputText" class="searchtitle">真实姓名</label>
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_user.realname" value="${param.search_LIKE_user.realname }"/>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text" style="background: #fff; border-color: #c8ced6; width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" name="search_GTE_creattime" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                  <input class="form-control datetime" type="text" style="background: #fff; border-color: #c8ced6;width:100%" value="" readonly> 
                   <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input2" value="" name="search_LTE_creattime" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">信息类型</label>
                <select name="search_LIKE_alarttype" data-placeholder="请选择信息类型" class="form-control searchtext" >
                      <option value="">全部</option>
                      <option value="driver">设备</option>
                      <option value="product">产品</option>
                </select>
              </li>
              <li class="form-group alarm_search">
              <button type="submit" onclick="time()" class="btn btn-info btn-search1">搜索</button>
            </li>
            </ul>
          </form>
          </div>
        </div>  
         <div id="toolBar">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_sendAlarm" href="${contextPath}/sendAlarm/addSendAlarm">
                <i class="fa fa-plus"></i>
                <span>添加</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:delete">
              <a class="btn btn-default1" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/sendAlarm/deleteMany" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>删除</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
        <table class="table table-striped" id="table" data-field="mesAlarmSendRelations" data-url="${contextPath}/sendAlarm/Data">
            <thead>
              <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"><input
                  class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="user.username" width="100">用户名称</th>
                <th data-field="user.realname" width="100">真实姓名</th>
                <th data-field="talarttype" width="100">告警信息类型</th>
                <th data-field="creattime" width="100">时间</th>
              </tr>
            </thead>
          </table>
       </div>
    </div>
  </div>
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
        <div class="modal-body">
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="Alarm" />
<c:set var="ModuleTitle" value="sendAlarm" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>

<script>
$(document).ready(function(){
    $("select").chosen({search_contains:true});
});
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
$('.form_datetime2').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
</script>
<script type="text/javascript">
function time(){
    var start = $('input[name="search_GTE_creattime"]').val();
    var end = $('input[name="search_LTE_creattime"]').val();
    start = start.substring(0,10);
    end = end.substring(0,10);
    $('input[name="search_GTE_creattime"]').val(start.toString('yyyy-MM-dd'));
    $('input[name="search_LTE_creattime"]').val(end.toString('yyyy-MM-dd'));
}
</script>

</body>
</html>
