<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="pageContent">
	<form method="post" action="${contextPath}/department/saveOrUpdate"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent" layoutH="58">
			<div class="listleft">
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">工厂名称</label>
					<div class="col-sm-6">
						<input type="text" name="companyname"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.companyname}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">父公司</label>
					<div class="col-sm-6">
						<input type="text" name="parentname"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.parentname}" />
					</div>
				</div>
				<%-- </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">法人</label>
          <div class="col-sm-6">
            <input type="text" name="legalperson"
              class="form-control input-medium validate[required,maxSize[32]] required"
              maxlength="32" readonly="readonly"
              value="${companyinfo.legalperson}" />
          </div>
        </div> --%>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">成立日期
					</label>
					<div class="col-sm-6">
						<input type="text" name="startdate" id="datetimepicker3"
							class="form-control input-medium validate[required]"
							readonly="readonly" value="${companyinfo.startdate}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">营业执照
					</label>
					<div class="col-sm-6">
						<div class="official">
							<img alt="营业执照"
								src="${contextPath }/company/showPic/${companyfile[0].id}">
						</div>
					</div>
				</div>
			</div>
			<div class="listright">
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">公司地址</label>
					<div class="col-sm-6">
						<input type="text" name="address"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly" value="${companyinfo.address}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">注册号 </label>
					<div class="col-sm-6">
						<input type="text" name="registernum"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registernum}" />
					</div>
				</div>
				<%--         <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">登记机关</label>
          <div class="col-sm-6">
            <input type="text" name="registerorgan"
              class="form-control input-medium validate[required,maxSize[32]] required"
              maxlength="32" readonly="readonly"
              value="${companyinfo.registernum}" />
          </div>
        </div> --%>
				<%--        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">登记状态</label>
          <div class="col-sm-6">
            <input type="text" name="registerstatus"
              class="form-control input-medium validate[required,maxSize[32]] required"
              maxlength="32" readonly="readonly"
              value="${companyinfo.registernum}" />
          </div>
        </div> --%>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">注册资本</label>
					<div class="col-sm-6">
						<input type="text" name="registercapital"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registercapital}" />
					</div>
				</div>
				<%--        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">类型</label>
          <div class="col-sm-6">
            <input type="text" name="type"
              class="form-control input-medium validate[required,maxSize[32]] required"
              maxlength="32" readonly="readonly" value="${companyinfo.type}" />
          </div>
        </div> --%>
				<%--         <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">所属行业</label>
          <div class="col-sm-6">
            <input type="text" name="businesstype"
              class="form-control input-medium validate[required,maxSize[32]] required"
              maxlength="32" readonly="readonly"
              value="${companyinfo.businesstype}" />
          </div>
        </div> --%>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">公章样本
					</label>
					<div class="col-sm-6">
						<div class="official">
							<img alt="公章样本"
								src="${contextPath }/company/showPic/${companyfile[1].id}">
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer" style="border-top: 0;">
			<%--       <a class="btn btn-default1" target="dialog" rel="updateInfo" mask="true" href="${contextPath }/company/findCompany" title="修改信息"> --%>
			<!--         <i class="icon-ok-circle"></i>  -->
			<!--         <span>修改公司信息</span> -->
			<!--       </a> -->
			<button type="button" class="btn btn-default"
				data-dismiss="pageswitch">关闭</button>
		</div>
	</form>
</div>
