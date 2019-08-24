<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath}/subsys/saveOrUpdate" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${subsysteminfo.id}"/> 
  <div class="pageFormContent" layoutH="58">
  	<div class="form-group">
		<label for="inputText" class="control-label col-sm-4">图标</label>
		<div class="col-sm-6">
			<input class="form-control input-medium validate[required]"
			type="file" name="file" id="" multiple="true" accept="image/*">
		</div>
	</div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">图标名称</label>
      <div class="col-sm-6">
        <input type="text" name="picname" class="form-control input-medium validate[required,maxSize[30]] required" maxlength="30" value="${subsysteminfo.picname}"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">子系统名称</label>
      <div class="col-sm-6">
        <input type="text" name="sysname" class="form-control input-medium validate[required,maxSize[30]] required" maxlength="30" value="${subsysteminfo.sysname}"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">子系统说明</label>
      <div class="col-sm-6">
        <textarea rows="" cols="" name="description" class="form-control input-medium validate[required,maxSize[30]] required" maxlength="30">${subsysteminfo.description}</textarea>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">价格</label>
      <div class="col-sm-6">
        <input type="text" name="price" id="pricell" class="form-control input-medium validate[required]" value="${subsysteminfo.price}"/>
      </div>
    </div>
   <div class="form-group">
   	<label for="inputText" class="control-label col-sm-4">状态</label>
		<div class="col-sm-6">
			<select name="status" class="form-control input-medium">
				<option value="0"
					${subsysteminfo.status == "0" ? 'selected="selected"' : ''}>未发布</option>
				<option value="1"
					${subsysteminfo.status == "1" ? 'selected="selected"' : ''}>已发布</option>
			</select>
		</div>
   </div>
  </div>
  <div class="modal-footer">
	<button type="submit" class="btn btn-primary">确定</button>
	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script>
jQuery(document).ready(function(){
	bindKeyEvent($("#pricell"));
});	
</script>