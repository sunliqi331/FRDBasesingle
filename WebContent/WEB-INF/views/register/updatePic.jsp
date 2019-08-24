<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

  <form method="post" id="typeForm" action="${contextPath}/register/savePic" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, reloadSubPage);">
<!--   <fieldset>
  <h4 class="media-heading">设备类型信息</h4> -->
     <div class="pageFormContent">
  		<input type="hidden" name="id" value="${user.id}"/>
  		<input type="hidden" name="username" value="${user.username}"/>
	    <div class="row">
	      <div class="form-group">
                     <label for="inputText" class="control-label col-sm-4">头像
                    </label>
                    <div class="col-sm-6">
                      <div class="a-upload">
					    <input readOnly="true" type='text' name='textfield' id="textfield2" class='txt' style="margin-top:0 " />
                        <span  class='btn_up' onclick="javascript:openBrowse2();" ><i class="fa fa-folder-open"></i> 选择</span>						
                        <input hideFocus class="form-control input-medium validate[required] required file" style="display: none" type="file" name="files" id="file2" onchange="document.getElementById('textfield2').value=this.value">
					    
					  </div>
                    </div>
      </div>
      
		<c:if test="${bool}">
      <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">当前头像 </label>
			<c:forEach var="item" items="${pic}">
				<div class="license">
					<img src="${contextPath }/company/showPic/${item.id}">
				</div>
			</c:forEach>
		</div>
		</c:if>
		<div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">
                  </label>
                  <span>(注:上传图片尺寸200*200,图片格式请选择jpg/png/jpeg)</span>
                  </div>
  </div>
  <div class="modal-footer">
    <button type="button" id="editBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
  </div>
</form>

<script type="text/javascript">
function openBrowse2(){ 
	var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	if(ie){ 
	document.getElementById("file2").click(); 
	//document.getElementById("filename").value=document.getElementById("file").value;
	}else{
	var a=document.createEvent("MouseEvents");//FF的处理 
	a.initEvent("click", true, true);  
	document.getElementById("file2").dispatchEvent(a); 
	} 
	} 

	$("#editBtn").click(
			function() {
				var file = $("#file2").val();
				if (file != "") {
					var FileExt = file.replace(/.+\./, "");
					var fileSize = $("#file2")[0].files[0].size;
					fileSize = fileSize / 1024;
					if (FileExt === "jpg" || FileExt === "png"
							|| FileExt === "jpeg" || FileExt === "JPG"
							|| FileExt === "PNG" || FileExt === "JPEG") {
						if (fileSize > "200") {
							swal("错误", "图片过大,请重新上传！", "error");
						} else {
							$("#typeForm").submit();
						}
					} else {
						swal("错误", "不支持该文件格式！", "error");
					}
				} else {
					$("#typeForm").submit();
				}
			});
</script>
