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
	<form method="post" id="testForm" action="${contextPath}/procedure/saveProcedure/${productId}"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
	<div class="form-group"></div>
<%-- 		<input type="hidden" name="mesProduct.id" value="${productId}" /> --%>
	<div class="pageFormContent clearfix " layoutH="58">
		<input type="hidden" name="id" value="${mesDriverprocedure.id}" />
		<input type="hidden" id="productId" value="${productId}">
      <div class="listleft">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工序号</label>
        <div   class="col-sm-8">
          <input   type="text"  id="procedurenum" name="procedurenum"
            class="form-control input-medium validate[required,maxSize[30]] required"
            maxlength="30" value="${mesDriverprocedure.procedurenum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工序名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="procedurename" name="procedurename"
            class="form-control input-medium validate[required,maxSize[30]] required"
            maxlength="30" value="${mesDriverprocedure.procedurename}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>步骤序号</label>
        <div   class="col-sm-8">
          <input   type="text" id="nextprocedurename" name="nextprocedurename"
            class="form-control input-medium validate[custom[integer],required,maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.nextprocedurename}" 
            onkeyup="value=value.replace(/[^\d]/g,'')"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">客户名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="customername" name="customername"
            class="form-control input-medium validate[maxSize[45]] "
            maxlength="45" value="${mesDriverprocedure.customername}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">零件名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="partname" name="partname"
            class="form-control input-medium validate[maxSize[45]] "
            maxlength="45" value="${mesDriverprocedure.partname}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">客户图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="customerpicturenum" name="customerpicturenum"
            class="form-control input-medium validate[maxSize[45]] "
            maxlength="45" value="${mesDriverprocedure.customerpicturenum}"/>
        </div>
      </div>
      
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">森威图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="swpicturenum" name="swpicturenum"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.swpicturenum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">设备名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="drivermodel" name="drivermodel"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.drivermodel}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">设备编号</label>
        <div   class="col-sm-8">
          <input   type="text" id="drivernum" name="drivernum"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.drivernum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">夹具名称</label>
        <div   class="col-sm-8">
          <input   type="text" id="jipname" name="jipname"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.jipname}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">夹具图号</label>
        <div   class="col-sm-8">
          <input   type="text" id="jippicturenum" name="jippicturenum"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.jippicturenum}"/>
        </div>
      </div>
      </div>
      <div class="listright">
      <div class="form-group">
	  <label for="inputText" class="control-label col-sm-4">版本日期</label>
		  <div class="col-sm-8">
			  <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text" style=" background: none;" value="${mesDriverprocedure.versiondate}" readonly>
                  <span class="add-on" style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;"><i class="fa fa-remove"></i></span>
                  <span class="add-on" style="position: absolute; bottom:0; right: 15px;padding: 5px 7px;"><i class="fa fa-th"></i></span>
		      </div>
	          <input type="hidden" id="dtp_input1" value="${mesDriverprocedure.versiondate}" name="versiondate"/>
		  </div>
	  </div>
	  <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">ops</label>
        <div   class="col-sm-8">
	        <select id="ops" name="ops" class="form-control validate[] ">
	        <option value="${mesDriverprocedure.ops}">${mesDriverprocedure.ops}</option>
	        <option value="OTS">OTS</option>
	        <option value="PPAP">PPAP</option>
	        <option value="SOP">SOP</option>
	        </select>
	         <%--  <input   type="text" id="ops" name="ops"
	            class="form-control input-medium validate[maxSize[45]] required"
	            maxlength="45" value="${mesDriverprocedure.ops}"/> --%>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">指导书编号</label>
        <div   class="col-sm-8">
          <input   type="text" id="operationnum" name="operationnum"
            class="form-control input-medium validate[maxSize[45]] "
            maxlength="45" value="${mesDriverprocedure.operationnum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">指导书版本</label>
        <div   class="col-sm-8">
          <input   type="text" id="operationversion" name="operationversion"
            class="form-control input-medium validate[maxSize[20]] "
            maxlength="20" value="${mesDriverprocedure.operationversion}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">材料牌号</label>
        <div   class="col-sm-8">
          <input   type="text" id="materialnum" name="materialnum"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.materialnum}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">材料规格</label>
        <div   class="col-sm-8">
          <input   type="text" id="materialstandards" name="materialstandards"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.materialstandards}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">下料重量</label>
        <div   class="col-sm-8">
          <input   type="text" id="weightsmaterials" name="weightsmaterials"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.weightsmaterials}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">毛坯外形尺寸</label>
        <div   class="col-sm-8">
          <input   type="text" id="blankshapesize" name="blankshapesize"
            class="form-control input-medium validate[maxSize[45]] "
            maxlength="45" value="${mesDriverprocedure.blankshapesize}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">每坯可制作数</label>
        <div   class="col-sm-8">
          <input type="text" id="makecase" name="makecase"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.makecase}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">周转装具</label>
        <div   class="col-sm-8">
          <input type="text" id="reservation" name="reservation"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.reservation}"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">生产车间</label>
        <div   class="col-sm-8">
          <input type="text" id="workshop" name="workshop"
            class="form-control input-medium validate[maxSize[30]] "
            maxlength="30" value="${mesDriverprocedure.workshop}"/>
        </div>
      </div>
      <%-- <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">备注</label>
        <div   class="col-sm-8">
          <input type="text" id="remarks" name="remarks"
            class="form-control input-medium validate[maxSize[45]] required"
            maxlength="45" value="${mesDriverprocedure.remarks}"/>
        </div>
      </div> --%>
      </div>
     </div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary">确定</button>
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
var oldNextNum = $("#nextprocedurename").val();
var submitStatus1 = new Array();
function checkValue(){
	if($("#sn").val()==oldSn){
		$("#divOfNext").find("div.parentFormformID").remove();
		$("#divOfNext").find("div.snformError").remove();
		submitStatus1.length=0;
	}else if($("#nextprocedurename").val()!=""&&$.trim($("#nextprocedurename").val()) != ''){
    	 ajaxTodo("${contextPath}/procedure/checkNext/"+$("#nextprocedurename").val()+"/"+$("#productId").val(), function(data) {
            checkData(data,$("#nextprocedurename"),"步骤序号不可重复",$("#divOfNext"),$("#testForm"),submitStatus1,"nextprocedurename");
        });
      }
  };
function checkValue1(){
	if($("#nextprocedurename").val()!=""&&$.trim($("#nextprocedurename").val()) != ''){
    	 ajaxTodo("${contextPath}/procedure/checkNext/"+$("#nextprocedurename").val()+"/"+$("#productId").val(), function(data) {
            checkData1(data,$("#nextprocedurename"),"步骤序号不可重复",$("#divOfNext"),$("#testForm"));
        });
      }
  };
  $("#nextprocedurename").keyup(checkValue);
$("#confirm").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else{
	        $("#testForm").submit();
	 }
});
</script>