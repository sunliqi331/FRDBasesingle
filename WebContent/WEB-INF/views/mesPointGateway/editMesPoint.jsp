<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="pageContent">
	<form method="post" action="${contextPath}/mesPointGateway/saveMesPoint"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent" layoutH="58">
		<input type="hidden" name="id" value="${mesPoints.id}">
			<input type="hidden" name="mesPointGateway.id"
				value="${mesPoints.mesPointGateway.id}">
<%-- 			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点类型</label>
				<div class="col-sm-6">
					<select id="mesPointType" name="mesPointType.id"
						class="form-control ">
						<option value="${mesPoints.mesPointType.id}">${mesPoints.mesPointType.name}</option>
						<c:forEach var="mesPointType" items="${mesPointType}">
							<option value="${mesPointType.id}">${mesPointType.name}</option>
						</c:forEach>
					</select>
				</div>
			</div> --%>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点名称</label>
				<div class="col-sm-6">
					<input type="text" name="name"
						class="form-control input-medium validate[maxSize[20],required] required"
						maxlength="20" value="${mesPoints.name}" />
				</div>
			</div>
			<%-- <div class="form-group">
				<label for="inputText" class="control-label col-sm-4">codekey</label>
				<div class="col-sm-6">
					<input type="text" name="codekey"
						class="form-control input-medium validate[maxSize[45],required] required"
						maxlength="45" value="${mesPoints.codekey}" />
				</div>
			</div> --%>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>数据类型</label>
				<div class="col-sm-6">
					<input type="text" name="datatype"
						class="form-control input-medium validate[maxSize[45],required] required"
						maxlength="45" value="${mesPoints.datatype}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>单位</label>
				<div class="col-sm-6">
					<input type="text" name="units"
						class="form-control input-medium validate[maxSize[45],required] required"
						maxlength="45" value="${mesPoints.units}" />
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary">确定</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</form>
</div>