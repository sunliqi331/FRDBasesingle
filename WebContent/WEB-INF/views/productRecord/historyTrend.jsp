<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var obj = JSON.parse('${page}');
    var productId = obj['analyzeSearch.productId'];
    if(productId != '' && productId != 0){
        $("#_productId").val(productId);
        $("#_productId").trigger("chosen:updated");
        $("#_productId").trigger("change");
    }
    var productProcedureId = obj['analyzeSearch.productProcedureId'];
    if(productProcedureId != '' && productProcedureId != 0){
        $("#_productProcedureId").val(productProcedureId);
        $("#_productProcedureId").trigger("chosen:updated");
    }
    console.log("--------------productId----------------" + productId);
    console.log("--------------productProcedureId----------------" + productProcedureId);

	var initSelectOption = function(){
		var productId = obj['analyzeSearch.productId'];
		if(productId != '' && productId != 0){
			$("#_productId").val(productId);
            $("#_productId").trigger("chosen:updated");
            $("#_productId").trigger("change");
			/*if(!$("#_productId").val()){
	            console.log("initSelectOption 2");

			}*/
		}
		var productProcedureId = obj['analyzeSearch.productProcedureId'];
		if(productProcedureId != '' && productProcedureId != 0){
			console.log("productProcedureId+-------"+productProcedureId);
			$("#_productProcedureId").val(productProcedureId);
			$("#_productProcedureId").trigger("chosen:updated");
		}
	}
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
					$("#_productProcedureId").empty();
			  	$.each(data,function(idx,item){
			  		option += "<option value='"+ item.id +"'>"+ item.procedurenum +"</option>";
			  	});
			  	$("#_productProcedureId").append(option);
			  	$("#_productProcedureId").trigger("change");
			  	$("#_productProcedureId").trigger("chosen:updated");
			  	if(callback)
			  		callback($("#_productProcedureId").val());
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
				  $("#_procedurePropertyId").empty();
			  	$.each(data,function(idx,item){
			  		option += "<option value='"+ item.id +"'>"+ item.propertyname +"</option>";
			  	});
			  	$("#_procedurePropertyId").append(option);
			  	$("#_procedurePropertyId").trigger("chosen:updated");
			  	if(callback){
			  		callback();
			  	}
			  	//initSelectOption();
			  }	
		  });
		  
	  }
	  $("#_productId").change(function(){
		  generateProcedureSelect($(this).val());
	  });
	  generateProcedureSelect($("#_productId").val(),generateProcedurePropertySelect);
	  $("#_productProcedureId").change(function(){
		  generateProcedurePropertySelect($(this).val());
	  });
	 // generateProcedureSelect($("#product").val(),generateProcedurePropertySelect($("#procedure").val()));
	 $("#searchBtn").click(function(){
		 $("#formID").submit();
	 });
	   /*  var main1 = echarts.init(document.getElementById('main1'));
		var main2 = echarts.init(document.getElementById('main2'));
		var main3 = echarts.init(document.getElementById('main3'));
		var main4 = echarts.init(document.getElementById('main4'));
		var main5 = echarts.init(document.getElementById('main5'));
		var main5 = echarts.init(document.getElementById('main6')); */
		
		$("#_formID").submit(function(){
			
			
			
			$(".msa_a").show();
			if(null == $("#_procedurePropertyId").val() || $("#_procedurePropertyId").val() == ''){
				swal("请选择参数");
				return false;
			}
			if('' == $("#begin").val() || $("#end").val() == ''){
				swal("请输入时间范围");
				return false;
			}
			//添加人性化提示
			var time1 = $("#begin").val();
       	    var time2 = $("#end").val();
       	    var startTime = Date.parse(time1)/1000; //秒
		    var endTime = Date.parse(time2)/1000;
			if(startTime>=endTime){
				swal("时间范围错误");
				return false;
			}
			if(endTime-startTime>660){
				//时间不可查过10分钟
				swal("时间范围要在10分钟之内，超过十分钟数据将失真！");
				return false;
			} 
			$.blockUI({
  				baseZ:99999,
  				message:"<h1>加载数据中,请稍等...</h1>",
			})
			var $that = $(this);
			$.ajax({
				url:'${contextPath}/statistics/propertyHistoryTrend',
				data:$that.serializeArray(),
				dataType:'JSON',
				type:'POST',
				success:function(data){
					console.log(data);
					var size = Math.floor(12 / data.length);
					// size = 1;
					var width = Math.floor($(".row").width() / data.length-30);
					$.each($(".charts"),function(idx,item){
						echarts.dispose($("#main"+(idx+1)));
						$("#main"+(idx+1)).remove();
					});
					$.each(data,function(idx,item){
						console.log("mark");
						// var echartsDiv = $('<div class="col-sm-'+size+' charts style='float:none'><div class=""><div id="main'+ (idx+1) +'" style="width: '+ width +'px; height: 250px;"></div></div></div>');
						var echartsDiv = $('<div style="float:none;" class="col-sm-'+size+' charts ><div class=""><div id="main'+ (idx+1) +'" style="width: '+ 898 +'px; height: 250px;"></div></div></div>');
						$(".row").append(echartsDiv);
						var main = echarts.init(document.getElementById('main'+(idx+1)));
						var echartsOption = JSON.parse(item);
						var option_grid=echartsOption.grid;
						option_grid.right="11%";
						echartsOption.title.textStyle ={fontSize:16,color:'#333',textAlign:'left'};
						echartsOption.series[0].markPoint.symbolSize=[90,48];
						if(idx == 0){
							echartsOption.itemStyle={normal: {color: '#e36c8c'}};
  						}else if( idx == 1){
  							echartsOption.itemStyle={normal: {color: '#9dddcf'}};
  						}else if(idx == 2){
  							echartsOption.itemStyle={normal: {color: '#d19978'}};
  						}else if(idx == 3){
  							echartsOption.itemStyle={normal: {color: '#838ebb'}};
  						}else if(idx == 4){
  							echartsOption.itemStyle={normal: {color: '#f09a77'}};
  						}else if(idx == 5){
  							echartsOption.itemStyle={normal: {color: '#779699'}};
  						}
						main.setOption(echartsOption);
						//console.log(item);
					});
					$.unblockUI();
				}
			});
			
			return false;
		});
		
});

</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
		<div>
			<form method="post" id="_formID" class="form-inline">
			<div class="pageFormContent" layoutH="58">

              <h4 class="media-heading">查询信息</h4>
              <hr class="hr-normal">
              <div class="searchBar search_driver clearfix" style="border:none;box-shadow:none;padding-left: 15px;">	
				
				<div class="form-group">
					<label for="_productId" class="searchtitle">产品型号 </label> <select
						id="_productId" name="productId" data-placeholder="请选择产品"
						class="form-control searchtext chosen-select">
						<c:forEach items="${products }" var="product">
							<option value="${product.id }">${product.name }</option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group">
					<label for="_productProcedureId" class="searchtitle">工序编号 </label> <select
						id="_productProcedureId" name="productProcedureId"
						data-placeholder="请选择工序"
						class="form-control searchtext chosen-select">
						<option value="0"></option>
					</select>

				</div>
				<div class="form-group">
					<label for="_procedurePropertyId" class="searchtitle">被测参数</label> <select
						id="_procedurePropertyId" name="procedurePropertyIds" multiple
						data-placeholder="请选择工序参数" class="form-control chosen-select">
					</select>

				</div>
				<div class="form-group ">
					<label for="begin" class="searchtitle">开始时间</label>

					<div class="controls searchtext input-append date  form_datetime1"
						style="position: relative;" data-date=""
						data-date-format="yyyy-mm-dd" data-link-field="begin">
						<input class="form-control  datetime" name="begin" id="begin"
							type="text" style="background: #fff; border-color: #c8ced6;"
							value="" readonly> <span class="add-on"
							style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
							<i class="fa fa-remove"></i>
						</span> <span class="add-on"
							style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
							<i class="fa fa-th"></i>
						</span>

						<!--  <input type="hidden" name="begin" id="begin" value="" /> -->
					</div>
				</div>
				<div class="form-group  ">
					<label for="form_datetime2" class="searchtitle">结束时间</label>

					<div id="form_datetime2" class="controls searchtext input-append date form_datetime2"
						style="position: relative;" data-date=""
						data-date-format="yyyy-mm-dd" data-link-field="end">
						<input class="form-control datetime" type="text" name="end"
							id="end" style="background: #fff; border-color: #c8ced6;"
							value="" readonly> <span class="add-on"
							style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
							<i class="fa fa-remove"></i>
						</span> <span class="add-on"
							style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
							<i class="fa fa-th"></i>
						</span>
					</div>
					<!--  <input type="hidden" name="end" id="end" value="" /> -->

				</div>
				<button class="btn btn-info btn-search1" type="submit">确认</button>
				</div>
				</div>
			</form>
			
			</div>
		
			
	<div class="msa_a">
	   <h4 class="media-heading">查询结果</h4>
              <hr class="hr-normal">
		<div class="row pre-scrollable" style="max-height:550px !important; overflow-x:hidden;">
		</div>
	</div>	
	<div>
	<div class="clearfix"></div>
	    <div class="modal-footer">
<!--           <button id="confirm" type="button" class="btn btn-primary">确定</button> -->
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
        </div>
	<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
	<script type="text/javascript"
		src="${contextPath }/styles/chosen/chosen.jquery.js"></script>

