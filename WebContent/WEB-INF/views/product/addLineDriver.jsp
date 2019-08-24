<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
/* $(document).ready(function(){
	$("table").attr("data-url","${contextPath}/productline/MesDriverData/"+$("#companyid").val()+"/"+"0");
	ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#selectCompany").val(),chooseCompany);
    $("#mesDriverIds").val(null);
    $("#chooseProductline").find("option").remove();
    $("#chooseProductline").append("<option value='0'>"+ "未绑定产线的设备" +"</option>");
}); */
/* function chooseCompany(data){
    $.each(data,function(idx,item){
        $("#chooseProductline").append("<option value='"+ item.id +"'>"+ item.linename +"的设备</option>");
    });
}; */
$("#submit").click(function(){
    $.table.setCurrent("mesDriversTable");
}); 
$("#submit").click(function(){
	if($("#mesDriverTable").find("tr.selected").length == 0){
		swal("未选择设备");
		return false;
	}
  $.each($("#mesDriverTable").find("tr.selected"),function(idx,item){
    var M = $("#mesDriverIds").val();
    var D = $(this).attr("data-uniqueid");
    if(M.length < 1){
      $("#mesDriverIds").val($(this).attr("data-uniqueid"));
    }else if(M.indexOf(D) >= 0){
    	return false;
    }else{
      $("#mesDriverIds").val(M+","+$(this).attr("data-uniqueid"));
    }
    });
  $("#lineDriverForm").submit();
})
$("#chooseProductline").change(function(event){
    $.table.setCurrent("mesDriverTable");
    $.table.refreshCurrent("${contextPath}/productline/MesDriverData/"+$("#companyId").val()+"/"+$(this).val());
});
</script>

    <form method="post" id="lineDriverForm" action="${contextPath}/productline/saveDriver/${lineId}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      <div class="pageFormContent" layoutH="58" >
    	<input type="hidden" id="companyId" value="${companyId}">
    	<input type="hidden" id="mesDriverIds" name="selectedMesDriverIds">
        <div class="row">
        <div class="form-group" style="margin-bottom:0px" >
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>选择产线</label>
          <div class="col-sm-6">
          <select id="chooseProductline" name="productline.id" class="form-control validate[required] required">
                      <option value="0">未绑定产线设备</option>
                      <c:forEach var="p" items="${mesProductlines }">
                        <option value="${p.id }">${p.linename }</option>
                      </c:forEach>
            </select>
          </div>
          </div>
          <div style="margin:0 15px 15px 15px">
          <table class="table table-striped" id="mesDriverTable" data-field="mesDrivers" data-url="${contextPath}/productline/MesDriverData/${companyId}/0">
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="sn" width="100">设备编号</th>
                <th data-field="name" width="100">设备名称</th>
              </tr>
            </thead>
          </table>
          </div>
      </div>
      </div>
      <div class="modal-footer">
        <button id="submit" class="btn btn-primary" >确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
          </form>
     
<!-- Modal -->
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="icon-edit">
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
  $(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('mesDriverTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesDriverTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesDrivers[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>