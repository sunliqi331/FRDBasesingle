<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
/* 	$("#editPropertyBtn").click(function() {
		$.table.setCurrent("mesProcedurePropertyTable");
		$("#editForm").submit();
	}); */
	$("#mesDriver").change(function(event){
		ajaxTodo("${contextPath}/procedure/getMesPointsById/" + $(this).val(), chooseMesPoints);
		 $("#mesPoints").find("option").remove();
		 $("#mesPoints").append("<option>"+ "请选择测点" +"</option>");
	});
	function chooseMesPoints(data){
		$.each(data,function(idx,item){
			$("#mesPoints").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
		});
	};
</script>

	<form method="post" id="editForm"
		action="${contextPath}/procedure/saveProcedureProperty/${procedureId}"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent" layoutH="58">
			<input type="hidden" name="id" value="${mesProcedureProperty.id}" />
<%-- 			<input type="hidden" id="mesPointsId" name="mesPoints.id" value="${mesProcedureProperty.mesPoints.id}"> --%>
			<%-- <div class="form-group">
				<label for="inputText" class="control-label col-sm-4">工序属性id</label>
				<div class="col-sm-6">
					<input type="text" name="keyid"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" value="${mesProcedureProperty.keyid}" />
				</div>
			</div> --%>
			<div class="row">
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工序属性名称</label>
				<div id="divOfPropertyname" class="col-sm-6">
					<input id="propertyname" type="text" name="propertyname"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" value="${mesProcedureProperty.propertyname}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测量方法</label>
				<div class="col-sm-6">
					<input type="text" name="controlWay"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" value="${mesProcedureProperty.controlWay}" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>上公差</label>
				<div id="divOfMaxValue" class="col-sm-6">
					<input id="maxValue" type="text" name="uppervalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="16" value="${mesProcedureProperty.uppervalues}" 
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>下公差</label>
				<div class="col-sm-6">
					<input id="minValue" type="text" name="lowervalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="16" value="${mesProcedureProperty.lowervalues}" 
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>标准值</label>
				<div class="col-sm-6">
					<input type="text" name="standardvalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="30" value="${mesProcedureProperty.standardvalues}"
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false" />
				</div>
			</div>
			<%-- <-- <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>数据类型</label>
          <div class="col-sm-6">
          <select id="datatype" name="datatype" class="form-control validate[required] required searchtext">
                <option value="">请选择数据类型</option>
               <c:forEach var="p" items="${datatype}"> 
                   <option value="${p.name}">${p.name}</option>
               </c:forEach>
            </select>
        </div>
        </div>
        <div id="unitsHide" class="form-group">
          <label for="chooseUnits" class="control-label col-sm-4"><span class="require">*&nbsp;</span>单位</label>
          <div class="col-sm-6">
          <select id="chooseUnits" name="units" class="form-control validate[required] required searchtext">
                <option value="">请选择单位</option>
               <c:forEach var="p" items="${units}"> 
                   <option value="${p.name}">${p.name}</option>
               </c:forEach>
            </select>
        </div> 
        </div>  --%>
			
		</div>
		</div>
		<div class="modal-footer">
			<button id="submit" type="submit" class="btn btn-primary">确定</button>
			<button type="button" class="btn btn-default"
				data-dismiss="modal">关闭</button>
		</div>
	</form>

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
		$("div").removeClass("modal-lg");		
		function close2upload() {
			$.table && $.table.refreshCurrent();
		}
		$.table.init('selectedMesPointsTable', {
			toolbar : '#toolBar1'
		}, function(data) {
			var $p = $('#selectedMesPointsTable').find('tbody');
			$('tr[data-uniqueid]', $p).each(function(i) {
				var $this = $(this);
				var item = data.selectedMesPoints[i];
				$this.attr('url', item.storeType + '/' + item.uuid);
			});
		});
	});
</script>