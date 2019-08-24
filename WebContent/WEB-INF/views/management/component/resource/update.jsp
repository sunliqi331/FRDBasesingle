<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/> 
<form method="post" action="${contextPath }/management/component/resource/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${resource.id}"/>
  <div class="pageFormContent" style="padding: 0 15px 15px 15px;">
    <div class="form-group">
      <label class="control-label col-sm-4">文件名称</label>
      <div class="col-sm-5">
        <input type="text" name="name" class="input-medium validate[required,maxSize[32]] required" maxlength="32" value="${name }"/>&nbsp;.${resource.type }
      </div>
    </div>
  </div>
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
