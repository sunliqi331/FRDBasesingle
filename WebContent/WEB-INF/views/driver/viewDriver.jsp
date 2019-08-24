<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<div class="pageContent">
<form method="post" action="#" class="form form-horizontal" >
   <div class="pageFormContent" layoutH="58">   
      <div class="row mes_see"> 
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备编号</label>
          <div id="divOfSn" class="col-sm-6">
            <input type="text" id="sn" name="sn" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.sn}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.name}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备类型</label>
          <div class="col-sm-6">
          <input type="text" name="mesDrivertype.id" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.mesDrivertype.typename}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备型号</label>
          <div class="col-sm-6">
            <input type="text" name="modelnumber" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.modelnumber}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">品牌</label>
          <div class="col-sm-6">
            <input type="text" name="brand" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.brand}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">出厂日期</label>
          <div class="col-sm-6">
            <input type="text" name="brand" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" readonly="readonly" value="${driver.leavefactorydate}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">描述</label>
          <div class="col-sm-6">
            <textarea name="description" class="form-control input-medium textarea-scroll" cols="29" rows="3" readonly="readonly" maxlength="256">${driver.description }</textarea>
          </div>
        </div>
       </div>
     </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	//alert("1");
  $(".mes_see .control-label").append("：");	
	
});
</script>
