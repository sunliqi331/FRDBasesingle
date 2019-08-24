<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>


<form method="post" id="pointForm" action="${contextPath}/mesPoints/addOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
       <div class="pageFormContent" layoutH="58">
    <div class="row">
      <div class="form-group">
	      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>选择网关</label>
	      <div class="col-sm-6">
            <select name="mesPointGateway.id" class="form-control validate[required] required searchtext" id="mesPointGateway">
            	<option value="">请选择网关</option>
               <c:forEach var="p" items="${mesPointGateway}"> 
                   <option value="${p.id}">${p.name}</option>
               </c:forEach>
            </select>
	      </div>
	    </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点Id</label>
          <div id="divOfCodekey" class="col-sm-6">
            <input type="text" id="codekey" name="codekey" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点名称</label>
          <div id="divOfName" class="col-sm-6">
            <input type="text" id="name" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
	    <div class="form-group">
	      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>选择测点类型</label>
	      <div class="col-sm-6">
            <select id="chooseMespointType" name="mesPointType.id" class="form-control validate[required] required searchtext">
            <option value="">请选择测点类型</option>
               <c:forEach var="p" items="${mesPointType}"> 
                   <option value="${p.id}">${p.name}</option>
               </c:forEach>
            </select>
	      </div>
	    </div>
	  
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>数据类型</label>
          <div class="col-sm-6">
          <select name="datatype" class="form-control validate[required] required searchtext">
                <option value="">请选择数据类型</option>
               <c:forEach var="p" items="${datatype}">
                   <option value="${p.name}">${p.name}</option>
               </c:forEach>
            </select>
        </div>
        </div>
         <div id="unitsHide" class="form-group">
          <label for="inputText" class="control-label col-sm-4"></span>单位</label>
          <div class="col-sm-6">
          <select id="chooseUnits" name="unitsId" class="form-control validate[required] required searchtext">
               <option value="${p.name}" >请选择单位</option>
               <c:forEach var="p" items="${units}" varStatus="idx" > 
                   <option value="${p.id}" >${p.name} </option>
               </c:forEach>
            </select>
        </div>
        </div>
   </div>
    </div>
  <div class="modal-footer">
    <button type="button" id="confirm" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>

<script type="text/javascript">
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
	if($("#codekey").val()!=""&&$.trim($("#codekey").val()) != ''){
		  
		  var gateway = $("select[name='mesPointGateway.id']").val();//获取网关Id
		  var codekey = $("#codekey").val();
		  if((gateway==null || gateway==undefined || gateway == "") || (codekey==null || codekey==undefined || codekey == "")){
			  return false;
		  }else{
			  ajaxTodo("${contextPath}/mesPoints/checkCodekeyNew?mesPointGateway="+gateway+"&codekey="+codekey, function(data) {
		            checkData(data,$("#codekey"),"测点Id不可重复",$("#divOfCodekey"),$("#pointForm"),submitStatus1,"codekey");
		      });
		  }
      }
  };
function checkValue1(){
	if($("#codekey").val()!=""&&$.trim($("#codekey").val()) != ''){
    	 ajaxTodo("${contextPath}/mesPoints/checkCodekey/"+$("#codekey").val(), function(data) {
            checkData1(data,$("#codekey"),"测点Id不可重复",$("#divOfCodekey"),$("#pointForm"));
        });
      }
  };
 function checkValue2(){
	 var mesPointGateway =$("#mesPointGateway").val();
	 if(mesPointGateway){
		 
		 var name =$("#name").val();
		 var temp=mesPointGateway+","+name;
		 if(name){
		    	ajaxTodo("${contextPath}/mesPoints/checkNameGateway/"+temp, function(data) {
		            checkData(data,$("#name"),"测点名称不可重复",$("#divOfName"),$("#pointForm"),submitStatus2,"name");
		        });
		  }else{
			  return false;
		  }
	 }else{
		 checkData( {0: 0},$("#name"),"请先选择网关",$("#divOfName"),$("#pointForm"),submitStatus2,"name");
	 }
	
  };
function checkValue3(){
	 var mesPointGateway =$("#mesPointGateway").val();
	 if(mesPointGateway){
		 
		 var name =$("#name").val();
		 var temp=mesPointGateway+","+name;
		 if(name){
		    	ajaxTodo("${contextPath}/mesPoints/checkNameGateway/"+temp, function(data) {
		            checkData(data,$("#name"),"测点名称不可重复",$("#divOfName"),$("#pointForm"),submitStatus2,"name");
		        });
		  }else{
			  return false;
		  }
	 }else{
		 checkData( {0: 0},$("#name"),"请先选择网关",$("#divOfName"),$("#pointForm"),submitStatus2,"name");
	 }
	
  }; 
  
  /*选中网关标签关联事件*/
  $("select[name='mesPointGateway.id']").change(function(){  
	  checkValue();
//	  checkValue2();
  });   
  $("#codekey").keyup(checkValue);
//  $("#name").keyup(checkValue2);
  $("#confirm").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else if(submitStatus2.length>0){
 	    	checkValue3();
	 }else{
	        $("#pointForm").submit();
	 }
});
$("#chooseMespointType").change(function (){
	if($("#chooseMespointType").val()==12 || $("#chooseMespointType").val()==21){
		$("#unitsHide").hide();
		$("#chooseUnits").attr("disabled","true");
	}else{
		$("#unitsHide").show();
		$("#chooseUnits").removeAttr("disabled");
	}
})
</script>