<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
        <div class="searchBar search_driver clearfix">
           <div class="search_header">
           <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 设备测点信息
           </div>
           <div class="ishidden" >
            <form id="searchForm" class="form-inline" method="post" action="${contextPath}/driver/driverPointData/${driverId}" data-target="statusMesDriverPointsTable" onsubmit="return navTabSearch(this)">
              <ul class="searchcontent" style="margin-bottom:0px">
              <li class="form-group">
                <label for="inputText" class="searchtitle">测点名称</label> 
                <input type="text" class="form-control searchtext" id="mesPointsName" name="search_LIKE_mesPoints.name"/>
              </li>
              <li class="form-group">
              <button id="search" type="submit" class="btn btn-info btn-search1">搜索</button>
        </li>
        </ul>
            </form>
            </div>
          </div>
          <div class="driver_info" > 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备编号:</label> ${driver.sn}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备名称:</label> ${driver.name}
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">设备类型:</label> ${driver.mesDrivertype.typename}
              </div>
            </form>
            </div>
    <ul class="tabs clearfix"  >  
        <li class="active"><a class="tab1" href="#tab1">设备状态测点</a></li>  
        <li style="margin-left: -1px"><a class="tab4" href="#tab4">设备属性测点</a></li>  
        <li style="margin-left: -1px"><a class="tab6" href="#tab6">产品工序测点</a></li>  
        <li style="margin-left: -1px"><a class="tab7" href="#tab7">统计类测点</a></li> 
        <li style="margin-left:-1px"><a class="tab8" href="#tab8">设备告警测点</a></li> 
    </ul>
    <div class="tab_container">  
        <div id="tab1" class="tab_content" style="display: block; ">  
          <div id="toolBar1" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" data-target="statusMesDriverPointsTable" rel="add_statusdriverPoint" refresh="true" href="${contextPath}/driver/addDriverPoint/${driverId}/1">
                <i class="fa fa-plus"></i> 
                <span>添加状态测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:deletePoint">
              <a class="btn btn-default1" target="selectedTodo" data-target="statusMesDriverPointsTable" rel="ids" href="${contextPath}/driver/deleteDriverPoint" title="确认要删除吗？">
                <i class="fa fa-remove"></i> 
                <span>删除状态测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:editPoint">
              <a class="btn btn-default1" target="dialog" data-target="statusMesDriverPointsTable" rel="configure_statusdriverPoint" refersh="true" href="${contextPath}/driver/configureDriverPoint/{slt_uid}/1">
                <i class="fa fa-cogs"></i> 
                <span>配置状态测点属性</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <input type="hidden" id="driverId" value="${driverId}">
          <table class="table table-striped" id="statusMesDriverPointsTable" data-field="statusMesDriverPoints" data-url="${contextPath}/driver/driverPointData/${driverId}">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
<!--                 <th data-field="mesDriver.name" width="100">设备名称</th> -->
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
                <th data-field="mesPoints.mesPointGateway.mac" width="100">测点网关</th>
              </tr>
            </thead>
          </table>
        </div>  
        <div id="tab4" class="tab_content" style="display: none; "> 
          <div id="toolBar2" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <input type="hidden" id="driverCheckRs" value="${driverCheckRs}">
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" id="addStatisticaldriverPoint" data-target="propertyMesDriverPointsTable" rel="add_propertydriverPoint" refresh="true" href="${contextPath}/driver/addDriverPoint/${driverId}/4">
                <i class="fa fa-plus"></i> 
                <span>添加设备属性测点</span>
              </a> 
              </shiro:hasPermission>
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" data-target="propertyMesDriverPointsTable" rel="edit_propertydriverPoint" refresh="true" href="${contextPath}/driver/editDriverPoint/{slt_uid}/${driverId}">
                <i class="fa fa-pencil"></i> 
                <span>编辑设备属性测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:deletePoint">
              <a class="btn btn-default1" target="selectedTodo" data-target="propertyMesDriverPointsTable" rel="ids" href="${contextPath}/driver/deleteDriverPoint" title="确认要删除吗？">
                <i class="fa fa-remove"></i> 
                <span>删除设备属性测点</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="propertyMesDriverPointsTable" data-field="propertyMesDriverPoints" data-url="${contextPath}/driver/driverPointData1/${driverId}">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
                <th data-field="mesDrivertypeProperty.propertyname" width="100">设备类型属性</th>
                <th data-field="maxValue" width="100">上公差</th>
                <th data-field="minValue" width="100">下公差</th>
                <th data-field="tvalidation" width="100">是否监控</th>
                <th data-field="mesPoints.mesPointGateway.mac" width="100">测点网关</th>
              </tr>
            </thead>
          </table> 
    </div>  
        <div id="tab6" class="tab_content" style="display: none; "> 
          <div id="toolBar3" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" data-target="procedureMesDriverPointsTable" rel="add_proceduredriverPoint" refresh="true" href="${contextPath}/driver/addDriverPoint/${driverId}/6">
                <i class="fa fa-plus"></i> 
                <span>添加产品工序测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:deletePoint">
              <a class="btn btn-default1" target="selectedTodo" data-target="procedureMesDriverPointsTable" rel="ids" href="${contextPath}/driver/deleteDriverPoint" title="确认要删除吗？">
                <i class="fa fa-remove"></i> 
                <span>删除产品工序测点</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="procedureMesDriverPointsTable" data-field="procedureMesDriverPoints" data-url="${contextPath}/driver/driverPointData2/${driverId}">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
                <th data-field="mesPoints.mesPointGateway.mac" width="100">测点网关</th>
              </tr>
            </thead>
          </table> 
    </div>  
        <div id="tab7" class="tab_content" style="display: none; "> 
          <div id="toolBar4" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" data-target="statisticalMesDriverPointsTable" rel="add_statisticaldriverPoint" refresh="true" href="${contextPath}/driver/addDriverPoint/${driverId}/7">
                <i class="fa fa-plus"></i> 
                <span>添加统计类测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:deletePoint">
              <a class="btn btn-default1" target="selectedTodo" data-target="statisticalMesDriverPointsTable" rel="ids" href="${contextPath}/driver/deleteDriverPoint" title="确认要删除吗？">
                <i class="fa fa-remove"></i> 
                <span>删除统计类测点</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="statisticalMesDriverPointsTable" data-field="statisticalMesDriverPoints" data-url="${contextPath}/driver/driverPointData3/${driverId}">
            <thead>
              <tr>
                   <th data-field="Number"  width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
                <th data-field="mesPoints.mesPointGateway.mac" width="100">测点网关</th>
              </tr>
            </thead>
          </table> 
    </div>
        <div id="tab8" class="tab_content" style="display: none; "> 
          <div id="toolBar5" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:savePoint">
              <a class="btn btn-default1" target="dialog" data-target="alarmMesDriverPointsTable" rel="add_alarmdriverPoint" refresh="true" href="${contextPath}/driver/addDriverPoint/${driverId}/21">
                <i class="fa fa-plus"></i> 
                <span>添加设备告警测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:deletePoint">
              <a class="btn btn-default1" target="selectedTodo" data-target="alarmMesDriverPointsTable" rel="ids" href="${contextPath}/driver/deleteDriverPoint" title="确认要删除吗？">
                <i class="fa fa-remove"></i> 
                <span>删除设备告警测点</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="alarmShow:edit">
              <a class="btn btn-default1" target="dialog" data-target="alarmMesDriverPointsTable" rel="edit_alarmRelation" href="${contextPath}/MesAlarmShow/editAlarmRelation/{slt_uid}?pagename=editAlarmRelation">
                <i class="fa fa-cogs"></i> 
                <span>配置告警测点信息</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div>
          <table class="table table-striped" id="alarmMesDriverPointsTable" data-field="alarmMesDriverPoints" data-url="${contextPath}/driver/driverPointData4/${driverId}">
            <thead>
              <tr>
                  <th data-field="Number"  width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="mesPoints.name" width="100">测点名称</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.mesPointType.name" width="100">测点类型</th>
                <th data-field="mesPoints.mesPointGateway.mac" width="100">测点网关</th>
              </tr>
            </thead>
          </table> 
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
    
    
    //updatedBy:xsq 7.12
    
//  					 $.table.init(
//		    		        	'alarmMesDriverPointsTable' ,{
//		    		        		toolbar:'#toolBar5'
//		    		        	}, function(data){
//		    		        		var $p =$('#alarmMesDriverPointsTable').find('tbody');
//		    		        		$('tr[data-uniqueid]',$p).each(function(i){
//		    		        			var $this =$(this);
//		    		        			var item =data.alarmMesDriverPoints[i];
//		    		        		})
//		    		        	});

      //2018 12 28 zq 修改初始化 start
      $.table.refreshCurrent("${contextPath}/driver/driverPointData/${driverId}");
      $.table.setCurrent("statusMesDriverPointsTable");
     $.table.init(
		    	'statusMesDriverPointsTable', {
		         toolbar : '#toolBar1'
		       }, function(data) {
		         var $p = $('#statusMesDriverPointsTable').find('tbody');
		         $('tr[data-uniqueid]', $p).each(function(i) {
		           var $this = $(this);
		   	    var item = data.statusMesDriverPoints[i];
		        //   $this.attr('url', item.storeType + '/' + item.uuid);
		           
		         });
		    	  $.table.init(
		    			  'propertyMesDriverPointsTable', {
		    		           toolbar : '#toolBar2'
		    		         }, function(data) {
		    		           var $p = $('#propertyMesDriverPointsTable').find('tbody');
		    		           $('tr[data-uniqueid]', $p).each(function(i) {
		    		             var $this = $(this);
		    		             var item = data.propertyMesDriverPoints[i];
		    		            // $this.attr('url', item.storeType + '/' + item.uuid);
		    		           });
		    		        
		    		           
		    		        
		    		         $.table.init(
				    			 'procedureMesDriverPointsTable', {
				    		           toolbar : '#toolBar3'
				    		         }, function(data) {
				    		           var $p = $('#procedureMesDriverPointsTable').find('tbody');
				    		           $('tr[data-uniqueid]', $p).each(function(i) {
				    		             var $this = $(this);
				    		             var item = data.procedureMesDriverPoints[i];
				    		            // $this.attr('url', item.storeType + '/' + item.uuid);
				    		           });

				    		        
				    		         $.table.init(
						    			 'statisticalMesDriverPointsTable', {
						    		           toolbar : '#toolBar4'
						    		         }, function(data) {
						    		           var $p = $('#statisticalMesDriverPointsTable').find('tbody');
						    		           $('tr[data-uniqueid]', $p).each(function(i) {
						    		             var $this = $(this);
						    		             var item = data.statisticalMesDriverPoints[i];
						    		           //  $this.attr('url', item.storeType + '/' + item.uuid);
						    		           });

                                             $.table.init(
                                                 'alarmMesDriverPointsTable' ,{
                                                     toolbar:'#toolBar5'
                                                 }, function(data){
                                                     var $p =$('#alarmMesDriverPointsTable').find('tbody');
                                                     $('tr[data-uniqueid]',$p).each(function(i){
                                                         var $this =$(this);
                                                         var item =data.alarmMesDriverPoints[i];
                                                     })
                                                 });
						    		     })
				    		     })
		    		   })
		    });
          //2018 12 28 zq 修改初始化 start
       
    		

    
    
    
    $(".tab_content").hide();
    $("ul.tabs li:first").addClass("active").show();
    $(".tab_content:first").show();
    $("ul.tabs li").click(function() {  
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab_content").hide();
        var activeTab = $(this).find("a").attr("href");
        $(activeTab).show();
        return false;  
    });  
  });
  
  
  //updatedBY:xsq 7.12
  
  $(".tab1").click(function (){
      $('#searchForm').removeAttr('data-target','*');
      $('#searchForm').removeAttr('action','*');
      $('#searchForm').attr('data-target','statusMesDriverPointsTable');
      $('#mesPointsName').val("");
      $.table.setCurrent("statusMesDriverPointsTable");
      $.table.refreshCurrent("${contextPath}/driver/driverPointData/${driverId}");
  })
  $(".tab4").click(function (){
	  $('#searchForm').removeAttr('target','*');
	  $('#searchForm').removeAttr('action','*');
	  $('#searchForm').attr('data-target','propertyMesDriverPointsTable');
      $('#mesPointsName').val("");
      $.table.setCurrent("propertyMesDriverPointsTable");
      $.table.refreshCurrent("${contextPath}/driver/driverPointData1/${driverId}");
  })
  $(".tab6").click(function (){
	  $('#searchForm').removeAttr('target','*');
	  $('#searchForm').removeAttr('action','*');
	  $('#searchForm').attr('data-target','procedureMesDriverPointsTable');
      $('#mesPointsName').val("");
      $.table.setCurrent("procedureMesDriverPointsTable");
      //$.table.destroy("procedureMesDriverPointsTable");
      $.table.refreshCurrent("${contextPath}/driver/driverPointData2/${driverId}");
  })
  $(".tab7").click(function (){
	  $('#searchForm').removeAttr('target','*');
	  $('#searchForm').removeAttr('action','*');
	  $('#searchForm').attr('data-target','statisticalMesDriverPointsTable');
      $('#mesPointsName').val("");
      $.table.setCurrent("statisticalMesDriverPointsTable");
      $.table.refreshCurrent("${contextPath}/driver/driverPointData3/${driverId}");
  })
  $(".tab8").click(function(){
	  $('#searchForm').removeAttr('target','*');
	  $('#searchForm').removeAttr('action','*');
	  $('#searchForm').attr('data-target','alarmMesDriverPointsTable');
	  $('#mesPointsName').val("");
	  $.table.setCurrent("alarmMesDriverPointsTable");
	  $.table.refreshCurrent("${contextPath}/driver/driverPointData4/${driverId}");
  })
  $("#addStatisticaldriverPoint").click(function(){
	  var point = $("#addStatisticaldriverPoint");
	  var checkRsByDriverId = $("#driverCheckRs").val();
	  if(checkRsByDriverId != null && checkRsByDriverId!=undefined && checkRsByDriverId !=""){
	      $("#addStatisticaldriverPoint").attr("checkRs","0");
	      swal("错误", checkRsByDriverId, "error");
	  }
  })
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.blockUI.js"></script>
<div class="modal-footer" >
 <button type="button" class="btn btn-default" id="goback" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
	$("#goback").click(function(){
		$.table.setCurrent("table");
		$.table.refreshCurrent("${contextPath}/driver/driverData"+$("#property").val());
	});
</script>