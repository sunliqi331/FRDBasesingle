<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button{
-webkit-appearance: none !important;
 }
</style>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

<!-- <div class="pageContent" > -->
	
	<div>
	<div style="margin-left: 20px;">
		<form action="#" class="form-inline">
            <div class="form-group">
               <label for="inputText" class="searchtitle">报警类型</label>
               <select data-placeholder="请选择类型" id="alarmType" style="width: 150px;" class="form-control searchtext">
	               <c:forEach var="p" items="${itemList}">
	               <option value="${p.value }">${p.value}</option>
               	   </c:forEach>
               </select> 
            </div>
			<div class="form-group" style="margin-left: 40px;">
                 <label for="inputText" class="searchtitle">&nbsp;&nbsp; 报警ID</label>
                 <input type="text" class="form-control searchtext" id="alarmCode" 
                 	style="width: 150px;" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"/>
            </div>
            <br>
			<div class="form-group">
                 <label for="inputText" class="searchtitle">信息</label>
                 <input type="text" class="form-control searchtext" id="alarmInfo" style="margin-left:26px; width: 320px;"/>
            </div>
            <a class="btn btn-default1" href="javascript:void(0);" id="addRelation" style="margin-left: 10px;">
                  <span>添加关联</span>
            </a>
			<input type="hidden" value="${point.id }" id="pointId">
		</form>	
	</div>
	
	<div class="btn-group pull-left" style="margin-top: 10px;margin-left: 10px;margin-bottom: 5px;">
              <shiro:hasPermission name="alarmShow:delete">
	              <a class="btn btn-default1" target="selectedTodo" data-target="relationTable" rel="ids" href="${contextPath }/MesAlarmShow/deleteAlarmRelation" title="确认要删除吗？">
	                <i class="fa fa-remove"></i> 
	                <span>删除关联</span>
	              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="alarmShow:edit">
              	  <form action="#" id="relationFileForm" method="post" hidden="true" enctype="multipart/form-data">
              	  		<input type="file" id="importFile" name="alarmRelationFile">
              	  		<input type="text" name="pointId" value="${point.id }">
              	  </form>
	              <a class="btn btn-default1" href="javascript:void(0);" id="importRelation">
	                <i class="fa fa-upload"></i>
	                <span>导入</span>
	              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="alarmShow:show">
              	  <button id="downloadRelation" hidden="true"></button>
	              <a class="btn btn-default1" href="javascript:void(0);" id="exportRelation">
	                <i class="fa fa-download"></i> 
	                <span>导出</span>
	              </a> 
              </shiro:hasPermission>
            </div>
	
	<div style="margin-left: 10px;margin-right: 10px;">
		<table class="table table-striped" id="relationTable" data-field="relationList" data-url="#">
	        <thead>
	             <tr>
	               <th data-field="Number" width="22" data-align="center">序号</th>
	               <th data-checkbox="true" width="22">
                	   	<input class="cbr checkboxCtrl" type="checkbox">
                   </th>
                   <th data-field="mesPoints.codekey" width="100">测点ID</th>
	               <th data-field="alarmType" width="100">报警类型</th>
                   <th data-field="alarmCode" width="100">报警ID</th>
	               <th data-field="info" width="100">报警信息</th>
	             </tr>
          		</thead>
        </table>
        <iframe id="ifile" style="display:none"></iframe>
  	</div>
  	</div>
  	
    <div class="modal-footer" >
      <button id="close" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>

<script type="text/javascript">
  $(document).ready(function($) {
	  //$("select").chosen({search_contains:true});
	  $("#relationTable").attr("data-url","${contextPath }/MesAlarmShow/initRelationTable/"+$("#pointId").val());
	  $.table.init("relationTable",{});
	  
	  //添加关联关系
	  $("#addRelation").bind("click",function(){
		  alarmType = $("#alarmType").val();
		  alarmCode = $("#alarmCode").val();
		  alarmInfo = $("#alarmInfo").val();
		  if(alarmCode==""||alarmCode==null){
			  swal("请输入报警ID");
			  return false;
		  }
		  if(alarmInfo==""||alarmInfo==null){
			  swal("请输入报警信息");
			  return false;
		  }
		  $.ajax({
	     		url:"${contextPath}/MesAlarmShow/addAlarmRelation",
	     		dataType:"text",
	     		type:"POST",
	     		data:"alarmType="+alarmType+"&alarmCode="+alarmCode+"&alarmInfo="+alarmInfo+"&pointId="+$("#pointId").val(),
	     		success:function(data){
	     			if(data && "ok" == data){
	     				$.table.refreshCurrent();
	     				swal("成功","添加成功!","success");
	     			}else{
	     				swal("错误",data,"error");
	     			}
	     		}
	     	}); 
	  });
	  
	  //导入报警信息模板	  
	  $("#importRelation").bind("click",function(){
		  $("#importFile").click();
	  });
	  
	  $("#importFile").bind("change",function(){
		  var pathArr = $("#importFile").val().split("\\");
		  var fileName = pathArr[pathArr.length-1]; //文件名
		  var fileSuffix = fileName.split(".")[1];  //文件后缀
		  //验证文件后缀是否正确
	      if(fileSuffix!="csv" || fileSuffix=="" || fileSuffix==null){
		  		swal("错误","请选择正确的文件","error");
				return;
		  }
	      var formData = new FormData($("#relationFileForm")[0]);
	      $.ajax({
	     		url:"${contextPath}/MesAlarmShow/importAlarmRelation",
	     		dataType:"text",
	     		type:"POST",
	     		data:formData,
	     		cache: false,
	          	contentType: false,
	          	processData: false,
	     		success:function(data){
	     			if("ok"==data){
	     				uplocadSuccess();
	     			}else{
	     				uplocadError(data);
	     			}
	     		}
	      }); 
	  });
	  
	  function uplocadSuccess(){
		  $.table.refreshCurrent();
		  swal("成功","导入模板成功！","success");
	  }
	  
	  function uplocadError(data){
		  window.setTimeout(function(){
			  if(data=="error"){
				  swal("错误","文件内容格式错误！","error");
			  }else{
				  $.table.refreshCurrent();
				  swal("警告","文件内部格式有误，部分内容导入失败！","warning");
			  }
		  },1000);
	  }
	  
	  
	  $("#exportRelation").click(function(){
		  $("#downloadRelation").click();
	  });
	  
	  //导出报警信息模板
	  $("#exportRelation").bind("click",function(){
		  swal({
	          title: "确定要导出模板吗？",
	          type: "warning",
	          showCancelButton: true,
	          confirmButtonColor: '#DD6B55',
	          confirmButtonText: '确认',
	          cancelButtonText: "取消",
	          closeOnConfirm: true
	        },
	        function(isConfirm){
	          if (isConfirm){
   					$.ajax({
   			     		url:"${contextPath}/MesAlarmShow/exportAlarmRelation",
   			     		dataType:"text",
   			     		type:"POST",
   			     		data:"pointId="+$("#pointId").val(),
   			     		success:function(data){
   			     			if("error"==data){
   				     			swal("错误","模板文件导出出错，请重试!","error");
   			     			}else{
   			     			    var url="${contextPath }/MesAlarmShow/downloadExport/"+data;
				     		    document.getElementById("ifile").src=url;
   			     			}
   			     		}
   			     	}); 
	          }
	        });
		  
	  });
	  
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>


