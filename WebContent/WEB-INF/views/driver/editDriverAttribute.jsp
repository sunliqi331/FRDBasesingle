<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

  <form method="post" id="proForm" action="${contextPath}/drivertype/saveDriverTypeProperty/${mesDrivertypeId}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      <div class="pageFormContent" layoutH="58">
      <input type="hidden" name="id" value="${mdtp.id }">
      <input type="hidden" id="mesDriverTypeId" value="${mesDrivertypeId}">
      <div class="row">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型属性号</label>
          <div id="divOfId" class="col-sm-6">
            <input type="text"id="propertykeyid" name="propertykeyid" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${mdtp.propertykeyid}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型属性名</label>
          <div id="divOfName" class="col-sm-6">
            <input type="text" id="propertyname" name="propertyname" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${mdtp.propertyname}"/>
          </div>
        </div>
        <%-- <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>数据类型</label>
          <div class="col-sm-6">
          <select id="chooseDataType" name="datatype" class="form-control validate[required] required searchtext">
                <option value="">请选择数据类型</option>
               <c:forEach var="p" items="${datatype}"> 
                   <option value="${p.name}">${p.name}</option>
               </c:forEach>
            </select>
        </div>
        </div>
        <div id="unitsHide" class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>单位</label>
          <div class="col-sm-6">
          <select id="chooseUnits" name="units" class="form-control validate[required] required searchtext">
                <option value="">请选择单位</option>
               <c:forEach var="p" items="${units}"> 
                   <option value="${p.name}">${p.name}</option>
               </c:forEach>
            </select>
        </div>
        </div> --%>
      </div>
      </div>
      <div class="modal-footer">
        <button type="button" id="Btn" class="btn btn-primary">确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>

<script type="text/javascript">
var oldName = $("#propertyname").val();
var oldId = $("#propertykeyid").val();
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
	if($("#propertyname").val()==oldName){
		$("#divOfName").find("div.parentFormformID").remove();
		$("#divOfName").find("div.snformError").remove();
		submitStatus1.length=0;
	}else if($("#propertyname").val()!=""&&$.trim($("#propertyname").val()) != ''){
    	 ajaxTodo("${contextPath}/drivertype/checkProname/"+$("#propertyname").val()+"/"+$("#mesDriverTypeId").val(), function(data) {
            checkData(data,$("#propertyname"),"属性名不可重复",$("#divOfName"),$("#proForm"),submitStatus1,"name");
        });
      }
  };
function checkValue1(){
	if($("#propertyname").val()!=""&&$.trim($("#propertyname").val()) != ''){
    	 ajaxTodo("${contextPath}/drivertype/checkProname/"+$("#propertyname").val()+"/"+$("#mesDriverTypeId").val(), function(data) {
            checkData1(data,$("#propertyname"),"属性名不可重复",$("#divOfName"),$("#proForm"));
        });
      }
  };
 function checkValue2(){
	 if($("#propertykeyid").val()==oldId){
			$("#divOfId").find("div.parentFormformID").remove();
			$("#divOfId").find("div.snformError").remove();
			submitStatus2.length=0;
		}else if($("#propertykeyid").val()!=""&&$.trim($("#propertykeyid").val()) != ''){
    	ajaxTodo("${contextPath}/drivertype/checkProid/"+$("#propertykeyid").val()+"/"+$("#mesDriverTypeId").val(), function(data) {
            checkData(data,$("#propertykeyid"),"属性号不可重复",$("#divOfId"),$("#proForm"),submitStatus2,"id");
        });
      }
  };
function checkValue3(){
	 if($("#propertykeyid").val()!=""&&$.trim($("#propertykeyid").val()) != ''){
    	ajaxTodo("${contextPath}/drivertype/checkProid/"+$("#propertykeyid").val()+"/"+$("#mesDriverTypeId").val(), function(data) {
            checkData1(data,$("#propertykeyid"),"属性号不可重复",$("#divOfId"),$("#proForm"));
        });
      }
  }; 
  $("#propertyname").keyup(checkValue);
  $("#propertykeyid").keyup(checkValue2);
$("#Btn").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else if(submitStatus2.length>0){
 	    	checkValue3();
	 }else{
	        $("#proForm").submit();
	 }
});
$(document).ready(function(){
	$("#chooseDataType").find("option[value = '"+"${mdtp.datatype}"+"']").attr("selected","selected");
  	$("#chooseUnits").find("option[value = '"+"${mdtp.units}"+"']").attr("selected","selected");
	
});
</script>