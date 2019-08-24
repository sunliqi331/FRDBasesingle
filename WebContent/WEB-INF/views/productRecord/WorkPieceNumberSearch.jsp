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
                <label for="productNo" class="searchtitle">产品名称</label> 
                <select name="analyzeSearch.productId" id="productId" data-placeholder="请选择产品名称" class="form-control searchtext" >
                       <option value="0"></option>
                       <c:forEach items="${products }" var="product">
                    <option value="${product.id }">${product.name }</option>
                  </c:forEach>
                </select>
              </li>
              <li class="form-group" id="productBatchidDisplay">
                <label for="productSn" class="searchtitle ">&nbsp;&nbsp;&nbsp;批次号</label> 
             	  <select name="analyzeSearch.productBatchid" id="productBatchid" data-placeholder="请选择批次号" class="form-control searchtext" >
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

              <!-- 工序号 -->
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
                  <label for="inputText" class="searchtitle ">测量类型</label>
                  <select name="analyzeSearch.meastype" id="meastype" data-placeholder="请选择测量类型" class="form-control searchtext" >
                      <option value="">全部</option>
                      <option value="0">首末检</option>
                      <option value="1">换刀检</option>
                      <option value="2">抽检</option>
                      <option value="3">全尺寸检</option>

                  </select>
              </li>




             <li class="form-group" style="padding-left: 78.5%;">
               <button id="search" type="submit" class="btn btn-info btn-search1" style="padding-left:50px;padding-right:50px;">搜索</button>
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
        <div  class="clearfix" style=" margin-bottom: 10px">
            <div class="">
            <shiro:hasPermission name="Department:save">
           <div class="col-sm-2" style="margin-bottom:10px;padding:0" >
            <a class="btn btn-default1" id="recordExport" href="#">
              <i class="fa fa-mail-forward"></i>
              <span> 生产记录导出</span>
           </a>
           </div>
            <iframe id="ifile" style="display:none"></iframe>
            </shiro:hasPermission>
  
           <div class="col-sm-2" style="margin-bottom:10px;padding:0" >
	            <a class="btn btn-default1" id="historyTrend" href="#">
	              <i class="fa fa-bar-chart"></i>
	              <span>历史数据趋势</span>
	           </a>
           </div>
           <div class="col-sm-4" style="background: #eceff3;padding: 8px 5px 3px 5px;color: #637792" > 
               <label for="inputText" class="searchtitle">总数:</label> <span id="totalNum">0</span>
               <label for="inputText" class="searchtitle">合格数:</label> <span id="goodNum">0</span>
               <label for="inputText" class="searchtitle">不合格数:</label> <span id="badNum">0</span>
               <label for="inputText" class="searchtitle">合格率:</label> <span id="passRate">0</span>

            </div>
        </div> 
 
<!--         <div>合格数：<span id="goodNum">0</span>&nbsp;&nbsp;不合格数：<span id="badNum">0</span>&nbsp;&nbsp;合格率：<span id="passRate">0</span></div> -->
       </div>
          <table class="table table-striped" id="recordTable" data-field="productionRecords">
            <thead>
              <tr>

                   <th data-field="Number" width="2%" data-align="center">序号</th>

               <!--  <th data-checkbox="false" width="22"><input class="cbr checkboxCtrl" type="checkbox" group="ids"></th> -->
                <th data-field="productBatchid" width="100">批次号</th>
                <th data-field="productSn" width="100">工件号</th>
                <th data-field="productlineName" width="100">生产线</th>
                <th data-field="productName" width="100">产品名称</th>
                <th data-field="datetime" width="100">日期</th>
                <th data-field="status" width="100">状态</th>
                <th data-field="meastype" width="100">测量类型</th>
                <th data-field="qualified" width="100">首检合格率</th>
                  <th data-field="productId" width="100" data-visible="false"></th>
               <!--  <th data-field="a" width="100">a</th> -->
              </tr>
            </thead>
          </table>
          </div>
          
          <div id="tab2" class="tab_content" style="display: none; "> 
          <table class="table table-striped" id="recordTable2" data-field="procedureRecords" >
            <thead>
              <tr>
               <!--  <th data-checkbox="false" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th> -->
                <th data-field="Number" data-width="2%" data-align="center">序号</th>
      <th data-field="procedurenum" width="100">工序号</th>
      <th data-field="procedurename" width="100">工序名称</th>
      <th data-field="productLine" width="100">生产线</th>
      <th data-field="mesProduct.name" width="100">产品名称</th>
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

        option = "<option value=''>全部批次</option>";
        $.ajax({
            url:"${contextPath}/statistics/generateProBatchids/"+productId,
            dataType:"JSON",
            type:"POST",
            async:false,
            success:function(data){
              $("#productBatchid").empty();
              $.each(data,function(idx,item){
                option += "<option value='"+ item.productBatchid +"'>"+ item.productBatchid +"</option>";
              });
              //option += "<option value=''>全部批次</option>";
              $("#productBatchid").append(option);
              $("#productBatchid").trigger("chosen:updated");
              if(callback)
                callback($("#productBatchid").val());
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
        
        var initRecordTable = function(){
        	$.table.init("recordTable", {
                uniqueId: 'rowkey',
                onPageChange:function(number,size){
                	/*  var $table = $.table.getCurrent();
                   	if($table && $table.data("table.data")){
                   		refreshPropertyValue($table.data("table.data"),$table.data("propertyIds"));
                   	}  */
                },
                onClickRow:function(row, $element){
               	 $("#searchForm").attr('data-target','recordTable2');
               	$.table.setCurrent("recordTable2");
               	 $(".tab2").trigger("click");
               	 console.log("row.rowkey"+row.rowkey);
               	 query["rowKey"] = row.rowkey;
               	 query["analyzeSearch.searchType"] = 1;
               	 $.table.refreshCurrent($("#searchForm").attr('action'), query,function(data){
               	 	 
               	 	// ajaxTodo("");
               	 });
                }
              },function(data){
                
              });
        }
        initRecordTable();
        var initRecordTable2 = function(){
        	$.table.init("recordTable2", {
                pagination:false,
                height:300,
                sidePagination:'server',
                uniqueId: 'rowKey',
                  onDblClickRow:function(row, $element){
                  	openPropertyDetail(row.sn,row.rowKey,row.id);
                },
                onClickCell:function(field, value, row, $element){
              	  if(field == 'processCharacteristicBtn'){
              		  
              	  }
              	  else if(field == 'processControlBtn'){
              		  console.log("is this uniqueId?"+row.id);
              		  openPropertyDetail(row.sn,row.rowKey,row.id);
              	  }
                }
              },function(data){
                
              });
        }
        initRecordTable2();
      function openPropertyDetail(sn,rowKey,procedureId){
    	  var op = {};
  	    op.title = "工序参数信息";
  	    op.url = "${contextPath}/statistics/toPropertyDetail?sn="+sn+"&rowKey="+rowKey+"&procedureId="+procedureId;
  	    op.destroyOnClose=true;
  		$.pdialog.open("propertyDetail",op);
      }
      
      $("#historyTrend").click(function(){
    	    var op = {};
    	    op.title = "工序参数趋势";
    	    //op.url = "${contextPath}/statistics/toPropertyHistoryTrend?page="+JSON.stringify($("#searchForm").serializeObject());
    	    op.url = "${contextPath}/statistics/toPropertyHistoryTrend";
    	    op.data = {page:JSON.stringify($("#searchForm").serializeObject()),
    	    		productLineId:$("#productLineId").val(),
    	    		productId:$("#productId").val(),
    	    		productProcedureId:$("#productProcedureId").val()};
    	    op.destroyOnClose=true;
    		$.pdialog.open("propertyHistoryTrend",op);
      });
      var columnSize = $("#recordTable").find("thead").find("th").length;
	  console.log(columnSize);
	  var refreshPropertyValue = function(data,propertyIds){
		    var $table = $.table.getCurrent();
    		var _map = {};
    		if(data.length != 0){
       	 	 var records = data.productionRecords;
       	 	 var rowKeys = [];
       	 	 $.each(records,function(idx,item){
       	 		var rowkey = item.rowkey;
       	 		rowKeys.push(rowkey);
       	 		_map[rowkey] = idx;
       	 	 });
       	 	console.log("_map:"+(_map));
       	 	console.log("rowKeys:"+rowKeys);
       	 	console.log("propertyIds:"+propertyIds);
       		$.ajax({
            		url:"${contextPath}/statistics/productionPropertyData",
            		dataType:"JSON",
            		type:"POST",
            		data:{"rowKeys":rowKeys.join(","),"propertyIds":propertyIds.join(",")},
            		success:function(data){
            			console.log(data);
            			$.each(data,function(k,v){
            					$.each(v,function(_k,_v){
            						 $table.bootstrapTable('updateCell',{
            							index : _map[k],
 	                                	field : _k,
 	                                	value : _v,
 	                                	triggered : false
 	                              	 });
            						 
            					});
            			});
            			/* $table.on('post-body.bs.table',function(){
            				refreshPropertyValue($table.data('table.data'),propertyIds);
            			}); */
                        $.unblockUI();
            		},
            		error:function(){
                        $.unblockUI();
                    }
            	});
    		}
	  };
      $("#searchForm").submit(function(e){
        var $that = $(this);
        var formArray = $that.serializeArray();
        $.each(formArray,function(){
          query[this.name] = this.value;
        });
        var productProcedureId = $("#productProcedureId").val();
        if(productProcedureId == 0 && $("#searchType").val() == 0){
        	swal("请选择工序");
        	return false;
        }
          $.table.setCurrent($that.attr('data-target'));
          if($that.attr('data-target') == 'recordTable'){
	         ajaxTodo("${contextPath}/statistics/getEntityOption?productProcedureId="+productProcedureId,function(data){
	        	var op = $.table.getOption();
	      		var $table = $.table.getCurrent();
	      		$table.bootstrapTable('destroy');
	      		$table.find("thead tr").find("th:gt("+columnSize+")").remove();
	      		var propertyIds = [];
	      		$.each(data,function(idx,item){
		      		$table.find("thead tr").append('<th data-field="'+ item.id +'" width="100">'+item.propertyname+'</th>');
		      		propertyIds.push(item.id);
	      		});
		       	$table.data("propertyIds",propertyIds);
		       	$table.bootstrapTable('destroy');
	      		 initRecordTable();
		       	  $.table.refreshCurrent($that.attr('action'), query,function(data){
		              var blockText = "请稍等......";
		              $.blockUI({
		                  baseZ:99999,
		                  message:"<h1>"+blockText+"</h1>",
		                  onBlock:function(){
		       		            refreshPropertyValue(data,propertyIds);
		                    }
	                  })
		       		/* var $table = $.table.getCurrent().off('post-body.bs.table').on('post-body.bs.table', function () {
		       			refreshPropertyValue($table.data('table.data'),propertyIds);
		            }); */
		       		
		       	  });  
	        }); 
	     	$.ajax({
	       		url:"${contextPath}/statistics/pageCaculate",
	       		dataType:"JSON",
	       		type:"POST",
	       		data:$("#searchForm").serializeObject(),
	       		success:function(data){
	       			$("#totalNum").html(data.page.totalCount);
	       			$("#goodNum").html(data.page.goodMap.good);
	       			$("#badNum").html(data.page.badMap.bad);
	       			if(data.page.goodMap.good && data.page.totalCount){
		       			$("#passRate").html(((Number(data.page.goodMap.good / data.page.totalCount) * 100).toFixed(4)) + "%");
		       			//$("#passRate").html(data.page.goodMap.good / data.page.totalCount);
	       			} else if((!data.page.goodMap.good) && (!data.page.totalCount)) {
	       				$("#passRate").html(null);
	       			}
	       		}
	       	}); 
          }else{
        	  $.table.refreshCurrent($that.attr('action'), query,function(data){});
        	  
          }
       
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
	  //去掉工件号的name属性
	  $("#productionSn").removeAttr('name');
      $('#searchProductionSn').removeAttr('style');
      $('#searchProcedure').attr('style','display:none');
      $('#searchForm').removeAttr('data-target','*');
      $('#searchForm').attr('data-target','recordTable');
      $('#Procedure').val("");

      // 批次号
      $('#productBatchidDisplay').removeAttr('style');
      //$('#searchProductBatchid').removeAttr('style');

      $.table.setCurrent("recordTable");
    //  $.table.refreshCurrent("${contextPath}/statistics/productionData");
  })
  $(".tab2").click(function (){
	  $("#searchType").val("1");
	  //给工件号加上name属性
	  $("#productionSn").attr('name','analyzeSearch.productionSn');
	  $('#searchProcedure').removeAttr('style');
      $('#searchProductionSn').attr('style','display:none');
      $('#searchForm').removeAttr('target','*');
      $('#searchForm').attr('data-target','recordTable2');
      $('#analyzeSearch.productionSn').val("");
      $.table.setCurrent("recordTable2");

      // 批次号
      $('#productBatchidDisplay').attr('style','display:none');
      //$('#searchProductBatchid').attr('style','display:none');
     // $.table.refreshCurrent("#");
  })
  
  $("#recordExport").click(function(){
	  if($.table.getCurrentPageData().length == 0){
		  swal("请先查询数据");
		  return;
	  }
	  var dataSize = parseInt($("#totalNum").html());
	  swal({
          title: dataSize+"条数据，确定导出？",
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: '#DD6B55',
          confirmButtonText: '确认',
          cancelButtonText: "取消",
          closeOnConfirm: true
        },
        function(isConfirm){
          if (isConfirm){
        	  var forms = $("#searchForm").serializeObject();
        	  //forms.tableTitles = [];
        	  var array;
        	   $.each($.table.getOption().columns[0],function(idx,item){
        		  if(item.visible){
        			  var value = item.field + ":" + item.title;
        			 // forms.tableTitles.push(item.title);
        			 if(idx == 0){
        				 array = value;
        			 }else{
        				 array += "," + value;
        			 }
        		  }
        	  }); 
        	  forms.tableTitles = array;
        	  console.log(forms);
        	  var blockText = "请稍等......";
        	  if(dataSize>5000){
        		  var blockText = "数据量较大，请耐心等待......";
        	  }
        	  $.blockUI({
        				baseZ:99999,
        				message:"<h1>"+blockText+"</h1>",
        				onBlock:function(){
        					$.ajax({
        			     		url:"${contextPath}/statistics/productRecordExport",
        			     		dataType:"JSON",
        			     		type:"POST",
        			     		data:forms,
        			     		success:function(data){
        			     			if(data.success){
        				     			var url="${contextPath }/statistics/downloadExport/"+data.success;
        				     		    document.getElementById("ifile").src=url;
        			     			}
        			     			 $.unblockUI();
        			     		}
        			     	}); 
        				}
        			}) 
          }
        });
	  //var url="${contextPath }/statistics/productRecordExport?forms="+JSON.stringify(forms);
	 // document.getElementById("ifile").src=url;
  })
</script>
</html>
