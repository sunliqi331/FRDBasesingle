<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath }/company/updateCompany/${companyid}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadRel2Factory);">
	<div class="pageFormContent mes_see clearfix" layoutH="58">
	<div class="form-group">
      <label for="inputText" class="control-label col-sm-4">公司名称</label>
        <div class="col-sm-6">
         <textarea name="companyname" 
         	style="border: 0 ;height:100% "
         class="form-control input-medium validate[required,maxSize[32]] required" maxlength="32" readonly="readonly" >${companyinfo.companyname}</textarea>
        </div>
    </div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">选择状态</label>
			<div class="col-sm-6">
			<input id="companystatus" value="${companyinfo.companystatus}" disabled style="display:none"/>
				<select id="status" name="status"
					class="form-control validate[required] required searchtext">
					<c:if test="${companyinfo.companystatus==1}">
					<option value="1">已审核</option>
                    <option value="4">未审核</option>
                    <option value="3">无效</option>
					</c:if>
					<c:if test="${companyinfo.companystatus==4}">
                    <option value="4">未审核</option>
					<option value="1">已审核</option>
                    <option value="3">无效</option>
					</c:if>
					<c:if test="${companyinfo.companystatus==3}">
                    <option value="3">无效</option>
					<option value="1">已审核</option>
                    <option value="4">未审核</option>
					</c:if>
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
	$('.form_datetime').datetimepicker({
		language : 'zh-CN',
		format : 'yyyy-mm-dd',
		weekStart : 1,
		todayBtn : 'linked',
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0,
		showMeridian : 1,
		minView : 2

	});
</script>