<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
</script>
<form method="post" id="userForm" action="${contextPath}/management/security/user/updateStatus" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${user.id}"/>
  <input type="hidden" name="username" value="${user.username }">
  <div class="pageFormContent" layoutH="58">
    <div class="row">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户状态</label>
      <div class="col-sm-6">
        <select name="status" class="form-control input-medium" >
          <option value="enabled" ${user.status == "enabled" ? 'selected="selected"' : ''}>可用</option>
          <option value="disabled" ${user.status == "disabled" ? 'selected="selected"' : ''}>不可用</option>
        </select>
      </div>
    </div>
  </div>
  </div>
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
