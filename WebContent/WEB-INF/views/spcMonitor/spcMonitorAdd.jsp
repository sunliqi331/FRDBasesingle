<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form method="post" id="testForm"
	action="${contextPath}/mesSpcMonitor/saveMesSpcMonitor"
	class="form form-horizontal"
	onsubmit="return validateCallback(this, dialogReloadCallback);">
	<input type="hidden" name="id" value="${mesSpcMonitor.id }">
	<div class="pageFormContent" layoutH="58">
		<div class="row">
			<div class="form-group">
              <label for="mesDriverId" class="control-label col-sm-4">检测设备
              </label>
              <div class="col-sm-6">
                <select id="mesDriverId" name="mesDriverId" data-placeholder="请选择检测设备"
                  class="form-control validate[required] chosen-select">
                  <c:forEach items="${checkDrivers }" var="map">
                   <optgroup label="${map.key }">
                  	<c:forEach items="${map.value }" var="checkDriver">
                  		<option value="${checkDriver.id }">${checkDriver.name }</option>
                  	</c:forEach>
                  	</optgroup>
                  </c:forEach>
                </select>
                <input type="hidden" id="mesDriverId_hidden" name="mesDriverName">
              </div>
            </div>
            <div class="form-group">
              <label for="productId" class="control-label col-sm-4">产品型号
              </label>
              <div class="col-sm-6">
                <select id="productId" name="productId" data-placeholder="请选择产品"
                  class="form-control validate[required] chosen-select">
                  <c:forEach items="${products }" var="product">
                  	<option value="${product.id }">${product.name }</option>
                  </c:forEach>
                </select>
                <input type="hidden" id="productId_hidden" name="productName">
             </div>
            </div>
            <div class="form-group">
              <label for="productProcedureId" class="control-label col-sm-4">产品工序
              </label>
              <div class="col-sm-6">
                <select id="productProcedureId" name="productProcedureId" data-placeholder="请选择工序"
                  class="form-control validate[required] chosen-select">
                </select>
                <input type="hidden" id="productProcedureId_hidden" name="productProcedureName">
              </div>
            </div>
            <div class="form-group">
              <label for="procedurePropertyId" class="control-label col-sm-4">被测参数</label>
              <div class="col-sm-6">
                <select id="procedurePropertyId" name="procedurePropertyId" data-placeholder="请选择工序参数"
                  class="form-control validate[required] chosen-select">
                </select>
                 <input type="hidden" id="procedurePropertyId_hidden" name="procedurePropertyName">
             </div>
            </div>
<!--             <div class="count_cg clearfix"> -->
              <div class="form-group">
                <label for="testCount" class="control-label col-sm-4">子组大小</label>
              <div class="col-sm-6">
                  <select id="subRange" name="subRange" data-placeholder="请选择子组大小"
                    class="form-control validate[required] chosen-select">
                  </select>
               </div>
              </div>
              <div class="form-group">
                <label for="subNum" class="control-label col-sm-4" >子组数量</label>
                <div class="col-sm-6">
                  <select id="subNum" name="subNum" data-placeholder="请选择子组数量"
                    class="form-control validate[required] chosen-select">
                  </select>
               </div>
              <a id="addRule" class="btn btn-primary" target="dialog" rel="add_rule" href="${contextPath}/mesSpcMonitor/toAddMesSpcMonitor/0">新增规则</a>
              </div>
              <%-- <div class="form-group">
                	<a id="addRule" class="btn btn-primary" target="dialog" rel="add_rule" href="${contextPath}/mesSpcMonitor/toAddMesSpcMonitor/0">新增规则</a>
              </div> --%>
		</div>
	</div>
	<div class="modal-footer">
		<button type="submit" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>
<script type="text/javascript"
	src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script>
	$(document).ready(function(){
		var subRangeOption;
		  for(var i = 2;i <= 25; i++){
			  subRangeOption += "<option value="+ i +">"+ i +"个</option>"
		  }
		  $("#subRange").append(subRangeOption);
		  var subNumOption;
		  for(var i = 2;i <= 100; i++){
			  subNumOption += "<option value="+ i +">"+ i +"组</option>"
		  }
		  $("#subNum").append(subNumOption);
		  $("select").chosen({search_contains:true});
		  $("select").on('chosen:ready',function(){
			  setTextValue($(this));
			  //console.log($(this).find("option:selected").text());
		  });
		  function setTextValue(obj){
			  var hiddenId = obj.attr("id")+"_hidden";
			  $("#"+hiddenId).val(obj.find("option:selected").text());
		  }
		  //通过产品ID获取该产品下的工序，并动态产生select
		  function generateProcedureSelect(productId,callback){
			  var option = "";
			  $.ajax({
				  url:"${contextPath}/statistics/generateProductProcedureSelect/"+(productId || 0),
				  dataType:"JSON",
				  type:"POST",
				  success:function(data){
						$("#productProcedureId").empty();
				  	$.each(data,function(idx,item){
				  		option += "<option value='"+ item.id +"'>"+ item.procedurename +"</option>";
				  	});
				  	$("#productProcedureId").append(option);
				  	$("#productProcedureId").trigger("chosen:updated");
				  	$("#productProcedureId").trigger("change");
				  	if(callback)
				  		callback($("#productProcedureId").val(),editView);
				  }
			  });
		  }
		  function generateProcedurePropertySelect(procedureId,callback){
			  var option = "";
			  $.ajax({
				  url:"${contextPath}/statistics/generateProcedurePropertySelect/"+(procedureId || 0),
				  dataType:"JSON",
				  type:"POST",
				  success:function(data){
					  $("#procedurePropertyId").empty();
				  	$.each(data,function(idx,item){
				  		option += "<option value='"+ item.id +"'>"+ item.propertyname +"</option>";
				  	});
				  	$("#procedurePropertyId").append(option);
				  	$("#procedurePropertyId").trigger("chosen:updated");
				  	$("#procedurePropertyId").trigger("change");
				  	if(callback)
				  		callback();
				  },
				  error:function(evt){
					  console.log("...........");
				  }
			  });
			  
		  }
		  $("#productId").change(function(){
			  setTextValue($(this));
			  generateProcedureSelect($(this).val());
			  
		  });
		  $("#mesDriverId").change(function(){
			  setTextValue($(this));
		  });
		  $("#procedurePropertyId").change(function(){
			  setTextValue($(this));
		  });
		  function editView(){
			  var mesSpcMonitorId = '${mesSpcMonitor.id}';
			  if(mesSpcMonitorId != 0){
				  $("#mesDriverId").val('${mesSpcMonitor.mesDriverId}');
				  $("#productId").val('${mesSpcMonitor.productId}');
				  $("#productProcedureId").val('${mesSpcMonitor.productProcedureId}');
				  $("#procedurePropertyId").val('${mesSpcMonitor.procedurePropertyId}');
				  $("#subRange").val('${mesSpcMonitor.subRange}');
				  $("#subNum").val('${mesSpcMonitor.subNum}');
			  }
		  }
		 
		  generateProcedureSelect($("#productId").val(),generateProcedurePropertySelect);
		  $("#productProcedureId").change(function(){
			  setTextValue($(this));
			  generateProcedurePropertySelect($(this).val());
		  });
	});
</script>