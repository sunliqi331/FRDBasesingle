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
$(document).ready(function(){
    $("table").attr("data-url","${contextPath}/driver/MesPointData/"+"0"+"/"+$("#pointtypekey").val()+"/"+$("#driverId").val());
});
$("#chooseMesPointGateway").change(function(event){
    $.table.setCurrent("MesPointTable");
    $.table.refreshCurrent("${contextPath}/driver/MesPointData/"+$(this).val()+"/"+$("#pointtypekey").val()+"/"+$("#driverId").val());
});
$("#submit").click(function(){
    $.each($("#MesPointTable").find("tr.selected"),function(idx,item){
      var m = $("#mesPointIds").val().split(",");
      var M = $("#mesPointIds").val();
      var d = $(this).attr("data-uniqueid").split(",");
      var D = $(this).attr("data-uniqueid");
      if(M.length < 1){
        $("#mesPointIds").val($(this).attr("data-uniqueid") + "-" + $("#chooseMesDrivertypeProperty").val());
      }else if(m.indexOf(d) >= 0){
      }else{
        $("#mesPointIds").val(M+","+$(this).attr("data-uniqueid") + "-" + $("#chooseMesDrivertypeProperty").val());
      }
      });
  });
//     var T = $("#chooseMesDrivertypeProperty").val();
//       $("option[value='"+T+"']").remove();
$("#submit").click(function(){
  var obj = $(".parentFormformID").size();
    if(obj>0){
        
    }else{
      if($("#MesPointTable").find("tr.selected").length>0){
        $("#driverPointForm").submit();
        $.table.setCurrent($('.tab_content:visible').find('.table').attr('id'));
        }else{
        	swal("错误","请选择测点","error");
        }
      }
});
$("#minValue").keyup(function() {
	var s1 = $("#maxValue").val();
	var s2 = $("#minValue").val();
    if (eval(s1) < eval(s2)) {
      $("#driverPointForm").find("div.parentFormformID").remove();
      $("#driverPointForm").find("div.formErrorContent").remove();
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
        $("#driverPointForm").find("div.formErrorContent").remove();
      $("#maxValue").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 不能小于下限值<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
    } else {
      $("#driverPointForm").find("div.parentFormformID").remove();
    }
  });
$("#searchPoint").click(function() {
    var M = $("#searchname").val();
    $.table.setCurrent("MesPointTable");
    $.table.refreshCurrent("${contextPath}/driver/MesPointData2/"+$("#searchname").val()+"/"+$("#pointtypekey").val()+"/"+$("#driverId").val(),{},function(){
      $("#MesPointTable").bootstrapTable("checkBy",{field:'name',values:[M]});
    });
  });
$(document).ready(function(){
//     $("select").chosen({search_contains:true});
//     $(".chosen-container-single").css("width","187px");
});
</script>
<!-- <div class="pageContent" > -->
  <form method="post" id="driverPointForm" action="${contextPath}/driver/saveDriverPoint/${driverId}" class="form form-horizontal" data-target="" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58" style="padding-top:0" >
        <input type="hidden" name="companyinfo.id" id="companyid" value="${companyid}" /> 
        <input type="hidden" id="driverId" value="${driverId}" /> 
        <input type="hidden" id="pointtypekey" value="${pointtypekey}" /> 
        <input type="hidden" name="differencetype" value="1" /> 
        <input type="hidden" name="mesPointIds" id="mesPointIds" /> 
        <div class="row" style="margin: 0 15px">
        <h4 class="media-heading" style="margin-left: -15px;margin-right: -15px;">选择测点</h4>
        <hr class="hr-normal" style="margin-left: -15px;margin-right: -15px;"/>
        <div class="searchBar " style="padding:0;  border:none; width:auto; margin:0; max-width:inherit ;" >
       
        <div class="form-inline" style="padding:0;margin-top: -5px;">
        
        <div class="form-group" >
        <label for="inputText" class="searchtitle">测点名称</label>
          <input id="searchname" name="name" class="form-control searchtext driver_input validate[maxSize[45]] required" maxlength="45" />
      </div>
      <div class="form-group">
        <label for="inputText" class="searchtitle">　网关</label>
        
          <select id="chooseMesPointGateway" name="mesPointGateway.id" class="form-control validate[required]">
            <option value="0">请选择</option>
            <c:forEach var="p" items="${mesPointGateways}">
              <option value="${p.id}">${p.name}</option>
            </c:forEach>
          </select>
      </div>
      <button type="button" class="btn btn-info btn-search1 search_person" id="searchPoint" >搜索</button>
      </div>
      <div class="form-group" >
        <div class="" style="margin:0">
          <table class="table table-striped" id="MesPointTable" data-field="mesPoints">
              <%--<table class="table table-striped" id="MesPointTable" data-field="mesPoints" data-checkbox-header="false" data-single-select="true">--%>
              <thead>
              <tr>
                <th data-checkbox="true" width="22">
                <th data-field="name" width="100">测点名称</th>
                <th data-field="codekey" width="100">测点ID</th>
                <th data-field="mesPointType.name" width="100">测点类型</th>
              </tr>
            </thead>
          </table>
        </div>
     </div>
      </div>
      
     <input type="text" hidden="true">
     <c:if test="${HMK}">
     <h4 class="media-heading" style="margin-left: -15px;margin-right: -15px;">配置测点</h4>
        <hr class="hr-normal" style="margin-left: -15px;margin-right: -15px;"/>
      <div class="form-group" >
        <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>设备类型属性</label>
        <div class="col-sm-8">
          <select id="chooseMesDrivertypeProperty" name="mesDrivertype.mesDrivertypeProperty" class="form-control validate[required]">
            <c:forEach var="p" items="${MesDrivertypeProperties}">
              <option value="${p.id}">${p.propertyname}</option>
            </c:forEach>
          </select>
        </div>
      </div>
       <c:if test="${HPK}">
      <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>上限值</label>
          <div id="divOfMaxValue" class="col-sm-8"  >
            <input id="maxValue" type="number" name="maxValue" class="form-control  validate[required,maxSize[30]] required" maxlength="30" />
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>下限值</label>
          <div id="divOfMinValue" class="col-sm-8" >
            <input id="minValue" type="number" name="minValue" class="form-control  validate[required,maxSize[30]] required" maxlength="30" />
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
      </c:if>
      </c:if>
    </div>
    </div>
    <div class="modal-footer" >
      <button id="submit" type="button" class="btn btn-primary">保存</button>
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
    $("#MesPointTable").attr("data-url","${contextPath}/driver/MesPointDataInit/"+$("#pointtypekey").val()+"/"+$("#driverId").val());
    $.table.init('MesPointTable', {
      //toolbar : '#toolBar1'
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
