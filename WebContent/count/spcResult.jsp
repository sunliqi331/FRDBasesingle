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
  $(".fixed-table-pagination").hide();
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
              <i class="fa fa-line-chart"></i> SPC分析条件
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="inputText" class="searchtitle">公司\工厂</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div>
              
              <div class="form-group">
                 <label for="inputText" class="searchtitle" >参数</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div> 
             
              <div class="form-group">
                 <label for="inputText" class="searchtitle">零件名称</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div> 
               <div class="form-group">
                 <label for="inputText" class="searchtitle">零件编号</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>
              </div>
             
                        
			  <div class="form-group">
                 <label for="inputText" class="searchtitle">设备号</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量检具名</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">量检具号</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
<!--               <div class="result_title text-center">统计方式</div> -->
              <div class="form-group">
                 <label for="inputText" class="searchtitle">工序名称</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>   
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">工序编号</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>   
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">子组大小</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">子组频率</label>
                 <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="inputText" class="searchtitle">子组数量</label>
                <input type="text" class="searchtext" id="inputText" name="search_LIKE_sn"/>  
              </div>
              <div class="form-group" >
                <label for="inputText" class="searchtitle" style="padding-right: 0">分析时间 </label>           
                <div class="searchtext" >2016-09-10 13:45:22</div>                 
			 </div>
			 <div class="form-group" >
                <label for="inputText" class="searchtitle">数据时间</label>           
                <div class="searchtext" >2016-09-07 13:45:22<br/>2016-09-07 14:45:22 </div>               
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
        <div class="main-body spc_result">          
		  
		
	   <table class="table table-striped table-hover text-center" id="table" data-field="mesDrivers"
>
    <thead>
        <tr>            
            <th style="" colspan="4" data-field="sn" tabindex="0">
                <div class="th-inner text-center" style="line-height:inherit;" >
                    图纸值
                </div>
                <div class="fht-cell">
                </div>
            </th>
             <th style="" colspan="5" data-field="sn" tabindex="0">
                <div class="th-inner text-center" style="line-height:inherit;">
                    测量值
                </div>
                <div class="fht-cell">
                </div>
            </th>
                       
        </tr>
        
    </thead>
    <tbody>
     
       
      <tr class="spc_title_sm">
         <td>
           
                    名义值
                
         </td>
         <td>
           
                    上公差
               
         </td>
          <td>
           
                    下公差
               
         </td>
         <td>
           
                    公差带宽度
               
         </td>
         
         <td>
           
                    原X平均值
               
         </td>
         <td>
           
                    原α标准差
            
         </th>
         <td>
        
                   最大值
               
         </td>
         <td colspan="9">
           
                   最小值
          
         </td>
        
        </tr>
        <tr class="spc_title_data" >
           
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
            
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="" colspan="2">
               46.0125
            </td>
           
            
        
        </tr>
        <tr class="spc_title_lg"><td colspan="9">统计值</td></tr>
        <tr class="spc_title_sm">
         
         <td>UCLx</td>
         <td>LCLx</td>
                 
         <td>UCLr</td>
         <td>LCLr</td>
         <td>新X平均值</td>
         <td>新α平均值</td>
         <td>极差平均值</td> 
         <td>子组频率</td>
         <td>子组大小数量</td>
         </tr>
         <tr class="spc_title_data" >
           
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
            
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="" >
               46.0125
            </td>
            <td style="" >
               46.0125
            </td>
           
            
        
        </tr>
         
        <tr class="spc_title_lg"><td colspan="9">过程能力</td></tr>
         <tr class="spc_title_sm">
         <td>
           
                    IP
                
         </td>
         <td>
           
                    PpK
               
         </td>
          <td>
           
                 Cp
               
         </td>
         <td>
           
                    CpK
               
         </td>
         
         <td>
           
                    标准度Ca
               
         </td>
         <td>
           
                    上限能力CpU
            
         </th>
         <td>
        
                   下限能力CpL
               
         </td>
         <td colspan="2">
           
                   CpK评级
          
         </td>
        
        </tr>
        
        <tr class="spc_title_data">
           
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
            
            <td style="">
                46.0125
            </td>
            <td style="">
                46.0125
            </td>
            <td style="" colspan="2">
               46.0125
            </td>
           
            
        
        </tr>
       
      
        
    </tbody>
  
</table>
  <div class="col-sm-12"  style="padding: 0 ;">
    <div class="searchBar" style="margin-top:15px;border-color:#00c0ef;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#00c0ef">
        X图
      </div>
      <div id="main1" style="width:1100px;height:200px;margin-top: 20px; margin-left: 10px; margin-bottom: 20px"></div>
		     
		  
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
  <div class="col-sm-4" style="padding: 0">
    <div class="searchBar" style="margin-top:15px;border-color:#00a65a;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#00a65a">
         XBar图
      </div>
      <div id="main2" style="width:350px;height:200px;margin-top: 20px; margin-left: 5px; margin-bottom: 20px"></div>
		     
		  
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
  <div class="col-sm-4" >
    <div class="searchBar" style="margin-top:15px;border-color:#f39c12;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#f39c12">
         R图
      </div>
      <div id="main3" style="width:330px;height:200px;margin-top: 20px; margin-left: 5px; margin-bottom: 20px"></div>
		     
		  
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
  <div class="col-sm-4"  style="padding: 0 ;">
    <div class="searchBar" style="margin-top:15px;border-color:#dd4b39;margin-bottom: 0px" >
      <div class="search_header text-center" style="background-color:#dd4b39">
         工序能力分析
      </div>
      <div id="main4" style="width:350px;height:200px;margin-top: 20px; margin-left: 10px; margin-bottom: 20px"></div>
		     
		  
		   <script type="text/javascript">
		   
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main4'));

        // 指定图表的配置项和数据
       //app.title = '世界人口总量 - 条形图';

var option = {
//     tooltip: {
//         trigger: 'axis'
//     },
    toolbox: {
        feature: {
            dataView: {show: false, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: false},
            saveAsImage: {show: false}
        }
    },
    legend: {
        data:['蒸发量','降水量','平均温度']
    },
    xAxis: [
        {
            type: 'category',
            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis: [
        {
            type: 'value',
            name: '水量',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
                formatter: '{value} ml'
            }
        },
        {
            type: 'value',
            name: '温度',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
                formatter: '{value} °C'
            }
        }
    ],
    series: [
        {
            name:'蒸发量',
            type:'bar',
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
            itemStyle: {
                normal: {
                    color: '#5a99d3'
                }
            }
        },
        {
            name:'降水量',
            type:'bar',
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
            itemStyle: {
                normal: {
                    color: '#e97c31'
                }
            }
        },
        {
            name:'平均温度',
            type:'line',
            yAxisIndex: 1,
            data:[2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
        }
    ]
};



        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>      
            
   </div>
  </div>

  <div class="clearfix"></div>
  
      </div>
   </div>
   </div>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
 </div>

  <script type="text/javascript">
$(document).ready(function($) {
	//alert("1");

  $(".fixed-table-pagination").hide();
  });
  
</script>

</body>
</html>
