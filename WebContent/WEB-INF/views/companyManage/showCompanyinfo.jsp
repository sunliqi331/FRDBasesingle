<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" action="${contextPath }/company/updateStatus"
	class="form form-horizontal"
	onsubmit="return validateCallback(this, checkCompanyCallback);">
	<input type="hidden" name="id" value="${companyinfo.id}" /> <input
		type="hidden" name="userid" value="${companyinfo.userid}" />
	<div class="pageFormContent mes_see" layoutH="58">
	  <div class="row">	
		<div class="listleft">
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">公司名称:</label>
				<div class="col-sm-8">
					<input type="text" name="companyname"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly"
						value="${companyinfo.companyname}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">公司邮箱:</label>
				<div class="col-sm-8">
					<input type="text" name="companyemail"
						class="form-control input-medium validate[required,maxSize[100]] required"
						maxlength="100" readonly="readonly"
						value="${companyinfo.companyemail}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">法人:</label>
				<div class="col-sm-8">
					<input type="text" name="legalperson"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly"
						value="${companyinfo.legalperson}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">类型:</label>
				<div class="col-sm-8">
					<input id="address" type="text" name="address"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly" value="${companyinfo.infotype}" />
				</div>
			</div>
            <div class="form-group">
				<label for="inputText" class="control-label col-sm-4">成立日期:</label>
				<div class="col-sm-8">
					<input type="text" name="startdate"
						class="form-control input-medium validate[maxSize[32]] required"
						maxlength="32" readonly="readonly"
						value="${companyinfo.startdate}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">所属行业:</label>
				<div class="col-sm-8">
					<input type="text" name="businesstype"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly"
						value="${companyinfo.businesstype}" />
				</div>
			</div>
			
			<div class="form-group">
                 <label for="inputText" class="control-label col-sm-4">营业执照:</label>
                    <div class="col-sm-8">
                        <div class="official">
                            <img alt="营业执照" src="${contextPath }/company/showPic/${companyfiles[0].id}">
                        </div>
                    </div>
            </div>
		</div>
		<div class="listright">
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">公司地址:</label>
				<div class="col-sm-8">
					<input id="address" type="text" name="address"
						class="form-control input-medium validate[required,maxSize[40]] required"
						maxlength="40" readonly="readonly" value="${companyinfo.address}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">注册号: </label>
				<div class="col-sm-8">
					<input id="registernum" type="text" name="registernum"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly" value="${companyinfo.registernum}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">注册资本(万元):</label>
				<div class="col-sm-8">
					<input id="registercapital" type="text" name="registercapital"
						class="form-control input-medium validate[custom[number],min[10],required,maxSize[32]] required"
						maxlength="32" readonly="readonly" value="${companyinfo.registercapital}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">登记机关:</label>
				<div class="col-sm-8">
					<input id="address" type="text" name="address"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly" value="${companyinfo.registerorgan}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">登记状态:</label>
				<div class="col-sm-8">
					<input id="address" type="text" name="address"
						class="form-control input-medium validate[required,maxSize[32]] required"
						maxlength="32" readonly="readonly" value="${companyinfo.registerstatus}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">公司电话:</label>
				<div class="col-sm-8">
					<input id="companyphone" type="text" name="companyphone"
						class="form-control input-medium validate[maxSize[15]]"
						maxlength="15" readonly="readonly" value="${companyinfo.companyphone}" />
				</div>
			</div>
			<div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">公章样本:</label>
                    <div class="col-sm-8">
                        <div class="official">
                            <img alt="公章样本" src="${contextPath }/company/showPic/${companyfiles[1].id}">
                        </div>
                    </div>
            </div>
		</div>
            <div class="form-group">
            <div class="pass">
                <div class="text-right">
                    <label><input type="radio" name="status" class="input_pass validate[required]"
                        id="adopt" value="1" checked /> 通过</label>
                </div>
            </div>
            <div class="nopass">
                <div class="text-left">
                    <label><input type="radio" name="status" class="input_nopass validate[required]"
                        id="notthrough" value="4" /> 不通过</label>
                </div>
            </div>
        </div>
<!-- 		<div class="form-group"> -->
<%-- 			<c:forEach var="item" items="${companyfiles}"> --%>
<!-- 				<div class="license"> -->
<%-- 					<img src="${contextPath }/company/showPic/${item.id}"> --%>
<!-- 				</div> -->
<%-- 			</c:forEach> --%>
<!-- 		</div> -->
		
	</div>
	</div>
	<div class="modal-footer">
		<button type="submit" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>

<script type="text/javascript">
// $(document).ready(function(){
// 	//alert("1");
//   $(".mes_see .control-label").append("：");	
	
// });
</script>