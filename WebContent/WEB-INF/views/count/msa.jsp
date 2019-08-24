<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=IE8" >
<meta name="title" content="FRD - Demo">
<title>MSA-分析</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/styles/assets/html2canvas/html2canvas.js"></script>
<script src="${contextPath}/styles/assets/html2canvas/html2canvas.svg.js"></script>
<script src="${contextPath}/js/jquery.jqprint-0.3.js"></script>
<script src="http://www.jq22.com/jquery/jquery-migrate-1.2.1.min.js"></script>
</head>
<script type="text/javascript">

$(document).ready(function(){
	$(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");
	$(".chooseBtn").click(function(){
		var $that = $(this);
		console.log($that.context.id);
		var checkedFlag = $that.data("checked");
		var _length = 0;
		if ($("#analysisMethod").val() == "cg") {
			_length = $("#testCount").val();
			
		}else{
			_length = ($("#personNum").val()*$("#checkNum").val()*$("#workpieceNum").val());
		}
		var start = 0;
//		if($that.context.id == 'sectionBtn'){
//			var data = $.table.getCurrent().bootstrapTable("getSelections");
//			if(data.length != 0){
//				start = data[0].id;
//			}
//		}
		$.table.getCurrent().bootstrapTable("uncheckAll");
		var length = parseInt(_length) + parseInt(start);
		if(length > $.table.getCurrentPageData().length){
			length = $.table.getCurrentPageData().length + 1;
		}
//		var ids = new Array();
//		for(var i = start; i < length; i++){
//	   		ids.push(i);
//		}
		if(!checkedFlag){
//			$.table.getCurrent().bootstrapTable("checkBy",{field:"id",values:ids});
            for(var i = start; i < length; i++){
                $.table.getCurrent().bootstrapTable('check', i);
            }
            $that.data("checked",true);
		}else{
// 			$.table.getCurrent().bootstrapTable("uncheckBy",{field:"id",values:ids});
		   	$that.data("checked",false);
		}
		
		
	});
	$("#data-confirm-Btn").click(function(){
		if($.table.getSelectedId().length == 0){
			swal("您未选择任何数据");
			return;
		}
		// 动态变化title
		$("title").html($("#analysisMethod").find("option:selected").text());
	    var values = new Array();
		var arrays = $.table.getCurrent().bootstrapTable("getSelections");
		 $.each(arrays, function(){
	            values.push(this.productionSn);
	     })
	    $.unique(values.sort()); 
		if ($("#analysisMethod").val() == "cg") {
			     if($.table.getSelectedId().length != $("#testCount").val()){
			    	 swal("选择的数据量不符合当前计算条件：测量次数为："+$("#testCount").val());
			    	 return;
			     }
				/* if(values.length > 1){
					swal("您选择的数据不符合要求,存在不同的产品序列号");
					return;
				} */
			$("#data-confirm").attr("href","${contextPath }/statistics/analyseCgDataPage");
			$("#data-confirm").trigger("click");
			    // window.open("${contextPath }/statistics/analyseGrrDataPage");
		}else{
			if($.table.getSelectedId().length != ($("#personNum").val()*$("#checkNum").val()*$("#workpieceNum").val())){
				swal("您选择的数据不符合要求,总条数应为："+($("#personNum").val()*$("#checkNum").val()*$("#workpieceNum").val()));
				return;
			}
			/* if(values.length != $("#workpieceNum").val()){
				swal("您选择的数据不符合要求,您选择的数据中,有"+values.length+"个工件,应为"+$("#workpieceNum").val()+"个工件");
				return;
			} */
			$("#data-confirm").attr("href","${contextPath }/statistics/analyseGrrDataPage");
			$("#data-confirm").trigger("click");
			//window.open("${contextPath }/statistics/analyseGrrDataPage");
		}
		
	});

	$("select").chosen({search_contains:true});
	
	$(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");
	
	$("#analysisMethod").change(function(e) {
	    //alert($("#analysisMethod").val());
	    if ($("#analysisMethod").val() == "grr") {
		 //$("#data-confirm").attr("href","${contextPath }/statistics/analyseGrrDataPage");
	      $(".count_cg").hide();
	      $(".count_cg").attr("disabled","disabled");
	      $(".count_grr").show();
	      $(".count_grr").removeAttr("disabled");
	      $(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");    
	      $("#measureRange").val("-1");
	      //$("#measureRange").find("option[value='1']").attr("disabled","disabled");
	      $("#productionSn").attr("disabled","disabled");
	      $("#measureRange").trigger("chosen:updated");
	      $("#measureRange").trigger("change");
	      if ($("#measureRange").val() == "-1") {
	    	$(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");  
	      }
	    } else {
	      $("#data-confirm").attr("href","${contextPath }/statistics/analyseCgDataPage");
	      $(".count_cg").show();
	      $(".count_cg").removeAttr("disabled");
	      $(".count_grr").hide();
	      $(".count_grr").attr("disabled","disabled");
	      $(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");
	      $("#measureRange").find("option[value='1']").removeAttr("disabled");
	      $("#productionSn").remove("disabled");
	      $("#measureRange").trigger("chosen:updated");
	      $("#measureRange").trigger("change");
	      
	    }
	  });
	  $(".count_grr").hide();
	  $("#measureRange").change(function(e) {
		  $(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");
	    //alert($("#analysisMethod").val());
	    if ($("#measureRange").val() == "-1") {
	      $(".rangeList").hide();
	      $(".rangeList").find("input").attr("disabled","disabled");
	      $(".rangetime").show();
	      if($("#analysisMethod").val()=="cg"){
	        $(".msa_search").css("width","auto").css("margin-top","0").css("margin-bottom","0").removeClass("text-center");
	      }
	      $(".rangetime").find("input").removeAttr("disabled");
	      
	      
	    } else if ($("#measureRange").val() == "1") {
	      $(".rangeList").show();
	      $(".rangeList").find("input").removeAttr("disabled");
	      $(".rangetime").hide();
	      $(".rangetime").find("input").attr("disabled","disabled");
	     //$(".msa_search").css("width","100%").css("margin-top","5px").css("margin-bottom","5px").addClass("text-center");	   
	    }
	    
	  });
	  $(".rangetime").hide();
	  $(".rangetime").find("input").attr("disabled","disabled");
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
	  
	  
	  //通过产品ID获取该产品下的工序，并动态产生select
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
			  	$("#productProcedureId").trigger("change");
			  	$("#productProcedureId").trigger("chosen:updated");
			  	if(callback)
			  		callback($("#productProcedureId").val());
			  },
			  error:function(jqXHR, textStatus, errorThrown){
				  console.log(textStatus);
			  }
		  });
	  }
	  function generateProcedurePropertySelect(procedureId,callback){
		  var option = "";
		  $.ajax({
			  url:"${contextPath}/statistics/generateProcedurePropertySelect/"+procedureId,
			  dataType:"JSON",
			  type:"POST",
			  async:false,
			  success:function(data){
				  $("#procedurePropertyId").empty();
			  	$.each(data,function(idx,item){
			  		option += "<option value='"+ item.id +"'>"+ item.propertyname +"</option>";
			  	});
			  	$("#procedurePropertyId").append(option);
			  	$("#procedurePropertyId").trigger("chosen:updated");
			  	if(callback)
			  		callback();
			  }	,
			  error:function(jqXHR, textStatus, errorThrown){
				  console.log(textStatus);
			  }
		  });
		  
	  }
	  $("#productId").change(function(){
		  generateProcedureSelect($(this).val());
	  });
	  generateProcedureSelect($("#productId").val(),generateProcedurePropertySelect);
	  $("#productProcedureId").change(function(){
		  generateProcedurePropertySelect($(this).val());
	  });
	 // generateProcedureSelect($("#product").val(),generateProcedurePropertySelect($("#procedure").val()));
	 $("#searchBtn").click(function(){
		 $("#formID").submit();
	 });
	 $("#formID").submit(function(e){
		 $("#checkedRowNum").html("0");
		 if($("#mesDriverId").val() == 0 || $("#productId").val() == 0 || $("#productProcedureId").val() == 0 || $("#procedurePropertyId").val() == 0){
		     swal("请选择筛选条件");
		     return false;
		 }
		 if($("#measureRange").val() == 1 && $("#productionSn").val() == ''){
		     swal("序列号未填写");
		     return false;
		 }
		 if($("#measureRange").val() == -1 && ($("#begin").val() == '' || $("#end").val() == '')){
		     swal("时间段未选择");
		     return false;
		 }
		    var query = {};
			$.each($("#formID").serializeArray(),function(){
				query[this.name] = this.value;
			});
			$.table.setCurrent($("#formID").attr('data-target'));
			$.table.refreshCurrent($("#formID").attr('action'), query,function(data){
				 /* if(data.cgAnalyzeData.length > 2000){
					swal("筛选数据量已达到" + data.cgAnalyzeData.length + "条,您可以尽量缩减搜索时间范围,以减少数据量");
				}
				if(data.cgAnalyzeData.length == 2000){
					swal("筛选数据量已超过" + data.cgAnalyzeData.length + "条,我们只为您显示2000条,请检查搜索条件");
				}  */
			});
			return false;
	 });
	 var analyzeSearch = '${analyzeData}';
	 console.log(analyzeSearch);
	 if(analyzeSearch != null && analyzeSearch != '' ){
		 var analyzeSearch = JSON.parse(analyzeSearch);
		   $.each(analyzeSearch,function(k,v){
			 $("#"+k).val(v);
			 if($("#"+k).is('select')){
				 $("#"+k).trigger("chosen:updated");
				  if(k == 'measureRange' || k == 'analysisMethod'){
					 $("#"+k).trigger("change");
				 }
			 }
		 });
	 }
	 $.table.init('analyzeTable', {
		 pagination:true,
		 sidePagination:'server',
		 height:1200,
		 onlyInfoPagination:true,
		 showExport: true,                     //是否显示导出
         exportDataType: 'selected',
         exportOptions:{
        	 ignoreColumn:[0]
         },
         onCheck:function(row){        
			 $("#checkedRowNum").html($.table.getSelectedId().length);
		 },
		 onUncheck:function(row){
			 $("#checkedRowNum").html($.table.getSelectedId().length);
		 },
		 onCheckAll:function(rows){
			 console.log(rows.length);
			 $("#checkedRowNum").html(rows.length);
		 },
		 onUncheckAll:function(rows){
			 $("#checkedRowNum").html("0");
		 }
	 },function(data){
		 
	 });
	 window.onload = function(){ 
	   if ( $("tr").hasClass("no-records-found") ){
			//alert("1");
			$(".fixed-table-container").css("height","65px");
		}
		else{
			$(".fixed-table-container").css("height","300px");	
		}
	 }
	 /* $.table.getCurrent().on("uncheck-all.bs.table",function(){
		 console.log($(this));
		 $("#fastChooseBtn").data("checked",false);
	 }); */
});

</script>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<body>
<div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp" %>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp" %>
 	 <div class="main-content removeble">
      <ol class="breadcrumb" style="background: #f1f2f7; height:41px; margin:0;line-height:40px;    border-radius: 0; display:block;">
        <li ><i class="fa fa-home"></i>
        <a href="${contextPath}/management/index"> 首页</a></li>
        <li >统计分析MSA</li>
      </ol>
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar search_driver" style="margin-bottom: 15px;" >
        <div class="search_header">
        <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> MSA分析条件
           </div>
           <div class="ishidden" >
           
        <form method="post" action="${contextPath}/statistics/analyseDataSearch" data-target="analyzeTable"
          id="formID" class="form-inline">
        
            <div class="form-group">
              <label for="inputText" class="searchtitle">分析方法
              </label>
              
                <select id="analysisMethod" name="analysisMethod"
                  class="form-control searchtext msa">
                  <option value="cg" >Cg分析</option>
                  <option value="grr">Grr分析</option>
                </select>
              
            </div>	
           <div class="form-group">
              <label for="mesDriverId" class="searchtitle">检测设备
              </label>
              
                <select id="mesDriverId" name="mesDriverId" data-placeholder="请选择检测设备"
                  class="form-control searchtext msa">
                  <c:forEach items="${checkDrivers }" var="map">
                   <optgroup label="${map.key }">
                  	<c:forEach items="${map.value }" var="checkDriver">
                  		<option value="${checkDriver.id }">${checkDriver.name }</option>
                  	</c:forEach>
                  	</optgroup>
                  </c:forEach>
                </select>
             </div>
            
            <div class="form-group">
              <label for="productId" class="searchtitle">产品型号
              </label>
              
                <select id="productId" name="productId" data-placeholder="请选择产品"
                  class="form-control searchtext msa">
                  <c:forEach items="${products }" var="product">
                  	<option value="${product.id }">${product.name }</option>
                  </c:forEach>
                </select>
             </div> 
            
            <div class="form-group">
              <label for="productProcedureId" class="searchtitle">工序编号
              </label>
              
                <select id="productProcedureId" name="productProcedureId" data-placeholder="请选择工序"
                  class="form-control searchtext msa">
                  <option value="0"></option>
                </select>
              
            </div>
            <div class="form-group">
              <label for="procedurePropertyId" class="searchtitle">被测参数</label>
              
                <select id="procedurePropertyId" name="procedurePropertyId" data-placeholder="请选择工序参数"
                  class="form-control searchtext msa">
                  <option value="0"></option>
                </select>
             
            </div>
           
              <div class="form-group count_cg">
                <label for="testCount" class="searchtitle">测量次数</label>
                
                  <select id="testCount" name="testCount" data-placeholder="请选择测量次数"
                    class="form-control searchtext msa">
                    <option value="25">25</option>
                    <option value="50">50</option>
                  </select>
                
              </div>
              <div class="form-group count_cg">
                <label for="statisticalStandard" class="searchtitle">统计标准</label>
                
                  <select id="statisticalStandard" name="statisticalStandard" data-placeholder="请选择统计标准"
                    class="form-control searchtext msa">
                    <option value="4">4σ</option>
                    <option value="6">6σ</option>
                  </select>
                
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">测量范围</label>
                
                  <select id="measureRange" name="measureRange" data-placeholder="请选择测量范围"
                    class="form-control searchtext validate[required,funcCall[checkSelect]]">
                    <option value="-1">时间段</option>
                    <option value="1" selected>工件号</option>
                  </select>
                
              </div>
              <div class="form-group rangetime ">
                <label for="inputText" class="searchtitle">开始时间</label>
              
                  <div class="controls searchtext input-append date  form_datetime1" style="position: relative;"
                    data-date="" data-date-format="yyyy-mm-dd"
                    data-link-field="begin">
                    <input class="form-control  datetime" name="begin" id="begin" type="text" style="background: #fff;border-color: #c8ced6;"
                      value="" readonly >
                    <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                  
                 <!--  <input type="hidden" name="begin" id="begin" value="" /> -->
                </div>
              </div>
              <div class="form-group  rangetime ">
                <label for="inputText" class="searchtitle">结束时间</label>
                
                  <div class="controls searchtext input-append date form_datetime2" style="position: relative;"
                    data-date="" data-date-format="yyyy-mm-dd"
                    data-link-field="end">
                    <input class="form-control datetime" type="text" name="end" id="end" style="background: #fff;border-color: #c8ced6;"
                       value="" readonly >
                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                  </div>
                 <!--  <input type="hidden" name="end" id="end" value="" /> -->
                
              </div>
              <div class="form-group rangeList">
                <label for="inputText" class="searchtitle" style="width:68px">序列号</label>
                
                  <input id="productionSn" type="text" name="productionSn"
                    class="form-control  searchtext"
                    maxlength="32" value="" />
                
              </div>
             
          
              <div class="form-group count_grr">
                <label for="analyseGrrType" class="searchtitle">评定方法</label>
                
                  <select id="analyseGrrType" name="analyseGrrType"  data-placeholder="请选择统计标准"
                    class="form-control searchtext">
                    <option value="0">最新数据（件、次、人）计算</option>
                    <option value="1">最新数据（件、人、次）计算</option>
                  </select>
               
              </div>
              <div class="form-group count_grr">
                <label for="workpieceNum" class="searchtitle">工件数量</label>
                
                  <select id="workpieceNum" name="workpieceNum"
                    class="form-control" >
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                    </select>
               
              </div>
              <div class="form-group count_grr">
                <label for="personNum" class="searchtitle">检验员数</label>
                
                  <select id="personNum" name="personNum"
                    class="form-control" >
                    <option value="2">2人</option>
                    <option value="3">3人</option>
                    </select>
                
              </div>
              <div class="form-group count_grr">
                <label for="checkNum" class="searchtitle" style="width:68px">每人</label>
                
					<select id="checkNum" name="checkNum"
                                class="form-control">
                                <option value="2">2次</option>
                                <option value="3">3次</option>
                    </select>
              </div>
            <div class="form-group msa_search">
              <a type="submit" id="searchBtn" class="btn btn-info btn-search1" style="width:150px">数据检索</a>
            </div>
         
        </form>
        </div>
      </div>
      <div id="toolBar" style="margin-bottom:10px;">
        <div class="btn-group">
        <a class="btn btn-default1 chooseBtn" id="fastChooseBtn">
          <i class="fa fa-check-circle"></i>     
                选择最新数据</a>
         <a class="btn btn-default1 chooseBtn" id="sectionBtn" >
           <i class="fa fa-check-circle"></i>
                        区间选择
       </a>
         <a class="btn btn-default1" id="data-confirm-Btn" >
           <i class="fa fa-check"></i>
                        数据确认
       </a>
         
      <a style="display: none" class="btn btn-primary" id="data-confirm" role="button" target="page" rel="calculate" ></a>
     </div>
     <div class="msa_count">已选择:&nbsp;<span id="checkedRowNum">0</span>&nbsp;条</div>
     </div>
     <%--  <a class="btn btn-primary" role="button" target="page" rel="calculate" href="${contextPath }/statistics/analyseGrrDataPage">数据确认2</a> --%>
     <div>
       <table class="table table-striped"  id="analyzeTable" data-field="cgAnalyzeData">
            <thead >
              <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"  >
                  <input class="cbr checkboxCtrl" id="num" type="checkbox">
                </th>
                <th  data-field="id" width="30" >编号</th>
                <th  data-field="productName" width="100" >产品名称</th>
                <th data-field="productionSn" width="100" >序列号</th>
                <th data-field="mesDriverName" width="100" >测量设备</th>
                <th data-field="productProcedureName" width="100" >工序名称</th>
                <th data-field="procedurePropertyName" width="100" >参数名称</th>
                <th data-field="value" width="150" >参数值</th>
                <th data-field="time" width="150" >上传时间</th>
              </tr>
            </thead>
         
          </table>
       </div>   
   
    </div>
    
       
    
    
  </div>
  </div>
</div>
<!--  <script type="text/template" id="dialogTemp"> -->
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
<!-- </script> -->
<c:set var="ParentTitle" value="Count"/>
<c:set var="ModuleTitle" value="analyse"/>	
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/js/canvas2image.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
  $(document).ready(function(){
	 console.log("@@@@@@@@@@@@----"+$("#analyzeTable").innerHeight());
	 if($("#analyzeTable").innerHeight()>300){
		// alert($("#analyzeTable").innerHeight());
		 $("#analyzeTable").parent().addClass("msa_table");
		 
	 }
	$("#analyzeTable").css("border-top","0");
	
	
  });
  </script>
</body>
</html>
