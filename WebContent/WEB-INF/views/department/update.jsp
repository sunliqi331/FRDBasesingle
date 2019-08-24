<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<form method="post" id="departmentForm" action="${contextPath}/department/saveOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadRel2Department);">
       <div class="pageFormContent" layoutH="58">
       <input type="hidden" name="id" value="${department.id}"/>
       <input type="hidden" name="companyid" value="${department.companyid}">
       <input type="hidden" name="department.id" value="${department.department.id}">
       <div class="row">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门编号</label>
          <div id="divOfSn" class="col-sm-6">
            <input id="sn" type="text" name="sn" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${department.sn}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门名称</label>
          <div id="divOfName" class="col-sm-6">
            <input id="name" type="text" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${department.name}"/>
          </div>
        </div>
        <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>负责人</label>
                  <div class="col-sm-6">
                    <select id="principal" name="principal" class="form-control validate[]">
                      <option value="${department.principal}">${department.principal}</option>
                      <c:forEach var="p" items="${users}">
                        <option value="${p.username}">${p.username}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>电话</label>
          <div class="col-sm-6">
            <input type="tel" name="phone" class="form-control input-medium validate[custom[phone],maxSize[11],required] required" maxlength="11" value="${department.phone}"/>
          </div>
        </div><div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门位置</label>
          <div class="col-sm-6">
            <input type="text" name="floor" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${department.floor}"/>
          </div>
    	</div>
    	<div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门所属</label>
          <div class="col-sm-6">
            <input type="radio" name="parentType" id="p_d" value="p_d" /><span>父部门为&nbsp;</span>
            <input type="radio" name="parentType" id="p_f" value="p_f" /><span>父工厂/公司为&nbsp;</span>
          </div>
          
        </div>
    	<div class="form-group" id="parentDepartmentDiv">
          <label for="inputText" class="control-label col-sm-4"><span class="require"></span>&nbsp;</label>
          <div class="col-sm-6">
            <select id="parentDepartment" name="parentDepartment" class="form-control validate[required]">
                      <option value="">请选择</option>
                      <c:forEach var="p" items="${departments}">
                        <option value="${p.id}">${p.name}</option>
                      </c:forEach>
                    </select>
          </div>
        </div>
        <div class="form-group" id="parentFactoryDiv">
          <label for="inputText" class="control-label col-sm-4"><span class="require"></span>&nbsp;</label>
          <div class="col-sm-6">
            <select id="parentFactory" name="parentFactory" class="form-control validate[required]">
                      <option value="">请选择</option>
                      <option value="0">${companyname}(当前公司)</option>
                      <c:forEach var="p" items="${factorys}">
                        <option value="${p.id}">${p.companyname}</option>
                      </c:forEach>
                    </select>
          </div>
        </div>
    </div>
    </div>
  <div class="modal-footer">
    <button id="confirm" type="button" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>

<script type="text/javascript">
var oldSn = $("#sn").val();
var oldName = $("#name").val();
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
	if($("#sn").val()==oldSn){
		$("#divOfSn").find("div.parentFormformID").remove();
		$("#divOfSn").find("div.snformError").remove();
		submitStatus1.length=0;
	}else if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
    	 ajaxTodo("${contextPath}/department/checkSn/"+$("#sn").val(), function(data) {
            checkData(data,$("#sn"),"部门编号不可重复",$("#divOfSn"),$("#departmentForm"),submitStatus1,"sn");
        });
      }
  };
function checkValue1(){
	if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
    	 ajaxTodo("${contextPath}/department/checkSn/"+$("#sn").val(), function(data) {
            checkData1(data,$("#sn"),"部门编号不可重复",$("#divOfSn"),$("#departmentForm"));
        });
      }
  };
 function checkValue2(){
		if($("#name").val()==oldName){
			$("#divOfName").find("div.parentFormformID").remove();
			$("#divOfName").find("div.snformError").remove();
			submitStatus2.length=0;
		}else if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/department/checkName/"+$("#name").val(), function(data) {
            checkData(data,$("#name"),"部门名称不可重复",$("#divOfName"),$("#departmentForm"),submitStatus2,"name");
        });
      }
  };
function checkValue3(){
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/department/checkName/"+$("#name").val(), function(data) {
            checkData1(data,$("#name"),"部门名称不可重复",$("#divOfName"),$("#departmentForm"));
        });
      }
  }; 
  $("#sn").keyup(checkValue);
  $("#name").keyup(checkValue2);
  $("#confirm").click(function(){
		 if(submitStatus1.length>0){
		    	checkValue1();
		 }else if(submitStatus2.length>0){
	 	    	checkValue3();
		 }else{
		        $("#departmentForm").submit();
		 }
});

  $(document).ready(function(){
	  	if("${department.department.id}"!=""){
	  		$("#p_d").attr("checked","true");
	  		$("#parentDepartmentDiv").show();
			$("#parentFactoryDiv").hide();
	  	}else{
	  		$("#p_f").attr("checked","true");
	  		$("#parentFactoryDiv").show();
      		$("#parentDepartmentDiv").hide();
	  	}
	  	$("#parentDepartment").find("option[value = '"+"${department.department.id}"+"']").attr("selected","selected");
	  	$("#parentFactory").find("option[value = '"+"${department.factoryid}"+"']").attr("selected","selected");
		if ("${department.factoryid}"=="") {
			$("#parentFactory").find("option[value = '0']").attr("selected","selected");
		}
		$("input[type=radio]").click(function(){
	      	var val = $(this).val();
	      	if(val == 'p_d'){
	      		$("#parentDepartmentDiv").show();
	      		$("#parentFactoryDiv").hide();
	      	}else{
	      		$("#parentFactoryDiv").show();
	      		$("#parentDepartmentDiv").hide();
	      	}
	      })
		});
</script>