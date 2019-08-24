<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>countChart</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>

</head>

<body>
  <div id="container">
  	  <%@ include file="/WEB-INF/views/include.header.jsp"%>
      <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
       <div class="main-content">
      <ol class="breadcrumb" >
        <li><i class="fa fa-home" ></i><a
          href="${contextPath}/management/index" > 首页</a></li>
        <li>统计</li>
      </ol>
      <div class="main-wrap fold-wrap">
      <div class="tree-list result_left" layouth="5" id="jbsxBox2personnelTree" tabindex="2" style="overflow: hidden; outline: none;">
        <div class="result">
            <div class="search_header" style="background: none;color: #6f8cb5;">
              <i class="fa fa-line-chart"></i> CG分析条件
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">程序名称</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量具名称</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div> 
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量具号</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">零件名称</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div> 
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >零件ID</label>
                 <select name="search_EQ_logLevel" class="searchtext">
                  <option value="">当天</option>
				  <option value="">一星期</option>
				  <option value="">当月</option>
				  <option value="">自定义时间段</option>
                  </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">特性</label>
                 <select name="search_EQ_logLevel" class="searchtext">
                  <option value="">当天</option>
				  <option value="">一星期</option>
				  <option value="">当月</option>
				  <option value="">自定义时间段</option>
                  </select> 
              </div> 
                        
			  <div class="form-group">
                 <label for="inputText" class="searchtitle">名义值</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">下公差</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">上公差</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">实测值</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group" >
                 <label for="inputText" class="searchtitle">测量次数</label>
                 <div class="pass" style="width:50px">
				
					<label><input type="radio" name="status" class="input_pass validate[required]"
						id="adopt" value="1" /> 25</label>
				
			</div>
			<div class="nopass" style="width:50px">
			
					<label><input type="radio" name="status" class="input_nopass validate[required]"
						id="notthrough" value="4" checked  /> 50</label>
			
			</div> 
			</div>
			<div class="form-group" >
			<label for="inputText" class="searchtitle" style="float:left; margin-top:7px;">统计标准</label>
                 <div class="pass" style="width:50px">
				
					<label><input type="radio" name="status1" class="input_pass validate[required]"
						id="adopt" value="1" /> 4G</label>
				
			</div>
			<div class="nopass" style="width:50px">
			
					<label><input type="radio" name="status1" class="input_nopass validate[required]"
						id="notthrough" value="4" checked  /> 6G</label>
			
			</div> 
              </div>
            
			  
<!--               <button type="submit" class="btn btn-info  btn-search1">打印</button>  -->
<!--               <button type="submit" class="btn btn-info  btn-search1">导出</button> -->
          </form> 
        </div>
      
      </div>
      <div class="collapse-trigger">
          <div class="collapse-trigger-inner">
            <span class="fa fa-outdent"></span>
          </div>
        </div>
        <div class="main-body">          
		  
		
	   <table class="table table-striped table-hover text-center" id="table" data-field="mesDrivers"
data-url="${contextPath}/driver/driverData">
    <thead>
        <tr>
            
            <th style="" colspan="5" data-field="sn" tabindex="0">
                <div class="th-inner text-center" >
                    检测数据
                </div>
                <div class="fht-cell">
                </div>
            </th>
           
           
          
            
            
        </tr>
    </thead>
    <tbody>
        <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
        <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
         <tr data-index="0" data-uniqueid="73">
          
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
               46.0125
            </td>
            <td style="">
              46.0125
            </td>
        
        </tr>
    </tbody>
</table>

    <div class="searchBar" style="margin-top:15px">
      <div class="search_header text-center">
         检测件操作者平均值图 Part Appraiser Average Chart
      </div>
      <div id="main2" style="width:800px;height:400px;margin-top: 20px; margin-left: 50px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main2'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
    title: {
        text: '设备时间效率'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {
        data:['Step Start', 'Step Middle', 'Step End']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
            name:'Step Start',
            type:'line',           
            step: 'start',
            data:[120, 132, 101, 134, 90, 230, 210],
            itemStyle: {
                normal: {
                    color: '#5a99d3'
                }
            }
        
        },
        {
            name:'Step Middle',
            type:'line',
            step: 'middle',
            data:[220, 282, 201, 234, 290, 430, 410],
            itemStyle: {
                normal: {
                    color: '#e97c31'
                }
            }
        },
        {
            name:'Step End',
            type:'line',
            step: 'end',
            data:[450, 432, 401, 454, 590, 530, 510],
            itemStyle: {
                normal: {
                    color: '#9e9e9e'
                }
            }
        }
    ]
};


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>      
            
   </div>

      </div>
   </div>
   </div>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
 </div>
<script type="text/javascript">
  $(document).ready(function($) {
// 			var CHART_MAP = {
// 				"bar": ["Capacity","ElectricConsumption","WaterConsumption","ElectricWaterConsumption","PassFailureRate"],
// 				"line": ["ElectricConsumption","Capacity","WaterConsumption"],
// 				"pie": ["WaterConsumption","ElectricConsumption","Capacity"],
// 				"scatter": ["PassFailureRate", "Capacity"]
// 			};
// 			var formEle = ['catagory','chartsType','begin','end','scope','map'];
// 	  var url = '${context}/statistics/productRecord';
// 	  var $formData = new Array();
// 	  var $data = {};
// 	  $.each(CHART_MAP,function(k,v){
// 		  var category = v[0];
// 		  $data.name = 'category';
// 		  $data.value = category;
// 	  });
// 	  var chartObj = echarts.init($("#energy"));
// 	  getChartsData(url, data, function(option){
// 		option && chartObj.setOption(option);
// 	  });
// 	  function getChartsData(url, data, callback) {
// 			$.ajax({
// 				type: 'POST',
// 				url: url,
// 				data: data,
// 				dataType:"json",
// 				success: function(res){
// 					callback(res);
// 				},
// 				complete: function(res){
// 					console.log(res);
// 				}
// 			});
// 		}

 

     $(".rangetime").hide();  
     $("#search_time").change(function(e){	 
	 if($("#search_time").val()=="define_time"){
		$(".rangetime").show();   
		 } 
	
	  });

    $('.form_datetime1').datetimepicker({
    language:  'zh-CN',
	   format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
     showMeridian: 1,
		//minView: 2
		
 });
 
   $('.form_datetime2').datetimepicker({
    language:  'zh-CN',
	   format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
     showMeridian: 1,
		//minView: 2
		
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
  });
  
  
</script>
  

</body>
</html>
