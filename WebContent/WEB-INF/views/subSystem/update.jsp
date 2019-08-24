<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="pageContent">
<form method="post" action="${contextPath}/subsys/saveOrUpdate" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${companyinfo.id}"/>
       <div class="pageFormContent" layoutH="58">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">子系统名称</label>
          <div class="col-sm-6">
            <input type="text" name="sysname" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${subsysteminfo.sysname}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">价格</label>
          <div class="col-sm-6">
            <input type="text" name="price" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.price}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">URL地址</label>
          <div class="col-sm-6">
            <input type="text" name="url" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.url}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">子系统说明</label>
          <div class="col-sm-6">
            <input type="text" name="description" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.description}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">图标名称</label>
          <div class="col-sm-6">
            <input type="text" name="createtime" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.createtime}"/>
          </div>
        </div>
         <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">状态</label>
          <div class="col-sm-6">
            <input type="text" name="picname" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.picname}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">公司创建时间</label>
          <div class="col-sm-6">
            <input type="text" name="status" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.status}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">进入许可</label>
          <div class="col-sm-6">
            <input type="text" name="limit" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.limit}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">默认角色</label>
          <div class="col-sm-6">
            <input type="text" name="defaultrole" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${subsysteminfo.defaultrole}"/>
          </div>
        </div>
    </div>
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</div>