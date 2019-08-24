<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button{
-webkit-appearance: none !important;
 }
</style>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
$("#minValue").keyup(function() {
	var s1 = $("#maxValue").val();
	var s2 = $("#minValue").val();
    if (eval(s1) < eval(s2)) {
      $("#driverPointForm").find("div.parentFormformID").remove();
      $("#minValue").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 不能大于上限值<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
    } else {
      $("#driverPointForm").find("div.parentFormformID").remove();
    }
  });
$("#maxValue").keyup(function() {
	var s1 = $("#maxValue").val();
	var s2 = $("#minValue").val();
    if (eval(s1) < eval(s2)) {
      $("#driverPointForm").find("div.parentFormformID").remove();
      $("#maxValue").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 不能小于下限值<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
    } else {
      $("#driverPointForm").find("div.parentFormformID").remove();
    }
  });
</script>
<!-- <div class="pageContent" > -->
  <form method="post" id="driverPointForm" action="${contextPath}/driver/saveEditDriverPoint" class="form form-horizontal" data-target="" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58" style="padding-top:0" >
        <input type="hidden" name="id" id="mesDriverPointsId" value="${mesDriverPoints.id}" /> 
        <input type="hidden" name="mesPoints.id" id="mesPointsId" value="${mesDriverPoints.mesPoints.id}" /> 
        <input type="hidden" name="mesDriver.id" id="mesDriverId" value="${mesDriverPoints.mesDriver.id}" /> 
     <div class="row">
      <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>测点名称</label>
          <div class="col-sm-8" >
            <input type="text" name="mesPointsName" class="form-control validate[required,maxSize[255]] required" maxlength="255" readonly="readonly" value="${mesDriverPoints.mesPoints.name}">
          </div>
        </div>
      <div class="form-group" >
        <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>设备类型属性</label>
        <div class="col-sm-8">
          <select id="chooseMesDrivertypeProperty" name="mesDrivertypeProperty.id" class="form-control validate[required]">
            <c:forEach var="p" items="${MesDrivertypeProperties}">
              <option value="${p.id}">${p.propertyname}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>上限值</label>
          <div id="divOfMaxValue" class="col-sm-8"  >
            <input id="maxValue" type="text" name="maxValue" class="form-control  validate[required,maxSize[30]] required" maxlength="30" value="${mesDriverPoints.maxValue}"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>下限值</label>
          <div id="divOfMinValue" class="col-sm-8" >
            <input id="minValue" type="text" name="minValue" class="form-control validate[required,maxSize[30]] required" maxlength="30" value="${mesDriverPoints.minValue}" />
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>是否监控</label>
          <div id="divOfValidation" class="col-sm-8">
        
        <div class="pass" style="width:60px">
            <div style="margin-right: 15px;">
                <input type="radio" name="validation" class="validate[required]" id="adopt" value="1" checked style="float: left;"/>
                <label for="inputText">&nbsp;是</label>
            </div>
        </div>
        <div class="nopass">
            <div style="margin-left: 15px;">
                <input type="radio" name="validation" class="validate[required]" id="notthrough" value="0" style="float: left;" />
                <label for="inputText" style="float: left;">&nbsp;否</label>
            </div>
        </div>
          </div>
        </div>
    </div>
    </div>
    <div class="modal-footer" style="margin-top: -15px">
      <button id="submit" type="submit" class="btn btn-primary">保存</button>
      <button id="close" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
  </form>
<!-- </div> -->
<!-- Modal -->
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
  $(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('MesPointTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#MesPointTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesPoints[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
