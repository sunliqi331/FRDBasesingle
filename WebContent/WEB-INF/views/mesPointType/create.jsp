<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="pageContent">
<form method="post" action="${contextPath}/mesPointType/addOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
       <div class="pageFormContent" layoutH="58">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点类型</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>key</label>
          <div class="col-sm-6">
            <input type="text" name="pointtypekey" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
    </div>
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</div>