<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

 <div class="searchBar page_see_title" >
          <div class="search_header">
             <i class="fa fa-eye"></i> 查看公司信息
           </div>
          </div>
  <div class="pageContent page_see_content" style="margin-left:0px">
	<form method="post" action="${contextPath}/department/saveOrUpdate"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent mes_see clearfix" layoutH="58" style="margin-left:0px">
			<div class="listleft">
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >公司名称</label>
					<div class="col-sm-6"  >
						<textarea  name="companyname"
						style="border: 0 ;height:60px"
					
						class="form-control input-medium validate[required,maxSize[40]] required"
						maxlength="40" readonly="readonly"
							 >${companyinfo.companyname}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >公司地址</label>
					<div class="col-sm-6">
						<textarea  name="address"
						
						rows="value"
						style="border: 0; height:60px" 
						id="comaddress"
							class="form-control input-medium validate[required,maxSize[50]] required"
							maxlength="50" readonly="readonly" >${companyinfo.address}</textarea>
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >法人</label>
					<div class="col-sm-6">
						<input type="text" name="legalperson"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.legalperson}" />
					</div>
				</div>
				
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >公司邮箱</label>
					<div class="col-sm-6">
						<input type="text" name="companyemail"
							class="form-control input-medium validate[required,maxSize[100]] required"
							maxlength="100" readonly="readonly" value="${companyinfo.companyemail}" />
					</div>
				</div>
			<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >注册号 </label>
					<div class="col-sm-6">
						<input type="text" name="registernum"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registernum}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4" >成立日期
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
							<img alt="营业执照" src="${contextPath }/company/showPic/${companyfile[0].id}">
						</div>
					</div>
			</div>
				<%-- 				<div class="form-group">
					<c:forEach var="item" items="${companyfile}">
						<label for="inputText" class="control-label col-sm-4">资料图片
						</label>
						<div class="col-sm-6">
							<div class="official">
								<img alt="资料图片" src="${contextPath }/company/showPic/${item.id}">
							</div>
						</div>
					</c:forEach>
				</div> --%>
				<%-- <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">营业执照
          </label>
          <div class="col-sm-6">
            <div class="license">
              <img alt="营业执照" src="${contextPath }/styles/img/license.jpg">
            </div>
          </div>
        </div> --%>
			</div>
			<div class="listright" >
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">登记机关</label>
					<div class="col-sm-6">
						<input type="text" name="registerorgan"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registernum}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">登记状态</label>
					<div class="col-sm-6">
						<input type="text" name="registerstatus"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registernum}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">注册资本(万元)</label>
					<div class="col-sm-6">
						<input type="text" name="registercapital"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.registercapital}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">类型</label>
					<div class="col-sm-6">
						<input type="text" name="infotype"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.infotype}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">公司电话</label>
					<div class="col-sm-6">
						<input type="text" name="companyphone"
							class="form-control input-medium validate[required,maxSize[15]]"
							maxlength="15" readonly="readonly"
							value="${companyinfo.companyphone}" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">所属行业</label>
					<div class="col-sm-6">
						<input type="text" name="businesstype"
							class="form-control input-medium validate[required,maxSize[32]] required"
							maxlength="32" readonly="readonly"
							value="${companyinfo.businesstype}" />
					</div>
				</div>
			<div class="form-group">
					<label for="inputText" class="control-label col-sm-4">公章样本
					</label>
					<div class="col-sm-6">
						<div class="official">
							<img alt="公章样本" src="${contextPath }/company/showPic/${companyfile[1].id}">
						</div>
					</div>
			</div>
				<%-- 				<div class="form-group">
					<c:forEach var="item" items="${companyfile}">
						<label for="inputText" class="control-label col-sm-4">资料图片
						</label>
						<div class="col-sm-6">
							<div class="official">
								<img alt="资料图片" src="${contextPath }/company/showPic/${item.id}">
							</div>
						</div>
					</c:forEach>
				</div> --%>
			</div>
<%-- 				<c:forEach var="item" items="${companyfile}"> --%>
<%-- 				</c:forEach> --%>
		</div>
		<div class="modal-footer" >
			<button type="button" class="btn btn-default"
				data-dismiss="pageswitch">返回</button>
		</div>
	</form>
</div>
<style type="text/css">

.form-group{
margin-left:5px
}
.listleft{
margin-left:0px
}
.listright{
margin-right:0px
}

</style> 
<script type="text/javascript">
$(document).ready(function(){
	//alert("1");
	
  $(".mes_see .control-label").append("：");	
	
});
</script>
