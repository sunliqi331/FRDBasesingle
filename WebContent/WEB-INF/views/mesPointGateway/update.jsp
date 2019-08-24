<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
	var temp = /[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/;
$("#editBtn").click(function(){
	var macs = new Array();
	macs = $("#mac").val().split("-");
	if(macs.length != 6){
		swal("错误","输入的mac地址格式不正确，请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error");
		return false;
	}else{
	 for (var s=0; s<6; s++) {
	        var temp = parseInt(macs[s],16);
	        if(isNaN(temp)){
	        	swal("错误","输入的mac地址格式不正确，请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error"); 
	        	return false;
	        }else{
	        if(temp < 0 || temp > 255){
	        	swal("错误","输入的mac地址格式不正确，请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error");  
	        	return false;
			     }else{
			    		if(s==5){
			        $("#editForm").submit();
			    		} 
			     }
	        }
	    }
	}
});
</script>

<form method="post" id="editForm" action="${contextPath}/mesPointGateway/saveOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${pointGateway.id}"/>
       <div class="pageFormContent" layoutH="58">
      <div class="row"> 
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>MAC地址</label>
          <div class="col-sm-6">
            <input type="text" name="mac" id="mac" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${pointGateway.mac}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>网关名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${pointGateway.name}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>验证码</label>
          <div class="col-sm-6">
            <input type="text" name="macCode" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" value="${pointGateway.macCode}"/>
          </div>
        </div>
		<div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>虚拟网关</label>
          <div class="col-sm-6">
            <select name="virtualFlag" class="form-control">
            
            	<option value="NO" <c:if test="${pointGateway.virtualFlag == 'NO'}">selected</c:if> >否</option>
            	<option value="YES" <c:if test="${pointGateway.virtualFlag == 'YES'}">selected</c:if>>是</option>
            </select>
          </div>
        </div>
    </div>
    </div>
  <div class="modal-footer">
    <button type="button" id="editBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
