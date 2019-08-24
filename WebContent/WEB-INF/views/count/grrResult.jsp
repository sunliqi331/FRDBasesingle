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
  .result_bg{
    visibility: hidden;
    margin: 2px 0;
  }
  .result_right .col-sm-6{
    padding:0 2px;
  }
  .result_right .col-sm-4{
    padding:0 1px 0 0;
  }
  .result_table > thead > tr > th, .result_table > tbody > tr > td{
    padding:2px;
  }
  .result_table > tbody > tr > td{
    color:#bbb;
  }
  
</style>
<div class="result_left" layouth="5" id="jbsxBox2personnelTree" tabindex="2" style="overflow: hidden; outline: none;">
        <div class="result">
            <div class="search_header" style="background: none;color: #bbb;">
              <i class="fa fa-line-chart"></i> GRR分析条件
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
              
              <div class="form-group">
                 <label for="mesDriver" class="searchtitleWhite">量具名</label>
                 <input type="text" class="searchtext" id="mesDriver" readonly="readonly"/>
              </div> 
              <div class="form-group">
                 <label for="product" class="searchtitleWhite">零件名</label>
                 <input type="text" class="searchtext" id="product" readonly="readonly"/>
              </div> 
              <div class="form-group">
                 <label for="procedureProperty" class="searchtitleWhite" >参数</label>
                 <input type="text" class="searchtext" id="procedureProperty" readonly="readonly"/>
              </div>
             
                        
			  <div class="form-group">
                 <label for="standardValue" class="searchtitleWhite">名义值</label>
                 <input type="text" class="searchtext" id="standardValue"/> 
              </div>
              <div class="form-group">
                 <label for="minValue" class="searchtitleWhite">下公差</label>
                 <input type="text" class="searchtext" id="minValue" /> 
              </div>
              <div class="form-group">
                 <label for="maxValue" class="searchtitleWhite">上公差</label>
                 <input type="text" class="searchtext" id="maxValue"/> 
              </div>
<!--               <div class="result_title text-center">统计方式</div> -->
				<div style="position: relative;border: 1px solid #bbb; margin-top: 5px; ">
				   <div style="position: absolute; left:60px; top:-12px; background: #383838; color: #bbb; height: 15px;">统计方式</div>
					<div class="form-group">
	                 <label for="personCount" class="searchtitleWhite">人数</label>
	                 <input type="text" class="searchtext" id="personCount" readonly="readonly"/>  
	              </div>
	              <div class="form-group">
	                 <label for="checkCount" class="searchtitleWhite">次数</label>
	                 <input type="text" class="searchtext" id="checkCount" readonly="readonly"/>  
	              </div>
	              <div class="form-group">
	                 <label for="workpieceCount" class="searchtitleWhite">工件数</label>
	                 <input type="text" class="searchtext" id="workpieceCount" readonly="readonly"/> 
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
				</div>
              
              <div style="position: relative;border: 1px solid #bbb; margin-top: 8px; ">
				   <div style="position: absolute; left:60px; top:-8px; background: #383838; color: #bbb; height: 15px;">送检件号</div>
              <div class="form-group" style="padding: 10px 15px;">
               <!--  <label for="inputText" class="searchtitle">送检件号</label>            -->
                 <textarea name="description" style="margin: 5px 2px;width: 96%" class="textarea-scroll" id="productSn" cols="29" rows="3"  maxlength="256"></textarea>                 
			 </div>
			 </div>
			   <a id="analyzeBtn" class="btn btn-info " style="padding: 2px">开始评定</a> 
 			<a id="saveAsImage"  class="btn btn-default " >保存图片</a>
			 <a id="back"  class="btn btn-default " >返回</a>
          </form> 
        </div>
      
      </div>
    
        <div class="result_right" >          
            <div id="screenArea" style="width: 40px;height:40px;margin-top:20px;position: absolute;z-index: 9999;">
             <ul class="monitor_large">
               <li ><a href="#"  id="maxScreen" style="display: none"><img src="${contextPath}/styles/img/large.png" width="40"/></a></li>
               <li ><a href="#" id="minScreen" style="display: none"><img src="${contextPath}/styles/img/small.png" width="40"/></a></li>
             </ul>
             </div>
		<div class="row" style="margin:0">
			<!-- <div class="col-sm-6"></div>
			<div class="col-sm-6"> -->
			<!-- <table class="table table-striped table-hover text-center" id="_table"> 
			  <thead>
		        <tr>            
		            <th style="" colspan="6" data-field="sn" tabindex="0">
		                <div class="th-inner text-center" >
		                                                                    检验员一
		                </div>
		                <div class="fht-cell">
		                </div>
		            </th>
		        </tr>
              </thead>
              </tbody>
                <tr data-index="0" data-uniqueid="73">
          			<td colspan="6" class="text-right">46.0125</td>
          			<td colspan="6" class="text-right">46.0125</td>
                </tr>
              </tbody>
            </table> 
   			</div> -->
		</div>
	   
  <div class="col-sm-6" >
    <div class="result_bg">
      	<div id="main1" style="width:390px;height:250px;"></div>
    </div> 
  </div>
   <div class="col-sm-6"  >
    <div class="result_bg">
      	<div id="main4" style="width:390px;height:250px;"></div>
    </div>
   
  </div>
  <div class="col-sm-6 " >
    <div class="result_bg">
      	<div id="main2" style="width:190px;height:250px;"></div>
    </div>
  </div>
  <div class="col-sm-6" >
    <div class="result_bg" style="height: 250px;">
      	<div id="main3" style="width:190px;height:250px;"></div>
    </div>
  </div>
 
  <div class="col-sm-6 ">
    <div class="result_bg">
      	<div id="main5" style="width:410px;height:190px;"></div>
    </div>
   </div>
   <div class="col-sm-6 " style="overflow-x:auto">
     <table class="table grr_table" id="tableResult" data-field="mesDrivers"
data-url="${contextPath}/driver/driverData" style="margin-top: 5px">
    <tbody>
      <tr>
	      <td><span id="XBAR"></span></td>
	      <td><span id="RBAR"></span></td>
	      <td><span id="Xdiff"></span></td>	     
      </tr>
       <tr>
	      <td><span id="UCLx"></span></td>
	      <td><span id="LCLx"></span></td>
	      <td><span id="UCLr"></span></td>	      
      </tr>
      
      <tr>
          <td><span id="Rp"></span></td>
          <td><span id="EV"></span></td>
          <td><span id="AV"></span></td>
    
      </tr>
      <tr>
        <td><span id="Grr"></span></td>
        <td><span id="PV"></span></td>
        <td><span id="TV"></span></td>
      </tr>
      <tr>
        <td><span id="percentEV"></span></td>
        <td><span id="percentAV"></span></td>
        <td><span id="percentGrr"></span></td>
      </tr>
      <tr>
          
          <td><span id="percentPV"></span></td>
          <td><span id="ndc"></span></td>
	      <td  class="text-center" style="vertical-align: middle; color: #de6942" ><span id="result"></span></td>
	    
      </tr>
  
    </tbody>
    </table>
   </div>
  </div>
      
  <div class="clearfix"></div>
   
      <div id="cgResultImage"></div>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
  <script src="${contextPath }/js/template.js"></script>
  <script type="text/html" id="people">
   {{each list as value0 i}}
	<div class="col-sm-4">
			<table id="{{i}}" class="table table-striped table-hover text-center result_table" style="margin-bottom:10px;color:#bbb;"> 
			  <thead>
		        <tr>            
		            <th colspan="6" data-field="sn" tabindex="0" style="border-bottom: none;border: 1px solid #ccc;">
		                <div class="th-inner text-center" style="color: #ccc;background:url('images/header.png') repeat-x 0 0;">
							{{i}}
		                </div>
		                <div class="fht-cell">
		                </div>
		            </th>
                            
		        </tr>
              </thead>
              </tbody>
					{{each value0 as value1 j}}
                          <tr data-uniqueid="73">
								{{if j > 0 && j < rowSize-1}}
									<td>{{j}}</td>
								{{else if j == 0}}
									<td></td>
								{{/if}}

							 	{{each value1 as value2 k}}	
									{{if value2 == 0}}
										<td colspan="{{value1.length-1}}"></td>
									{{else if value2 != null}}
          			            		<td>{{value2}}</td>
									{{/if}}
							 	{{/each}}                          
						  </tr>
                    {{/each}}
              </tbody>
            </table> 
   	</div>
   {{/each}}
</script>
<%--   <script type="text/html" id="people">
	<% for(var i = 0; i < list.length; i++){%>
	<div class="col-sm-4">
			<table class="table table-striped table-hover text-center result_table" style="margin-bottom:10px"> 
			  <thead>
		        <tr>            
		            <th colspan="6" data-field="sn" tabindex="0" style="border-bottom: none;background: #87c8f0;border: 1px solid #333;">
		                <div class="th-inner text-center" style="color: #333;">
							i
		                </div>
		                <div class="fht-cell">
		                </div>
		            </th>
                            
		        </tr>
              </thead>
              </tbody>
					<%for(var j = 0; j < list[i].length; j++){%>
                          <tr data-index="1" data-uniqueid="73">
								<%if(j > 0){%>
									<td>j</td>
								<%}%>
								<%else{%>
									<td></td>
								<%}%>

						  </tr>
                    <%}%>
              </tbody>
            </table> 
   	</div>
   <%}%>
</script> --%>
<script type="text/javascript" src="${contextPath }/js/page/screenChange.js"></script>
  <script>
  	$(document).ready(function(){
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
  	$(".breadcrumb").append("<span><span style='padding:0 5px; color:#ccc'>></span>统计分析MSA</span><span><span style='padding:0 5px; color:#ccc'>></span>Grr分析</span>");
	
  		var right_width;
  		 if(document.body.clientWidth>767){
  	 		 right_width=$(".main-body:last").width()-250;  
  	 	  }
  	 	  else{
  	 		 right_width= document.body.clientWidth-30; 
  	 	  }
  		$(".result_right").width(right_width);
  		$("div[id^='main']").each(function(idx,item){
			//alert($(item).parent().width());
			if(document.body.clientWidth>767){
			$(item).css("width",right_width*0.5-35+"px");
			}
			else{
				$(item).css("width",right_width-30+"px");
	  	 	  }
			
			//$(item).css("width",$(item).parent().width()-30+"px");
			//$(item).css("height",$(item).parent().height()+"px");
		});
		
  		$("#saveAsImage").click(function(){
  			//$("html").jqprint();
  			//$("#cgResultImage").html($(".main-wrap").html());
  		   html2canvas($(".main-wrap"), {
  			    taintTest : false,
//  			  height: $(".main-body:last").scrollHeight,
  			  height: document.getElementsByName("main-body")[0].scrollHeight+100,
  		        onrendered: function(canvas) {
  		            // document.body.appendChild(canvas); 
  		            //$("#cgResultImage").append(canvas);
  		            Canvas2Image.saveAsPNG(canvas, 1200, 800);
  		        }
  			}); 
  	  });
  		$(".result_left").css("overflow-y","auto");
  		 var  backParam = "";
  		  $("#back").click(function(){
  			  console.log("---"+backParam);
  			  window.location.href="${contextPath }/statistics/msa?refresh=true"+backParam;
  		  });
  		 var analysisMethod = $("#analysisMethod").val();
  		  backParam += "&analysisMethod="+analysisMethod;
  		  var testCount = $("#testCount").val();
  		  backParam += "&testCount="+testCount;
  		  var statisticalStandard = $("#statisticalStandard").val();
  		  backParam += "&statisticalStandard="+statisticalStandard;
  		  var measureRange = $("#measureRange").val();
  		  backParam += "&measureRange="+measureRange;
  		  var mesDriverName = $("#mesDriverId").find("option:selected").text();
  		  var mesDriverId = $("#mesDriverId").val();
  		  backParam += "&mesDriverId="+mesDriverId;
  		  $("#mesDriver").val(mesDriverName);
  		  var mesDriverNo;
  		  var productName = $("#productId").find("option:selected").text();
  		  console.log("-----"+productName);
  		  var productId = $("#productId").val();
  		  backParam += "&productId="+productId;
  		  $("#product").val(productName);
  		  var productProcedureId = $("#productProcedureId").val();
  		  backParam += "&productProcedureId="+productProcedureId;
  		  if($('#analyzeTable').bootstrapTable('getSelections').length != 0){
  			var values = new Array();
  			var arrays = $('#analyzeTable').bootstrapTable("getSelections");
  			 $.each(arrays, function(){
  		            values.push(this.productionSn);
  		     })
  		    $.unique(values.sort()); 
  			  $("#productSn").val(values);
  		  }
  		  var procedurePropertyName = $("#procedurePropertyId").find("option:selected").text();
  		  var procedurePropertyId = $("#procedurePropertyId").val();
  		  backParam += "&procedurePropertyId="+procedurePropertyId;
  		  backParam += "&begin="+$("#begin").val();
  		  backParam += "&end="+$("#end").val();
  		  $("#procedureProperty").val(procedurePropertyName);
  		  $("input[name='count'][value='"+ $("#testCount").val() +"']").attr("checked","checked");
  		  $("input[name='scope'][value='"+ $("#statisticalStandard").val() +"']").attr("checked","checked");
  		  $.ajax({
  			  url:"${contextPath}/statistics/getProcedureProperty/"+procedurePropertyId,
  			  dataType:"JSON",
  			  type:"POST",
  			  success:function(data){
  				  console.log(data.standardvalues);
  				  $("#standardValue").val(data.standardvalues);
  				  $("#minValue").val(data.lowervalues);
  				  $("#maxValue").val(data.uppervalues);
  			  },
  			  error:function(jqXHR, textStatus, errorThrown){
  				  console.log(textStatus);
  			  }
  		  });
  		 var checkCount = $("#checkNum").find("option:selected").text();
  		 $("#checkCount").val(checkCount);
  		 var personCount = $("#personNum").find("option:selected").text();
  		 $("#personCount").val(personCount);
  		 var workpieceCount = $("#workpieceNum").find("option:selected").text();
  		 $("#workpieceCount").val(workpieceCount);
  		 if($('#table').bootstrapTable('getSelections').length != 0){
  			  var productSn = $('#table').bootstrapTable('getSelections')[0].productionSn;
  			  $("#productSn").val(productSn);
  		 }
  		
  		
  		
  		
  		var main1 = echarts.init(document.getElementById('main1'), 'chalk');
  		var main2 = echarts.init(document.getElementById('main2'), 'chalk');
  		var main3 = echarts.init(document.getElementById('main3'), 'chalk');
  		var main4 = echarts.init(document.getElementById('main4'), 'chalk');
  		var main5 = echarts.init(document.getElementById('main5'), 'chalk');
  		$("#analyzeBtn").click(function(){
  			$(".grr_table").show();
  			$(".result_bg").css("visibility","visible");
  			//$(".result_right").show();
  		  if($("#standardValue").val() != "" && $("#minValue").val() != ""
  				  && $("#maxValue").val() != ""){
  			  var standardValue = {name:"standardValue",value:$("#standardValue").val()};
  			  var minValue = {name:"minValue",value:$("#minValue").val()};
  			  var maxValue = {name:"maxValue",value:$("#maxValue").val()};
  			  var actualValue = {name:"actualValue",value:$("#actualValue").val()};
  			  var productionSn = {name:"productionSn",value:$("#productSn").val()};
  			  var scale = {name:"scale",value:$("#scale").val()};
  			  var values = new Array();
  			  var arrays = $('#analyzeTable').bootstrapTable("getSelections");
  			  $.each(arrays, function(){
				  var obj = {productionSn:this.productionSn,value:this.value,time:this.time};
  				  values.push(obj);
  		      })
  			  var value = {name:"datas",value:JSON.stringify(values)}; 
  			  var formArray = $("#formID").serializeArray();
  			  formArray.push(standardValue);
  			  formArray.push(minValue);
  			  formArray.push(maxValue);
  			  formArray.push(actualValue);
  			  formArray.push(value);
  			  formArray.push(scale);
  			  $.ajax({
  				  url:"${contextPath}/statistics/analyseGrrData",
  				  dataType:"JSON",
  				  type:"POST",
  				  data:formArray,
  				  success:function(data){
  					  console.log(data);
  					$(".row").empty();
  					var tableMap = data.tableMap;
  					var rowSize = 0;
  					var _tableMap = {};
  					$.each(Object.keys(tableMap).sort(),function(idx,item){
  						_tableMap[item] = tableMap[item];
  					});
  					for(var table in tableMap){
			  			rowSize = Object.keys(_tableMap[table]).length;
  					}
  					console.log("rowSize:"+rowSize);
  					var datas = {
  						list:_tableMap,
  						rowSize:rowSize
  					};
  					var tmp = template("people",
  							    datas
	    			    );
  					$(".row").append(tmp); 
  					
  					var option1 = JSON.parse(data.partAppraiserAverageMap);
  					var option2 = JSON.parse(data.partAppraiserAverageCompareMap);
  					var option3 = JSON.parse(data.repeatAbilityRangeCompareMap);
  					var option4 = JSON.parse(data.repeatAbilityRangeMap);
  					var option5 = JSON.parse(data.variationMap);
  					var option_legend1=option1.legend;
   					var option_legend2=option2.legend;
   					var option_legend3=option3.legend;
   					var option_legend4=option4.legend;
   					var option_legend5=option5.legend;
   					var option_grid1=option1.grid;
   					var option_grid2=option2.grid;
   					var option_grid3=option3.grid;
   					var option_grid4=option4.grid;
   					var option_grid5=option5.grid;
                    option_grid1.right="50";
                    option_grid2.right="50";
                    option_grid3.right="40";
                    option_grid4.right="40";
//                     option_grid5.right="5%";
   					option_legend2.x = "right";  					
   					option_legend3.x = "right";
   					option_legend5.x= "right";
   					
   		     		option_legend5.height = "5";
   					option_legend5.orient = "vertical";

   					option_legend1.textStyle = {color:'#bbb',fontWeight:'lighter'};
   					option_legend2.textStyle = {color:'#bbb',fontWeight:'lighter'};
   					option_legend3.textStyle = {color:'#bbb',fontWeight:'lighter'};
   					option_legend4.textStyle = {color:'#bbb',fontWeight:'lighter'};
   					option_legend5.textStyle = {color:'#bbb',fontWeight:'lighter'};
  					option1.title={text:'检测件操作者平均值图',textStyle:{fontSize:16,color:'#ccc',fontWeight:'lighter'}}; 
  					option2.title={text:'操作者平均值比较图',textStyle:{fontSize:16,color:'#ccc',fontWeight:'lighter'}};
  					option3.title={text:'操作者极差比较图',textStyle:{fontSize:16,color:'#ccc',fontWeight:'lighter'}};
  					option4.title={text:'重复性极差控制图',textStyle:{fontSize:16,color:'#ccc',fontWeight:'lighter'}};
  					option5.title={text:'变异分量直列图',textStyle:{fontSize:16,color:'#ccc',fontWeight:'lighter'}};
  					option1.series[0].itemStyle={normal: {color: '#de6942'}};
  					option1.series[0].markPoint.symbolSize=[90,40];
  
  					option1.series[0].markPoint.label={emphasis :{textStyle: {color : '#000000'}}}; 					
  					option1.series[0].markPoint.itemStyle={normal:{opacity: 0.5}};
  					option4.series[0].markPoint.symbolSize=[90,30];
  					option4.series[0].markPoint.itemStyle={normal:{opacity: 0.5}};
  					option4.series[0].markPoint.label={emphasis :{textStyle: {color : '#000000'}}}; 
  					
  					
  					
  					var tmpOptionSeries = new Array();
  					tmpOptionSeries.push(option2.series);
  					tmpOptionSeries.push(option3.series);
  					$.each(tmpOptionSeries,function(_idx,_item){
  						$.each(_item,function(idx,item){
  	  						if(idx == 0){
  	  							item.itemStyle={normal: {color: '#c12e34'}};
  	  							
  	  							$("#"+item.name).find("thead > tr > th").css("background","#c12e34")
  	  							item.markPoint.symbolSize=[90,35]
  	  							item.markPoint.itemStyle={normal:{opacity: 0.5}}
  	  							item.markPoint.label={emphasis :{textStyle: {color : "#000000"}}}
  	  						}else if( idx == 1){
  	  							item.itemStyle={normal: {color: '#0098d9'}};
  	  							$("#"+item.name).find("thead > tr > th").css("background","#0098d9")
  	  							item.markPoint.symbolSize=[90,35]
  	  							item.markPoint.itemStyle={normal:{opacity: 0.5}}
  	  							item.markPoint.label={emphasis :{textStyle: {color : "#000000"}}}
  	  						}else if(idx == 2){
  	  							item.itemStyle={normal: {color: '#e6b600'}};
  	  							$("#"+item.name).find("thead > tr > th").css("background","#e6b600")
  	  							item.markPoint.symbolSize=[90,35]
  	  							item.markPoint.itemStyle={normal:{opacity: 0.5}}
  	  							item.markPoint.label={emphasis :{textStyle: {color : "#000000"}}}
  	  						}
  	  					});
  					});
  					
  				/* 	$.each(option3.series,function(idx,item){
  						if(idx == 0){
  							item.itemStyle={normal: {color: '#de6942'}};
  							item.markPoint.symbolSize=[90,48];
  						}else if( idx == 1){
  							item.itemStyle={normal: {color: '#7ac8f9'}};
  							item.markPoint.symbolSize=[90,48]
  						}else if(idx == 2){
  							item.itemStyle={normal: {color: '#2e9b66'}};
  							item.markPoint.symbolSize=[90,48]
  						}
  						
  					}); */
  				/* 	option2.series[0].itemStyle={normal: {color: '#7ac8f9'}};
  					
  					option2.series[1].itemStyle={normal: {color: '#de6942'}};
  					option2.series[2].itemStyle={normal: {color: '#ba8bdc'}}; */
  					//option3.series[0].itemStyle={normal: {color: '#52b36e'}};
  					//option3.series[1].itemStyle={normal: {color: '#ebcd63'}};
  				   // option3.series[2].itemStyle={normal: {color: '#de6942'}};
  					option4.series[0].itemStyle={normal: {color: '#e6b600'}};
  					option5.series[0].itemStyle={normal: {color: '#0098d9'}};
  					option5.series[1].itemStyle={normal: {color: '#7ac8f9'}};
  					option5.series[2].itemStyle={normal: {color: '#e6b600'}};
  					console.log(JSON.stringify(option3));
  					main1.setOption(option1);
  					main2.setOption(option2);
  					main3.setOption(option3);
  					main4.setOption(option4);
  					main5.setOption(option5);
  					
  					$("#tableResult").find("span").each(function(idx,item){
  						var result = data[$(item).attr("id")];
  						console.log($(item).attr("id")+"--"+result);
  						var name = $(item).attr("id");
  						if($(item).attr("id").indexOf("percent") != -1){
  							name = name.substr("percent".length)+"%";
  						}
  						if($(item).attr("id").indexOf("result") != -1){
  							name = "GR&R/(T/6)%";
  						}
  						$(item).empty();
  						$(item).append(name+"="+result);
  					})
  						
  					
  				  },
  				  error:function(jqXHR, textStatus, errorThrown){
  					  console.log(textStatus);
  				  }
  			  });
  		  }else{
  			  swal("存在未输入的参数,请检查");
  		  }
  		  
  	  });
  		
  		var resizeMainContainer = function () {
  			$("div[id^='main']").each(function(idx,item){
  				$(item).css("width",$(item).parent().width()+"px");
  				$(item).css("height",$(item).parent().height()+"px");
  			});
  			/* document.getElementById('main1').style.width = $('#main1').parent().width() + "px";
  			document.getElementById('main2').style.width = $('#main2').parent().width() + "px";
  			document.getElementById('main4').style.width = $('#main4').parent().width() + "px";
  			document.getElementById('main1').style.height = $('#main1').parent().height()+"px";
  			document.getElementById('main2').style.height = $('#main2').parent().height()+"px";
  			document.getElementById('main4').style.height = $('#main4').parent().height()+"px"; */
  		}; 
  		window.onresize = function () {
  		    //重置容器高宽
  		    resizeMainContainer();
  		     main1.resize();
  		     main2.resize();
  		     main3.resize();
  		     main4.resize();
  		     main5.resize();
  		};
  		 
  		 
  		 
  		
  		
  		
  		
  		
  	  $(".fixed-table-pagination").hide();
//   	  $(".main-wrap").addClass("fold-result");
//   	  $(".main-wrap").addClass("fold-wrap");    
//   	     $(".tree-list").insertBefore(".main-body:first");
//   	     $(".collapse-trigger").insertBefore(".main-body:first");
//   	   $(".grr_result").parent().removeClass("main-body");
  	  });
  </script>
