<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath }/management/security/organization/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadRel2Org);">
  <input type="hidden" name="id" value="${organization.id }"/>
  <input type="hidden" name="parent.id" value="${organization.parent.id }"/>
  <div class="pageFormContent" layoutH="58">
    <div class="form-group">
      <label class="control-label col-sm-4">父组织：</label>
      <div class="col-sm-6">
        <input type="hidden" name="parent.id" value="${organization.parent.id }" />
        <input type="text" name="parent.name" readonly="readonly" class="input-medium validate[required,maxSize[64]] required" value="${organization.parent.name }" style="width: 140px;"/>
        <a class="btnLook" href="${contextPath}/management/security/organization/lookupParent/${organization.id}" lookupGroup="parent" mask="true" title="更改父组织" width="400">查找带回</a>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">名称：</label>
      <div class="col-sm-6">
        <input type="text" name="name" class="input-medium validate[required,maxSize[64]] required" maxlength="64" value="${organization.name }" style="width: 140px;"/>
        <span class="info">（越小越靠前）</span>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">优先级：</label>
      <div class="col-sm-6">
        <input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" value="${organization.priority }" maxlength="3" style="width: 140px;"/>
        <span class="info">（越小越靠前）</span>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述：</label>
      <div class="col-sm-6">
        <textarea name="description" cols="29" rows="3" maxlength="256" class="form-control input-medium textarea-scroll">${organization.description }</textarea>
      </div>
    </div>
  </div>
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>