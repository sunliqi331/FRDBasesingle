<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <script src="${contextPath}/js/bootstrap.js"></script>
  <script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
  <script src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<link href="${contextPath }/styles/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
$("#editBtn").click(function(){
	var start = $("#dtp_input1s").val();
	var end = $("#dtp_input2s").val();
	start = start.split("-");
	end = end.split("-");
	var date1 = Date.parse(start);
	var date2 = Date.parse(end);
/*  	alert(start);
	alert(end);
	alert(date1);
	alert(date2); */ 
	if(date1>date2){
		swal("错误","结束时间必须晚于开始时间","error");
	}else{
		$("#editForm").submit();
	}
});
</script>

  <form method="post" id="editForm" action="${contextPath}/driverRepair/editMesDriverrepair" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${mesDriverrepair.id}"/>
      <div class="pageFormContent" layoutH="58">
   <div class="row">
        <div class="form-group">
                <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备名称</label>
                <div class="col-sm-6">
                    <input id="mesDriverName" name="mesDriver.name"
                        class="form-control validate[required] required" readonly="readonly" value="${MesDriver.name}" >
                    <input type="hidden" name="mesDriver.id"
                        class="form-control validate[required] required" value="${MesDriver.id}">
                </div>
            </div>
        <div class="form-group">
                <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>开始时间</label>
                <div class="col-sm-6">
                 <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="dtp_input1">
                    <input id="dtp_input1s" class="form-control datetime validate[required]" type="text" style=" background: none;" value="${mesDriverrepair.startDate}" readonly>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 45px; padding: 5px 7px;"><i class="fa fa-remove"></i></span>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 15px;padding: 5px 7px;"><i class="fa fa-th"></i></span>
                   </div>
                  <input type="hidden" id="dtp_input1" value="${mesDriverrepair.starttime}" name="starttime"/>
          </div>         
        </div>
         <div class="form-group">
                <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>结束时间</label>
                <div class="col-sm-6">
                 <div class="controls input-append date form_datetime2" data-date="" data-date-format="" data-link-field="dtp_input2">
                    <input id="dtp_input2s" class="form-control datetime validate[required]" type="text" style=" background: none;" value="${mesDriverrepair.endDate}" readonly>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 45px; padding: 5px 7px;"><i class="fa fa-remove"></i></span>
                    <span class="add-on" style="position: absolute; bottom:0px; right: 15px;padding: 5px 7px;"><i class="fa fa-th"></i></span>
                   </div>
                  <input type="hidden" id="dtp_input2" value="${mesDriverrepair.endtime}" name="endtime"/>
          </div>
        </div>
         <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护人员</label>
          <div class="col-sm-6">
            <input type="text" name="repairperson" class="form-control input-medium validate[required,maxSize[20]] required" maxlength="20" value="${mesDriverrepair.repairperson }"/>
          </div>         
        </div>
        <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护人员电话</label>
        <div class="col-sm-6">
          <input type="tel" name="telnum"
            class="form-control input-medium validate[required,maxSize[11],custom[phone]] required"
            maxlength="11" value="${mesDriverrepair.telnum }"/>
        </div>
      </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">维护周期</label>
          <div class="col-sm-6">
            <input type="datetime" name="repaircycle" class="form-control input-medium validate[maxSize[20]] required" maxlength="20" value="${mesDriverrepair.repaircycle }"/>
          </div>         
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>状态</label>
          <div class="col-sm-6">
          <select id="status" name="status" class="form-control validate[required]">
          <option value="未开始">未开始</option>
          <option value="维修中">维修中</option>
          <option value="已完成">已完成</option>
          </select>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>维护原因</label>
          <div class="col-sm-6">
            <textarea name="repaircause" class="form-control input-medium validate[required,maxSize[30]] required textarea-scroll" cols="29" rows="3" maxlength="30">${mesDriverrepair.repaircause }</textarea>
          </div>
        </div>
      </div>
      </div>
      <div class="modal-footer">
        <button type="button" id="editBtn" class="btn btn-primary">确定</button>
        <button type="button" class="btn btn-default"
          data-dismiss="modal">关闭</button>
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
       //  minView: 1
  });
</script>