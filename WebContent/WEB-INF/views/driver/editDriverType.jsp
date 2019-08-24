<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<script>
jQuery(document).ready(function(){
  jQuery("#formID").validationEngine();
  $("#file_upload").uploadify({
      'swf'      : '<%=basePath%>styles/uploadify/scripts/uploadify.swf',    //指定上传控件的主体文件
      'uploader' : '<%=basePath%> styles/uploadify/scripts/uploadify.php', //指定服务器端上传处理文件
      'auto' : false,
      'multi' : true,
      'height' : 25,
      'width' : 50,
      'simUploadLimit' : 3,
      'wmode' : 'transparent',
      onError : function(event, queueID, fileObj) {
        swal("文件:" + fileObj.name + " 上传失败");
      }
    });
  });
</script>
<script type="text/javascript">
jQuery(document).ready(function(){
  $.checkbox.initCheckbox('#createMesDriverTypePropForm');
  var $fieldset = $("#createNewPermissonInput");
    
    var $name = $("input[name=_name]",$fieldset);
    
    $("#createNewPermission").click(function(event){
      var nameValidate = !$name.validationEngine('validate');
      
     // 验证
      if (!nameValidate) {
        return false;
      }
      
      var $toNewPermission = $("#createMesDriverTypePropForm div.toNewPermission");
      // 判断是否有定义权限
      var maxId = 0;
      if ($("input:last", $toNewPermission).length > 0) {
        maxId = parseInt($("input:last", $toNewPermission).attr("rel")) + 1;  
      }
      
      var htmls = [];
      htmls.push('<label><input class="cbr" type="checkbox" name="mesDrivertypeProperties[' + maxId + '].propertyname" value="' + $name.val() + '" checked="checked" rel="' + maxId + '"/>');
      htmls.push($name.val());
      htmls.push('</label>');
      
      $toNewPermission.append(htmls.join(''));
      $.checkbox.initCheckbox($toNewPermission);
    
      $name.val("");
      
      event.preventDefault();
      event.stopPropagation();
    });
    
});
</script>

  <form method="post" id="typeForm" action="${contextPath}/drivertype/saveDriverType" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, dialogReloadCallback);">
<!--   <fieldset>
  <h4 class="media-heading">设备类型信息</h4> -->
     <div class="pageFormContent">
  		<input type="hidden" name="id" value="${mdt.id}"/>
      <div class="row">  
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型名称</label>
          <div id="divOfTypename" class="col-sm-6">
            <input type="text" id="typename" name="typename" class="form-control input-medium validate[required,maxSize[20]] required" maxlength="20" value="${mdt.typename }"/>
          </div>
        </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备图片</label>
        <div class="col-sm-6">
          <div class="a-upload">
          <input readOnly="true" type='text' name='textfield' id="textfield1" class='txt' style="margin-top: -5px" /> 
          <span  class='btn_up' onclick="javascript:openBrowse();"  >
          <i class="fa fa-folder-open"></i> 选择</span>
          <input class="form-control input-medium " type="file" name="files" id="file" style="display:none" onchange="document.getElementById('textfield1').value=this.value"></div>
        </div>
      </div>
      
				  <c:if test="${bool}">
      <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">当前设备图片 </label>
			<c:forEach var="item" items="${typeFile}">
				<div class="license">
					<img src="${contextPath }/company/showPic/${item.id}">
				</div>
			</c:forEach>
		</div>
		</c:if>
		<div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">
                  </label>
                  <span>(注:图片尺寸280*210,图片格式请选择jpg/png/jpeg)</span>
                  </div>
<!--       </fieldset> -->
<%--     <fieldset>
      <h4 class="media-heading">属性信息</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <div class="col-lg-12">
      <c:forEach var="p" items="${mdtp }" varStatus="s">
      <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">设备类型属性</label>
          <div class="col-sm-6">
            <input type="text" name="mesDrivertypeProperties[${s.index}].propertyname" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${p.propertyname }"/>
            <input type="hidden" name="mesDrivertypeProperties[${s.index}].id" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${p.id }"/>
          </div>
        </div>
      </c:forEach>
          </div>
        </div>
    </fieldset> --%>
    </div>
    </div>
  <div class="modal-footer">
    <button type="button" id="editBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>

</form>

<script type="text/javascript">
function openBrowse(){ 
	var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	if(ie){ 
	document.getElementById("file").click(); 
	//document.getElementById("filename").value=document.getElementById("file").value;
	}else{
	var a=document.createEvent("MouseEvents");//FF的处理 
	a.initEvent("click", true, true);  
	document.getElementById("file").dispatchEvent(a); 
	} 
	}
	$("#editBtn").click(
			function() {
				var file = $("#file").val();
				if(file.length > 30){
					swal("错误", "图片名过长,请重新上传！", "error");
				}else if (file != "") {
					var FileExt = file.replace(/.+\./, "");
					var fileSize = $("#file")[0].files[0].size;
					fileSize = fileSize / 1024;
					if (FileExt === "jpg" || FileExt === "png"
							|| FileExt === "jpeg" || FileExt === "JPG"
							|| FileExt === "PNG" || FileExt === "JPEG") {
						if (fileSize > "200") {
							swal("错误", "图片过大,请重新上传！", "error");
						} else {
								 if(submitStatus1.length>0){
								    	checkValue1();
								 }else{
								        $("#typeForm").submit();
								 }
						}
					} else {
						swal("错误", "不支持该文件格式！", "error");
					}
				} else {
					if(submitStatus1.length>0){
				    	checkValue1();
				 }else{
				        $("#typeForm").submit();
				 }
				}
			});
	var oldName = $("#typename").val();
	var submitStatus1 = new Array();
	$("#typename").keyup(checkValue);
	 function checkValue(){
		 if($("#typename").val()==oldName){
				$("#divOfTypename").find("div.parentFormformID").remove();
				$("#divOfTypename").find("div.snformError").remove();
				submitStatus1.length=0;
			}else if($("#typename").val()!=""&&$.trim($("#typename").val()) != ''){
		    	ajaxTodo("${contextPath}/drivertype/checkTypename/"+$("#typename").val(), function(data) {
		            checkData(data,$("#typename"),"设备类型名称不可重复",$("#divOfTypename"),$("#typeForm"),submitStatus1,"name");
		        });
		      }
		  };
		function checkValue1(){
			if($("#typename").val()!=""&&$.trim($("#typename").val()) != ''){
		    	ajaxTodo("${contextPath}/drivertype/checkTypename/"+$("#typename").val(), function(data) {
		            checkData1(data,$("#typename"),"设备类型名称不可重复",$("#divOfTypename"),$("#typeForm"));
		        });
		      }
		  }; 
</script>
