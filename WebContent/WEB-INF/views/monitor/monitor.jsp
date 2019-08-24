<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="title" content="FRD - Demo">
<title id="tt" >监控画面</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link href="${contextPath }/styles/css/jquery-ui.min.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery.contextMenu.css" rel="stylesheet">
<link href="${contextPath }/styles/colorpicker/css/bootstrap-colorpicker.css" rel="stylesheet">
<link href="${contextPath }/styles/css/editable-widget.css" rel="stylesheet">
<link href="${contextPath }/styles/css/monitor.css" rel="stylesheet">
</head>
<script type="text/javascript">
var contextPath = "${contextPath}";

var tableLine = {};
// 定时器返回值：用clear定时器
var interval;
// 定时器返回值：用clear定时器
var interval_;
var interval__ = new Array();
var isFullScreen = false;
// 监控内容
var subscriber = null;
// connectClientStatus：定时器链接状态  connectSubscriberStatus：ActiveMQ的链接状态
var connectClientStatus = null,connectSubscriberStatus = null;
// ActiveMQ的链接对象
var monitorClient = null;
// 通过webscoket.js ，storm.js 通信到activeMq获取内容
var monitorSubscriber = null;
// 从jmsConfig.properties中获取，ActiveMQ的通信地址
var monitorStomp = "${monitorStomp}";
var processArray = [];
var driverTimeArray = [];
</script>
<body>

<style>
circle{ 
 display:none; 
 } 
 .searchBar .form-inline{
   padding:10px 0 0 0;
 } 
 .searchBar lable.searchtitle{
   padding:0;
 }
 .form-inline  .form-group{
   margin-bottom:5px;
 }
</style>
<div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp" %>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp" %>
    
    <div class="main-content removeble">
      <ol class="breadcrumb" style="background: #f1f2f7; height:41px; margin:0;line-height:40px;    border-radius: 0; display:block;">
        <li >
        	<i class="fa fa-home"></i>
        	<a href="${contextPath}/management/index"> 首页</a>
        </li>
        <li style="display:none">
        	<i class="fa fa-hand-o-left" ></i>
        	<a id="goBack" style="cursor: pointer;">返回监控</a>
        </li>
        <li >监控画面</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body" style="overflow-y: hidden;">
		  <div class="searchBar " style="padding:0;">
		   <div class="search_header">
              <i class="fa fa-hand-pointer-o"></i> 监控操作
          </div>
			<input type="hidden" id="productionLineId" />
			<form class="form-inline" >
			 <div class="clearfix">
			 <div class="monitorSearch">
			    <div class="col-sm-4">
			      <div class="form-group " >
				  <label for="monitorPainterList" class="searchtitle" style="width: 95px">监控页面选择</label>
				  <select id="monitorPainterList" class="form-control" style="padding-right:20px; min-width:160px">
					<c:forEach items="${monitorPainterList}" var="monitorPainter">
					  <option value="${monitorPainter.id }">${monitorPainter.name }</option>
					</c:forEach>
				  </select>
				  </div>
				  </div>
				   <div class="col-sm-3">
				   <div class="form-group" style="padding-top:5px">
					  <div class="monitorPeople">
					    <label class="searchtitle" >创建人：</label><span id="founder"  style="border:none;">${user.realname }</span>
				      </div>
					  	<!-- 创建人ID -->
					  	<input type="hidden" value="${user.id }" id="founderId">
					  	<!-- 当前用户ID -->
					 	<input type="hidden" value="${currentUserId }" id="currentUserId">
	
				  </div>
				</div>
				 
				  <div class="col-sm-5">
				  <div class="form-group pull-left" ><a id="startMonitor" class="btn btn-primary">开始监控</a></div>
				  <div class="form-group pull-left" ><a id="stopMonitor" disabled="disabled" class="btn btn-danger">停止监控</a></div>
				 
					  <shiro:hasPermission name="monitor:delete">
					    <div class="form-group pull-left" ><a id="del" href="javascript:void(0);" class="btn btn-primary" style="margin-right:-1px;">删除监控</a></div>
					  </shiro:hasPermission>
					  <div class="form-group pull-left" ><a id="edit" onclick="javascript:monitorIndex()" href="javascript:void(0);" class="btn btn-primary">返回设计界面</a></div>
				  </div>
			 </div> 
		  </div>
			</form>	
			
			<!-- 要上传的监控画面文件 -->
			<form action="#" id="painterFileForm" method="post" hidden="true" enctype="multipart/form-data">
				<input type="file" name="painterFile" id="painterFile">
				<input type="text" name="painterNewName" id="painterNewName">
			</form>
			<iframe id="ifile" style="display:none"></iframe>
			
		 </div>
		 <div class="monitor_content" id="canvas" >
		   <div id="downarea" style="width:40px;height:40px;margin-left:5px;position:absolute;z-index:9999;">
		    <a id="down_load_hidden" target="blank" style="display: none;"></a>
			<a id="down_button" href="javascript:void(0)" onclick="domShot()" style="display: none" ><img  src="${contextPath}/styles/img/download_24px.png" width="30"> </a>
		   </div>		 
		 	<div id="screenArea" style="width: 40px;height:40px;margin-top:20px;position: absolute;z-index: 9999;">
		     <ul class="monitor_large">
		       <li ><a href="#"  id="maxScreen" style="display: none"><img src="${contextPath}/styles/img/large.png" width="40"/></a></li>
		       <li ><a href="#" id="minScreen" style="display: none"><img src="${contextPath}/styles/img/small.png" width="40"/></a></li>
		     </ul>
		     </div>
		       <div id="monitorPainter">
				 <div  class="demo ui-sortable"  id="monitor_Content" widget-type="Container">
			     </div>
		      </div>
		 </div>
	     </div>
	   </div>
</div>
</div>
    <!-- Modal -->
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
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
<c:set var="ParentTitle" value="monitorManage" />
<c:set var="ModuleTitle" value="monitor" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
<script type="text/javascript" src="${contextPath }/js/chart-data.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.contextMenu.js"></script>
<script type="text/javascript" src="${contextPath }/js/jsPlumb-2.1.5.js"></script>
<script type="text/javascript" src="${contextPath }/styles/colorpicker/js/bootstrap-colorpicker.js"></script>
<script type="text/javascript" src="${contextPath }/js/editable-widget.js"></script>
<script src="${contextPath}/js/countChart/echartDriver.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.index.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.render.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
<script src="${contextPath }/js/sockjs.js"></script>
<script src="${contextPath }/js/stomp.js"></script>
<%-- <script type="text/javascript" src="${contextPath }/js/monitor.connector.js"></script> --%>


<!-- 画面截图/监控画面导入导出 -->
<script type="text/javascript" src="${contextPath}/js/html2canvas.js"></script>
<script type="text/javascript" src="${contextPath}/js/monitor/domShot.js"></script>
<!-- 网管监控逻辑 -->
<script type="text/javascript" src="${contextPath}/js/monitor/macStatusMonitor.js"></script>
<!-- 产品信息 -->
<script type="text/javascript" src="${contextPath}/js/monitor/productInfo.js"></script>
<!-- 设备警告信息监控 -->
<script type="text/javascript" src="${contextPath}/js/monitor/timeSeter.js"></script>
<!-- 建立MQ链接，订阅，取消订阅，清除缓存 -->
<script type="text/javascript" src="${contextPath}/js/monitor/connection.js"></script>
<!-- 监控运行逻辑 -->
<script type="text/javascript" src="${contextPath}/js/monitor/doingMonitor.js"></script>
<!-- 收取MQ的订阅消息，描画画面元素 -->
<script type="text/javascript" src="${contextPath}/js/monitor/doSubscriber.js"></script>
<!-- 全屏/取消全屏/删除监控 -->
<script type="text/javascript" src="${contextPath}/js/monitor/monitorPageChange.js"></script>
<!-- 画面元素初期化描画 -->
<script type="text/javascript" src="${contextPath}/js/monitor/pageLoad.js"></script>
<!-- 画面EVENT -->
<script type="text/javascript" src="${contextPath}/js/monitor/clientevent.js"></script>
<!-- 码表option -->
<script type="text/javascript" src="${contextPath }/js/monitor/driverGauge.js"></script>
</body>
</html>
