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
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li><i class="fa fa-puzzle-piece"></i> 统计</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">          
		  <div class="searchBar search_driver" style="margin-bottom:15px">
            <div class="search_header">
              <i class="fa fa-area-chart"></i> 统计
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">时间选择</label>
                 <select name="search_time" id="search_time" class="form-control searchtext">
	                  <option value="">当天</option>
					  <option value="">一星期</option>
					  <option value="">当月</option>
					  <option value="define_time">自定义时间段</option>
                 </select> 
              </div>
			   <div class="form-group  rangetime "  >
                   <label for="inputText" class="searchtitle">开始时间</label>
                    <div class="date  form_datetime1" style="position:relative"  data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                      <input class="form-control datetime " type="text" style=" background: #e5eaf1; " value="" readonly>
                      <span class="add-on" style="position: absolute; right: 28px;bottom:0; padding:5px 7px;background: #dfe3e8;border-color:#c8ced6;"><i class="fa fa-remove"></i></span>
                      <span class="add-on" style="position: absolute; right: 0px;bottom:0;padding:5px 7px;background: #dfe3e8;border-color:#c8ced6;"><i class="fa fa-th"></i></span>
                    </div>
                    <input type="hidden" id="begin" value="" />
                                 
                                  
                </div>
                <div class="form-group  rangetime "  >
                  <label for="inputText" class="searchtitle">结束时间</label>
                  <div class="date  form_datetime2" style="position:relative"  data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                    <input class="form-control datetime " type="text" style=" background: #e5eaf1;" value="" readonly>
                       <span class="add-on" style="position: absolute; right: 28px;bottom:0;padding:5px 7px;background: #dfe3e8;border-color:#c8ced6;"><i class="fa fa-remove"></i></span>
                       <span class="add-on" style="position: absolute; right: 0px;bottom:0;padding: 5px 7px;background: #dfe3e8;border-color:#c8ced6;"><i class="fa fa-th"></i></span>
                  </div>
                  <input type="hidden" id="end" value="" />
                                 
               </div>
			  <div class="form-group">
                 <label for="inputText" class="searchtitle">统计范围</label>
                 <select name="search_EQ_logLevel" class="form-control searchtext">
                  <option value="">当天</option>
				  <option value="">一星期</option>
				  <option value="">当月</option>
				  <option value="">自定义时间段</option>
                  </select> 
              </div>
			  
<!--               <button type="submit" class="btn btn-info  btn-search1">搜索</button> -->
          </form> 
        </div>
		
	   <ul class="tabs clearfix"  >  
        <li class="active"><a href="#tab1">全部</a></li>  
        <li style="margin-left: -1px"><a href="#tab2">产品</a></li>
        <li style="margin-left: -1px"><a href="#tab3">设备</a></li>
        <li style="margin-left: -1px"><a href="#tab4">设施</a></li>  		
      </ul>
	  	  <div class="tab_container"> 
	     <div id="tab1" class="tab_content clearfix" style="display: block; padding-bottom:15px ">
		    <div id="main1" style="width: 45%;height:400px; margin-top: 20px;float:left; margin-right: 5%; margin-left: 2%"></div>
		    <div id="main1_1" style="width: 40%;height:400px;margin-top: 20px;float:left;"></div>
		   <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main1'));
        var myChart_1 = echarts.init(document.getElementById('main1_1'));
        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

option = {
    title : {
        text: '产品合格率',
        //subtext: '纯属虚构',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['合格','不合格']
    },
    series : [
        {
            name: '产品合格率',
            type: 'pie',
            radius : '55%',
            center: ['40%', '50%'],
            data:[
                {value:10, name:'不合格',
                  itemStyle: {
                        normal: {
                            color: '#e97c31'
                        }
                    }	
                },
                {value:90, name:'合格',
                	itemStyle: {
                        normal: {
                            color: '#5a99d3'
                        }
                    }	
                },
               
                
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};

var option_1 = {
	   color: ['#5a99d3'],
        title: {
            text: '耗电量'
        },
        tooltip: {},
        legend: {
            data:['耗电量']
        },
        xAxis: {
            data: ["公司1","公司2","公司3","公司4"]
        },
        yAxis: {},
        series: [{
            name: '耗电量',
            type: 'bar',
            data: [5, 20, 36, 10]
        }],
       
    };



        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        myChart_1.setOption(option_1);
    </script>
		 </div>
		 <div id="tab2" class="tab_content" style="display: none;padding-bottom:15px ">
		  <div class="driver_info">
		     <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">产品型号</label>
                 <select name="search_time" id="search_product" class="form-control searchtext" style="padding-right: 50px">
	                  <option value="">全部</option>
					  <option value="">缸盖</option>
					  <option value="">发动机</option>					  
                 </select> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">产品总数</label>
                 <input type="text" class="form-control searchtext" value="100" disabled style="width:80px"/>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">合格计数</label>
                 <input type="text" class="form-control searchtext" value="100" disabled style="width:80px"/>
              </div>
               <div class="form-group">
                 <label for="inputText" class="searchtitle">统计图形</label>
                 <select name="search_time" id="search_product" class="form-control searchtext">
	                  <option value="">饼图-合格率</option>
					  <option value="">柱状图-产能</option>
					  <option value="">柱状图-各公司产能</option>
					  <option value="">柱状图-各工厂产能</option>
					  <option value="">柱状图-各产线产能</option>	
					  <option value="">折线图-产品返工率</option>					  
                 </select> 
              </div>
<!--               <button type="submit" class="btn btn-search2">统计</button> -->
              </form>
		  </div>
		   <div id="main2" style="width: 600px;height:400px; margin-left: 20%;margin-top: 20px"></div>
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
		 <div id="tab3" class="tab_content" style="display: none;padding-bottom:15px ">
		 <div class="driver_info">
		     <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
             
               <div class="form-group">
                 <label for="inputText" class="searchtitle">统计图形</label>
                 <select name="search_time" id="search_product" class="form-control searchtext">
	              
					  <option value="">柱状图-能耗</option>
					  <option value="">柱状图-运行时间</option>
					  <option value="">柱状图-开动率</option>
					  <option value="">柱状图-各公司能耗</option>	
					  <option value="">柱状图-各工厂能耗</option>	
					  <option value="">折线图-设备时间效率</option>
					  <option value="">折线图-设备性能效率</option>
					  <option value="">折线图-设备OEE</option>					  
                 </select> 
              </div>
<!--               <button type="submit" class="btn btn-search2">统计</button> -->
              </form>
		  </div>
		   <div id="main3" style="width: 600px;height:400px;margin-left: 20%;margin-top: 20px""></div>
		   <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main3'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
    
    title: {
        text: '设备运行时间',
       // subtext: '数据来自网络'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    legend: {
        data: ['运行时间', '停机时间']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    yAxis: {
        type: 'value',
        boundaryGap: [0, 0.01]
    },
    xAxis: {
        type: 'category',
        data: ['设备1','设备2']
    },
    series: [
        {   
            name: '运行时间',
            type: 'bar',
            data: [100, 120],
            itemStyle: {
                normal: {
                    color: '#5a99d3'
                }
            }
        },
        {  
		  
            name: '停机时间',
            type: 'bar',
            data: [20, 50],
            itemStyle: {
                normal: {
                    color: '#e97c31'
                }
            }
        }
    ]
};

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
		 </div>
		 <div id="tab4" class="tab_content" style="display: none; padding-bottom:15px">
		   <div class="driver_info">
		     <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
             
               <div class="form-group">
                 <label for="inputText" class="searchtitle">统计图形</label>
                 <select name="search_time" id="search_product" class="form-control searchtext">
	              
					  <option value="">柱状图-耗电量</option>
					  <option value="">柱状图-耗水量</option>
					  <option value="">柱状图-耗气量</option>
					  					  
                 </select> 
              </div>
<!--               <button type="submit" class="btn btn-search2">统计</button> -->
              </form>
		  </div>
		   <div id="main4" style="width: 600px;height:400px;margin-left: 20% ;margin-top: 20px"></div>
		   <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main4'));

        // 指定图表的配置项和数据
        var option = {
           color: ['#5a99d3'],
            title: {
                text: '耗电量'
            },
            tooltip: {},
            legend: {
                data:['耗电量']
            },
            xAxis: {
                data: ["公司1","公司2","公司3","公司4"]
            },
            yAxis: {},
            series: [{
                name: '耗电量',
                type: 'bar',
                data: [5, 20, 36, 10]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
		 </div>
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
