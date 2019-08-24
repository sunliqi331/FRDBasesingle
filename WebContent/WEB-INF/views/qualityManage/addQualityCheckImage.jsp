<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script>
jQuery(document).ready(function(){
  jQuery("#formID").validationEngine();
  $("#file_upload").uploadify({
      'swf'      : '${contextPath}/styles/uploadify/scripts/uploadify.swf',    //指定上传控件的主体文件
      'uploader' : '${contextPath}/styles/uploadify/scripts/uploadify.php', //指定服务器端上传处理文件
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
  /*
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
      
      $("#create").attr("class", "required");
    });
    */
});
</script>
<form id="typeForm" method="post" action="${contextPath}/qualityCheck/saveQualityCheckImg" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, dialogReloadCallback);">
  <div id="createMesDriverTypePropFormContent" class="pageFormContent" layoutH="58">
<!--     <fieldset>
      <h4 class="media-heading">设备类型信息</h4> -->
   <div class="row">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备类型名称</label>
        <div id="divOfTypename" class="col-sm-6">
          <input id="qualitynm" type="text" name="qualitynm" class="form-control input-medium validate[required,maxSize[20]] required" maxlength="20" />
        </div>
      </div>
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>设备图片</label>
        <div class="col-sm-6">
          <div class="a-upload">
          <input readOnly="true" type='text' name='textfield' id="textfield1" class='txt' style="margin-top: -5px" /> 
          <span  class='btn_up' onclick="javascript:openBrowse();"   >
          <i class="fa fa-folder-open"></i> 选择</span>
          <input class="form-control input-medium validate[required] required file" type="file" name="files" id="file" onchange="document.getElementById('textfield1').value=this.value"></div>
        </div>
        <!-- <span class="col-sm-8 col-sm-offset-4"  style="line-height: 30px">(注:图片尺寸280*210,图片格式请选择jpg/png/jpeg)</span> -->
      </div>
<!--       <div class="form-group"> -->
<!--                   <label for="inputText" class="control-label col-sm-4"> -->
<!--                   </label> -->
<!--                   <span >(注:上传图片不得大于200k,图片格式请选择jpg/png/jpeg)</span> -->
<!--                   </div> -->
<!--     </fieldset> -->
    <!-- <fieldset>
      <h4 class="media-heading">属性信息</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <div class="col-lg-12">
          <div class="checkbox toNewPermission">
          </div>
        </div>
      </div>
    </fieldset>
    <fieldset id="createNewPermissonInput">
      <h4 class="media-heading">动态新增</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <label class="control-label col-sm-4">属性名称：</label>
        <div class="col-sm-6">
          <input id="create" type="text" name="_name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64"/>
        </div>
      </div>
      <div class="form-group">
        <div class="control-label col-sm-8">
          <button type="button" id="createNewPermission" class="btn btn-primary" style="width:160px;">新  增</button>
        </div>
      </div>
    </fieldset> -->
  </div>
   </div>   
  <div class="modal-footer">
    <button id="addBtn" type="button" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
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
    $("#addBtn").click(
            function() {
                var file = $("#file").val();
                if(file.length > 30){
                    swal("错误", "图片名过长,请重新上传！", "error");
                }else if(file!= ""){
                var FileExt = file.replace(/.+\./, "");
                var fileSize = $("#file")[0].files[0].size;
                fileSize = fileSize / (1024*1024);
                if (FileExt === "jpg" || FileExt === "png"
                        || FileExt === "jpeg" || FileExt === "JPG"
                        || FileExt === "PNG" || FileExt === "JPEG") {
                    console.log("fileSize:" + fileSize);
                    if (fileSize > "100") {
                        swal("错误", "图片过大,请重新上传！", "error");
                    } else {
                    	/*
                        if(submitStatus1.length>0){
                                checkValue1();
                         }else{
                                $("#typeForm").submit();
                         } */
                        $("#typeForm").submit();
                    }
                } else {
                    swal("错误", "不支持该图片格式！", "error");
                }
                }else{
                    swal("错误","请上传图片！","error");
                }
            });
    /*
    var submitStatus1 = new Array();

    $("#qualitynm").keyup(checkValue);
     function checkValue(){
            if($("#qualitynm").val()!=""&&$.trim($("#qualitynm").val()) != ''){
                ajaxTodo("${contextPath}/drivertype/checkTypename/"+$("#qualitynm").val(), function(data) {
                    checkData(data,$("#qualitynm"),"设备类型名称不可重复",$("#divOfTypename"),$("#typeForm"),submitStatus1,"name");
                });
              }
          };
        function checkValue1(){
            if($("#qualitynm").val()!=""&&$.trim($("#qualitynm").val()) != ''){
                ajaxTodo("${contextPath}/drivertype/checkTypename/"+$("#qualitynm").val(), function(data) {
                    checkData1(data,$("#qualitynm"),"设备类型名称不可重复",$("#divOfTypename"),$("#typeForm"));
                });
              }
          }; */
</script>
</form>