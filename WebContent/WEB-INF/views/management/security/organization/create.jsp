<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath }/management/security/organization/create" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadRel2Org);">
  <input type="hidden" name="parent.id" value="${parentOrganizationId }"/>
  <div class="pageFormContent" layoutH="58">
    <div class="form-group">
      <label class="control-label col-sm-4">名称：</label>
      <div class="col-sm-6">
        <input type="text" name="name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">优先级：</label>
      <div class="col-sm-6">
        <input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" value="999" maxlength="3" style="width: 80px;"/>
        <span class="info">（越小越靠前）</span>
      </div>
    </div>        
    <div class="form-group">
      <label class="control-label col-sm-4">描述：</label>
      <div class="col-sm-6">
        <textarea name="description" cols="29" rows="3" maxlength="256" class="form-control input-medium textarea-scroll"></textarea>
      </div>
    </div>
  </div>
      
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>