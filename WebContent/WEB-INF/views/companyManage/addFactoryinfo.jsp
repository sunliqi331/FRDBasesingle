<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%-- <script src="${contextPath}/js/bootstrap.js"></script> --%>
<script
  src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script
  src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<%-- <link href="${contextPath }/styles/css/bootstrap.min.css" rel="stylesheet" type="text/css" /> --%>
<link
  href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css"
  rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<form method="post"
	action="${contextPath }/company/saveOrUpdateFactory/${parentid}"
	class="form form-horizontal"
	onsubmit="return validateCallback(this, dialogReloadRel2Factory);">
	<input type="hidden" name="parentid" value="${parentid}" />
	<div class="pageFormContent" layoutH="58">
	  <div class="row">
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工厂名称</label>
			<div class="col-sm-6">
				<input type="text" name="companyname"
					class="form-control input-medium validate[required,maxSize[30]] required"
					maxlength="30" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工厂地址</label>
			<div class="col-sm-6">
				<input id="address" type="text" name="address"
					class="form-control input-medium validate[required,maxSize[40]] required"
					maxlength="40" value="" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>成立日期</label>
			<div class="col-sm-6">
				<div class="controls input-append date form_datetime" data-date=""
					data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
					<input class="form-control datetime validate[required]" type="text"
						style="background: none;" value="" readonly> <span
						class="add-on"
						style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;"> <i
						class="fa fa-remove"></i>
					</span> <span class="add-on"
						style="position: absolute; bottom:0; right: 15px; padding: 5px 7px;"> <i
						class="fa fa-th"></i></span>
				</div>
				<input type="hidden" id="dtp_input1" value="" name="startdate" />
			</div>
		</div>
	</div>
	</div>
	<div class="modal-footer">
		<button type="submit" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default"
			data-dismiss="modal">关闭</button>
	</div>
</form>
<script>
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
</script>