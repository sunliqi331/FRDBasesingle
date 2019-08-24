<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
.main-body{
  background: #383838;
/*   background: -webkit-linear-gradient(right top, #00c0ef , #195e80); /* Safari 5.1 - 6.0 */ */
/*   background: -o-linear-gradient(bottom left, #00c0ef, #195e80); /* Opera 11.1 - 12.0 */ */
/*   background: -moz-linear-gradient(bottom left, #00c0ef, #195e80); /* Firefox 3.6 - 15 */ */
/*   background: linear-gradient(to bottom left, #00c0ef , #195e80); */
  
  }
  .result_table > thead > tr > th,.result_table > tbody > tr > td{
  padding:4px 2px;
  }
  .result_right .col-sm-6,.result_right .col-sm-12{
    padding:0 5px;
  }
  .result_bg{
    margin: 5px 0;
  }
  .result_right{
    min-height:500px;
  }
  .result_bg{
    visibility: hidden;
  }
  
  .table th, .table td {
    text-align: center;
    vertical-align: middle!important;
   }
</style>		
      <div class="result_left" layouth="5" id="jbsxBox2personnelTree" tabindex="2" style="overflow: hidden; outline: none;">
        <div class="result">
            <div class="search_header" style="background: none;color: #bbb;">
              <i class="fa fa-line-chart"></i> SPC分析条件
            </div>
          <input type="hidden" id="_standardValue" />
		<input type="hidden" id="_minValue" />
		<input type="hidden" id="_maxValue" />
		<input type="hidden" id="monitorFlg" value="0"/>
		<input type="hidden" id="dataPackage" value=""/>

		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                 <label for="procedureProperty" class="searchtitleWhite" >参数</label>
                 <input type="text" class="searchtext" id="procedureProperty" readonly="readonly"/>
              </div> 
             
             <div class="form-group">
                 <label for="product" class="searchtitleWhite">零件名称</label>
                 <input type="text" class="searchtext" id="product" readonly="readonly"/>
              </div> 
               <div class="form-group">
                 <label for="productSn" class="searchtitleWhite" >零件编号</label>
                 <input type="text" class="searchtext" id="productSn" name="productSn" readonly="readonly"/> 
              </div>
             
                        
			  <div class="form-group">
                 <label for="mesDriver" class="searchtitleWhite">量具名称</label>
                 <input type="text" class="searchtext" id="mesDriver" readonly="readonly"/>
              </div> 
<!--               <div class="result_title text-center">统计方式</div> -->
              <div class="form-group">
                 <label for="productProcedure" class="searchtitleWhite">工序名称</label>
                 <input type="text" class="searchtext" id="productProcedure" readonly="readonly"/>   
              </div>
              <div class="form-group">
                 <label for="productProcedureNum" class="searchtitleWhite">工序编号</label>
                 <input type="text" class="searchtext" id="productProcedureNum" readonly="readonly"/>   
              </div>
              <div class="form-group">
                 <label for="scale" class="searchtitleWhite">保留小数</label>
                 <select id="scale" class="searchtext">
                 	<option value="4">4</option>
                 	<option value="5">5</option>
                 	<option value="6">6</option>
                 	<option value="7">7</option>
                 	<option value="8">8</option>
                 </select>
              </div>
              <div style="position: relative;border: 1px solid #ffffff; margin-top: 8px; ">
				   <div style="position: absolute; left:60px; top:-8px; background: #383838; color: #bbb; height: 15px; ">统计采样方式</div>
              <div style="margin: 8px 2px;">
              <div class="form-group" >
                 <label for="_subRange" class="searchtitleWhite">子组大小</label>
                 <input type="text" class="searchtext" id="_subRange" readonly="readonly"/> 
              </div>
              <div class="form-group">
                 <label for="_subSeq" class="searchtitleWhite">子组频率</label>
                 <input type="text" class="searchtext" id="_subSeq" readonly="readonly"/> 
              </div>
              <div class="form-group">
                 <label for="_subNum" class="searchtitleWhite">子组数量</label>
                <input type="text" class="searchtext" id="_subNum" readonly="readonly"/>  
              </div>
              </div>
              </div>
              
              <!-- <div class="form-group" >
                <label for="inputText" class="searchtitle" style="padding-right: 0">分析时间 </label>           
                <div class="searchtext" ><span id="analyzeTime"></span></div>                 
			 </div>
			 <div class="form-group" >
                <label for="inputText" class="searchtitle">数据时间</label>           
                <div class="searchtext" ><span id="analyzeBegin"></span><br/><span id="analyzeEnd"></span></div>               
			 </div> -->
			 <div class="clearfix"></div>
           <div>&nbsp;</div>
			<div class="form-group" >
              <a id="analyzeBtn" style="margin:5px 5px 0 0" title="单次评定"><img src="${contextPath}/styles/img/pd.png" width="35"/></a>
              <a id="saveAsImage" style="margin:5px 5PX 0 0"  title="下载评定结果"><img src="${contextPath}/styles/img/xz.png" width="35"/></a>
              <a id="monitorBtn" style="margin:5px 5px 0 0"  title="SPC评定监控"><img src="${contextPath}/styles/img/star.png" width="35"/></a>
              <a id="monitorStopBtn" style="margin:5px 5PX 0 0;display:none"  title="监控停止" ><img src="${contextPath}/styles/img/end.png" width="35"/></a>
			</div>
			<div>
			<textarea id="description" name="description" class="textarea-scroll" style="background:#282828;
			border-color:#515151;color:#c2c2c2;
			margin: 15px 0px; width: 212px; height: 233px;" cols="29" rows="20" ></textarea>
			</div>
          </form> 
        </div>
      
      </div>
   
        <div class="result_right" style="width:1540px;">
            <div id="screenArea" style="width: 40px;height:40px;margin-top:20px;position: absolute;z-index: 9999;">
             <ul class="monitor_large">
               <li ><a href="#"  id="maxScreen" style="display: none"><img src="${contextPath}/styles/img/large.png" width="40"/></a></li>
               <li ><a href="#" id="minScreen" style="display: none"><img src="${contextPath}/styles/img/small.png" width="40"/></a></li>
             </ul>
             </div>
		
  <div class="col-sm-12" >
    <div class="result_bg">
      	<div id="main1" style="width:400px;height:250px;display:none;"></div>
    </div> 
  </div>
  <div class="col-sm-6" >
    <div class="result_bg">
      		<div id="main2" style="width:350px;height:250px;"></div>
    </div>
  </div>
  <div class="col-sm-6" >
   <div class="result_bg" style="height: 250px;">
      	<div id="main3" style="width:400px;height:250px;"></div>
  </div>
  </div>
  <div class="col-sm-6"  >
   <div class="result_bg">
      	<div id="main4" style="width:430px;height:353px;display:none  "></div>
   </div>       
   </div>
 
  <div class="col-sm-6" style=" overflow-x:auto;">
  <div class="result_bg">
   <table style="height:352px;" class="table table-striped table-hover text-center result_table" style="display:none;margin-top:10px;color:#bbb;" id="tableResult" data-field="mesDrivers">
     <thead>
        <tr>            
            <th class="text-center" colspan="2" data-field="sn" tabindex="0" style="border-bottom: none;background:url('images/header.png') repeat-x 0 0;border: 1px solid ;color:#bbb;">
                
                    图纸值
                
            </th>
             <th class="text-center"  colspan="2" data-field="sn" tabindex="0" style="border-bottom: none;background:url('images/header.png') repeat-x 0 0;border: 1px solid ;color:#bbb;">
              
                    测量值
              
            </th>
             <th class="text-center"  colspan="2" data-field="sn" tabindex="0" style="border-bottom: none;background:url('images/header.png') repeat-x 0 0;border: 1px solid ;color:#bbb;">
              
                  统计值
              
            </th>
                       
        </tr>
        
    </thead>
    <tbody>
      <tr>
        <td class="color_fixed">名义值</td>
        <td class="color_fixed" style=""><span id="standardValue"></span></td>
        <td class="color_measure" rowspan="2" style="vertical-align: middle;" >最大值</td>
        <td class="color_measure"  rowspan="2" style="vertical-align: middle;" ><span id="Max"></span></td>
        <td >X平均值</td>
        <td style=""><span id="X_average"></span></td>
      </tr>
      <tr>
        <td class="color_fixed">上公差</td>
        <td class="color_fixed" style=""><span id="maxValue"></span></td>       
        <td>α标准差</td>
        <td style=""><span id="sigma"></span></td>
      </tr>
      <tr>
        <td class="color_fixed">下公差</td>
        <td class="color_fixed" style=""><span id="minValue"></span></td>
        <td class="color_measure" rowspan="2" style="vertical-align: middle;">最小值</td>
        <td class="color_measure" rowspan="2" style="vertical-align: middle;" ><span id="Min"></span></td>
        <td class="color_count">UCLx</td>
        <td class="color_count" style=""><span id="UCLx"></span></td>       
      </tr>
      <tr>
        <td class="color_fixed">公差带宽度</td>
        <td class="color_fixed" style=""><span id="max_minValue"></span></td>
       
        <td class="color_count">LCLx</td>
        <td class="color_count" style=""><span id="LCLx"></span></td>
      </tr>
      <tr>
        <td colspan="4" style="background:url('images/header.png') repeat-x 0 0;font-weight: bold;vertical-align: middle;color:#bbb;">过程能力</td>
        <td style="color:#bbb;">极差平均值</td>
        <td style=""><span id="rBar"></span></td>
      </tr>
      <tr>
        <td class="color_bbb">Pp</td>
        <td style=""><span id="Pp"></span></td>
        <td class="color_bbb">标准度Ca</td>
        <td style=""><span id="Ca"></span></td>
        <td class="color_bbb">UCLr</td>
        <td style=""><span id="UCLr"></span></td>
      </tr>
      <tr>
        <td class="color_bbb">PpK</td>
        <td style=""><span id="Ppk"></span></td>
        <td class="color_bbb">上限能力CpU</td>
        <td style=""><span id="Cpu"></span></td>
        <td class="color_bbb">LCLr</td>
        <td style=""><span id="LCLr"></span></td>
      </tr>
      <tr>
        <td class="color_bbb">Cp</td>
        <td style=""><span id="Cp"></span></td>
        <td class="color_bbb">下限能力CpL</td>
        <td style=""><span id="Cpl"></span></td>
        <td class="color_bbb">子组频率</td>
        <td style=""><span id="subSeq"></span></td>
        
      </tr>
      <tr>
        <td class="color_bbb">CpK</td>
        <td style=""><span id="Cpk"></span></td>
        <td class="color_bbb">CpK评级</td>
        <td class="color_fixed"><span id="CpkLevel"></span></td>
        <td class="color_bbb">子组大小数量</td>
        <td style=""><span id="subNum"></span></td>
      </tr>
    </tbody>
  </table>
  </div>
  </div>

  <div class="clearfix"></div>
  
  </div>
  <div id="cgResultImage" style="display: none"></div>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
  <script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
  <script type="text/javascript" src="${contextPath }/js/page/screenChange.js"></script>
  <script type="text/javascript">
  var connectClientStatus = null,connectSubscriberStatus = null;
//监控内容
var subscriber = null;
//ActiveMQ的链接对象
var monitorClient = null;
//通过webscoket.js ，storm.js 通信到activeMq获取内容
var monitorSubscriber = null;
//建立监控链接
function connectClient(){
   console.info("${contextPath}/monitor");
   monitorClient = $.monitorSocket.connect({connectUrl:"${contextPath}/monitor",connectCallback:function(frame){
      if(null != connectClientStatus){
          // 清除定时器
          clearInterval(connectClientStatus);
          if(null == subscriber ){
              //
               subscriber = doSubscriber();
               // doingMonitor();
           }
       }
       monitorClient.ws.onclose = function(event){
           console.log("monitorClient的关闭时间 -- start");
           // unSubscribeMonitor();
           monitorClient.disconnect(function(){
               console.log("主动关闭monitor链接。");
           });
           console.log("丢失monitorClient链接。");
           connectClientStatus = setInterval(connectClient,2000);
           console.log("monitorClient的关闭时间 -- end");
       }

   }});
};
function connectSubscriber(){
   // 通信到ActiveMQ获取数据
   monitorSubscriber = $.monitorSocket.connectRegular({connectUrl:"${monitorStomp}",connectCallback:function(frame){
       if(null != connectSubscriberStatus){
           clearInterval(connectSubscriberStatus);
           if(null == subscriber ){
               // 重新开始描画监控画面
               subscriber = doSubscriber();
               // 开始监控
               //doingMonitor();
           }
       } else {
           // 重新开始描画监控画面
           subscriber = doSubscriber();
       }
       monitorSubscriber.ws.onclose = function(event){
           unSubscribeMonitor();
           monitorSubscriber.disconnect(function(){
               console.log("lalalala");
           });
           console.log("丢失monitorSubscriber链接");
           connectSubscriberStatus = setInterval(connectSubscriber,2000);
       }
   }});
};
//扫描spc最新数据
var doSubscriber = function(){
    console.info("doSubscriber start");
    return monitorSubscriber.subscribe("/topic/showMonitor/monitor/spc",function(data){
        if(null != data && null != data.body) {
        	console.log("接受到的数据包：" + data.body);
            var dataArray = data.body.split("_");
            // 555_272_438_58813116411_288_1520488453_sn1013-1
            console.log("接受到的工厂ID" + dataArray[0]);
            /*if("555" != dataArray[0]){
            	return;
            }*/
            /*
            console.log("产线ID" + dataArray[1]);
            if("272" != dataArray[1]){
                return;
            }*/

            console.log("接受到的设备ID" + dataArray[2]);
            console.log("接受到的产品ID" + dataArray[3]);
            console.log("接受到的工序ID" + dataArray[4]);

            console.log("已选择的设备ID" + $('#mesDriverId').val());
            console.log("已选择的工序ID" + $('#productProcedureId').val());
            // 已选择设备
            var mesDriverIdSelected = $('#mesDriverId').val();
            if(mesDriverIdSelected != dataArray[2]) {
                console.log("非已选择设备");
                console.log("已选择设备:" + productProcedureIdSeleted + "接受到的设备" +dataArray[2]);
                return;
            }

            // 以选择工序
            var productProcedureIdSeleted = $('#productProcedureId').val();
            if(productProcedureIdSeleted != dataArray[4]) {
                console.log("非已选择工序");
                console.log("已选择工序:" + productProcedureIdSeleted + "接受到的工序" +dataArray[4]);
                return;
            }

            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth()+1;
            var day = date.getDate();
            var hour = date.getHours();
            var minute = date.getMinutes();
            var second = date.getSeconds();
            console.log("监控各个画面元素组装----------------开始" + new Date());
            var time = year+'/'+month+'/'+day+'/ '+ hour + ':'+minute+':'+second;
            console.log("textarea-start");
            $("#description").val($("#description").val() + time + "\n");
            console.log("textarea-start1");
            $("#description").val($("#description").val() + "获取接到最新的SPC数据！" + "\n");
            console.log("textarea-start2");
            // var prop = JSON.parse(data.body);
            $("#monitorFlg").val("1");
            $("#dataPackage").val(data.body);
            $("#description").val($("#description").val() + "开始重新评定。" + "\n");
            $("#analyzeBtn").click();
            $("#description").val($("#description").val() + "评定结束。" + "\n\n");
            $("#monitorFlg").val("0");
            // console.log(prop);
        }

        console.log("监控各个画面元素组装----------------结束" + new Date());
   });
}
// 向Storm发送【SimpMessagingTemplate】 URL:/showMonitor/advise/protocal
var unSubscribeMonitor = function(){
    // 设备数据 《- 监控内容的状态设置为-1 不监控
    if(null != subscriber ){
        // 不监控
        subscriber.unsubscribe();
        // 监控对象设置为空
        subscriber = null;
    }
};
function spcSubcirbe() {
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    console.log("监控各个画面元素组装----------------开始" + new Date());
    var time = year+'/'+month+'/'+day+'/ '+ hour + ':'+minute+':'+second;
    console.log("textarea-start2");
    $("#description").val($("#description").val()
    		 + "SPC定时监控评定中！"+ "\n"
    		 +  "当前执行时间："+ "\n"
    		 + time + "\n");
    $("#description").val($("#description").val() + "开始重新评定。" + "\n");
    $("#monitorFlg").val("2");
    $("#analyzeBtn").click();
    $("#description").val($("#description").val() + "本次评定完成！" + "\n\n");
    $("#monitorFlg").val("0");
    $("#description").scrollTop($("#description").prop("scrollHeight"));
}

$(document).ready(function($) {
    $("#description").hide();
    // 双环传动公司的spc数据取消时间限制。
    <%--if("${companyId}" != "511") {--%>
      <%--$("#monitorBtn").hide();--%>
      <%--$("#monitorStopBtn").hide();--%>
    <%--}--%>
	// 监控执行
    $("#monitorBtn").click(function(){
    	console.log("开始运行监控。");
        fullScreen();
        $("#monitorStopBtn").show();
        $("#maxScreen").hide();
        $("#description").show();
        $("#description").empty();
        $("#description").val($("#description").val() + "开始进行定时监控！" + "\n");
        setInterval(spcSubcirbe, 15000);

        // 创建连接，尝试连接，并扫描
        //console.info("connectSubscriber start");
        // 创建扫描对象
        //connectSubscriber();

        // 创建定时检索器
    });

    // 监控画面最小化
    $("#monitorStopBtn").click(function(){
        minScreen();
        $("#monitorStopBtn").hide();
        $("#minScreen").hide();
        $("#description").hide();
        $("#description").val("");
        unSubscribeMonitor();
    });

	// 区域最大化/最小化
    $("#screenArea").hover(function(){
        console.log($("#container").find(".removeble").hasClass("main-content"));
        if(!$("#container").find(".removeble").hasClass("main-content")){
           $("#minScreen").show();
        }else{
            $("#maxScreen").show();
        }
    },function(){
        console.log($("#container").find(".removeble").hasClass("main-content"));
        if(!$("#container").find(".removeble").hasClass("main-content")){
            $("#minScreen").hide();
        }else{
            $("#maxScreen").hide();
        }
    });
    // 监控画面最小化
    $("#minScreen").click(function(){
        minScreen();
        // initExportContextMenu();//重新开启右键点击事件
        $("#minScreen").hide();
    });
    // 监控画面最大化
    $("#maxScreen").click(function(){
        // $.contextMenu("destroy"); //销毁右键点击事件
        fullScreen();
        $("#maxScreen").hide();
    });

	$(".breadcrumb li:last").detach();
	$(".breadcrumb").append("<span><span style='padding:0 5px; color:#ccc'>></span>统计分析SPC</span><span><span style='padding:0 5px; color:#ccc'>></span>SPC分析</span>");
	var right_width;
	if(document.body.clientWidth>767){
	 		 right_width=$(".main-body:last").width()-250;
	 	  }
	 	  else{
	 		 right_width= document.body.clientWidth-30;
	 	  }

		$(".result_right").width(right_width);
		//$(".main-body").height('100%');
		$("div[id^='main']").each(function(idx,item){
			if(document.body.clientWidth>767){
				$(item).css("width",right_width*0.5-35+"px");
				}
				else{
					$(item).css("width",right_width-30+"px");
		  	 	  }
		});
		$("#main1").width(right_width-35);
		$("#saveAsImage").click(function(){
			html2canvas($(".main-wrap"), {
				    taintTest : false,
	//			  height: $(".main-body:last").scrollHeight,
				  height: document.getElementsByName("main-body")[0].scrollHeight+100,
			        onrendered: function(canvas) {
			            // document.body.appendChild(canvas);
			            //$("#cgResultImage").append(canvas);
			            Canvas2Image.saveAsPNG(canvas, 1200, 800);
			        }
				});
		  });
		 var data = new Array();
		 var dataIndex = new Array();
		 $.each($("#analyzeTable").bootstrapTable("getSelections"),function(idx,item){
			 data.push(item.value);
			 dataIndex.push(idx+1);
		 });
		var main2 = echarts.init(document.getElementById('main2'), 'chalk');
		var main3 = echarts.init(document.getElementById('main3'), 'chalk');
		var main4 = echarts.init(document.getElementById('main4'), 'chalk');
		var myChart = echarts.init(document.getElementById('main1'), 'chalk');
		var option = {
				title: {
                    text: 'X图 ',
                    textStyle:{
                    	fontSize:16,
                    	color:'#bbb'
                    	}
         },
		 tooltip: {
		     trigger: 'axis'
		 },
		 legend: {
		     data:['检测数据值'],
		     x:'right',
		     textStyle:{
	             	color:'#bbb'
	             	}
		 },
		 toolbox: {
		     feature: {
		         //saveAsImage: {}
		     }
		 },
		 grid: {
			    left: '3%',
		        right: '50',
		        bottom: '3%',
		        containLabel: true
		    },
		 xAxis: {
		     type: 'category',
		     data: dataIndex
		 },
		 yAxis: {
		     type: 'value',
		     boundaryGap:['20%','20%'],
		     scale:true
		 },
		 series: [
		     {
		         name:'检测数据值',
		         type:'line',
                 label: {
                     normal: {
                         show: true,
                         position: 'top',
                         color:'#bbb'
                     }
                 },
		         data:data,
		         itemStyle: {
		             normal: {
		                 color: '#b3252a'
		             }
		         },
		         markPoint: {
		        	 symbolSize:[90,48],
		                data: [
		                    {type: 'max', name: '最大值'},
		                    {type: 'min', name: '最小值'}
		                ]
		            },
		         markLine: {
		                data: [
		                     {type: 'average', name: '平均值'},
		                ]
		         }

		     }
		 ]
	};

		myChart.clear();
		myChart.setOption(option);
		var option4 = {
				title: {
                    text: '工序能力分析',
                    textStyle:{
                    	fontSize:16, color:'#bbb'
                    	}
                 },
			     legend: {
			        data:['LSL','USL','SL'],
			        x:'right'
			    },
			    tooltip: {
			        formatter: function (params) {
			        	return params.seriesName;

			        }
			    },
			    xAxis: {
			        type: 'value',
			        min: -5,
			        max: 5,
			        interval: 1,
			        axisLabel: {
			            show: false
			        }
			    },
			    yAxis: {
			        type: 'value',
			        axisLabel: {
			            show: false
			        }
			    },
			    grid:{
			    	//height:$("#main4").height() * 0.6
			    	height:270,
			    	width:575
			    }

			};
		main4.clear();
		main4.setOption(option4);
		var mesDriverName = $("#mesDriverId").find("option:selected").text();
		$("#mesDriver").val(mesDriverName);
		var procedurePropertyName = $("#procedurePropertyId").find("option:selected").text();
		$("#procedureProperty").val(procedurePropertyName);
		var procedurePropertyId = $("#procedurePropertyId").val();
		var productName = $("#productId").find("option:selected").text();
		$("#product").val(productName);
		$("#_subRange").val($("#subRange").find("option:selected").text());
		$("#_subSeq").val($("#subSeq").find("option:selected").text());
		$("#_subNum").val($("#subNum").find("option:selected").text());
		if($('#analyzeTable').bootstrapTable('getSelections').length != 0){
			  var productSn = $('#analyzeTable').bootstrapTable('getSelections')[0].productionSn;
			  console.log($('#analyzeTable').bootstrapTable('getSelections')[0].productionSn);
			  $("#productSn").val(productSn);
		}
		$.ajax({
			  url:"${contextPath}/statistics/getProcedureProperty/"+procedurePropertyId,
			  dataType:"JSON",
			  type:"POST",
			  success:function(data){
				  console.log(data.mesProductProcedure.procedurenum);
				  $("#productProcedureNum").val(data.mesProductProcedure.procedurenum);
				  $("#productProcedure").val(data.mesProductProcedure.procedurename);
				  $("#_standardValue").val(data.standardvalues);
				  $("#_minValue").val(data.lowervalues);
				  $("#_maxValue").val(data.uppervalues);
			  },
			  error:function(jqXHR, textStatus, errorThrown){
				  console.log(textStatus);
				  // swal("出错了:"+jqXHR.status+"-"+textStatus);
				  swal("服务器连接中断，请重新刷新画面");
			  }
		});
		var startAnalyze;
		$("#analyzeBtn").click(function(){
            console.log("var option = myChart.getOption();:"+  myChart.getOption().series[0].data);
            console.log("var option = myChart.getOption();:"+  myChart.getOption().series[0].data);
            console.log("var option = myChart.getOption();:"+  myChart.getOption().series[0].data);
            console.log("var option = myChart.getOption();:"+  myChart.getOption().series[0].data);

			$("#main1").show();
			$("#main4").show();
			$(".result_bg").css("visibility","visible");
			$(".result_table").show();

			  var scale = {name:"scale",value:$("#scale").val()};
			  var values = new Array();
			  var arrays = $('#analyzeTable').bootstrapTable("getSelections");
			  arrays = arrays.reverse();
			  $.each(arrays, function(){
			 	// var obj = {productionSn:this.productionSn,value:this.value,time:this.time};
			      values.push(this.value);
		      })
			  var value = {name:"values",value:values};
 			  var formArray = $("#formID").serializeArray();
 			  console.log("-------value-------" + value);
 			  console.log("-------scale-------" + scale);
 			  var monitorFlg = {name:"monitorFlg",value:$("#monitorFlg").val()};
 			  var dataPackage = {name:"dataPackage",value:$("#dataPackage").val()};
 			  formArray.push(monitorFlg);
 			  formArray.push(dataPackage);
              formArray.push(value);
			  formArray.push(scale);
			  $.ajax({
  				  url:"${contextPath}/statistics/analyseSPCData",
  				  dataType:"JSON",
  				  type:"POST",
  				  data:formArray,
  				  success:function(data){
  					// 刷新1数据
  					var optionChart1 = myChart.getOption();
  					optionChart1.series[0].data = data.datas;
  					/* optionChart1.series[0].label.normal.show = true;
  					optionChart1.series[0].label.normal.position = 'top';
  					optionChart1.series[0].label.normal.color = '#bbb'; */
  					optionChart1.series[0].label = {
 		                normal: {
 		                  show: true,
 		                        position: 'top',
 		                        color:'#bbb'
 		                }
  					}
  					optionChart1.series[0].markLine = {
  	                        data: [
  	                            {type: 'average', name: '平均值'},
  	                            [{name: '上公差', value:data.xImageUcl, xAxis: -1, yAxis:data.xImageUcl ,},{xAxis: dataIndex.length,yAxis: data.xImageUcl,}],
                                [{name: '下公差', value:data.xImageLcl, xAxis: -1, yAxis:data.xImageLcl ,},{xAxis: dataIndex.length,yAxis: data.xImageLcl,}],
  	                        ]
  	                 }
  					myChart.clear();
  					myChart.setOption(optionChart1);

  					var option1 = JSON.parse(data.xBarMap);
  					var option2 = JSON.parse(data.RBarMap);
  					var option_grid1=option1.grid;
   					var option_grid2=option2.grid;
   				    option_grid1.right="50";
                    option_grid2.right="40";
  					option1.title={text:'XBar图',textStyle:{fontSize:16, color:'#bbb'}};
  					option2.title={text:'R图',textStyle:{fontSize:16, color:'#bbb'}};
  					option1.series[0].itemStyle={normal: {color: '#c29b09'}};
  					option2.series[0].itemStyle={normal: {color: '#0098d9'}};
  					option1.series[0].markPoint.symbolSize=[90,48];
  					option2.series[0].markPoint.symbolSize=[90,48];
  					option1.series[0].markLine = {
                            data: [
                                {type: 'average', name: '平均值'},
                            ]
                     }
                    option2.series[0].markLine = {
                            data: [
                                {type: 'average', name: '平均值'},
                            ]
                     }

  					main2.clear();
  					main2.setOption(option1);
  					main3.clear();
  					main3.setOption(option2);

  					$("#tableResult").find("span").each(function(idx,item){
  						$(item).empty();
  						var result = data[$(item).attr("id")];
  						console.log($(item).attr("id")+"--"+result);
  						$(item).append(result);
  					})

  					var m_fBZC_new = data.sigma;
					var m_fPJZ_new = data.X_average;
					var fMaxValue = 3 * m_fBZC_new;
					var fMingyizhi = data.standardValue,
					    fShanggch = data.maxValue,
					    fXiagcha = data.minValue;
  					if (Math.abs(fMingyizhi + fShanggch - m_fPJZ_new) > fMaxValue) {
    						fMaxValue = Math.abs(fMingyizhi + -m_fPJZ_new);
					}
					if (Math.abs(fMingyizhi + fXiagcha - m_fPJZ_new) > fMaxValue) {
					    fMaxValue = Math.abs(fMingyizhi + fXiagcha - m_fPJZ_new);
					}
					console.log("fMaxValue:" + fMaxValue);
					var OnePixelValue = fMaxValue / (600 / 2);
					var ux = (fMingyizhi + fShanggch - m_fPJZ_new) / OnePixelValue;
					var lx = (fMingyizhi + fXiagcha - m_fPJZ_new) / OnePixelValue;
					var myx = (fMingyizhi - m_fPJZ_new) / OnePixelValue;
					ux = (ux / (600 / 10));
					lx = lx / (600 / 10);
					myx = myx / (600 / 10);
					if(ux > 5){
						ux = 5;
					}
					if(ux < -5){
						ux = -5;
					}
					if(lx > 5){
						lx = 5;
					}
					if(lx < -5){
						lx = -5;
					}
					if(myx > 5){
						myx = 5;
					}
					if(myx < -5){
						myx = -5;
					}
					var part = (ux - lx) / 10;
					var series = [{
			            type: 'line',
			            showSymbol: false,
			            data: generateData(1.5, -5, 5)
			        }, {
			            name:'USL',
			            type: 'line',
			            showSymbol: true,
			            data: [
			                [ux, 0],
			                [ux, top]
			            ],
			            markPoint: {
			                itemStyle:{
	                               normal:{label:{
	                               		show: true,
	                                    formatter: function (param) {
	                                    		return (param.data.coord[0]).toFixed(2);
	                                    	}
	                                    }
	                               }
			                },
			                data: [{type: 'max', name: 'X轴'}],
			                silent:true
			            },
			            itemStyle:{normal:{
			            	color:'#7ac8f9',
			            	type:'solid'
			            	}},
			            lineStyle:{normal:{
			            	color:'#7ac8f9',
			            	type:'solid'
			            	}}
			        }, {
			            name:'LSL',
			            type: 'line',
			            showSymbol: true,
			            data: [
			                [lx, 0],
			                [lx, top]
			            ],
			            markPoint: {
			                itemStyle:{
	                               normal:{label:{
	                               		show: true,
	                                    formatter: function (param) {
	                                    		return (param.data.coord[0]).toFixed(2);
	                                    	}
	                                    }
	                               }
			                },
			                data: [{type: 'max', name: 'X轴'}],
			                silent:true
			            },
			            itemStyle:{normal:{
			            	color:'#de6942',
			            	type:'solid'
			            	}},
			            lineStyle:{normal:{color:'#de6942'}}
			        }, {
			            name:'SL',
			            type: 'line',
			            showSymbol: true,
			            data: [
			                [myx, 0],
			                [myx, top]
			            ],
			            markPoint: {
			                itemStyle:{
	                               normal:{label:{
	                               		show: true,
	                                    formatter: function (param) {
	                                    		return (param.data.coord[0]).toFixed(2);
	                                    	}
	                                    }
	                               }
			                },
			                data: [{type: 'max', name: 'X轴'}],
			                silent:true
			            },
			            itemStyle:{normal:{
			            	color:'#c43a69',
			            	type:'solid'
			            	}},
			            lineStyle:{normal:{color:'#c43a69'}}
			        }];
					var barPosition = lx;

					//所有数据在lx和ux范围内的数据长度
					var lslMark = fMingyizhi+fXiagcha;
					var uslMark = fMingyizhi+fXiagcha+ (fShanggch - fXiagcha);
					var valueTemp = new Array();
					for(var i=0;i<=values.length;i++){
						var val = values[i];
	            		if((val <= uslMark) && (val > lslMark)){
	            			valueTemp.push(val);
	            		}
					}

			        for(var j = 1; j <=10; j++){
			            var _part = part;
			            var compareData = fMingyizhi+fXiagcha+ (j*(fShanggch - fXiagcha))/10;
			            var preCompareData = fMingyizhi+fXiagcha+ ((j-1)*(fShanggch - fXiagcha))/10;
			            var tmp = new Array();
			            for(var k = 0; k < valueTemp.length; k++){
			            	var val = valueTemp[k];
		            		if((val <= compareData) && (val > preCompareData)){
		            			tmp.push(val);
		            		}
			            }

			            var percentData = tmp.length/valueTemp.length;
			            percentData = percentData.toFixed(3);
			            if(j == 1){
			                _part = _part/2;
			            }
						barPosition = barPosition + _part;
						if(percentData==0){
							continue;
						}
						var barColor;
						if(j%2==0){
							barColor = '#7ac8f9';
		            	}else{
		            		barColor = '#330066';
		            	}
						var obj = {
								itemStyle:{normal:{
					            	color:barColor,
					            	}},
							    type:'bar',
							    name:("比率 : "+percentData*100)+'%',
							    //barWidth:(left2- left1)/10
							    barGap:'-0.98',
							    data:[
							            [barPosition,0],
							            [barPosition,percentData/10]
							        ],
						        label: {
					                normal: {
					                    show: true,
					                    position: 'top',
					                    formatter:function(params){
					                    	var temp = params.seriesName.split(":")[1];
					                    	temp = temp.substr(1,temp.length-1);
					                    	if(temp.length>2){
					                    		return temp.substr(0,2)+"%";
					                    	}else{
					                    		return temp+"%";
					                    	}
					                    }
					                }
					            }
							};
						series.push(obj);
					}
					main4.setOption({series:series});
					// console.log(JSON.stringify(main4.getOption()));
  				  },
  				  error:function(jqXHR, textStatus, errorThrown){
  					  console.log(textStatus);
  					  swal("出错了:"+jqXHR.status+"-"+textStatus);
  				  }
  			  });
			 /*  startAnalyze = setInterval(function(){
				  $("#analyzeBtn").trigger("click");
			  },6000); */
		});
		var resizeMainContainer = function () {
			$("div[id^='main']").each(function(idx,item){
				$(item).css("width",$(item).parent().width()+"px");
				$(item).css("height",$(item).parent().height()+"px");
			});
		};
		window.onresize = function () {
		    //重置容器高宽
		    resizeMainContainer();
		    myChart.resize();
		     main2.resize();
		     main3.resize();
		     main4.resize();
		};
		var top;
		//var values_ = new Array();
		function normalDist(theta, x) {
			var mul1 = 1 / (theta * Math.sqrt(2 * Math.PI));
		    return  mul1 * Math.exp(-x * x / 2 / theta / theta);
		}

		//console.log(main4.getOption().grid[0].width);
		var gridWidth = main4.getOption().grid[0].width;
		function generateData(theta, x0, x1) {
			var values_ = new Array();
		    var data = [];
		    for (var i = 0; i <= 600; i++) {
		        var x = (x1 - x0) * i / 600 + x0;
		        var normalData = normalDist(theta, x);
		        values_.push(normalData);
		        top = Math.max.apply(null, values_);
		        data.push([x, normalData]);
		    }
		    return data;
		}

		function getBarDatas(datas){

		}
		/* resizeMainContainer();
	    myChart.resize();
	     main2.resize();
	     main3.resize();
	     main4.resize(); */
});

	
// 使用刚指定的配置项和数据显示图表。
		  $(".fixed-table-pagination").hide();
// 		  $(".main-wrap").addClass("fold-result");
// 		  $(".main-wrap").addClass("fold-wrap");    
// 		     $(".tree-list").insertBefore(".main-body:first");
// 		     $(".collapse-trigger").insertBefore(".main-body:first");
// 		$(".spc_result").parent().removeClass("main-body");

</script>
<!-- MQ通信引用 -->
<script type="text/javascript" src="${contextPath }/js/monitor.index.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.render.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
<script src="${contextPath }/js/sockjs.js"></script>
<script src="${contextPath }/js/stomp.js"></script>