<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
jQuery("#userForm").validationEngine();
});
</script>
<form method="post" id="userForm" action="${contextPath }/management/security/user/create" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <div class="pageFormContent" layoutH="58">
   <div class="row">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户名</label>
      <div class="col-sm-6">
        <input type="text" name="username" class="form-control input-medium validate[required,custom[onlyNumAndLet],maxSize[25]] required" maxlength="25"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputPassword" class="control-label col-sm-4"><span class="require">*&nbsp;</span>登录密码</label>
      <div class="col-sm-6">
        <input type="password" name="password" class="form-control input-medium validate[required,minSize[6],maxSize[16]] required" maxlength="16" alt="字母、数字、下划线 6-16位"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>手机号</label>
      <div id="divOfPhone" class="col-sm-6">
        <input name="phone" id="phone" type="tel" class="form-control input-medium input_text validate[required,maxSize[11],minSize[11]]" maxlength="11"
         onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>邮箱</label>
      <div id="divOfEmail" class="col-sm-6">
      <input type="text" id="email" name="email" class="form-control input-medium validate[custom[email],maxSize[128]] required" maxlength="128"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">真实姓名</label>
      <div class="col-sm-6">
        <input type="text" name="realname" class="form-control input-medium validate[maxSize[16]]" maxlength="16"/>
      </div>
<!--     </div> -->
<!--     <div class="form-group"> -->
<!--       <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户状态</label> -->
<!--       <div class="col-sm-6"> -->
<!--         <select name="status" class="form-control"> -->
<!--           <option value="enabled">可用</option> -->
<!--           <option value="disabled">不可用</option> -->
<!--         </select> -->
<!--       </div> -->
    </div>
  </div>
  </div>
  <div class="modal-footer">
    <button type="button" id= "createBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script type="text/javascript">
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
	if($("#phone").val()!=""&&$.trim($("#phone").val()) != ''){
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
$("#createBtn").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else if(submitStatus2.length>0){
	    	checkValue3();
	 }else{
	        $("#userForm").submit();
	 }
});
</script>