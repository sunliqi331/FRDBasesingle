<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
    
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

	<form method="post" id="addForm"
		action="${contextPath}/procedure/saveProcedureProperty/${procedureId}"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent" layoutH="58">
			<!-- <div class="form-group">
				<label for="inputText" class="control-label col-sm-4">工序属性id</label>
				<div class="col-sm-6">
					<input type="text" name="keyid"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" />
				</div>
			</div> -->
			<br>
			<div class="row">
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工序属性名称</label>
				<div id="divOfPropertyname" class="col-sm-6">
					<input id="propertyname" type="text" name="propertyname"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" />
				</div>
			</div>
<%-- 			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">设备名称</label>
				<div class="col-sm-6">
					<select id="mesDriver" name="" class="form-control ">
						<option value="0">请选择设备</option>
						<c:forEach var="mesDriver" items="${mesDriver}">
							<option value="${mesDriver.id}">${mesDriver.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4">测点名称</label>
				<div class="col-sm-6">
					<select id="mesPoints" name="mesPoints.id"
						class="form-control validate[required] ">
					</select>
				</div>
			</div> --%>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测量方法</label>
				<div class="col-sm-6">
					<input type="text" name="controlWay"
						class="form-control input-medium validate[required,maxSize[30]] required"
						maxlength="30" />
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>上公差</label>
				<div id="divOfMaxValue" class="col-sm-6">
					<input id="maxValue" type="text" name="uppervalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="16" 
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>下公差</label>
				<div class="col-sm-6">
					<input id="minValue" type="text" name="lowervalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="16" 
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>标准值</label>
				<div class="col-sm-6">
					<input type="text" name="standardvalues"
						class="form-control input-medium validate[required,custom[number],maxSize[30]] required"
						maxlength="30" 
						onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
				(event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
				</div>
			</div>
			
			<div style="width: 500px;margin-left: 230px;">
			<table class="table table-striped" id="activeProcedurePoints" 
			data-field="mesDriverPoints" data-url="${contextPath }/procedure/initActiveProcedurePoints">
            <thead>
              <tr>
                <th data-field="Number" width="22" data-align="center">序号</th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesPoints.name" width="100">可用测点</th>
                <th data-field="mesDriver.name" width="100">所属设备</th>
              </tr>
            </thead>
          </table>
          </div>
		<%-- 	<div class="form-group">
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
        </div> --%>
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
    <div class="modal-dialog" role="document" >
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
		/* $("div").removeClass("modal-lg");
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
		});  */
		var originalObject = new Object();
		$.each($.table,function(idx,obj){
			originalObject[idx] = obj;
		});
		var toolbar = {toolbar : '#toolBar1'};
		originalObject = $.extend({},originalObject,toolbar);
		originalObject._op.pageSize = 5;
		$.table.init('activeProcedurePoints', originalObject);
		
		$("#submit").bind("click",function(){
			//刷新产品工序属性表格
			console.log("=================");
			$.table.init("mesProcedurePropertyTable",{});
		});

	}); 
</script>