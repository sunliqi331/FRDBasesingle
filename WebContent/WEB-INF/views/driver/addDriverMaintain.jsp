<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script src="${contextPath}/js/bootstrap.js"></script>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
$("#productline").change(function(event){
  ajaxTodo("${contextPath}/driverRepair/getDriversByProductline/"+$("#productline").val(),paintDrivers);
  $("#mesDriverName").find("option").remove();
   $("#mesDriverName").append("<option value=''>"+ "请选择" +"</option>");
});
function paintDrivers(data){
  $.each(data,function(idx,item){
    $("#mesDriverName").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
  });
}
$("#addBtn").click(function(){
	var start = $("#dtp_input1s").val();
	var end = $("#dtp_input2s").val();
	start = start.split("-");
	end = end.split("-");
	var date1 = Date.parse(start);
	var date2 = Date.parse(end);
	if(date1>date2){
		swal("错误","结束时间必须晚于开始时间","error");
	}else{
		$("#addForm").submit();
	}
});
</script>

  <form method="post" id="addForm" action="${contextPath}/driverRepair/saveDriverRepair" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58">
     <div class="row">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>选择产线</label>
        <div class="col-sm-6">
          <select id="productline" name="productline"
            class="form-control validate[required]">
            <option value="0">请选择</option>
            <c:forEach var="p" items="${productline}">
              <option value="${p.id}">${p.linename}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备名称</label>
        <div class="col-sm-6">
          <select id="mesDriverName" name="mesDriver.id"
            class="form-control validate[required] required">
<!--             <option value="">请选择</option> -->
          </select>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>开始时间</label>
        <div class="col-sm-6">
         <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1">
                    <input id="dtp_input1s" class="form-control datetime validate[required]" type="text" style=" background: none;" value="" readonly>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 45px; padding: 5px 7px;"><i class="fa fa-remove"></i></span>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 15px;padding: 5px 7px;"><i class="fa fa-th"></i></span>
                   </div>
                  <input type="hidden" id="dtp_input1" value="" name="starttime"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>结束时间</label>
        <div class="col-sm-6">
         <div class="controls input-append date form_datetime2" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input2">
                    <input id="dtp_input2s" class="form-control datetime validate[required] required " type="text" style=" background: none;" value="" readonly>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 45px; padding: 5px 7px;"><i class="fa fa-remove"></i></span>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 15px;padding: 5px 7px;"><i class="fa fa-th"></i></span>
                   </div>
                  <input type="hidden" id="dtp_input2"  class="validate[required] required" value="" name="endtime"/>
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护人员</label>
        <div class="col-sm-6">
          <input type="text" name="repairperson"
            class="form-control input-medium validate[required,maxSize[20]] required"
            maxlength="20" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护人员电话</label>
        <div class="col-sm-6">
          <input type="tel" name="telnum"
            class="form-control input-medium validate[required,custom[phone],maxSize[11]] required"
            maxlength="11" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护周期</label>
        <div class="col-sm-6">
          <input type="text" name="repaircycle"
            class="form-control input-medium validate[maxSize[20]] required"
            maxlength="20" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护原因</label>
        <div class="col-sm-6">
          <textarea name="repaircause"
            class="form-control input-medium validate[required,,maxSize[30]] textarea-scroll" cols="29"
            rows="3" maxlength="30"></textarea>
        </div>
      </div>
    </div>
    </div>
    <div class="modal-footer">
      <button type="button" id="addBtn" class="btn btn-primary">确定</button>
      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
  </form>

<script>
 $('.form_datetime').datetimepicker({
     language:  'zh-CN',
        format: 'yyyy-mm-dd hh:ii:ss',
      weekStart: 1,
      todayBtn:'linked',
         autoclose: 1,
         todayHighlight: 1,
         startView: 2,
         forceParse: 0,
      showMeridian: 1,
        // minView: 1
        
  });
 $('.form_datetime2').datetimepicker({
     language:  'zh-CN',
        format: 'yyyy-mm-dd hh:ii:ss',
      weekStart: 1,
      todayBtn:'linked',
         autoclose: 1,
         todayHighlight: 1,
         startView: 2,
         forceParse: 0,
      showMeridian: 1,
        // minView: 1
  });
</script>