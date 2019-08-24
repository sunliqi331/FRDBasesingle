<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
jQuery("#userForm").validationEngine();
});
</script>
<form method="post" id="userForm" action="${contextPath}/management/security/user/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${user.id}"/>
  <div class="pageFormContent" layoutH="58">
    <div class="row">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户名</label>
      <div class="col-sm-6">
        <input type="text" name="username" class="form-control input-medium validate[required,maxSize[25]] required" maxlength="25" readonly="readonly" value="${user.username}"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>手机号</label>
      <div id="divOfPhone" class="col-sm-6">
       <input name="phone" id="phone" type="tel" class="form-control input-medium validate[required,custom[phone],maxSize[11],minSize[11]] required" maxlength="11" value="${user.phone}"
       onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
      </div>
    </div>  
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>邮箱</label>
      <div id="divOfEmail"class="col-sm-6">
        <input type="text" id="email" name="email" class="form-control input-medium validate[custom[email],maxSize[128]] required"  maxlength="128" value="${user.email}"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">真实姓名</label>
      <div class="col-sm-6">
        <input type="text" name="realname" class="form-control input-medium validate[maxSize[16]]" maxlength="16" value="${user.realname}"/>
      </div>
    </div>
    <%-- <c:set var="currentUser">
    	<shiro:principal type="java.lang.String"/>
    </c:set>
    <c:if test="${currentUser == 'manage' }">
	    <div class="form-group">
	      <label for="inputText" class="control-label col-sm-4">重置用户密码</label>
	      <div class="col-sm-6">
	        <a id="resetPwd" class="btn btn-success">重置密码</a>
	      </div>
	    </div>
    </c:if> --%>
    
<!--     <div class="form-group"> -->
<!--       <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户状态</label> -->
<!--       <div class="col-sm-6"> -->
<!--         <select name="status" class="form-control input-medium" > -->
<%--           <option value="enabled" ${user.status == "enabled" ? 'selected="selected"' : ''}>可用</option> --%>
<%--           <option value="disabled" ${user.status == "disabled" ? 'selected="selected"' : ''}>不可用</option> --%>
<!--         </select> -->
<!--       </div> -->
<!--     </div> -->
  </div>
  </div>
  <div class="modal-footer">
    <button type="button" id="updateBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script type="text/javascript">
var submitStatus1 = new Array();
var submitStatus2 = new Array();
var oldPhone = $("phone").val();
var oldEmail = $("email").val();
function checkValue(){
	if($("#phone").val() == oldPhone){
		$("#divOfPhone").find("div.parentFormformID").remove();
		$("#divOfPhone").find("div.snformError").remove();
		submitStatus2.length=0;
	}else if($("#phone").val()!=""&&$.trim($("#phone").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkPhone/"+$("#phone").val(), function(data) {
            checkData(data,$("#phone"),"手机号不可重复",$("#divOfPhone"),$("#userForm"),submitStatus1,"phone");
        });
      }
  };
function checkValue1(){
	if($("#phone").val()!=""&&$.trim($("#phone").val()) != ''){
    	ajaxTodo("${contextPath}/register/checkPhone/"+$("#phone").val(), function(data) {
            checkData1(data,$("#phone"),"手机号不可重复",$("#divOfPhone"),$("#userForm"));
        });
      }
  }; 
  function checkValue2(){
		if($("#email").val()!=""&&$.trim($("#email").val()) != ''){
	    	ajaxTodo("${contextPath}/register/checkEmail?email="+$("#email").val(), function(data) {
	            checkData(data,$("#email"),"邮箱不可重复",$("#divOfEmail"),$("#userForm"),submitStatus2,"email");
	        });
	      }
	  };
	function checkValue3(){
		if($("#email").val()!=""&&$.trim($("#email").val()) != ''){
	    	ajaxTodo("${contextPath}/register/checkEmail?email="+$("#email").val(), function(data) {
	            checkData1(data,$("#email"),"邮箱不可重复",$("#divOfEmail"),$("#userForm"));
	        });
	      }
	  };
  $("#phone").blur(checkValue);
  $("#email").blur(checkValue2);
$("#updateBtn").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else if(submitStatus2.length>0){
	    	checkValue3();
	 }else{
	        $("#userForm").submit();
	 }
});
/* $("#resetPwd").click(function(){
	var obj = $("#userForm").serializeObject();
	obj.password = "${defaultPwd}";
	console.log(obj);
	$.ajax({
		url:"${contextPath}/management/security/user/update",
		dataType:"JSON",
		type:"POST",
		data:obj,
		success:function(data){
			if(data.statusCode == 200)
				swal("重置成功");
			else
				swal("重置失败");
		} 
	});
}); */
</script>