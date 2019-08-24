<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	  jQuery("#formID").validationEngine();
});
</script>
<form method="post" id="formID" action="${contextPath}/register/savePassword" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${user.id}"/>
  <input type="hidden" name="username" value="${user.username}"/>
	<div class="pageFormContent" layoutH="58">
     <div class="row">
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">请输入原密码</label>
			<div class="col-sm-6">
				<input type="password" id="oldPassword" name="oldPassword"
					class="form-control input-medium validate[required,minSize[6],maxSize[16]] required"
					maxlength="16" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">设置新密码</label>
			<div class="col-sm-6">
				<input type="password" id="pw1" name="password"
					class="form-control input-medium validate[required,minSize[6],maxSize[16]] required"
					maxlength="16" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">确认新密码</label>
			<div class="col-sm-6">
				<input type="password" id="pw2" name="passwordagain"
					class="form-control input-medium validate[required,equals[pw1]] required"
					maxlength="16" />
			</div>
		</div>
	</div>
	</div>
	<div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>