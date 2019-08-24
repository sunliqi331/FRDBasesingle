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
<script type="text/javascript">
$(document).ready(function($) {
	 // alert("1");
  $("#sidebar").width(0).css("z-index","-555");
  $(".main-content").css("left","0");

  });
  
</script>
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
              <i class="fa fa-line-chart"></i> GRR分析条件
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量具名</label>
                 <input type="text" class="searchtext" id="inputText" readonly="readonly" name="search_LIKE_sn"/>
              </div> 
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量具号</label>
                 <input type="text" class="searchtext" id="inputText" readonly="readonly" name="search_LIKE_sn"/>
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">零件名</label>
                 <input type="text" class="searchtext" id="inputText" readonly="readonly" name="search_LIKE_sn"/>
              </div> 
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >参数</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
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
<!--               <div class="result_title text-center">统计方式</div> -->
              <div class="form-group">
                 <label for="inputText" class="searchtitle">人数</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>  
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">次数</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>  
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">工件数</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group" >
                <label for="inputText" class="searchtitle">送检件号</label>           
                 <textarea name="description" class="searchtext textarea-scroll" cols="29" rows="3"  maxlength="256"></textarea>                 
			 </div>
			 <div class="form-group" >
                <label for="inputText" class="searchtitle">选择用户</label>           
                 <textarea name="description" class="searchtext textarea-scroll" cols="29" rows="3"  maxlength="256"></textarea>                 
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
            <th style="" colspan="6" data-field="sn" tabindex="0">
                <div class="th-inner text-center" >
                    检验员一
                </div>
                <div class="fht-cell">
                </div>
            </th>
             <th style="" colspan="6" data-field="sn" tabindex="0">
                <div class="th-inner text-center" >
                    检验员二
                </div>
                <div class="fht-cell">
                </div>
            </th>
             <th style="" colspan="6" data-field="sn" tabindex="0">
                <div class="th-inner text-center" >
                    检验员三
                </div>
                <div class="fht-cell">
                </div>
            </th>          
        </tr>
    </thead>
    <tbody>
        <tr data-index="0" data-uniqueid="73">
            <td class="grr_td_bg">1</td>
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
            <td class="grr_td_bg">1</td>
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
            <td class="grr_td_bg">1</td>
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
          
            <td class="grr_td_bg">2</td>
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
            <td class="grr_td_bg">2</td>
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
            <td class="grr_td_bg">2</td>
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
          
            <td class="grr_td_bg">3</td>
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
           <td class="grr_td_bg">3</td>
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
            <td class="grr_td_bg">3</td>
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
          
            <td class="grr_td_bg">4</td>
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
            <td class="grr_td_bg">4</td>
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
            <td class="grr_td_bg">4</td>
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
          
           <td class="grr_td_bg">5</td>
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
            <td class="grr_td_bg">5</td>
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
            <td class="grr_td_bg">5</td>
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
          <td class="grr_td_bg">6</td>
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
            <td class="grr_td_bg">6</td>
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
            <td class="grr_td_bg">6</td>
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
          <td class="grr_td_bg">7</td>
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
            <td class="grr_td_bg">7</td>
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
            <td class="grr_td_bg">7</td>
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
          <td class="grr_td_bg">8</td>
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
            <td class="grr_td_bg">8</td>
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
            <td class="grr_td_bg">8</td>
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
          <td class="grr_td_bg">9</td>
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
            <td class="grr_td_bg">9</td>
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
            <td class="grr_td_bg">9</td>
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
          <td class="grr_td_bg">10</td>
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
            <td class="grr_td_bg">10</td>
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
            <td class="grr_td_bg">10</td>
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
          <td colspan="6" class="text-right">46.0125</td>
          <td colspan="6" class="text-right">46.0125</td>
          <td colspan="6" class="text-right">46.0125</td>
         </tr>
    </tbody>
</table>
  <div class="col-sm-6"  style="padding: 0 ;">
    <div class="searchBar" style="margin-top:15px;border-color:#00c0ef;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#00c0ef">
         检测件操作者平均值图 Part Appraiser Average Chart
      </div>
      <div id="main1" style="width:500px;height:200px;margin-top: 20px; margin-left: 10px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main1'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     title: {
//         text: '设备时间效率'
//     },
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
  <div class="col-sm-3" style="">
    <div class="searchBar" style="margin-top:15px;border-color:#00a65a;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#00a65a">
         操作者平均值比较图
      </div>
      <div id="main2" style="width:240px;height:200px;margin-top: 20px; margin-left: 5px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main2'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     title: {
//         text: '设备时间效率'
//     },
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
  <div class="col-sm-3" style="padding: 0">
    <div class="searchBar" style="margin-top:15px;border-color:#f39c12;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#f39c12">
         操作者极差比较图
      </div>
      <div id="main3" style="width:240px;height:200px;margin-top: 20px; margin-left: 5px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main3'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     title: {
//         text: '设备时间效率'
//     },
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
  <div class="col-sm-6"  style="padding: 0 ;">
    <div class="searchBar" style="margin-top:15px;border-color:#dd4b39;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#dd4b39">
         重复性极差控制图 RepeatAbility Range Control Chart
      </div>
      <div id="main4" style="width:500px;height:200px;margin-top: 20px; margin-left: 10px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main4'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     title: {
//         text: '设备时间效率'
//     },
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
  <div class="col-sm-6"  style="padding: 0 0 0 15px ;">
    <div class="searchBar" style="margin-top:15px;border-color:#3c8dbc; margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#3c8dbc">
         变异分量直列图
      </div>
      <div id="main5" style="width:500px;height:200px;margin-top: 20px; margin-left: 10px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main5'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     title: {
//         text: '设备时间效率'
//     },
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
  <div class="clearfix"></div>
   <table class="table grr_table" id="table" data-field="mesDrivers"
data-url="${contextPath}/driver/driverData" style="margin-top: 15px">
    <tbody>
      <tr>
	      <td>XBAR=46.0156</td>
	      <td>RBAR=0.0187</td>
	      <td>Xdiff=0.0043</td>
	      <td>UCLx=46.0347</td>
	      <td>LCLx=45.9965</td>
	      <td>UCLr=0.0481</td>
	      <td>Rp=0.0135</td>
      </tr>
      <tr>
	      <td>E.V.=0.0570</td>
	      <td>A.V.=0.0052</td>
	      <td>GR&R=0.0572</td>
	      <td>P.V.=0.0219</td>
	      <td >T.V.=0.0613</td>
	      <td  rowspan="2" colspan="2" class="text-center" style="vertical-align: middle; color: #3c8dbc" >GR&R/(T/6)%=2453.56</td>
	      
      </tr>
      <tr>
	      <td>E.V.%=93.0</td>
	      <td>A.V.%=8.5</td>
	      <td>GR&R%=93.4</td>
	      <td>P.V.%=35.8</td>
	      <td >Ndc=1</td>
	      
	    
      </tr>
      <tr>
      </tr>
    </tbody>
    </table>
      </div>
   </div>
   </div>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
 </div>

  

</body>
</html>
