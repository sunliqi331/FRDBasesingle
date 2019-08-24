<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
  <div class="pageFormContent form form-horizontal">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">名称：</label>
      <div class="col-sm-7">
        <input type="text" name="name" class="form-control input-large" value="${dataControl.name }" readonly="readonly"/>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">条件：</label>
      <div class="col-sm-7">
        <textarea name="control" rows="12" maxlength="10240" class="form-control textarea-scroll" readonly="readonly">${dataControl.control }</textarea>
      </div>
    </div>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3">描述：</label>
      <div class="col-sm-7">
        <textarea name="description" rows="3" maxlength="256" class="form-control textarea-scroll" readonly="readonly">${dataControl.description }</textarea>
      </div>
    </div>
  </div>
  
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>