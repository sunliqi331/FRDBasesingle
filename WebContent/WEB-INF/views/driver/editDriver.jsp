<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
  $("#editMesPoint").click(function(){
	    $.table.setCurrent("MesPointTable");
	    $.table.refreshCurrent();
	});
$('.form_datetime').datetimepicker({
    language : 'zh-CN',
    format : 'yyyy-mm-dd',
    weekStart : 1,
    todayBtn : 'linked',
    autoclose : 1,
    todayHighlight : 1,
    startView : 2,
    forceParse : 0,
    showMeridian : 1,
    minView : 2

});

// var oldSn = $("#sn").val();
// $("#sn").keyup(function(){
//     if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''&&$("#sn").val()!=oldSn){
//         ajaxTodo("${contextPath}/driver/checkSn/" + $("#companyid").val()+"/"+$("#sn").val(), function(data) {
//             checkData(data,$("#sn"),"设备编号不可重复",$("#divOfSn"),$("#driverForm"));
//         });
//       }
//   });
// $("#editDriverBtn").click(function(){
//     obj = $(".parentFormformID").size();
//     if(obj>0){
        
//     }else{
//         $("#driverForm").submit();
//     }
// });
</script>
<script type="text/javascript">
var submitStatus = new Array();
var oldSn = $("#sn").val();
function checkValue(){
	if($("#sn").val()==oldSn){
		$("#divOfSn").find("div.parentFormformID").remove();
		$("#divOfSn").find("div.snformError").remove();
		submitStatus.length=0;
	}else if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''&&$("#sn").val()!=oldSn){
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
$("#editDriverBtn").click(function(){
    if(submitStatus.length>0){
        checkValue1();
    }else{
        $("#driverForm").submit();
    }
});
</script>

<div class="pageContent">
<form id="driverForm" method="post" action="${contextPath}/driver/saveEditDriver" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <div class="pageFormContent" layoutH="58">
      <div class="row"> 
      <input type="hidden" name="id" value="${mesDriver.id}"/>
      <input type="hidden" name="companyinfo.id" id="companyid" value="${companyid}" />
      <input type="hidden" name="differencetype" value="1"/>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备编号</label>
          <div id="divOfSn" class="col-sm-6">
            <input type="text" id="sn" name="sn" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${mesDriver.sn}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${mesDriver.name}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型</label>
          <div class="col-sm-6">
            <select id="drivertype" name="mesDrivertype.id" class="form-control validate[required]">
                      <option value="${mdt.id }">${mdt.typename}</option>
                      <c:forEach var="MesDrivertype" items="${MesDrivertype }">
                        <option value="${MesDrivertype.id }">${MesDrivertype.typename }</option>
                      </c:forEach>
            </select>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备型号</label>
          <div class="col-sm-6">
            <input type="text" name="modelnumber" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${mesDriver.modelnumber}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">品牌</label>
          <div class="col-sm-6">
            <input type="text" name="brand" class="form-control input-medium validate[maxSize[45]] required" maxlength="45" value="${mesDriver.brand}"/>
          </div>
        </div>
        <div class="form-group">
            <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>出厂日期</label>
            <div class="col-sm-6">
                <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                    <input class="form-control datetime validate[required]" type="text" style="background: none;" value="${mesDriver.leavefactorydate}" readonly> 
                    <span class="add-on" style="position: absolute; bottom:0; right: 40px; padding: 5px;"> <i class="fa fa-remove"> </i> </span> 
                    <span class="add-on" style="position: absolute; bottom:0; right: 15px; padding: 5px;"> <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" name="leavefactorydate" value="${mesDriver.leavefactorydate}"/>
            </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">描述</label>
          <div class="col-sm-6">
            <textarea name="description" class="form-control input-medium textarea-scroll" cols="29" rows="3" maxlength="100">${mesDriver.description }</textarea>
          </div>
        </div>
       </div>
     </div>
  <div class="modal-footer">
    <button type="button" id="editDriverBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
</div>
