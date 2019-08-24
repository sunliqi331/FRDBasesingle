<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <script>
jQuery(document).ready(function(){
  $("#phone").keyup(function(){
	  $("#divOfPhone").find("div.parentFormformID").remove();
	  if($("#phone").val().length == 11){
		  ajaxTodo("${contextPath}/register/checkPhone/"+$("#phone").val(), function(data) {
			    $.each(data, function(idx, item) {
			      if (item != 1) {
			        $("#divOfPhone").find("div.parentFormformID").remove();
			        $("#phone").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 该手机号已注册<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
			        $("#send").attr("disabled", "true");
			        } else {
			          $("#divOfPhone").find("div.parentFormformID").remove();
			          $("#send").removeAttr("disabled");
			          }
			      });
			    });
	    }
  });
  var t = 0, n = 0;
  $("#send").click(function(){
	  if($("#phone").val().length == 11) {
		  if(isNaN($("#phone").val())){
				return;
			 }else{
	 		 ajaxTodo("${contextPath}/register/sendMessage/" + $("#phone").val(), function(){
	 			 });
			 }
	  }
  }); 
  });
</script>
<script type="text/javascript"> 
	var countdown = 60;
	function settime(val) {
		if($("#phone").val().length != 11 && countdown == 60){
			return;
		}
		if(isNaN($("#phone").val())){
			 return;
		 }else{
		if (countdown == 0) {
			val.removeAttribute("disabled");
			val.value = "发送验证码";
			countdown = 60;
			return;
			
		} else {
			val.setAttribute("disabled", true);
			val.value = "重新发送(" + countdown + ")";
			countdown--;
		}
		 }
		setTimeout(function() {
			settime(val)
		}, 1000)
	}
</script>
<script type="text/javascript">
var submitStatus = new Array();
function checkValue(){
    if($("#password").val()!=""&&$.trim($("#password").val()) != ''){
        ajaxTodo("${contextPath}/register/checkPaasword/" + $("#password").val(), function(data) {
        	checkPhone(data,$("#password"),"请输入正确的密码",$("#divOfPassword"),$("#phoneForm"),submitStatus,"password");
        });
      }
}
function checkValue1(){
    if($("#password").val()!=""&&$.trim($("#sn").val()) != ''){
        ajaxTodo("${contextPath}/register/checkPaasword/" + $("#password").val(), function(data) {
        	checkPhone1(data,$("#password"),"请输入正确的密码",$("#divOfPassword"),$("#phoneForm"));
        });
      }
}
$("#password").keyup(checkValue);
$("#confirm").click(function(){
    if(submitStatus.length>0){
    	checkValue1();
    }else{
        $("#phoneForm").submit();
    }
});
function checkPhone(data,checkRegion,msg,msgRegion,submitButton,submitStatus,id){
    $.each(data, function(idx, item) {
        if (item != 1) {
            msgRegion.find("div.parentFormformID").remove();
            msgRegion.find("div.snformError").remove();
            checkRegion.after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -12px;''><div class='formErrorContent'>* "+ msg +"<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
            checkRegion.focus();
            submitStatus.push(id);
            $("#phone").attr("readonly","readonly");
          } else {
            msgRegion.find("div.parentFormformID").remove();
            submitStatus.length=0;
            $("#phone").removeAttr("readonly");
            }
        });
  }
function checkPhone1(data,checkRegion,msg,msgRegion,submitButton){
  $.each(data, function(idx, item) {
      if (item != 1) {
          msgRegion.find("div.parentFormformID").remove();
          msgRegion.find("div.snformError").remove();
          checkRegion.after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -12px;''><div class='formErrorContent'>* "+ msg +"<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
          checkRegion.focus();
          $("#phone").attr("readonly","readonly");
      } else {
          msgRegion.find("div.parentFormformID").remove();
          $("#phone").removeAttr("readonly");
      }
  });
}
</script>

<form id="phoneForm" method="post" action="${contextPath}/register/savePhone" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${user.id}"/>
  <input type="hidden" name="username" value="${user.username}"/>
	<div class="pageFormContent" layoutH="58">
	  <div class="row">
		<div class="form-group">
            <label for="inputText" class="control-label col-sm-4">请输入密码</label>
            <div id="divOfPassword" class="col-sm-6">
                <input type="password" id="password" name="password"
                    class="form-control input-medium validate[required,minSize[6],maxSize[16]] required"
                    maxlength="16" />
            </div>
        </div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">手机号码</label>
			<div class="col-sm-6" id="divOfPhone">
				<input type="text" name="phone" id="phone"
					class="form-control input-medium input_text validate[required,custom[phone],maxSize[11],minSize[11]]"
					maxlength="11" readonly="readonly"
					onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-4">验证码</label>
			<div class="col-sm-3">
				<input type="text" id="code" name="code"
					class="form-control input-medium input_text validate[required] required" maxlength="6"
					onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
			</div>
			<input type="button" class="btn btn-default" id="send" onclick="settime(this)" disabled="disabled"
			 value="发送验证码" />
		</div>
	</div>
	</div>
	<div class="modal-footer">
    <button id="confirm" type="button" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>