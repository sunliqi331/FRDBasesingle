<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<form method="post" action="${contextPath }/management/security/dataControl/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${dataControl.id}"/>
  <div class="pageFormContent">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">名称：</label>
      <div class="col-sm-7">
        <input type="text" name="name" class="form-control input-large validate[required] required" maxlength="32" value="${dataControl.name }" readonly="readonly"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">条件：</label>
      <div class="col-sm-7">
        <textarea name="control" rows="12" maxlength="10240" class="form-control textarea-scroll required validate[required]">${dataControl.control }</textarea>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">描述：</label>
      <div class="col-sm-7">
        <textarea name="description" rows="3" maxlength="256" class="form-control textarea-scroll">${dataControl.description }</textarea>
      </div>
    </div>
  </div>
  
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
