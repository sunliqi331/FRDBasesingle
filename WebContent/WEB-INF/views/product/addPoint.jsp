<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
	$("#mesDriver").change(function(event){
		ajaxTodo("${contextPath}/procedure/getMesPointsById/" + $(this).val(), chooseMesPoints);
		 $("#mesPoints").find("option").remove();
		 $("#mesPoints").append("<option value='0'>"+ "请选择测点" +"</option>");
	});
	function chooseMesPoints(data){
		$.each(data,function(idx,item){
			$("#mesPoints").append("<option value='"+ item.id +"'>"+ item.codekey +"</option>");
		});
	};
	$("#mesPoints").change(function(){
		$("#divOfCheck").hide();
        ajaxTodo("${contextPath}/procedure/getMesPointsNameById/" + $(this).val(), mesPointsName);
	})
    function mesPointsName(data){
        $("#pointidname").val(data);
    };
</script>
<div class="pageContent">
	<form method="post"
		action="${contextPath}/procedure/savePoint/${propertyId}"
		class="form form-horizontal"
		onsubmit="return validateCallback(this, dialogReloadCallback);">
		<div class="pageFormContent clearfix" layoutH="58">
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备名称</label>
				<div class="col-sm-6">
				<c:choose>
				<c:when test="${md.id != null }">
					<select id="mesDriver" name="" class="form-control ">
						<option value="${md.id}">${md.name }</option>
						<c:forEach var="mesDriver" items="${mesDriver}">
							<option value="${mesDriver.id}">${mesDriver.name}</option>
						</c:forEach>
					</select>
					</c:when>
					<c:otherwise>
					<select id="mesDriver" name="" class="form-control ">
						<option value="0">请选择设备</option>
						<c:forEach var="mesDriver" items="${mesDriver}">
							<option value="${mesDriver.id}">${mesDriver.name}</option>
						</c:forEach>
					</select>
					</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点ID</label>
				<div class="col-sm-6">
					<select id="mesPoints" name="pointid"
						class="form-control validate[] ">
						<option value="${property.mesPoints.id}">${property.mesPoints.codekey}</option>
						<c:forEach var="mesPoints" items="${mesPoints}">
							<option value="${mesPoints.id}">${mesPoints.codekey}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>测点名称</label>
				<div class="col-sm-6">
					<input type="text" id="pointidname" value="${property.mesPoints.name}" readonly="readonly"/>
				</div>
			</div>
			<c:if test= "${bool}">
			<div id="divOfCheck" class="form-group">
				<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>是否解绑</label>
				<div class="col-sm-6">
				<input type="checkbox" name="checkbox" value="yes"/>是
<!-- 				<input type="radio" name="radioBtn" value="yes" checked>是 -->
<!-- 				<input type="radio" name="radioBtn" value="no" >否 -->
				</div>
			</div>
			</c:if>
		</div>
		<div class="modal-footer">
			<button id="submit" type="submit" class="btn btn-primary">确定</button>
			<button type="button" class="btn btn-default"
				data-dismiss="modal">关闭</button>
		</div>
	</form>
</div>
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
		$("div").removeClass("modal-lg");
		});
</script>