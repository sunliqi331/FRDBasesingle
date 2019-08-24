<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
 <%@ include file="/WEB-INF/views/include.inc.jsp"%>
          <form class="form form-horizontal" method="post" action="${contextPath }/mesPoints/data" data-target="table" onsubmit="return navTabSearch(this)">
          		<input type="hidden" id="monitorCountTime" />
              <input type="hidden" id="bindType" value="${bindType }">
              <div class="pageFormContent" layoutH="58">
     <div class="row">
      <div class="searchBar personnel_search" style="padding: 0px 15px;border:none;box-shadow: none;  width:auto; max-width:inherit ;" >
	 
	   <div class="form-inline" style="padding:0px">
	   		  <div class="form-group">
	   			<label for="selectGateway" class="searchtitle">选择网关</label> 
                <select id="selectGateway" data-placeholder="请选择网关" style="width:120px" class="form-control searchtext chosen-select">
                      <c:forEach var="p" items="${mesGateways }">
                        <option value="${p.id }">${p.name }</option>
                      </c:forEach>
                </select>
              </div>
              <div class="form-group">
                <label for="name" class="searchtitle">测点名称</label>
                <input id="search_LIKE_name" type="text" class="form-control searchtext" style="width:120px" name="search_LIKE_name" value="${param.search_LIKE_name}">
              </div>
              <div class="form-group">
                <label for="codekey" class="searchtitle">测点id</label>
                <input id="search_LIKE_codekey" type="text" class="form-control searchtext" style="width:120px"  name="search_LIKE_codekey" value="${param.search_LIKE_codekey}">
              </div>
              <div class="form-group">
                <label for="mesPointType" class="searchtitle">测点类型</label>
                <select id="selectMesPointType" data-placeholder="请选择测点类型" style="width:120px" class="form-control searchtext chosen-select">
                   <option value="">全部</option>
	               <c:forEach var="mpt" items="${mesPointTypes}"> 
	                   <option value="${mpt.name}">${mpt.name}</option>
	               </c:forEach>
                 </select>
              </div>
              <button id="searchBtn" type="button" class="btn btn-info btn-search1">搜索</button>
              </div>
              </div>
               <div class="form-group" style=" margin:0 15px 15px 15px">
        <div class="" style=" margin-top: 0px;" >
        <table class="table table-striped" id="table" data-field="points" data-url="${contextPath }/mesPoints/data">
          <thead>
            <tr>
               <th data-field="Number" width="50" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
              <th data-field="codekey" width="100">测点Id</th>
              <th data-field="name" width="100">测点名称</th>
              <th data-field="mesPointGateway.name" width="100">所属网关</th>
              <th data-field="mesPointGateway.mac" width="100" data-visible="false"></th>
              <th data-field="currentMesDriver.name" width="100">所属设备</th>
              <th data-field="currentMesDriver.id" width="100" data-visible="false"></th>
              <th data-field="currentMesProcedurePropertyName" width="100">绑定参数</th>
              <th data-field="currentMesProcedurePropertyId" width="100" data-visible="false"></th>
              <th data-field="currentMesProductProcedure.procedurename" width="100">绑定工序</th>
              <th data-field="currentMesProductProcedure.id" width="100" data-visible="false"></th>
              <th data-field="currentMesProduct.name" width="100">产品</th>
              <th data-field="currentMesProduct.id" width="100" data-visible="false"></th>
              <th data-field="mesPointType.id" width="100" data-visible="false"></th>
              <th data-field="mesPointType.name" width="100">测点类型</th>
              <th data-field="mesPointType.pointtypekey" width="100" data-visible="false"></th>
              <th data-field="datatype" width="100">数据类型</th>
              <th data-field="units" width="100">单位</th>
            </tr>
          </thead>
        </table> 
        
        </div></div>
              </div>
              <div id="countTimeArea" class="row" style="display: none" data-display="hide">
              <div class="form-inline" style="padding:0px">
              <label for="timeCount" class="searchtitle">选择时间：</label>
						<div class="controls input-append date form_datetime2"
							style="position: relative;" data-date=""
							data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
							<input class="form-control datetime" type="text" id="countTimeText"
								style="background: #fff;; border-color: #c8ced6; width: 100%"
								value="" readonly> <span class="add-on"
								style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
								<i class="fa fa-remove"></i>
							</span> <span class="add-on"
								style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
								<i class="fa fa-th"></i>
							</span>
              </div></div>
                
              </div>
              <div class="modal-footer">
       <span style="float: left;" id="recordTime"></span>
      <button type="button" id="inviteBtn" class="btn btn-primary">确定</button>
      <button type="button" class="btn btn-default"
        data-dismiss="modal">关闭</button>
    </div>
            </form>
        <script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
  $(document).ready(function($) {
	  
	  $("#recordTime").html("${countTime}");
	  
	  if($("#countTimeText").val()!=""){
		  $("#countTimeArea").show();
	  } 
	  $('.form_datetime2').datetimepicker({
	        language : 'zh-CN',
	        format : 'yyyy-mm-dd hh:ii:ss',
	        weekStart : 1,
	        todayBtn : 'linked',
	        autoclose : 1,
	        todayHighlight : 1,
	        startView : 2,
	        forceParse : 0,
	        showMeridian : 1,
	      //minView: 2

	      });
	  var gateway = "${dataMac}";
	  console.log(gateway);
	  var codekey = "${dataCodekey}";
	  $("#searchBtn").click(function(){
		  $.table.getCurrent().bootstrapTable('destroy');
		  init($("#selectGateway").val(),$("#search_LIKE_codekey").val(),$("#selectMesPointType").val());
		  $.table.refreshCurrent("${contextPath }/mesPoints/data",{},function(){
		  });
	  });
	 /*  $("#selectFactory").change(function(){
		  var $this = $(this);
		  if($this.val() == ""){
			  $("#selectGateway").val("");
		  }
	  }); */
	  $("#inviteBtn").click(function(){
		  console.log("绑定------start-");
		  var $dialog = $.pdialog.getUpper();
		  var params = $dialog.data("params");
		  if(params){
			  params = DWZ.jsonEval(params);
			  var rowData = $.table.getCurrent().bootstrapTable('getSelections')[0];
			  console.log(rowData.currentMesDriver.id);
			  console.log(rowData.codekey);
			  params.mac=rowData.mesPointGateway.mac;
			  params.driverId=rowData.currentMesDriver.id;
			  params.pointTypeName=rowData.mesPointType.pointtypekey;
			  params.codekey=rowData.codekey;
			  params.propertyId=rowData.currentMesProcedurePropertyId;
			  params.procedureId=rowData.currentMesProductProcedure.id;
			  params.productId=rowData.currentMesProduct.id;
			  params.units=rowData.units;
			  params.countTime=$("#countTimeText").val();
		  }
		  $dialog.data("params",params);
		  // $dialog.data("params","{\"aa\":\"bb\"}");
		  $dialog.modal('hide');
          console.log("绑定------end-");
	  });
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    function init(gateway_,codekey_,mesPointType_){
    	$.table.init('table', {
    	      toolbar : '#toolBar1',
    	      singleSelect  : true,
    	      pageSize: 5,
    	      queryParams: function (params) {
    	    	  if("" == gateway_ && "" == codekey_){
    	    		  gateway_ = $("#selectGateway").val();
    	    		  codekey_ = $("#search_LIKE_codekey").val();
    	    	  }
    	    	  var  parameters = null;
    	    	  if("0" == gateway_) {
                      parameters = { 
                              search_LIKE_name: $("#search_LIKE_name").val(),
                              'search_LIKE_mesPointType.name': mesPointType_,
                              search_LIKE_codekey:codekey_,
                              pageNum: this.pageNumber
                             };
    	    	  } else {
                      parameters = { 
                              'search_EQ_mesPointGateway.id': gateway_,
                              search_LIKE_name: $("#search_LIKE_name").val(),
                              'search_LIKE_mesPointType.name': mesPointType_,
                              search_LIKE_codekey:codekey_,
                              pageNum: this.pageNumber
                             };
    	    	  }

    	    	  if($("#bindType").val() == 'circular'){
    	    		  parameters['search_EQ_mesPointType.pointtypekey'] = 'D_STATUS';
    	    		  console.log(parameters);
    	    	  }
    	    	  var _parames = $.extend({},$.table._op.queryParams(params),parameters);
    	               return _parames;
    	        },
    	       
    	        onClickRow:function(row, $element){
    	        	  
                	  if(row.mesPointType.pointtypekey == 'P_COUNT' || 
                			  row.mesPointType.pointtypekey == 'D_COUNT' ||
                			  row.mesPointType.pointtypekey == 'WATER' || 
                			  row.mesPointType.pointtypekey == 'GAS' || 
                			  row.mesPointType.pointtypekey == 'ELECTRIC'){
                		     var op = {};
	                  	    op.title = "产量时间点";
	                  	    //op.url = "${contextPath}/statistics/toPropertyHistoryTrend?page="+JSON.stringify($("#searchForm").serializeObject());
	                  	    op.url = "${contextPath}/procedureMonitor/showCountTime";
	                  	    op.destroyOnClose=true;
	                  	  	op.close = function(params){
	                  	  		$("#monitorCountTime").val(params.countTime);
	          		    		//that.$widget.data("params",JSON.stringify(params));
	          		    	};
	                  	    /* var dialog = $('#dialogTemp').html();
	                  	    dialog = dialog.replace('data-style','style="width:400px"');
	                  	    $('#dialogTemp').html(dialog); */
	                  		//$.pdialog.open("countTime",op);
	                  	   /* var selectedIds = $.table.getSelectedId();
	                  	    if(selectedIds.length == 1){
	                  	    	
	                  	    } */
	                  	    if($("#countTimeArea").attr("data-display") == "show"){
		                  	    $("#countTimeArea").hide();
		                  	  $("#countTimeArea").attr("data-display","hide");
	                  	    }else{
	                  	    	$("#countTimeArea").show();
	                  	    	 $("#countTimeArea").attr("data-display","show");
	                  	    	$("#countTimeText").val("");
	                  	    }
	                  	    
	                  	    //给时间选择赋予上次选择的值
	                  	    //$("#countTimeText").val("${countTime}");
	                  	    
                	  }else{
                		  $("#countTimeArea").hide();
                		  $("#countTimeArea").attr("data-display","hide");
                		  $("#countTimeText").val("");
                	  }
                	  console.log($element);
                  }
    	    }, function(data) {
    	    	console.log(data)
    	    	if(data && data.points.length == 1){
    	    		$.table.getCurrent().bootstrapTable('checkAll');
    	    	}
    	    });
    }
    init(gateway,codekey,"");
  
  });
  
</script>