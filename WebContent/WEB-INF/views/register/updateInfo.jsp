<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<link rel="stylesheet" href="${contextPath}/styles/css/index.css"
	type="text/css">
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath}/register/saveOrUpdate"
	class="form form-horizontal"
	onsubmit="return validateCallback(this, reloadSubPage);">
	<input type="hidden" name="id" value="${user.id}" />
	<div class="pageFormContent" layoutH="58">
		<div class="row">
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>用户名</label>
			<div class="col-sm-6">
				<input type="text" name="username"
					class="form-control input-medium validate[required,maxSize[26]] required"
					maxlength="16"  readonly="readonly" value="${user.username}" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>邮箱</label>
			<div class="col-sm-6">
				<input type="text" name="email"
					class="form-control input-medium validate[custom[email],maxSize[32],required] required "
					maxlength="32" value="${user.email}" />
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">性别</label>
			<div class="col-sm-6">
			<c:choose>
			<c:when test="${i == 0 }">
			<input type="radio" value="男" name="sex" >男&nbsp;
			<input type="radio" value="女" name="sex" >女
			</c:when>
			<c:when test="${i == 1 }">
			<input type="radio" value="男" name="sex" checked>男&nbsp;
			<input type="radio" value="女" name="sex" >女
			</c:when>
			<c:otherwise>
			<input type="radio" value="男" name="sex" >男&nbsp;
			<input type="radio" value="女" name="sex" checked>女
			</c:otherwise>
			</c:choose>
			</div>
		</div>
		<div class="form-group">
			<label for="inputText" class="control-label col-sm-4">真实姓名</label>
			<div class="col-sm-6">
				<input type="text" name="realname"
					class="form-control input-medium validate[maxSize[26]]"
					maxlength="16" value="${user.realname}" />
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-4">所在地(省):<span
				class="require"></span></label>
			<div class="col-sm-6">
				<select name="province" id="province"
					class="form-control input-medium" /></select>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-sm-4">所在地(市):<span
				class="require"></span></label>
			<div class="col-sm-6">
				<select name="city" id="city" class="form-control input-medium" /></select>
			</div>
		</div>
		<script type="text/javascript"
			src="${contextPath}/js/region_select.js"></script>
		<script type="text/javascript">
			          new PCAS('province', 'city','${user.province}', '${user.city}');
			        </script>
	</div>
	</div>
	<div class="modal-footer">
		<button type="submit" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>