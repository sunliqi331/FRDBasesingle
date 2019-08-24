<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>日志管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body >
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
  <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
      <li>日志管理</li>
    </ol>
    <div class="main-wrap">
      <div class="main-body">
       
        <div class="searchBar search_driver search_log">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 日志查询条件
           </div>
           <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath }/management/security/logInfo/data" data-target="table" onsubmit="return navTabSearch(this)">
            <div class="searchcontent">
              <div class="form-group">
                <label for="inputText" class="searchtitle">登录名称</label>
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_username" value="${param.search_EQ_username }"/>
              </div>
              <%-- div class="form-group">
                <label for="inputText" class="searchtitle">日志等级</label>  
                 <select name="search_EQ_logLevel" class="form-control searchtext">
                  <option value="">所有</option>
                  <c:forEach var="logLevel" items="${logLevels }"> 
                    <option value="${logLevel}" ${param.search_EQ_logLevel == logLevel ? 'selected="selected"':'' }>${logLevel}</option>
                  </c:forEach>
                </select>  
              </div> --%>
<!--             </div> -->
<!--             <div class="searchcontent"> -->
              <div class="form-group" >
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                        <input class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;" value="" readonly> 
                 <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                         <!-- <i class="fa fa-calendar" style="font-size: 16px"></i></span> -->
                      </div>
                      <input type="hidden" id="dtp_input1" value="" name="search_GTE_createTime" />
              </div>
              <div class="form-group" >
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                        <input class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                          <!-- <i class="fa fa-calendar" style="font-size: 16px"></i></span> -->
                      </div>
                      <input type="hidden" id="dtp_input2" value="" name="search_LTE_createTime" />
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">　登录IP</label>
                <input type="text" class="form-control searchtext validate[custom[url]]" id="inputText" name="search_LIKE_ipAddress" value="${param.search_EQ_ipAddress }"/>
              </div>
              
              <div class="form-group"><button type="submit" onclick="time()" class="btn btn-info btn-search1">搜索</button></div>
            </div>
          </form>
          </div>
        </div>  
        <div id="toolBar">
          <div class="btn-group pull-left">
            
            <shiro:hasPermission name="LogInfo:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" callback="deleteCallback" href="${contextPath}/management/security/logInfo/delete" title="确认要删除?">
                <i class="fa fa-remove"></i> 
                <span>删除日志</span>
              </a>
            </shiro:hasPermission>
          </div>
        </div>
        <table class="table table-striped" id="table" data-field="logInfos" data-url="${contextPath }/management/security/logInfo/data">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
              <th data-field="username" width="100">登录名称</th>
              <th data-field="ipAddress" width="100">登录IP</th>
              <th data-field="logLevel" width="200">日志等级</th>
              <th data-field="message" orderField="logLevel" width="120">日志内容</th>
              <th data-field="createTime" orderField="createTime" width="120">创建时间</th>  
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
<c:set var="ParentTitle" value="Security"/>
<c:set var="ModuleTitle" value="LogInfo"/>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>


<script>

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
	var start = $('input[name="search_GTE_createTime"]').val();
	var end = $('input[name="search_LTE_createTime"]').val();
	start = start.substring(0,10);
	end = end.substring(0,10);
	$('input[name="search_GTE_createTime"]').val(start.toString('yyyy-MM-dd'));
    $('input[name="search_LTE_createTime"]').val(end.toString('yyyy-MM-dd'));
}

// $(document).ready(function (){
	<!--       var query = {}; -->
	<!--       $.each($("#formID").serializeArray(),function(){ -->
	<!--           query[this.name] = this.value; -->
	<!--       }); -->
// 	      $.table.setCurrent("logInfosTable");
// 	      $.table.refreshCurrent($("#logInfosTable").attr('data-url'), "",function(data){
// 	          add_nicescroll();
// 	      });
// 	      $.table.init('logInfosTable', {
// 	             pagination:false,
// 	             sidePagination:'server'
// 	         });
// 	  })
</script>
<%-- <script src="${contextPath}/styles/assets/bootstrap-daterangepicker/date.js"></script> --%>
<%-- <script src="${contextPath}/styles/assets/bootstrap-daterangepicker/daterangepicker.js"></script> --%>

</body>
</html>
