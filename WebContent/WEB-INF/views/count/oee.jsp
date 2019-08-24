<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>OEE</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
<script src="${contextPath}/js/jquery.js"></script>
<script src="${contextPath}/js/bootstrap.js"></script>

<style type="text/css">
	.lab_inp label{
		width: 100px;
	}
	.form-inline .form-group{
	  margin-right:40px;
	}
	.form-inline .form-group select,.form-inline .form-group input{
	  min-width:200px;
	}
	.lab_inp input{
		width: 100px;
	}
	.lab_inp select{
		width: 100px;
	}
	.lab_inp{
		line-height:200%;
	}
	#startTime{
 	float:right 
	}
	#endTime{
	float:right 
	}
	#start{
     margin-top:10px
	}
	#end{
     margin-top:10px
	}
	.oHeight{
	  height:6px; 
	  width:100% ;
	}
	.mar{
	margin-top:6px
	}
	#new{
	margin-top:29px
	}
	.oee_left{
	  width:22%;
	  float: left;
	}
	.oee_right{
	  width:75%;
	  float: right;
	}
	.oee_right1{
	  width:30%;
	  float: left;
	}
	.oee_right2{ 
	  width:70%;
	  float: right;
	  overflow: auto;
	}
	.startTime2{
	  position:relative;
	  float:right;
	}
	.oeeBtn{
	  float: right;
	  margin-bottom: 0px;
	  margin-top:10px;
	}
	@media (max-width: 767px) {
		#startTime{
	 	  display:block;
	 	  float: none;
		}
		#endTime,.startTime2{
		  display:block;
		  float: none;
		}

		#start{
	     margin-top:0px
		}
		#end{
	     margin-top:0px
		}
		.oHeight{
		 height:0
		}
		.oee_left,.oee_right,.oee_right1,.oee_right2{
		  width:100%;
		  float: none;
		}
		.lab_inp select,.lab_inp input{
		 width:60%;
		}
		#tab3 .btn{
		  margin-bottom:10px;
		}
		
}
	@media (max-width: 368px) {
		.oeeBtn{
		  float: none;
		  clear:both;
		  margin-bottom: 10px;
		  margin-top:0px;
		}
   }
</style>

<script type="text/javascript">

	//根据柱状图的数据计算应该以什么颜色显示
	function makeColorList(originalDataArr){
		var resColorList = [];
		if(originalDataArr){
			$.each(originalDataArr, function(idx, obj){
				if(obj < 70.0){
					resColorList.push('#D53A35');
				}else{
					resColorList.push('#3398DB');
				}
			});
		}
		return resColorList;
	}

	//初始化手动OEE中的柱状图 参数 ：data 柱状图数据的数组 
	function getDataAndShowBar1(data){
		var colorList = makeColorList(data);
		var main1 = echarts.init(document.getElementById('main1'));
		var option_main_bar = {
			title: {text: "设备OEE",x: "center"},
			//color: ['#d14a61'],
			tooltip: {trigger: "axis"},
			legend: {data: ["百分比"],x: "right",right: 10},
			xAxis: [{type: "category",
				data: ["设备OEE","时间开动率","性能开动率","合格品率"]}],
			yAxis: [{type: "value"}],
			series: [{name: "百分比",type: "bar",
				label: {normal: {position: 'top',show: true}},
				data: data,
				itemStyle: {   
	                normal:{  
	                    color: function (params){
	                        return colorList[params.dataIndex];
	                    }
	                }
	            }
				}]
			};
		main1.clear();
		main1.setOption(option_main_bar);
	}
	
	//初始化自动OEE中的柱状图 参数 ：data 柱状图数据的数组 
	function getDataAndShowBar2(data){
		var colorList = makeColorList(data);
		var main2 = echarts.init(document.getElementById('main2'));
		var option_main_bar = {
			title: {text: "设备OEE",x: "center"},
			//color: ['#d14a61'],
			tooltip: {trigger: "axis"},
			legend: {data: ["百分比"],x: "right",right: 10},
			xAxis: [{type: "category",
				data: ["设备OEE","时间开动率","性能开动率","合格品率"]}],
			yAxis: [{type: "value"}],
			series: [{name: "百分比",type: "bar",
				label: {normal: {position: 'top',show: true}},
				data: data,
				itemStyle: {   
	                normal:{  
	                    color: function (params){
	                        return colorList[params.dataIndex];
	                    }
	                }
	            }
				}]
			};
		main2.clear();
		main2.setOption(option_main_bar);
	}
	
	//历史OEE中的柱状图显示某一天的数据
	function getDataAndShowHistoryDayBar(data){
		var DriverOEEs = data.DriverOEEs;
		if(DriverOEEs.length<1){
			return;
		}
		var time = DriverOEEs[0].createDate.split(" ");
  		var arr1 = new Array();//柱状图X轴数据
  		var arr2 = new Array();//柱状图柱子数据
  		for(var i=DriverOEEs.length-1;i>=0;i--){
  			var obj = DriverOEEs[i];
  			var time2 = obj.createDate.split(" ")[1];
  			arr1.push(obj.classes+"/"+time2);
  			arr2.push(parseFloat(obj.oeeRate));
  		}
  		var colorList = makeColorList(arr2);
  		/* if(DriverOEEs.length>15){ //判断数据条数，当条数过多时加上滚动条
  			$("#history_day").css("width","1800px");
  			$("#history_control").css("overflow-x","scroll");
  		}else{
  			$("#history_day").css("width",$("#history_control").parent().width()+"px");
  		} */
  		$("#history_day").show();
  		$("#history_day").empty();
  		//$("#history_day").css("width",$("#history_control").parent().width()+"px");
  		//$("#history_day").css("height","360px");
		var history_day = echarts.init(document.getElementById('history_day'));
		var option_history_day = {
				title:{text:DriverOEEs[0].mesDriver.name+"日OEE记录",x:'50'},
		        tooltip: {trigger: 'axis'},
		        toolbox: {feature: {saveAsImage: {show: true}}},
		        legend: {data:[time[0]]},
		        xAxis: [{type: 'category',
		        		data: arr1}],
		        yAxis: [{type: 'value',name: '单位：%',
		                axisLabel: {formatter: '{value}'}}],
		        series: [{name:time[0],
		                color:'#d14a61',
		                label: {normal: {position: 'top',show: true}},
		                type:'bar',
		                data:arr2,
		                itemStyle: {   
			                normal:{  
			                    color: function (params){
			                        return colorList[params.dataIndex];
			                    }
			                }
			            }
		                }]};
		//数据条数超过15条时加上dataZoom
		if(DriverOEEs.length>15){
			var ss = {dataZoom:[{startValue: arr1[0]}, {type: 'inside'}]};
			option_history_day = $.extend({},option_history_day,ss);
		}
		history_day.clear();
		history_day.setOption(option_history_day);
	}
	//历史OEE中的柱状图显示某周或者某月的数据
	function getDataAndShowHistoryWeekBar(data){
		$("#history_day").show();
		$("#history_day").empty();
		//$("#history_day").css("width",$("#history_control").parent().width()+"px");
  		//$("#history_day").css("height","360px");
  		/* if(data.xAxis.length>15){ //判断数据条数，当条数过多时加上滚动条
  			$("#history_day").css("width","1800px");
  			$("#history_control").css("overflow-x","scroll");
  		} */
		var history_day = echarts.init(document.getElementById('history_day'));
		var option_history_day = {
				title:{text:data.driverName+"日均OEE",x:'50'},
		        tooltip: {trigger: 'axis'},
		        color:  ['#00A0E9', '#E5910D', '#359F67','#00A0E9', '#E5910D', '#359F67'],
		        toolbox: {feature: {saveAsImage: {show: true}}},//axisLabel:{interval: 0,rotate:40}}],
		        dataZoom: [{startValue: data.xAxis[0]}, {type: 'inside'}],
		        legend: {data:["早班","中班","晚班","A折线","P折线","Q折线"],
		        		 selected : {'A折线': false,'P折线':false,'Q折线':false}},
		        xAxis: [{type: 'category',
		        		 data: data.xAxis}],
		        yAxis: [{type: 'value',name: '单位：%',
		                axisLabel: {formatter: '{value}'}}],
		        series: [
		        {name:'早班',label: {normal: {position: 'top',show: true,
		        	formatter:function(params){return  Math.round(params.data)}}},type:'bar',data:data.aList},
		        {name:'中班',label: {normal: {position: 'top',show: true,
		        	formatter:function(params){return  Math.round(params.data)}}},type:'bar',data:data.pList},
		        {name:'晚班',label: {normal: {position: 'top',show: true,
		        	formatter:function(params){return  Math.round(params.data)}}},type:'bar',data:data.qList},
		        {name:'A折线',type:'line',smooth:true,data:data.aList},
		        {name:'P折线',type:'line',smooth:true,data:data.pList},
		        {name:'Q折线',type:'line',smooth:true,data:data.qList}
		                ]};
		if(dateType=="week"){
			delete option_history_day.dataZoom;
		}
		history_day.clear();
		history_day.setOption(option_history_day);
		//console.log(JSON.stringify(history_day.getOption()));
	}
	
    //页面标签页相互切换，点击事件
	$(document).ready(function($) {
		$('.dropdown-toggle').dropdown();
		$("#history_day").hide();
		getDataAndShowBar1([0,0,0,0]);
		getDataAndShowBar2([0,0,0,0]);
		//$("#history_day").hide();
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
	    //点击历史oee标签页时刷新纪录
	    $("ul.tabs li:last").bind("click",function(){
	    	$.table.refreshCurrent("${contextPath }/stats/listAllOEE");
	    });
	    
	    $('.form_datetime1').datetimepicker({
	        language:  'zh-CN',
	    	   format: 'hh:ii:ss',
	         weekStart: 1,
	         todayBtn:'linked',
	    		autoclose: 1,
	    		todayHighlight: 1,
	    		startView: 1,
	    		forceParse: 0,
	         showMeridian: 1,
	     });
	     
	     $('.form_datetime3').datetimepicker({
		        language:  'zh-CN',
		    	format: 'yyyy-mm-dd hh:ii:ss',
		        weekStart: 1,
		        todayBtn:'linked',
		        autoclose: 1,
		        todayHighlight: 1,
		        startView: 2,
		    	forceParse: 0,
		        showMeridian: 1,
		 });  
	     $.table.init('table',{});
	});
</script>

</head>

<body>
<div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp" %>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp" %>
 <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
      <li>设备OEE</li>
    </ol>
    
    
  <div class="main-wrap">
    <div class="main-body">
      
      <ul class="tabs clearfix">  
        <li class="active"><a class="tab1" href="#tab1">手动OEE</a></li>
        <li style="margin-left: -1px"><a class="tab2" href="#tab2">自动OEE</a></li>
        <li style="margin-left: -1px"><a class="tab3" href="#tab3">历史OEE记录</a></li>
      </ul>
      
      <div class="tab_container chart_tab">
      <!-- 第一个导航页  手动OEE -->
      <div id="tab1" class="tab_content clearfix" style="display: block; padding-bottom:15px;min-height:540px;">
      	  <div class="driver_info">
          <form class="form-inline" action="#" id="saveHandGetOee" onsubmit="return false;">
              <div class="form-group" >
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory1" class="form-control searchtext validate[required]" data-placeholder="请选择工厂" >
                  <option value="">请选择工厂</option>
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                  </select> 
              </div>
			  <div class="form-group" >
                 <label for="inputText" class="searchtitle">选择产线</label>
                 <select id="chooseProductLine1" class="form-control validate[required]" >
                 <option value=''>请选择产线</option>
                  </select> 
              </div>
              <div class="form-group" >
                 <label for="inputText" class="searchtitle">选择设备</label>
                 <select id="chooseDriver1" class="form-control validate[required]" >
                 <option value=''>请选择设备</option>
                  </select> 
              </div>
              
              <div class="oHeight" ></div>
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle" style="margin-top:5px">选择班次</label>
                 <select id="chooseClasses1" class="form-control validate[required]" >
                  	<option value="A">早班</option>
                    <option value="P">中班</option>
                    <option value="Q">晚班</option>
                  </select> 
              </div>
		    	    <div class="form-group" >
		                  <label id="start" for="inputText" class="searchtitle"  >开始时间</label>
		                     <div id="startTime" class="controls input-append date form_datetime1" style="position:relative;margin-left:4px" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="begin">
		                        <input id="handtime1" class="form-control datetime autotime validate[required]" type="text" style="background: #fff;margin-top:5px" value="" readonly>
		                        <input type="hidden" id="handtime1_hid" value="">
		                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                     </div>

		                     </div>
		                 <div class="form-group" >
		                   <label id="end" for="inputText" class="searchtitle" >结束时间</label>
		                  <div id="endTime"  class="controls input-append date form_datetime1" style="position:relative;margin-left:3px"  data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">

		                    <input id="handtime2" class="form-control datetime autotime validate[required]" type="text" style="background: #fff;margin-top:5px" value="" readonly>
		                    <input type="hidden" id="handtime2_hid" value="">
		                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                  </div>
		    	    </div>
          </form>
          </div>
          
          <hr>
          
    	  <div class="lab_inp oee_left" >
    	  <form action="#" id="calcHandOee" onsubmit="return false;">
			<label >设备运行时间：</label><input  type='text' id="runTime" value="" class="validate[required]"
			onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="分钟"><br>
    	  	<label class="mar">计划停机时间：</label><input type='text' id="haltTime" value="" class="mar validate[required]"
    	  	onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="分钟"><br>
    	  	<label class="mar">设备调整时间：</label><input type='text' id="adjustTime" value="" class="mar validate[required]"
    	  	onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="分钟"><br>
    	  	<label class="mar">故障停机时间：</label><input type='text' id="hitchTime" value="" class="mar validate[required]"
    	  	onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="分钟"><br>
    	  	<label class="mar">理论加工周期：</label><input type='text' id="circle" value="" class="mar validate[required]"
    	  	onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
                (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" placeholder="分钟/件"><br>
    	  	<label class="mar">总产量：</label><input type='text' id="output" value="" class="mar validate[required]"
    	  	onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="件"><br>
    	  	<label class="mar">废品件：</label><input type='text' id="waste" value="" class="mar validate[required]"
    	  	onkeyup="value=value.replace(/[^\d]/g,'')" placeholder="件"><br>
    	  	<label class="mar">选择精确度：</label><select class="mar" id="chooseExact">
                  	<option value="4">2</option>
                    <option value="2">0</option>
                    <option value="3">1</option>
                    <option value="5">3</option>
                    <option value="6">4</option>
                  </select> 
    	  	<br><br>
    	  	<button class="btn btn-info" id="handOperate">开始运算</button>
    	  	<button class="btn btn-info" id="saveHandResult" style="margin-left:10px">保存运算结果</button>
    	  </form>
    	  </div>
    	  <div class="lab_inp oee_right" style="">
    	  	<div class="oee_right1" >
    	  		<label id="new">最新运算结果</label><br>
	    	    <label >OEE：</label><input type='text' id="oeeRate1" readonly="readonly" value=""><br>
				<label class="mar">时间开动率%：</label><input class="mar" type='text' id="timeRate1" readonly="readonly" value=""><br>
	    	  	<label class="mar">性能开动率%：</label><input class="mar" type='text' id="propertyRate1" readonly="readonly" value=""><br>
	    	  	<label class="mar">合格品率%：</label><input class="mar" type='text' id="passRate1" readonly="readonly" value=""><br>
    	  	</div>
    	  	
    	  	<!-- 柱状图 -->
    	  	<div class="oee_right2" style="margin-top: auto; "><div id="main1" style="width:460px; height: 260px;"></div></div>
    	  	
    	  </div>
          
          <!-- 第一个标签页逻辑处理代码 -->
          <script type="text/javascript">
        //填充表单数据
    	  $(".form-group").bind("change",function(){
    		  //给隐藏域赋值
    		  var handtime1 = $("#handtime1").val();
    		  var handtime2 = $("#handtime2").val();
    		  var oDate = new Date();
    		  var year = oDate.getFullYear();
    		  var month = oDate.getMonth()+1;
    		  if(month<10){
    			  month = "0"+month;
    		  }
    		  var day = oDate.getDate();
    		  if(day<10){
    			  day = "0"+day;
    		  }
    		  var prefix = year+"-"+month+"-"+day+" ";
    		  if((handtime1!="")&&(handtime1!=null)){
    			  handtime1 = prefix + handtime1;
    			  $("#handtime1_hid").val(handtime1);
    		  }
    		  if((handtime2!="")&&(handtime2!=null)){
    			  handtime2 = prefix + handtime2;
    			  $("#handtime2_hid").val(handtime2);
    		  }
    	  });
          
          
          //手动运算OEE时的点击事件
          $("#handOperate").bind("click",function(){
        	  if (!$("#calcHandOee").validationEngine('validate')) {
  				return false;
  		  	  }
        	  $.ajax({
      	        type : "POST",
      	        url : "${contextPath }/stats/handOperate",
      	        data: "runTime="+$("#runTime").val()+"&haltTime="+$("#haltTime").val()+"&adjustTime="+$("#adjustTime").val()
      	        +"&hitchTime="+$("#hitchTime").val()+"&circle="+$("#circle").val()+"&output="+$("#output").val()
      	        +"&waste="+$("#waste").val()+("&chooseExact=")+$("#chooseExact").val(),
      	        dataType : "text", 
      	        success : function(data) {
      	        	if(data=="paramError"){
      	        		swal("错误", "参数有误。", "error");
      	        	}else{
      	        		var strs = data.split(",");
          	        	$("#oeeRate1").val(strs[0]+" %");
          	        	$("#timeRate1").val(strs[1]+" %");
          	        	$("#propertyRate1").val(strs[2]+" %");
          	        	$("#passRate1").val(strs[3]+" %");
          	        	var dat = [parseFloat(strs[0]),parseFloat(strs[1]),
          	        	           parseFloat(strs[2]),parseFloat(strs[3])];
          	        	getDataAndShowBar1(dat);
      	        	}
      	        },
      			error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
      	    });
          });
          
          //点击保存运算结果时的点击事件
          $("#saveHandResult").bind("click",function(){
        	  if (!$("#saveHandGetOee").validationEngine('validate')) {
    				return false;
    		  	  }
          	  var handtime1 = $("#handtime1_hid").val();
          	  var handtime2 = $("#handtime2_hid").val();
          	  var time1 = Date.parse(handtime1)/1000;
  			  var time2 = Date.parse(handtime2)/1000;
          	  if(time1>=time2){
          		  swal("班次时间错误");
      			  return false;
          	  }else if($("#oeeRate1").val()==""){
          		  swal("无数据，请先计算数据");
      			  return false;
          	  }
          	  var oeeRate1 = $("#oeeRate1").val().split(" ")[0];
          	  var timeRate1 = $("#timeRate1").val().split(" ")[0];
          	  var propertyRate1 = $("#propertyRate1").val().split(" ")[0];
          	  var passRate1 = $("#passRate1").val().split(" ")[0];
          	  $.ajax({
        	        type : "POST",
        	        url : "${contextPath }/stats/saveHandResult",
        	        data: "chooseFactory1="+$("#chooseFactory1").val()+"&chooseProductLine1="+$("#chooseProductLine1").val()
        	        +"&chooseDriver1="+$("#chooseDriver1").val()+"&chooseClasses1="+$("#chooseClasses1").val()
        	        +"&handtime1="+$("#handtime1_hid").val()+"&handtime2="+$("#handtime2_hid").val()+"&oeeRate1="+oeeRate1
        	        +"&timeRate1="+timeRate1+"&propertyRate1="+propertyRate1+"&passRate1="+passRate1,
        	        dataType : "text", 
        	        success : function(data) {
        	        	if(data=="ok"){
        	        		swal("","保存成功！", "success");
        	        	}
        	        },
        			error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
        	 });
          });
          
          //第一个导航页选择工厂时的点击事件
          $("#chooseFactory1").change(function(event){
              $("#chooseProductLine1").empty();
              $("#chooseProductLine1").append("<option value=''>请选择产线</option>");
              $("#chooseProductLine1").trigger("chosen:updated");
            if($("#chooseFactory1").val()!=""){
                ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseFactory1").val(),paintProductLine1);
            }
          });
          function paintProductLine1(data){
            $.each(data,function(idx,item){
              $("#chooseProductLine1").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
            });
              $("#chooseProductLine1").trigger("chosen:updated");
          };
          
          //第一个导航页选择产线时的点击事件
          $("#chooseProductLine1").change(function(event){
              $("#chooseDriver1").empty();
              $("#chooseDriver1").append("<option value=''>请选择设备</option>");
              $("#chooseDriver1").trigger("chosen:updated");
            if($("#chooseProductLine1").val()!=""){
                ajaxTodo("${contextPath}/productline/DriverData2/"+$("#chooseProductLine1").val(),paintDrivers1);
            }
          });
          function paintDrivers1(data){
            $.each(data,function(idx,item){
              $("#chooseDriver1").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
            });
              $("#chooseDriver1").trigger("chosen:updated");
          };
          </script>
          
      </div>
      
      <!-- 第二个导航页 自动OEE -->
      <div id="tab2" class="tab_content" style="display: none;padding-bottom:15px;min-height:540px;">
      	  <div class="driver_info">
          <form class="form-inline" action="#" id="autoGetOee" onsubmit="return false;">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory2" class="form-control validate[required]" >
                  <option value="">请选择工厂</option>
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                  </select> 
              </div>
			  <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择产线</label>
                 <select id="chooseProductLine2" class="form-control validate[required]" >
                 	<option value=''>请选择产线</option>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择设备</label>
                 <select id="chooseDriver2" class="form-control validate[required]" >
                 	<option value=''>请选择设备</option>
                  </select> 
              </div>
              
              <div style=" height:4px; width:100% ;"></div>
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择班次</label>
                 <select id="chooseClasses2" class="form-control searchtext">
                  	<option value="A">早班</option>
                    <option value="P">中班</option>
                    <option value="Q">晚班</option>
                  </select> 
              </div>
		    	      <div class="form-group" >
		                   <label for="inputText" class="searchtitle" >开始时间</label>
		                     <div class="controls input-append date form_datetime1 startTime2" style="" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="begin">
		                        <input id="autotime1" class="form-control datetime autotime validate[required]" type="text" style="background: #fff;" value="" readonly>
		                        <input type="hidden" id="autotime1_hid" value="">
		                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                     </div>

		                     </div>
		                  <div class="form-group" >
		                  <label for="inputText" class="searchtitle" >结束时间</label>
		                  <div class="controls input-append date form_datetime1 startTime2"   data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">

		                    <input id="autotime2" class="form-control datetime autotime validate[required]" type="text" style="background: #fff; value="" readonly>
		                    <input type="hidden" id="autotime2_hid" value="">
		                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                  </div>
		    	    </div>
		    	    
            	<div class="oHeight"></div>
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择产品</label>
                 <select id="chooseModel" class="form-control searchtext validate[required]">
                 	<option value="">请选择产品</option>
                  	<c:forEach var="p" items="${mesProductList}">
                  	<option value="${p.modelnum}">${p.name}</option>
                  	</c:forEach>
                 </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >起始时间</label>
                 <div class="controls input-append date form_datetime3 startTime2"  data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="begin">
                        <input id="autotime3" class="form-control datetime validate[required]" type="text" style="background: #fff;" value="" readonly>
                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                        <i class="fa fa-remove"></i>
                        </span>
                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                        <i class="fa fa-th"></i></span>
                 </div>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >储存周期</label>
                 <select id="chooseCycle" class="form-control searchtext">
                	<option value="5">5分钟</option>
                    <option value="10">10分钟</option>
                    <option value="30">30分钟</option>
                    <option value="60">1小时</option>
                    <option value="180">3小时</option>
                    <option value="300">5小时</option>
                  </select> 
              </div>
          </form>
          </div>
          
          <hr>
          
    	  <div class="lab_inp oee_left" >
			<label>设备运行时间：</label><input type='text' readonly="readonly" id="runningTime" value=""><br>
    	  	<label class="mar">实际运行时间：</label><input class="mar" type='text' readonly="readonly" id="trueTime" value=""><br>
    	  	<label class="mar">设备停机时间：</label><input class="mar" type='text' readonly="readonly" id="stopTime" value=""><br>
    	  	<label class="mar">理论加工周期：</label><input class="mar contr" type='text' id="period" style="border:1.5px solid" value="" 
    	  	onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
                (event.keyCode<48 || event.keyCode>57)) event.returnValue=false" placeholder="分钟/件"><br>
    	  	<label class="mar">总产量：</label><input class="mar" type='text' readonly="readonly" id="yield" value=""><br>
    	  	<label class="mar">废品件：</label><input class="mar" type='text' readonly="readonly" id="scrap" value=""><br>
    	  	<label class="mar">选择精确度：</label><select class="mar contr" id="chooseExact2">
                  	<option value="4">2</option>
                    <option value="2">0</option>
                    <option value="3">1</option>
                    <option value="5">3</option>
                    <option value="6">4</option>
                  </select>
    	  	<br><br>
    	  	<input type="hidden" id="timerNum" value="">
    	  	<button class="btn btn-info" id="autoOperate" >实时运算</button>
			<button class="btn btn-warning" id="stopOperate" style="margin-left:10px">停止运算</button>
    	  </div>
    	  <div class="lab_inp oee_right" >
    	  	<div class="oee_right1">
    	  		<label style="margin-top:29px">最新运算结果</label><br>
	    	    <label class="mar">OEE：</label><input class="mar" type='text' id="oeeRate2" readonly="readonly" value=""><br>
				<label class="mar">时间开动率%：</label><input class="mar" type='text' id="timeRate2" readonly="readonly" value=""><br>
	    	  	<label class="mar">性能开动率%：</label><input class="mar" type='text' id="propertyRate2" readonly="readonly" value=""><br>
	    	  	<label class="mar">合格品率%：</label><input class="mar" type='text' id="passRate2" readonly="readonly" value=""><br>
    	  	</div>
    	  	
    	  	<!-- 柱状图 -->
    	  	<div class="oee_right2" style="margin-top: auto;"><div id="main2" style="width:460px; height: 260px;"></div></div>
    	  </div>
          
          
          <!-- 第二个标签页逻辑处理代码 -->
          <script type="text/javascript">
    
          //调用ajax计算数据
          function autoOperateData(){
        	  $.ajax({
      	        type : "POST",
      	        url : "${contextPath }/stats/autoOperate",
      	        data: "chooseFactory2="+$("#chooseFactory2").val()+"&chooseProductLine2="+$("#chooseProductLine2").val()
			   	        +"&chooseDriver2="+$("#chooseDriver2").val()+"&chooseClasses2="+$("#chooseClasses2").val()
			   	        +"&autotime1="+$("#autotime1_hid").val()+"&autotime2="+$("#autotime2_hid").val()
			   	        +"&chooseModel="+$("#chooseModel").val()+"&autotime3="+$("#autotime3").val()
			   	        +"&period="+$("#period").val()+"&chooseExact2="+$("#chooseExact2").val(),
      	        dataType : "text",
      	        success : function(data) {
      	        	$.unblockUI();
      	        	if(data.length>100){ //登陆过期
      	        		location.reload();
      	        	}else if(data=="none"){
      	        		swal("错误","没有查询到数据","error");
      	        	}else{
      	        		if($("#oeeRate2").val()==""){
      	        			swal({title:"数据获取成功",type:"success"});
      	        		}
      	        		var strs = data.split(",");
        	        	$("#oeeRate2").val(strs[0]+" %");
        	        	$("#timeRate2").val(strs[1]+" %"); 
        	        	$("#propertyRate2").val(strs[2]+" %");
        	        	$("#passRate2").val(strs[3]+" %");
        	        	$("#runningTime").val(strs[4]+" 分");
        	        	$("#trueTime").val(strs[5]+" 分");
        	        	$("#stopTime").val(strs[6]+" 分");
        	        	$("#yield").val(strs[7]+" 件");
        	        	$("#scrap").val(strs[8]+" 件");
        	        	var dat = [parseFloat(strs[0]),parseFloat(strs[1]),
        	        	           parseFloat(strs[2]),parseFloat(strs[3])];
        	        	getDataAndShowBar2(dat);
      	        	}
      	        },
   			    error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
      	  	});
        	  
          }
          
          //给隐藏域赋值
          function initHidden(){
        	  var autotime1 = $("#autotime1").val();
    		  var autotime2 = $("#autotime2").val();
    		  var oDate = new Date();
    		  var year = oDate.getFullYear();
    		  var month = oDate.getMonth()+1;
    		  if(month<10){
    			  month = "0"+month;
    		  }
    		  var day = oDate.getDate();
    		  if(day<10){
    			  day = "0"+day;
    		  }
    		  var prefix = year+"-"+month+"-"+day+" ";
    		  if((autotime1!="")&&(autotime1!=null)){
    			  autotime1 = prefix + autotime1;
    			  $("#autotime1_hid").val(autotime1);
    		  }
    		  if((autotime2!="")&&(autotime2!=null)){
    			  autotime2 = prefix + autotime2;
    			  $("#autotime2_hid").val(autotime2);
    		  }
          }
          
    	  //点击实时运算时的点击事件
    	  $("#autoOperate").bind("click",function(){
    		  if (!$("#autoGetOee").validationEngine('validate')) {
    				return false;
    		  }
    		  //获取表单数据
    		  var autotime1 = $("#autotime1_hid").val();
    		  var autotime2 = $("#autotime2_hid").val();
    		  var autotime3 = $("#autotime3").val();
    		  var period = $.trim($("#period").val()+"");
    		  
    		  var time1 = Date.parse(autotime1)/1000;
			  var time2 = Date.parse(autotime2)/1000;
			  var time3 = Date.parse(autotime3)/1000;
			  var now = Date.parse(new Date())/1000;
    		  //验证班次开始时间和结束时间是否合理
    		  if(period==""||period==null){
    			  swal("请输入理论加工周期");
    			  return false;
    		  }
			  if(time2<=time1){
    			  //temp = Math.round(((time2-time1)/60));
				  swal("班次时间错误");
				  return false;
			  }
			  //验证起始时间是否处于当前时间之前
    		  if(time3>=now){
    			  swal("起始时间错误");
    			  return false;
    		  }
			  //限制时间范围
    		  if((now-time3)>5357000){
    			  swal("起始时间过早，请选择前2月以内");
    			  return false;
    		  }
    		  $.blockUI({
  				baseZ:99999,
  				message:"<h1>加载数据中,请稍等...</h1>",
			  })
    		  //设置定时任务
    	      var timespan = parseInt($("#chooseCycle").val());
    	      timespan = timespan*60*1000;
    	      $("#autoOperate").html("正在运算……");
    	      //设置输入框不可用
        	  $("#autoOperate").attr("disabled","disabled");
        	  $("#tab2 .form-control").attr("disabled","disabled");
        	  $("#tab2 .contr").attr("disabled","disabled");
        	  $(".add-on").hide();
    	      autoOperateData(); //立马执行一次数据，计算一次
			  var timerId = window.setInterval(function(){
    		  	initHidden();
    		  	autoOperateData();//定时任务
			  },timespan);
			  $("#timerNum").val(timerId);
    	  });
    	  
    	  //点击停止运算时的点击事件
    	  $("#stopOperate").bind("click",function(){
    		  var keyer1 = $("#timerNum").val();
    		  var keyer = parseInt(keyer1);
    		  window.clearInterval(keyer);
    		  $("#autoOperate").html("实时运算");
    		  //恢复输入框可用状态
        	  $("#autoOperate").attr("disabled",false);
        	  $("#tab2 .form-control").attr("disabled",false);
        	  $("#tab2 .contr").attr("disabled",false);
        	  $(".add-on").show();
    	  });  
    	  
    	  $(".form-group").bind("change",function(){
    		  //给隐藏域赋值
    		  initHidden();
    	  });
    	  
    	  //第二个导航页选择工厂时的点击事件
	      $("#chooseFactory2").change(function(event){
	          $("#chooseProductLine2").empty();
	          $("#chooseProductLine2").trigger("chosen:updated");
	        if($("#chooseFactory2").val()!=""){
	            ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseFactory2").val(),paintProductLine2);
	        }
	      });
	      function paintProductLine2(data){
	    	$("#chooseProductLine2").append("<option value=''>请选择产线</option>");
	        $.each(data,function(idx,item){
	          $("#chooseProductLine2").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
	        });
	          $("#chooseProductLine2").trigger("chosen:updated");
	      };
	      //第二个导航页选择产线时的点击事件
	      $("#chooseProductLine2").change(function(event){
              $("#chooseDriver2").empty();
              $("#chooseDriver2").trigger("chosen:updated");
            if($("#chooseProductLine2").val()!=""){
                ajaxTodo("${contextPath}/productline/DriverData2/"+$("#chooseProductLine2").val(),paintDrivers2);
            }
          });
          function paintDrivers2(data){
        	$("#chooseDriver2").append("<option value=''>请选择设备</option>");
            $.each(data,function(idx,item){
              $("#chooseDriver2").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
            });
              $("#chooseDriver2").trigger("chosen:updated");
          	};
          	
      	  </script>
          
      </div>
      
      <!-- 第三个导航页  OEE历史记录-->
      <div id="tab3" class="tab_content" style="min-height:620px; display: none;padding-bottom:15px ">
      	  <div>
      	  <div class="driver_info">
          <form class="form-inline" action="#" onsubmit="return false;">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择工厂</label>
                 <select id="chooseFactory3" class="form-control searchtext" >
                  <option value=''>请选择工厂</option>
                  <c:forEach var="p" items="${companyinfos}">
                  <option value="${p.id }">${p.companyname}</option>
                  </c:forEach>
                  </select>
              </div>
			  <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择产线</label>
                 <select id="chooseProductLine3" class="form-control searchtext" >
                 	<option value=''>请选择产线</option>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择设备</label>
                 <select id="chooseDriver3" class="form-control searchtext" >
                 	<option value=''>请选择设备</option>
                  </select> 
              </div>
              
              <div style=" height:6px; width:100% ;"></div>
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle">选择班次</label>
                 <select id="chooseClasses3" class="form-control" >
					<option value="">全部</option>
                  	<option value="A">早班</option>
                    <option value="P">中班</option>
                    <option value="Q">晚班</option>
                  </select> 
              </div>
               <div class="form-group">
              <label for="inputText" class="searchtitle" >选择类型</label>
              	<select id="type" class="form-control searchtext" >
              		  <option value="All">全部</option>
                 	  <option value="手动">手动</option>
					  <option value="自动">自动</option>
                 </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >选择时间</label>
                 <select class="form-control searchtext" id="search_time" >
                 	<option value="1">本日</option>
					  <option value="2">本周</option>
					  <option value="3">本月</option>
					  <option value="define_time">自定义时间段</option>
                 </select> 
              </div>
              
              <div class="form-group" id="rangetime">           
		    	    <div style="margin-left:auto;margin-right:auto">
		    	    <div style=" height:6px; width:100% ;"></div>
		    	    <div class="form-group" >
		                   <label for="inputText" class="searchtitle" style="margin-top:5px">开始时间</label>
		                     <div class="controls input-append date form_datetime3" style="position:relative;float:right;margin-left:3px" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="begin">
		                        <input id="historytime1" class="form-control datetime" type="text" style="background: #fff;width: 210px;height: 30px;" value="" readonly>
		                        <input type="hidden" id="autotime1_hid" value="">
		                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                     </div>
		                     </div>
		                  <div class="form-group">
		                  <label for="inputText" class="searchtitle" style="margin-top:8px" >结束时间</label>
		                  <div class="controls input-append date form_datetime3" style="position:relative;float:right"  data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">
		                    <input id="historytime2" class="form-control datetime" type="text" style="background: #fff;width: 210px;height: 30px;" value="" readonly>
		                    <input type="hidden" id="autotime2_hid" value="">
		                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-remove"></i>
		                          </span>
		                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
		                          <i class="fa fa-th"></i></span>
		                  </div>
		    	    </div>
            	</div>
              </div>
             
              </form>
			<div class="oeeBtn" style="">
				<a id="deleteA" style="visibility:hidden;padding:0" class="btn btn-tool" target="selectedTodo" data-target="table" href="${contextPath }/stats/deleteDrvierOEE"></a>
				<button class="btn btn-warning" id="delete" style="float: right;margin-left:10px">删除记录</button>
	          	<button class="btn btn-info" id="historyExport" style="float: right;margin-left:10px">导出数据</button>
	          	<button class="btn btn-info" id="search" style="float:right;">历史记录查询</button>
	          	<iframe id="ifile" style="display:none"></iframe>
         	</div><br><br>
          </div>
          
          <hr>
           
          	<table class="table table-striped" id="table" data-field="DriverOEEs" data-url="${contextPath }/stats/listAllOEE">
            <thead>
              <tr>
                <th data-field="Number" width="22" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox">
                </th>
                <th data-field="companyinfo.companyname" width="100">工厂</th>
                <th data-field="mesProductline.linename" width="100">产线</th>
                <th data-field="mesDriver.name" width="100">设备</th>
                <th data-field="createDate" width="100">运算记录日期</th>
                <th data-field="classes" width="100">班次</th>
                <th data-field="starttime" width="100">班次开始时间</th>
                <th data-field="endtime" width="100">班次结束时间</th>
                <th data-field="timeRate" width="100">时间开动率</th>
                <th data-field="propertyRate" width="100">性能开动率</th>
                <th data-field="passRate" width="100">合格品率</th>
                <th data-field="oeeRate" width="100">设备OEE</th>
                <th data-field="type" width="100">记录类型</th>
              </tr>
            </thead>
          </table>
          <!-- <button class="btn btn-info" id="showWeek">显示柱状图</button> -->
          </div>
          
          <br>
    	  <!-- 柱状图，折线图结合 -->
    	  <div id="history_control" style="width:100%;overflow:auto">
    	  <div id="history_day" style="min-height: 360px;min-width: 1000px;"></div>
    	  </div>
          </div>
          
          <!-- 第三个标签页逻辑处理代码 -->
		  <script type="text/javascript">
			  //获取所选记录的id
			  function getIds(){
				  var ids = "";
				  $("#table").find("tr.selected").each(function(i){
					  var val = $(this).attr("data-uniqueid");
					  ids += i==0 ? val : ","+val;
				  });
				  return ids;
			  }
			  //删除按钮  的点击事件
			  $("#delete").bind("click",function(){
				  if(getIds!=""){
					  $("#deleteA").attr("title","确认要删除吗？");
					  $("#deleteA").trigger("click");
				  }
			  });
			  
			  //显示柱状图按钮的点击事件
			 /*  $("#showWeek").bind("click",function(){
				  if($("#showWeek").html()=='显示柱状图'){
					  $("#history_day").show();
					  $("#showWeek").html("隐藏柱状图");
					  $("#showWeek").prop("class","btn btn-success");
				  }else if($("#showWeek").html()=='隐藏柱状图'){
					  $("#history_day").hide();
					  $("#showWeek").html("显示柱状图");
					  $("#showWeek").prop("class","btn btn-info");
				  }
			  }); */
			  
			  var startDate;
			  var endDate;
			  var dateType;
			  //今天时间：
			  function getDate(){
	        	  var year=new Date().getFullYear();
	        	  var month=new Date().getMonth()+1;
	        	  var day=new Date().getDate();
		      }
	        	//判断开始时间和结束时间
	        	function judgeDate(){
				  var myDate = new Date();
				  //获取当前年
				  var year=myDate.getFullYear();
				  //获取当前月
				  var month=myDate.getMonth()+1;
				  //获取当前日
				  var date=myDate.getDate(); 
				  var h=myDate.getHours();       //获取当前小时数(0-23)
				  var m=myDate.getMinutes();     //获取当前分钟数(0-59)
				  var s=myDate.getSeconds();  
				  var now=year+'-'+p(month)+"-"+p(date)+" "+p(h)+':'+p(m)+":"+p(s);
				
				  switch ($("#search_time").val()){
				  case "1":
				    startDate = year+'-'+p(month)+"-"+p(date)+" "+"00:00:00";
				    endDate = year+'-'+p(month)+"-"+p(date)+" "+"23:59:59";
				    dateType="day";
				    break;
				  case "2":
				    var M = new Date(dateRangeUtil.getCurrentWeek()[0]);
				    var S = new Date(dateRangeUtil.getCurrentWeek()[1]);
				    startDate = M.getFullYear() + '-' + (M.getMonth() + 1) + '-' + M.getDate() + ' 00:00:00';
				    endDate = S.getFullYear() + '-' + (S.getMonth() + 1) + '-' + S.getDate() + ' 23:59:59';
				    dateType="week";
				    break;
				  case "3":
				    startDate = year+'-'+p(month)+"-"+01+" "+"00:00:00";
				    if(p(month)=="1"||p(month)=="3"||p(month)=="5"||p(month)=="7"||p(month)=="8"||p(month)=="10"||p(month)=="12"){
				        endDate = year+'-'+p(month)+"-"+"31"+" "+"23:59:59";
				    }else if(p(month)=="4"||p(month)=="6"||p(month)=="9"||p(month)=="11"){
				        endDate = year+'-'+p(month)+"-"+"30"+" "+"23:59:59";
				    }else{
				        if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
				            endDate = year+'-'+p(month)+"-"+"28"+" "+"23:59:59";
				        }else{
				            endDate = year+'-'+p(month)+"-"+"29"+" "+"23:59:59";
				            }
				    }
				    dateType="month";
				    break;
				  case "4":
					    startDate = year+'-'+"01"+"-"+p(date)+" "+"00:00:00";
					    endDate = year+'-'+"12"+"-"+p(date)+" "+"23:59:59";
					    dateType="year";
					    break;
				  case "define_time":
					startDate = $("#historytime1").val();
		        	endDate = $("#historytime2").val();
				    dateType="defineDate";
				    break;
				  }
				};
				function p(s) {
				  return s < 10 ? '0' + s: s;
				};
			  
			  $("#rangetime").hide();  
			  $("#search_time").change(function(e){	 
				 if($("#search_time").val()=="define_time"){
					$("#rangetime").show();
				 }else{
					$("#rangetime").hide();
				 }
		      });
	          //历史记录查询按钮  的点击事件
			  $("#search").bind("click",function (){
	    		  var chooseDriver3 = $("#chooseDriver3").val();
	    		  var chooseClasses3 = $("#chooseClasses3").val();
	    		  var type = $("#type").val(); //记录类型
				  if(chooseDriver3==""||chooseDriver3==null){
					  swal("请选择具体设备哦");
	    			  return;
	    		  }
				  judgeDate();
				  var mil = (1000 * 60 * 60 * 24)+10000; //一天的毫秒数
    		  	  var tTemp = Date.parse(endDate)-Date.parse(startDate);//所选时间段毫秒数
    		  	  if(tTemp<=0){ //结束时间不能小于开始时间
    		  		  return ;
    		  	  }
				  //刷新表格
				  $.table.refreshCurrent("${contextPath }/stats/searchOeeHistory?startDate="+startDate+"&endDate="+endDate
						  +"&driverId="+$("#chooseDriver3").val()+"&chooseClasses3="+$("#chooseClasses3").val()+"&type="+type);
    		  	  
    		  	  var sDate = startDate.split(" ")[0];
    		  	  var eDate = endDate.split(" ")[0];
    		  	  //tTemp = Date.parse(endDate)-Date.parse(startDate);//所选时间段毫秒数
    		  	  if((tTemp<mil)&&(sDate==eDate)){ //如果时间范围是天
    		  		  //请求柱状图数据
    		      	  ajaxTodo("${contextPath }/stats/oEEBarDay?"+"startDate="+startDate+"&type="+type
    		      			  +"&endDate="+endDate+"&driverId="+$("#chooseDriver3").val()
    		      			  +"&chooseClasses3="+$("#chooseClasses3").val(),getDataAndShowHistoryDayBar);
    		  	  }else if(tTemp<(mil*32)){ //如果时间范围是周或者月
		   		  		ajaxTodo("${contextPath }/stats/oEEBarWeekOrMonth?"+"startDate="+startDate
		   		  				+"&dateType="+dateType+"&endDate="+endDate+"&type="+type
		   		  				+"&driverId="+$("#chooseDriver3").val(),getDataAndShowHistoryWeekBar);
    		  	  }else{
    		  		  return;
    		  	  }
			  });
		  	  
			  $("#historyExport").bind("click",function(){
				  if($.table.getCurrentPageData().length == 0){
					  swal("请先查询数据");
					  return;
				  }
				  var chooseDriver3 = $("#chooseDriver3").val();
				  if(chooseDriver3==""){
					  swal("错误", "请选择设备。", "error");
	    			  return;
	    		  }
				  judgeDate();
				  swal({
			          title: "确定要导出数据吗？",
			          type: "warning",
			          showCancelButton: true,
			          confirmButtonColor: '#DD6B55',
			          confirmButtonText: '确认',
			          cancelButtonText: "取消",
			          closeOnConfirm: true
			        },
			        function(isConfirm){
			          if (isConfirm){
			        	  $.blockUI({
			    				baseZ:99999,
			    				message:"<h1>请稍等......</h1>",
			    				onBlock:function(){
			    					$.ajax({
			    			     		url:"${contextPath}/stats/exportHistory",
			    			     		dataType:"JSON",
			    			     		type:"POST",
			    			     		data:"startDate="+startDate+"&endDate="+endDate+"&type="+$("#type").val()
			    			     		+"&driverId="+$("#chooseDriver3").val()+"&chooseClasses3="+$("#chooseClasses3").val(),
			    			     		success:function(data){
			    			     			if(data.success){
			    				     			var url="${contextPath }/statistics/downloadExport/"+data.success;
			    				     		    document.getElementById("ifile").src=url;
			    			     			}
			    			     			$.unblockUI();
			    			     		}
			    			     	}); 
			    				}
		    			  }); 
			          }
			        });
				  
			  });
	          
    	  //第三个导航页选择工厂时的点击事件
	      $("#chooseFactory3").change(function(event){
	          $("#chooseProductLine3").empty();
	          $("#chooseProductLine3").trigger("chosen:updated");
	        if($("#chooseFactory3").val()!=""){
	            ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#chooseFactory3").val(),paintProductLine3);
	        }
	      });
	      function paintProductLine3(data){
	    	$("#chooseProductLine3").append("<option value=''>请选择产线</option>");
	        $.each(data,function(idx,item){
	          $("#chooseProductLine3").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
	        });
	          $("#chooseProductLine3").trigger("chosen:updated");
	      };
	      //第三个导航页选择产线时的点击事件
	      $("#chooseProductLine3").change(function(event){
              $("#chooseDriver3").empty();
              $("#chooseDriver3").trigger("chosen:updated");
            if($("#chooseProductLine3").val()!=""){
                ajaxTodo("${contextPath}/productline/DriverData2/"+$("#chooseProductLine3").val(),paintDrivers3);
            }
          });
          function paintDrivers3(data){
        	$("#chooseDriver3").append("<option value=''>请选择设备</option>");
            $.each(data,function(idx,item){
              $("#chooseDriver3").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
            });
              $("#chooseDriver3").trigger("chosen:updated");
          }
     	 </script>
          
      </div>
      </div>
    </div>
  </div>
</div>
<c:set var="ParentTitle" value="Driver" />
<c:set var="ModuleTitle" value="countOee" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>