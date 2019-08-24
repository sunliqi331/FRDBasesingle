<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
        <form method="post" id="driverForm" action="${contextPath}/driver/saveDriver" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
            <input type="hidden" name="companyinfo.id" id="companyid" value="${companyid}" /> 
              <input type="hidden" name="differencetype" value="1" /> 
            <div class="pageFormContent" layoutH="58">
              <div class="row">
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备编号</label>
                  <div id="divOfSn" class="col-sm-6">
                    <input id="sn" type="text" name="sn" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" />
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备名称</label>
                  <div class="col-sm-6">
                    <input type="text" name="name" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" />
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型</label>
                  <div class="col-sm-6">
                    <select id="chooseMesDrivertype" name="mesDrivertype.id" class="form-control validate[required]">
                    	<option value="">请选择设备类型</option>
                      <c:forEach var="p" items="${mesDrivertypes}">
                        <option value="${p.id}">${p.typename}</option>
                      </c:forEach>
                    </select>
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">设备型号</label>
                  <div class="col-sm-6">
                    <input type="text" name="modelnumber" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" />
                  </div>
                </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">品牌</label>
                  <div class="col-sm-6">
                    <input type="text" name="brand" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" />
                  </div>
                </div>
                <div class="form-group">
            <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>出厂日期</label>
            <div class="col-sm-6">
                <div class="controls input-append date form_datetime" data-date=""
                    data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                    <input class="form-control datetime validate[required]" type="text"
                        style="background: none;" value="" readonly> <span
                        class="add-on"
                        style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;"> <i
                        class="fa fa-remove"></i>
                    </span> <span class="add-on"
                        style="position: absolute; bottom:0; right: 15px; padding: 5px 7px;"> <i
                        class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" name="leavefactorydate" />
            </div>
        </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">描述</label>
                  <div class="col-sm-6">
                    <textarea name="description" class="form-control input-medium textarea-scroll" cols="29" rows="3" maxlength="100"></textarea>
                  </div>
                </div>
                </div>
                </div>
        <div class="modal-footer">
          <button id="confirm" type="button" class="btn btn-primary">确定</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
        </form>
<script type="text/javascript">
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
    minView: 2
    
 });
</script>
<script type="text/javascript">
var submitStatus = new Array();
function checkValue(){
	if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkSn/" + $("#companyid").val()+"/"+$("#sn").val(), function(data) {
            checkData(data,$("#sn"),"设备编号不可重复",$("#divOfSn"),$("#driverForm"),submitStatus,"sn");
        });
      }
}
function checkValue1(){
	if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkSn/" + $("#companyid").val()+"/"+$("#sn").val(), function(data) {
            checkData1(data,$("#sn"),"设备编号不可重复",$("#divOfSn"),$("#driverForm"));
        });
      }
}
$("#sn").keyup(checkValue);
$("#confirm").click(function(){
    if(submitStatus.length>0){
    	checkValue1();
    }else{
        $("#driverForm").submit();
    }
});

</script>
