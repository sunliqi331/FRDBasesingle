<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
.main-body{
  background: #383838;
/*   background: -webkit-linear-gradient(top bottom, #79dddd , #4ad0d0); /* Safari 5.1 - 6.0 */ */
/*   background: -o-linear-gradient(bottom top, #79dddd , #4ad0d0); /* Opera 11.1 - 12.0 */  */
/*   background: -moz-linear-gradient(bottom top, #79dddd , #4ad0d0); /* Firefox 3.6 - 15 */ */
/*   background: linear-gradient(to bottom top, #79dddd , #4ad0d0);  */
  
  }
</style>
      <div class="result_left" layouth="5" id="jbsxBox2personnelTree" tabindex="2" style="overflow: hidden; outline: none;">
        <div class="result">
<!--            <a class="clear_menu" style="color: #51ace1;cursor: pointer;"><i class="fa fa-arrow-left"></i> 移出左侧菜单</a> -->
<!--            <a class="clear_menu_out" style="color: #51ace1;cursor: pointer; display: none;"><i class="fa fa-arrow-right"></i> 移回左侧菜单</a> -->
            <div class="search_header" style="background: none;color: #bbb;">
              <i class="fa fa-line-chart"></i> CG分析条件  
            </div>
          
		   <form class="form-inline" method="post" action="${contextPath}/companyRole/data" data-target="table" onsubmit="return navTabSearch(this)">
             
              <div class="form-group">
                 <label for="mesDriver" class="searchtitleWhite">量具名称</label>
                 <input type="text" class="searchtext" id="mesDriver" readonly="readonly"/>
              </div> 
              <div class="form-group">
                 <label for="product" class="searchtitleWhite">零件名称</label>
                 <input type="text" class="searchtext" id="product"  readonly="readonly"/>
              </div> 
              <div class="form-group">
                 <label for="productSn" class="searchtitleWhite" >零件ID</label>
                 <input type="text" class="searchtext" id="productSn" name="productSn" readonly="readonly"/> 
              </div>
              <div class="form-group">
                 <label for="procedureProperty" class="searchtitleWhite">特性</label>
                 <input type="text" class="searchtext" id="procedureProperty" readonly="readonly"/> 
              </div> 
                        
			  <div class="form-group">
                 <label for="standardValue" class="searchtitleWhite">名义值</label>
                 <input type="text" class="searchtext" id="standardValue" name="search_LIKE_sn"/> 
              </div>
              <div class="form-group">
                 <label for="minValue" class="searchtitleWhite">下公差</label>
                 <input type="text" class="searchtext" id="minValue" name="minValue"/> 
              </div>
              <div class="form-group">
                 <label for="maxValue" class="searchtitleWhite">上公差</label>
                 <input type="text" class="searchtext" id="maxValue" name="maxValue"/> 
              </div>
              <div class="form-group">
                 <label for="actualValue" class="searchtitleWhite">实测值</label>
                 <input type="text" class="searchtext" id="actualValue" name="actualValue"/> 
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
				   <div style="position: absolute; left:60px; top:-8px; background: #383838; color: #bbb; height: 15px;">测量次数</div>
              <div class="form-group" style="padding: 10px 15px;">
                 <!-- <label for="inputText" class="searchtitle">测量次数</label> -->
                 <div class="pass" style="width:50px">
				
					<label><input type="radio" disabled  name="count" class="input_pass validate[required]"
						id="adopt" value="25" /><span style="color:white;"> 25</span></label>
				
			</div>
			<div class="nopass" style="width:50px">
			
					<label><input type="radio" disabled  name="count" class="input_nopass validate[required]"
						id="notthrough" value="50" /><span style="color:white;"> 50</span></label>
			
			</div> 
			</div>
			</div>
			<div style="position: relative;border: 1px solid #ffffff; margin-top: 8px; ">
				   <div style="position: absolute; left:60px; top:-8px; background: #383838; color: #bbb; height: 15px;">统计标准</div>
			<div class="form-group" style="padding: 10px 15px;">
			<!-- <label for="inputText" class="searchtitle" style="float:left; margin-top:7px;">统计标准</label> -->
                 <div class="pass" style="width:50px">
				
					<label><input type="radio" disabled  name="scope" class="input_pass validate[required]"
						id="adopt" value="4" /><span style="color:white;"> 4σ</span></label>
				
			</div>
			<div class="nopass" style="width:50px">
			
					<label><input type="radio" disabled name="scope" class="input_nopass validate[required]"
						id="notthrough" value="6"  /><span style="color:white;"> 6σ</span></label>
			
			</div> 
              </div>
              </div>
            
              <div class="clearfix"></div>
              <div style="margin-top: 5px"><a id="analyzeBtn" class="btn btn-info " style="padding: 2px">开始评定</a> 			   
			   <a id="saveAsImage" class="btn btn-default " >保存图片</a>
               <a id="back" class="btn btn-default " >返回</a></div>
			   
<!--               <button type="submit" class="btn btn-info  btn-search1">打印</button>  -->
<!--               <button type="submit" class="btn btn-info  btn-search1">导出</button> -->
          </form> 
        </div>

      
      </div>
  
        <div class="result_right">          
            <div id="screenArea" style="width: 40px;height:40px;margin-top:20px;position: absolute;z-index: 9999;">
             <ul class="monitor_large">
               <li ><a href="#"  id="maxScreen" style="display: none"><img src="${contextPath}/styles/img/large.png" width="40"/></a></li>
               <li ><a href="#" id="minScreen" style="display: none"><img src="${contextPath}/styles/img/small.png" width="40"/></a></li>
             </ul>
             </div>
		
	   <table class="table table-striped table-hover text-center result_table" id="_table" data-toggle="table" style="margin-bottom:10px">
	    <thead>
	        <tr>
	            <th  colspan="5" data-field="sn" tabindex="0" style="border-bottom: none;background:url('images/header.png') repeat-x 0 0;border: 1px solid #ccc;">
	                <div class="th-inner text-center" style="color: #bbb;">
	                                                         检测数据
	                </div>
	                <div class="fht-cell">
	                </div>
	            </th>
	        </tr>
	    </thead>
	    <tbody>
	    </tbody>
</table>

    <div class="searchBar" style="margin-top:10px;margin-bottom: 0px;border: none;" >
<!--       <div class="search_header text-center result_title" > -->
<!--          检测件操作者平均值图  -->
<!--       </div> -->
      <div class="result_bg" style="margin:5px;">
        <div id="main2" style="width:800px;height:300px;"></div>
      </div>
      
   </div>
	<div>
		<div class="row cg_count">
			<div class="col-xs-4" style="color:white;">Cg：<span id="Cg"></span></div>
			<div class="col-xs-4" style="color:#11da43;">Cgk：<span id="Cgk"></span></div>
			<div class="col-xs-4" style="color:red;">NG：<span id="NG"></span></div>
		
		</div>
	</div>
      </div>
<div id="cgResultImage" style="display: none"></div>
<script type="text/javascript" src="${contextPath }/js/page/screenChange.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
<script type="text/javascript">


  $(document).ready(function($) {
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

	  var right_width;
 	  if(document.body.clientWidth>767){
 		 right_width=$(".main-body:last").width()-250;  
 	  }
 	  else{
 		 right_width= document.body.clientWidth-30; 
 	  }
 	 $(".result_right").width(right_width);
	  $(".clear_menu").click(function(){
		    $(this).hide();
			$(".clear_menu_out").show();
		  $("#sidebar").width(0).css("z-index","-555");	
		  $(".main-content").css("left","0");
		  if(document.body.clientWidth>767){
		 		 right_width=$(".main-body:last").width()-250;  
		 	  }
		 	  else{
		 		 right_width= document.body.clientWidth-30; 
		 	  }
		  $(".result_right").width(right_width);
		  var instance = echarts.getInstanceByDom("#main2");
		  
	  });
	  $(".clear_menu_out").click(function(){
		  $(this).hide();
		  $(".clear_menu").show();		  
		  $("#sidebar").width(220).css("z-index","99999");
		  $(".main-content").animate({left: '+220px'}, "500");
		 // alert($(".main-body:last").width());
		  if(document.body.clientWidth>767){
		 		 right_width=$(".main-body:last").width()-470;  
		 	  }
		 	  else{
		 		 right_width= document.body.clientWidth-30; 
		 	  }
		  $(".result_right").width(right_width);
	  });
	  $(".breadcrumb li:last").detach();
	$(".breadcrumb").append("<span><span style='padding:0 5px; color:#ccc'>></span>统计分析MSA</span><span><span style='padding:0 5px; color:#ccc'>></span>Cg分析</span>");
	
	//  alert(document.body.clientWidth);
 	  
 		  
 		
 		
 		$("#main2").css("width",right_width-35+"px");
	  $("#saveAsImage").click(function(){
		  html2canvas($(".main-wrap"), {
			    taintTest : false,
			  	height: document.getElementsByName("main-body")[0].scrollHeight+100,
		        onrendered: function(canvas) {
		            Canvas2Image.saveAsPNG(canvas, 1200, 800);
		        }
			}); 
	  });
	  
	  var  backParam = "";
	  $("#back").click(function(){
		  console.log("---"+backParam);
		  window.location.href="${contextPath }/statistics/msa?refresh=true"+backParam;
	  });
	 $("#_table").find("tbody td").each(function(idx,item){
		 console.log(idx+"--"+$(item).text());
	 });
	 var tr;
	 var data = new Array();
	 var dataIndex = new Array();
	 $.each($("#analyzeTable").bootstrapTable("getSelections"),function(idx,item){
		 console.log(item.value);
		 data.push(item.value);
		 dataIndex.push(idx+1);
         console.log("--------------------idx+1-------------------------" + idx+1);
		 if(idx % 5 == 0){
			 tr = $("<tr></tr>"); 
		     $("#_table").find("tbody").append(tr);
		 }
		 tr.append("<td>"+item.value+"</td>");
	 });
	 var myChart = echarts.init(document.getElementById('main2'), 'chalk');
		var option = {
				//backgroundColor:'#e0f0fa',
		
		title: {
                    text: '检测件操作者平均值图 ',
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
		        // saveAsImage: {}
		     }
		 },
		 grid: {
		        left: '3%',
		        right: '40',
		        bottom: '3%',
		        containLabel: true,
		        color:'#fff'
		       
		    },
		 xAxis: {
			 show:true,
		     type: 'category',
		     data: dataIndex,
	        axisLine: {
	            lineStyle: {
	                color: 'red'
	            }
	        }
		},
		 yAxis: {
		     type: 'value',
		     boundaryGap:['20%','20%'],
		     scale:true,
             nameTextStyle:{
                 color:'#fff',
                 
             }
		 },
		
		 series: [
		     {
		         name:'检测数据值',
		         type:'line',   
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
		                    {type: 'average', name: '平均值'}
		                ]
		         }
		     
		     }
		 ]
	};


// 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);
	 
	 
	 
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
	  var productId = $("#productId").val();
	  backParam += "&productId="+productId;
	  $("#product").val(productName);
	  var productProcedureId = $("#productProcedureId").val();
	  backParam += "&productProcedureId="+productProcedureId;
	  if($('#analyzeTable').bootstrapTable('getSelections').length != 0){
		  var productSn = $('#analyzeTable').bootstrapTable('getSelections')[0].productionSn;
		  $("#productSn").val(productSn);
		  console.log("has productionSN=====" + productSn);
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
    
	  $("#analyzeBtn").click(function(){
		  if($("#standardValue").val() != "" && $("#minValue").val() != ""
				  && $("#maxValue").val() != "" && $("#actualValue").val() != "" && $("#productSn").val() != ""){
			  var standardValue = {name:"standardValue",value:$("#standardValue").val()};
			  var minValue = {name:"minValue",value:$("#minValue").val()};
			  var maxValue = {name:"maxValue",value:$("#maxValue").val()};
			  var actualValue = {name:"actualValue",value:$("#actualValue").val()};
			  var productionSn = {name:"productionSn",value:$("#productSn").val()};
			  var scale = {name:"scale",value:$("#scale").val()};
			  var values = new Array();
			 $("#_table").find("tbody td").each(function(idx,item){
				  values.push($(item).text());
			  });
			  var value = {name:"values",value:values}; 
			  var formArray = $("#formID").serializeArray();
			  formArray.push(standardValue);
			  formArray.push(minValue);
			  formArray.push(maxValue);
			  formArray.push(actualValue);
			  formArray.push(productionSn);
			  formArray.push(value);
			  formArray.push(scale);
			  
			  $.ajax({
				  url:"${contextPath}/statistics/analyseCgData",
				  dataType:"JSON",
				  type:"POST",
				  data:formArray,
				  success:function(data){
					  console.log(data);
					  $("#Cg").text(data.Cg);
					  $("#Cgk").text(data.Cgk);
					  $("#NG").text(data.NG);
				  },
				  error:function(jqXHR, textStatus, errorThrown){
					  console.log(textStatus);
				  }
			  });
		  }else{
			  swal("存在未输入的参数,请检查");
		  }
		  
	  });
	//  $(".main-body:first").remove();
// 	 $(".main-wrap").addClass("fold-result");
// 	 $(".main-wrap").addClass("fold-wrap");    
//      $(".tree-list").insertBefore(".main-body:first");
//      $(".collapse-trigger").insertBefore(".main-body:first");

     
	  
	  
	  
  });
  
  
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>  

