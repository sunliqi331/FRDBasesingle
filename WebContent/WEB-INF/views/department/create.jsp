<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<form method="post" id="departmentForm" action="${contextPath}/department/saveOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadRel2Department);">
       <div class="pageFormContent" layoutH="58">
       <input type="hidden" name="id" value="${department.id}"/>
       <input type="hidden" name="companyid" value="${companyId}">
       <div class="row"> 
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门编号</label>
          <div id="divOfSn" class="col-sm-6">
            <input id="sn" type="text" name="sn" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门名称</label>
          <div id="divOfName" class="col-sm-6">
            <input id="name" type="text" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
        <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>负责人</label>
                  <div class="col-sm-6">
                    <select id="principal" name="principal" class="form-control validate[required]">
                      <option value="">请选择</option>
                      <c:forEach var="p" items="${users}">
                        <option value="${p.username}">${p.username}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>电话</label>
          <div class="col-sm-6">
            <input type="tel" name="phone" class="form-control input-medium validate[custom[phone],maxSize[11],required] required" maxlength="11"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门位置</label>
          <div class="col-sm-6">
            <input type="text" name="floor" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
    	</div>
    	<div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门所属</label>
          <div class="col-sm-6">
            <input type="radio" name="parentType" value="p_d" checked="checked" /><span>父部门为&nbsp;</span>
            <input type="radio" name="parentType" value="p_f" /><span>父工厂/公司为&nbsp;</span>
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
                      <option value="0">${currentCompany.companyname}(当前公司)</option>
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
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
	if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
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
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/department/checkNameNew?name="+($("#name").val()), function(data) {
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
	$("#parentDepartmentDiv").show();
	$("#parentFactoryDiv").hide();
	
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