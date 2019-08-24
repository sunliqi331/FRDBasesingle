<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <script>
   $("#sendcod").hide();
   $("#sendpassword").hide();
   $("#phonee").hide();
   $("#ver").change(function(){
	   if($("#ver").val()=="codede"){
		   $("#sendcod").show();
		   $("#phonee").show();
		   $("#sendpassword").hide();
	   }else if($("#ver").val()=="passwordd"){
		   $("#sendpassword").show();
		   $("#sendcod").hide(); 
		   $("#phonee").hide();
	   }else{
		   $("#sendcod").hide();
		   $("#sendpassword").hide();
		   $("#phonee").hide();
	   }
   })
   
  function checkPhone(){
	  if($("#phone").val().length == 11){
          ajaxTodo("${contextPath}/personnel/checkPhone/"+$("#phone").val(), function(data) {
                $.each(data, function(idx, item) {
                  if (item != 0) {
                    $("#divOfPhone").find("div.parentFormformID").remove();
                    $("#phone").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 请输入正确的手机号<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    $("#send").attr("disabled", "true");
                    $("#code").attr("readonly", "readonly");
                    } else {
                    $("#divOfPhone").find("div.parentFormformID").remove();
                    $("#code").removeAttr("readonly");
                    $("#send").removeAttr("disabled");
                      }
                  });
                });
        }else{
        	$("#send").attr("disabled", "true");
            $("#code").attr("readonly", "readonly");
        }
  }
  $("#phone").keyup(function(){
      checkPhone();
  });
$("#send").click(function() {
           ajaxTodo("${contextPath}/register/sendMessage/" + $("#phone").val(), function(){})
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
<form method="post" action="${contextPath}/personnel/transferPermission/${user.id}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${user.id}"/>
  <input type="hidden" name="username" value="${user.username}"/>
    <div class="pageFormContent" layoutH="58">
      <div class="row">
        <div class="form-group" id="phonee">
            <label for="inputText" class="control-label col-sm-4">手机号码</label>
            <div class="col-sm-6" id="divOfPhone">
                <input type="text" name="phone" id="phone"
                    class="form-control input-medium input_text validate[required,custom[phone],maxSize[11],minSize[11]]"
                    maxlength="11" 
                    onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
            </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-4">请选择验证方式</label>
        <select id="ver" style="height: 30px; width:150px;margin-left:15px;font-size:15px;border:solid 1px #CCC">
        <option>请选择</option>       
        <option value="passwordd">密码验证</option>
        <option value="codede">验证码验证</option>
        </select>
        </div>
        <div class="form-group" id="sendcod">
            <label class="control-label col-sm-4">验证码</label>
            <div class="col-sm-3">
                <input type="text" id="code" name="code" readonly="readonly"
                    class="form-control input-medium input_text validate[required] required" maxlength="6"
                    onKeyPress="if ((event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
            </div>
            <input type="button" class="btn btn-default" id="send" onclick="settime(this)" disabled="disabled"
             value="发送验证码" />
        </div>
         <div class="form-group" id="sendpassword">
            <label for="inputText" class="control-label col-sm-4">密码</label>
            <div class="col-sm-6" id="divofpassword" >
                <input type="password" name="password" id="password"
                    maxlength="11" 
                    />
            </div>
        </div>
    </div>
    </div>
    <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</form>