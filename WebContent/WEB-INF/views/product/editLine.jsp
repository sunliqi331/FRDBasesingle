<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
/* $("#addDriver").click(function(){
    $.table.setCurrent("mesDriverTable");
    $.table.refreshCurrent();
});
$("#submit").click(function(){
    $.table.setCurrent("table");
    $.table.refreshCurrent();
}); */
$.each($("#selectedMesDriverTable").find("tr.selected"),function(idx,item){
    $("#selectedEditMesDriverIds").val($(this).attr("data-uniqueid"));
})
$("#submit").click(function(){
  $.each($("#selectedMesDriverTable").find("tr.selected"),function(idx,item){
    var M = $("#selectedEditMesDriverIds").val();
    var D = $(this).attr("data-uniqueid");
    if(M.length < 1){
      $("#selectedEditMesDriverIds").val($(this).attr("data-uniqueid"));
    }else if(M.indexOf(D) >= 0){
        return false;
    }else{
      $("#selectedEditMesDriverIds").val(M+","+$(this).attr("data-uniqueid"));
    }
    });
});
/* function Ajax(){
    ajaxTodo("${contextPath}/productline/checkLinesn/" + $("#companyid").val()+"/"+$("#linesn").val(), function(data) {
      $.each(data, function(idx, item) {
        if (item != 1) {
          $("#divOfLinesn").find("div.parentFormformID").remove();
          $("#linesn").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 此设备已添加<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
          $("#submit").attr("disabled", "true");
          } else {
            $("#divOfLinesn").find("div.parentFormformID").remove();
            $("#submit").removeAttr("disabled");
            }
        });
      });
    } */
/* $(document).ready(function($){
  var oldLinesn = $("#linesn").val();
   $("#linesn").blur(function(){
    $("#divOfLinesn").find("div.parentFormformID").remove();
    if($("#linesn").val()!=""){
      if($("#linesn").val()==oldLinesn){
        $("#submit").removeAttr("disabled");
        }else{
              Ajax();
          }
      }
    }); 
  }); */
</script>
<div class="pageContent">
  <form method="post" action="${contextPath}/productline/editMesProductline" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      <input type="hidden"name="companyid" id="companyid" value="${companyid }" />
      <input type="hidden"name="mesDriverIds" id="mesDriverIds" />
      <input type="hidden"name="selectedEditMesDriverIds" id="selectedEditMesDriverIds" />
      <input type="hidden"name="selectedMesdrivers" id="selectedMesdrivers" value="${selectedMesdrivers }"/>
      <input type="hidden" name="id" value="${MesProductline.id }">
      <div class="pageFormContent" layoutH="58">
       <div class="row">
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产线编号</label>
          <div id="divOfLinesn" class="col-sm-6">
            <input id="linesn" type="text" name="linesn" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${MesProductline.linesn }"/>
          </div>
         
        </div>
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产线名称</label>
          <div class="col-sm-6">
            <input type="text" name="linename" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45"value="${MesProductline.linename }"/>
          </div>
        </div>
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>所属工厂</label>
          <div class="col-sm-6">
            <input type="text" id="selectCompany" name="companyinfo.companyname" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" readonly="readonly" value="${company.companyname}"/>
            <input type="hidden" name="companyinfo.id" value="${company.id}"/>
            <input type="hidden" id="sCompany" value="${company.id}"/>
          </div>
        </div>
       <%--  <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">添加设备</label>
          <div class="col-sm-6" id="mesDriverName">
          <a class="btn btn-default1" target="dialog" rel="editLineDriver" id="addDriver" href="${contextPath}/productline/editLineDriver/${MesProductline.id }" >
                <i class="fa fa-plus"></i> 
                <span>添加设备</span>
                </a>
          </div>
        </div> --%>
         <%-- <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备列表</label>
           <div class="col-sm-6">
        <table class="table table-striped" id="selectedMesDriverTable" data-field="selectedMesDrivers" data-url="${contextPath}/productline/selectedMesDriverData/${selectedMesdrivers}">
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
<!--                 <th data-field="id" width="100">序号</th> -->
                <th data-field="sn" width="100">设备编号</th>
                <th data-field="name" width="100">设备名称</th>
              </tr>
            </thead>
          </table>
          </div>
          </div> --%>
      </div>
      </div>
      <div class="modal-footer">
        <button id="submit" type="submit" class="btn btn-primary" >确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>
</div>
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
    $.table.init('selectedMesDriverTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#selectedMesDriverTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.selectedMesDrivers[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
