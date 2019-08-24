<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script src="${contextPath}/js/bootstrap.js"></script>
<script
	src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script
	src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<%-- <link href="${contextPath }/styles/css/bootstrap.min.css" rel="stylesheet" type="text/css" /> --%>
<link
	href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css"
	rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<div class="pageContent">
	<form method="post" id="testForm" action="#"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
<%-- 		<input type="hidden" name="mesProduct.id" value="${productId}" /> --%>
		    <div class="pageFormContent clearfix mes_see" layoutH="58">
		<input type="hidden" name="id" value="${mesDriverprocedure.id}" />
      <div class="listleft">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">工序号</label>
        <div   class="col-sm-8">
          <input   type="text"  id="procedurenum" name="procedurenum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.procedurenum}" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">工序名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="procedurename" name="procedurename" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.procedurename}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">步骤序号</label>
        <div   class="col-sm-8">
          <input   type="text" id="nextprocedurename" name="nextprocedurename" readonly="readonly"
            class="form-control input-medium validate[custom[integer],required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.nextprocedurename}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">客户名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="customername" name="customername" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.customername}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">零件名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="partname" name="partname" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.partname}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">客户图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="customerpicturenum" name="customerpicturenum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.customerpicturenum}"/>
        </div>
      </div>
      
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">森威图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="swpicturenum" name="swpicturenum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.swpicturenum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">设备名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="drivermodel" name="drivermodel" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.drivermodel}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">设备编号</label>
        <div   class="col-sm-8">
          <input   type="text" id="drivernum" name="drivernum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.drivernum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">夹具名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="jipname" name="jipname" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.jipname}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">夹具图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="jippicturenum" name="jippicturenum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.jippicturenum}"/>
        </div>
      </div>
      </div>
      <div class="listright">
      <div class="form-group">
				<label for="inputText" class="control-label col-sm-4">版本日期</label>
				 <div   class="col-sm-8">
          <input   type="text" id="versiondate" name="versiondate" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.versiondate}"/>
        </div>
			</div>
			<div class="form-group">
        <label for="inputText" class="control-label col-sm-4">ops</label>
         <div   class="col-sm-8">
          <input   type="text" id="ops" name="ops" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.ops}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">指导书编号</label>
        <div   class="col-sm-8">
          <input   type="text" id="operationnum" name="operationnum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.operationnum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">指导书版本</label>
        <div   class="col-sm-8">
          <input   type="text" id="operationversion" name="operationversion" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.operationversion}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">材料牌号</label>
        <div   class="col-sm-8">
          <input   type="text" id="materialnum" name="materialnum" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.materialnum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">材料规格</label>
        <div   class="col-sm-8">
          <input   type="text" id="materialstandards" name="materialstandards" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.materialstandards}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">下料重量</label>
        <div   class="col-sm-8">
          <input   type="text" id="weightsmaterials" name="weightsmaterials" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.weightsmaterials}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">毛坯外形尺寸</label>
        <div   class="col-sm-8">
          <input   type="text" id="blankshapesize" name="blankshapesize" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.blankshapesize}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">每坯可制作数</label>
        <div   class="col-sm-8">
          <input type="text" id="makecase" name="makecase" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.makecase}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">周转装具</label>
        <div   class="col-sm-8">
          <input type="text" id="reservation" name="reservation" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.reservation}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">生产车间</label>
        <div   class="col-sm-8">
          <input type="text" id="workshop" name="workshop" readonly="readonly"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.workshop}"/>
        </div>
      </div>
      <%-- <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">备注</label>
        <div   class="col-sm-8">
          <input type="text" id="remarks" name="remarks"
            class="form-control input-medium validate[required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.remarks}"/>
        </div>
      </div> --%>
      </div>
     </div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default"
				data-dismiss="modal">关闭</button>
		</div>
	</form>
</div>
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
         minView: 1
        
  });
</script>
<script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>

<script type="text/javascript">
$(document).ready(function(){
	//alert("1");
  $(".mes_see .control-label").append("：").css("margin-right","-25px").css("margin-left","15px")	
	
});
</script>