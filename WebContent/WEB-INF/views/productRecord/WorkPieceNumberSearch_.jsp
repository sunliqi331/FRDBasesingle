<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>生产记录</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
</head>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index">首页</a></li>
        <li>生产记录查询</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver clearfix " style="margin-bottom:15px">
           <div class="search_header">
           <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
            <i class="fa fa-search"></i> 生产记录查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" id="searchForm" method="post" action="${contextPath}/statistics/productionData" data-target="recordTable">
              <input type="hidden" id="searchType" name="analyzeSearch.searchType" value="0">
              <ul class="searchcontent search_work">
           
              <li class="form-group">
                <label for="productLineId" class="searchtitle">选择产线</label> 
                <select id="productLineId" name="analyzeSearch.productLineId" data-placeholder="请选择产线"  class="form-control searchtext">
                   <option value="0"></option>
                   <c:forEach items="${productLineSelect }" var="map">
                   <optgroup label="${map.key }">
                    <c:forEach items="${map.value }" var="productLine">
                      <option value="${productLine.id }">${productLine.linename }</option>
                    </c:forEach>
                    </optgroup>
                  </c:forEach>
                </select>
              </li>
              <li class="form-group">
                <label for="productNo" class="searchtitle">产品类型</label> 
                <select name="analyzeSearch.productId" id="productId" data-placeholder="请选择产品型号" class="form-control searchtext" >
                       <option value="0"></option>
                       <c:forEach items="${products }" var="product">
                    <option value="${product.id }">${product.name }</option>
                  </c:forEach>
                </select>
              </li>
              <li class="form-group" id="searchProcedure" style="display: none">
                <label for="productNo" class="searchtitle">工件号</label> 
              <input type="text" class="form-control searchtext" id="productionSn" name="analyzeSearch.productionSn"/>
              </li>
              <li class="form-group" id="searchProductionSn">
                <label for="productSn" class="searchtitle ">&nbsp;&nbsp;&nbsp;工序号</label> 
                
             	  <select name="analyzeSearch.productProcedureId" id="productProcedureId" data-placeholder="请选择工序号" class="form-control searchtext" >
                    <option value="0"></option>
                </select>
              </li>
                   <li class="form-group">
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime1" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text"  style="background: #fff;; border-color: #c8ced6; width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" name="analyzeSearch.begin" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                  <input class="form-control datetime" type="text" style="background: #fff;; border-color: #c8ced6;width:100%" value="" readonly> 
                   <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input2" name="analyzeSearch.end" />
              </li>
             <li class="form-group">
               <button id="search" type="submit" class="btn btn-info btn-search1" >搜索</button>
             </li>
        </ul>
        <input type="hidden" id="start_rowKey" >
        <input type="hidden" id="start_rowKey_detail" >
        <input type="hidden" id="start_preRowKey" >
        <input type="hidden" id="start_preRowKey_detail" >
        <input type="hidden" id="pageStartRowKey" >
        <input type="hidden" id="pagePreStartRowkey" >
            </form>
            </div>
          </div>
        
    <ul class="tabs clearfix"  >  
        <li class="active"><a class="tab1" href="#tab1">工序号查询</a></li>  
        <li style="margin-left: -1px"><a class="tab2" href="#tab2">工件号查询</a></li>  
    </ul>
        
        <div class="tab_container">  
        <div id="tab1" class="tab_content" style="display: block; "> 
<!--         <div class="driver_info" > 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">合格数:</label> <span id="goodNum">0</span>
              </div>
              <div class="form-group">
               <label for="inputText" class="searchtitle">不合格数:</label> <span id="badNum">0</span>
              </div>
              <div class="form-group">
               <label for="inputText" class="searchtitle">合格率:</label> <span id="passRate">0</span>
              </div>
            </form>
            </div> -->
        <div id="toolBar1" class="clearfix" style=" margin-bottom: 10px">
            <div class="btn-group pull-left clearfix">
            <shiro:hasPermission name="Department:save">
            <a class="btn btn-default1" id="recordExport" href="#">
              <i class="fa fa-mail-forward"></i>
              <span> 生产记录导出</span>
           </a>
            <iframe id="ifile" style="display:none"></iframe>
            </shiro:hasPermission>
            <a class="btn btn-default1" id="historyTrend" href="#">
              <i class="fa fa-bar-chart"></i>
              <span>历史数据趋势</span>
           </a>
           <div style="float: right;margin-bottom: 10px;background: #eceff3;padding: 4px;color: #637792" > 
         <form class="form-inline" method="post" action="#" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle">总数:</label> <span id="totalNum">0</span>
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">合格数:</label> <span id="goodNum">0</span>
              </div>
              <div class="form-group">
               <label for="inputText" class="searchtitle">不合格数:</label> <span id="badNum">0</span>
              </div>
              <div class="form-group">
               <label for="inputText" class="searchtitle">合格率:</label> <span id="passRate">0</span>
              </div>
            </form>
            </div>
        </div>
<!--         <div>合格数：<span id="goodNum">0</span>&nbsp;&nbsp;不合格数：<span id="badNum">0</span>&nbsp;&nbsp;合格率：<span id="passRate">0</span></div> -->
       </div>
          <table class="table table-striped" id="recordTable" data-field="productionRecords">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
               <!--  <th data-checkbox="false" width="22"><input class="cbr checkboxCtrl" type="checkbox" group="ids"></th> -->
                <th data-field="productSn" width="100">工件号</th>
                <th data-field="productlineName" width="100">生产线</th>
                <th data-field="productName" width="100">产品类型</th>
                <th data-field="datetime" width="100">日期</th>
                <th data-field="status" width="100">状态</th>
                <th data-field="productId" width="100" data-visible="false"></th>
              </tr>
            </thead>
          </table>
          </div>
          
          <div id="tab2" class="tab_content" style="display: none; "> 
         <%--  <div id="toolBar" class="clearfix" style=" margin-bottom:10px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_sendAlarm" href="${contextPath}/sendAlarm/addSendAlarm">
                <i class="fa fa-plus"></i>
                <span>过程控制</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:delete">
              <a class="btn btn-default1" target="dialog" data-target="table" rel="ids" href="${contextPath }/sendAlarm/deleteMany" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>过程特性</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div> --%>
          
          <table class="table table-striped" id="recordTable2" data-field="procedureRecords" >
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
               <!--  <th data-checkbox="false" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th> -->
                <th data-field="procedurenum" width="100">工序号</th>
      <th data-field="procedurename" width="100">工序名称</th>
      <th data-field="productLine" width="100">生产线</th>
      <th data-field="mesProduct.name" width="100">产品类型</th>
      <th data-field="nextprocedurename" width="100">步骤序号</th>
      <th data-field="processControlBtn" width="100">过程控制</th>
      <th data-field="customername" width="100">客户名称</th>
      <th data-field="ops" width="100">ops</th>
      <th data-field="partname" width="100">零件名称</th>
      <th data-field="customerpicturenum" width="100">客户图号</th>
      <th data-field="versiondate" width="100">版本日期</th>
      <th data-field="swpicturenum" width="100">森威图号</th>
      <th data-field="drivermodel" width="100">设备名称</th>
      <th data-field="drivernum" width="100">设备编号</th>
      <th data-field="jipname" width="100">夹具名称</th>
      <th data-field="jippicturenum" width="100">夹具图号</th>
      <th data-field="operationnum" width="100">操作指导书编号</th>
      <th data-field="operationversion" width="100">操作指导书版本</th>
      <th data-field="materialnum" width="100">材料牌号</th>
      <th data-field="materialstandards" width="100">材料规格</th>
      <th data-field="weightsmaterials" width="100">下料重量</th>
      <th data-field="blankshapesize" width="100">毛坯外形尺寸</th>
      <th data-field="makecase" width="100">每坯可制作数</th>
      <th data-field="reservation" width="100">周转装具</th>
      <th data-field="workshop" width="100">生产车间</th>
      <th data-field="remarks" width="100">备注</th>
      <th data-field="sn" width="100" data-visible="false"></th>
                
              </tr>
            </thead>
          </table>
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
  <c:set var="ParentTitle" value="Production" />
  <c:set var="ModuleTitle" value="ProductRecord" />  
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
</body>
<script type="text/javascript">
$(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
          /* $.table.init('recordTable', {
              toolbar : '#toolBar1'
            }, function(data) {
              var $p = $('#recordTable').find('tbody');
              $('tr[data-uniqueid]', $p).each(function(i) {
                var $this = $(this);
                var item = data.productionRecords[i];
                $this.attr('url', item.storeType + '/' + item.uuid);
              });
              
              $.table.init('recordTable2', {
                  toolbar : '#toolBar1'
                }, function(data) {
                  var $p = $('#recordTable2').find('tbody');
                  $('tr[data-uniqueid]', $p).each(function(i) {
                    var $this = $(this);
                    var item = data.position[i];
                    $this.attr('url', item.storeType + '/' + item.uuid);
                  });
        });
       }); */
    });
</script>
<script type="text/javascript">
  $(document).ready(function(){
	  
	  
    function generateProcedureSelect(productId,callback){
        var option = "";
        $.ajax({
          url:"${contextPath}/statistics/generateProductProcedureSelect/"+productId,
          dataType:"JSON",
          type:"POST",
          async:false,
          success:function(data){
            $("#productProcedureId").empty();
            $.each(data,function(idx,item){
              option += "<option value='"+ item.id +"'>"+ item.procedurenum +"</option>";
            });
            $("#productProcedureId").append(option);
            $("#productProcedureId").trigger("chosen:updated");
            if(callback)
              callback($("#productProcedureId").val());
          }
        });
      }
     $("#productId").change(function(){
        generateProcedureSelect($(this).val());
      });
     //generateProcedureSelect( $("#productId").val());
      $("#next").click(function(){
        $("#recordTable").bootstrapTable("nextPage");
      });
      $("#pre").click(function(){
        $("#recordTable").bootstrapTable("prevPage");
      });
    
    
      $('.form_datetime1').datetimepicker({
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
      $("select").chosen({search_contains:true});
      var currentPageNumber = 1;
      var currentPageNumber_detail = 1;
      var flag = true;
      var flag_detail = true;
        var query = {};
      $.table.init("recordTable", {
         pagination:true,
         sidePagination:'server',
         uniqueId: 'rowKey',
         onPageChange:function(number,size){
           //console.log($.table.getCurrent().bootstrapTable("getOptions"));
            if(number > currentPageNumber){
              flag = true;
           }else{
              flag = false;
           }
          // $.table.getCurrent().bootstrapTable("refreshOptions",query);
           currentPageNumber = number;
         },
         queryParams: function (params) {
           var startRow;   
           if(flag){
             startRow = $("#start_rowKey").val();   
           }else{
             //startRow = $("#start_preRowKey").val();   
             if(null != JSON.parse(sessionStorage.getItem("productRecordPageNumber"))){
               startRow = JSON.parse(sessionStorage.getItem("productRecordPageNumber"))[currentPageNumber];
             }
           }
           var preRowKey="";
                return {
                  numPerPage: params.limit,   //(this.options.pageSize)
                  pageStartRowKey:startRow,
                  pagePreStartRowkey:preRowKey
                }
         },
         onClickRow:function(row, $element){
        	 $.table.setCurrent('recordTable2');
        	 $(".tab2").trigger("click");
        	 query["analyzeSearch.productionSn"] = row.productSn;
        	 $("#productionSn").val(row.productSn);
        	 query["analyzeSearch.searchType"] = "1";
        	 query["analyzeSearch.productId"] = row.productId;
        	 $.table.refreshCurrent($("form").attr('action'), query,function(data){
        	 	 
        	 });
         }
       },function(data){
         
       });
      $.table.init("recordTable2", {
          pagination:false,
          height:300,
          sidePagination:'server',
          uniqueId: 'rowKey',
          onPageChange:function(number,size){
              //console.log($.table.getCurrent().bootstrapTable("getOptions"));
               if(number > currentPageNumber_detail){
            	   flag_detail = true;
              }else{
            	  flag_detail = false;
              }
             // $.table.getCurrent().bootstrapTable("refreshOptions",query);
              currentPageNumber_detail = number;
            },
            queryParams: function (params) {
              var startRow;   
              if(flag_detail){
                startRow = $("#start_rowKey_detail").val();   
              }else{
                //startRow = $("#start_preRowKey").val();   
                if(null != JSON.parse(sessionStorage.getItem("productRecordDetailPageNumber"))){
                  startRow = JSON.parse(sessionStorage.getItem("productRecordDetailPageNumber"))[currentPageNumber_detail];
                }
              }
              var preRowKey="";
                   return {
                     numPerPage: params.limit,   //(this.options.pageSize)
                     pageStartRowKey:startRow,
                     pagePreStartRowkey:preRowKey
                   }
            },
            onDblClickRow:function(row, $element){
            	openPropertyDetail(row.sn,row.id);
        	   
          },
          onClickCell:function(field, value, row, $element){
        	  if(field == 'processCharacteristicBtn'){
        		  
        	  }
        	  else if(field == 'processControlBtn'){
        		  openPropertyDetail(row.sn,row.id);
        	  }
          }
        },function(data){
          
        });
      function openPropertyDetail(sn,procedureId){
    	  var op = {};
  	    op.title = "工序参数信息";
  	    op.url = "${contextPath}/statistics/toPropertyDetail?sn="+sn+"&procedureId="+procedureId;
  	    op.destroyOnClose=true;
  		$.pdialog.open("propertyDetail",op);
      }
      
      $("#historyTrend").click(function(){
    	    var op = {};
    	    op.title = "工序参数趋势";
    	    op.url = "${contextPath}/statistics/toPropertyHistoryTrend?page="+JSON.stringify($("#searchForm").serializeObject());
    	    op.destroyOnClose=true;
    		$.pdialog.open("propertyHistoryTrend",op);
      });
      $("#test").click(function(){
    	  $("#recordTable").bootstrapTable("updateCell",{index:1,field:"productSn",value:"asdasdasd"});
      });
      $("form").submit(function(e){
       /*  if($('#dtp_input2').val() == '' || $('#dtp_input1').val() == ''){
          swal("请输入时间");
          return false;
        } */
        $("#start_rowKey").val("");
        $("#start_rowKey_detail").val("");
        sessionStorage.removeItem("productRecordPageNumber");
        sessionStorage.removeItem("productRecordDetailPageNumber");
        var $that = $(this);
        var formArray = $that.serializeArray();
        $.each(formArray,function(){
          query[this.name] = this.value;
        });
          $.table.setCurrent($that.attr('data-target'));
        	  $.table.refreshCurrent($that.attr('action'), query,function(data){
        		  //if($("#goodNum").html() == '0' && $("#badNum").html() == '0' && $("#passRate").html() == '0'){
        			  $("#goodNum").html(data.page.goodMap.good);
        			  $("#badNum").html(data.page.badMap.bad);
        			  $("#totalNum").html(data.page.totalCount);
        			  $("#passRate").html(Math.round(data.page.goodMap.good * 100 / data.page.totalCount , 2)+"%");
        		 // }
      	          $("#start_rowKey").val(data.nextStartRowKey);
      	          if(null != sessionStorage.getItem("productRecordPageNumber")){
      	              var pageNumber = JSON.parse(sessionStorage.getItem("productRecordPageNumber"));
      	              pageNumber[currentPageNumber] = data.startRowKey;
      	              sessionStorage.setItem("productRecordPageNumber",JSON.stringify(pageNumber));
      	            }else{
      	              var tmp = {1:data.startRowKey};
      	              sessionStorage.setItem("productRecordPageNumber",JSON.stringify(tmp))
      	            }
      	            $("#start_preRowKey").val(data.preStartRowKey);
              	
              }); 
          
       
        $(".pagination-detail").css("background","white");
        return false;
      });
     
  });

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
  
  $(".tab1").click(function (){
	  $("#searchType").val("0");
      $('#searchProductionSn').removeAttr('style');
      $('#searchProcedure').attr('style','display:none');
      $('#searchForm').removeAttr('data-target','*');
      $('#searchForm').attr('data-target','recordTable');
      $('#Procedure').val("");
      $.table.setCurrent("recordTable");
    //  $.table.refreshCurrent("${contextPath}/statistics/productionData");
  })
  $(".tab2").click(function (){
	  $("#searchType").val("1");
	  $('#searchProcedure').removeAttr('style');
      $('#searchProductionSn').attr('style','display:none');
      $('#searchForm').removeAttr('target','*');
      $('#searchForm').attr('data-target','recordTable2');
      $('#analyzeSearch.productionSn').val("");
      $.table.setCurrent("recordTable2");
     // $.table.refreshCurrent("#");
  })
  
  $("#recordExport").click(function(){
	  var begin = $("#dtp_input1").val();
	  var end = $("#dtp_input2").val();
	  var productLineId = $("#productLineId").val();
	  var productId = $("#productId").val();
	  var productProcedureId = $("#productProcedureId").val();
	  var searchType = parseInt($("#searchType").val());
	  var url="${contextPath }/statistics/productRecordExport?searchType="+searchType+"&begin="+begin+"&end="+end+"&productLineId="+productLineId+"&productId="+productId+"&productProcedureId="+productProcedureId+"";
	  document.getElementById("ifile").src=url;
  })
</script>
</html>
