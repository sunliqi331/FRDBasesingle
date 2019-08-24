<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
jQuery(document).ready(function(){
	var arr=new Array(); 
	for(var i = 0;i < $(".cbr").size();i++){
	    arr[i] = $("*[name='permissions["+i+"].sn']").val();
	}
	var submitStatus = new Array();
	function checkValue(){
		for(var i = 0;i < arr.length;i++){
	        if ($("#operateName").val() == arr[i]) {
	            $("#divOfOperateName").find("div.parentFormformID").remove();
	            $("#divOfOperateName").find("div.snformError").remove();
	            $("#operateName").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>*操作名称不可重复<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
	            $("#operateName").focus();
	            submitStatus.push("operateName");
	            return;
	          } else {
	            $("#divOfOperateName").find("div.parentFormformID").remove();
	            submitStatus.length=0;
	            }
	    }
	}
    $.checkbox.initCheckbox('#createModuleForm');
    var $fieldset = $("#createNewPermissonInput");
    
    var $name = $("input[name=_name]",$fieldset);
    var $sn = $("input[name=_sn]",$fieldset);
    var $description = $("input[name=_description]",$fieldset);
    
    $("#operateName").keyup(checkValue);
    $("#createNewPermission").click(function(){
    	 checkValue();
      if(submitStatus.length>0){
         checkValue();
     }else{
    	 CreateNewPermission();
     }
    });
    
//     $("#createNewPermission").click(function(event){
      function CreateNewPermission(){
      var nameValidate = !$name.validationEngine('validate');
      var snValidate = !$sn.validationEngine('validate');
      var descriptionValidate = !$description.validationEngine('validate');
      
     // 验证
      if (!nameValidate || !snValidate || !descriptionValidate) {
        return false;
      }
      
      var $toNewPermission = $("#createModuleForm div.toNewPermission");
      // 判断是否有定义权限
      var maxId = 0;
      if ($("input:last", $toNewPermission).length > 0) {
        maxId = parseInt($("input:last", $toNewPermission).attr("rel")) + 1;  
      }
      
      var htmls = [];
      htmls.push('<label><input class="cbr" type="checkbox" name="permissions[' + maxId + '].sn" value="' + $sn.val() + '" checked="checked" rel="' + maxId + '"/>');
      htmls.push('<input type="hidden" name="permissions[' + maxId + '].name" value="' + $name.val() + '" rel="' + maxId + '"/>');
      htmls.push('<input type="hidden" name="permissions[' + maxId + '].description" value="' + $description.val() + '" rel="' + maxId + '"/>');
      htmls.push($name.val() + '(' + $sn.val() + ')');
      htmls.push('</label>');
      
      $toNewPermission.append(htmls.join(''));
      $.checkbox.initCheckbox($toNewPermission);
    
      $name.val("");
      $sn.val("");
      $description.val("");
      
      event.preventDefault();
      event.stopPropagation();
      
      for(var i = 0;i < $(".cbr").size();i++){
          arr[i] = $("*[name='permissions["+i+"].sn']").val();
      }
    };
    
    $("#createModuleForm").submit(function(event){
      event.preventDefault();
      event.stopPropagation();
   
      var _nameClass = $name.attr("class");
      var _snClass = $sn.attr("class");
      var _descriptionClass = $description.attr("class");
      
      $name.attr("class", "required");
      $sn.attr("class", "required");
      
       var result = validateCallback(this, dialogReloadRel2Module);
       if (!result) {
        $name.attr("class", _nameClass);
        $sn.attr("class", _snClass);
        $description.attr("class", _descriptionClass);
       }
      return result;
    });
});
</script>
<form id="createModuleForm" method="post" action="${contextPath }/management/security/module/create" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
 
  <input type="hidden" name="parent.id" value="${parentModuleId }"/>
  <div id="createModuleFormContent" class="pageFormContent form-horizontal" layoutH="58">
   <div class="row">
    <fieldset>
    <br/>
      <h4 class="media-heading">模块信息</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <label class="control-label col-sm-4">名称</label>
        <div id="divOfModuleName" class="col-sm-6">
          <input id="moduleName" type="text" name="name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">优先级</label>
        <div class="col-sm-6">
          <input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" value="999" maxlength="3" style="width: 80px;"/>
          <span class="info">（越小越靠前）</span>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">URL</label>
        <div class="col-sm-6">
          <input type="text" name="url" class="form-control input-medium validate[required,maxSize[256]] required"  maxlength="256" alt="以#、/或者http开头"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">授权名称</label>
        <div class="col-sm-6">
          <input type="text" name="sn" class="form-control input-medium validate[required,maxSize[32]] required"  maxlength="32"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">模块类名</label>
        <div class="col-sm-6">
          <input type="text" name="className" class="form-control input-medium" maxlength="256"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">描述</label>
        <div class="col-sm-6">
          <textarea name="description" cols="29" rows="3" maxlength="256" class="form-control input-medium textarea-scroll"></textarea>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <h4 class="media-heading">自定义授权</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <div class="col-lg-12">
          <div class="checkbox toNewPermission">
            <label class="check_label">
              <input class="cbr" type="checkbox" name="permissions[0].sn" value="show" checked="checked" rel="0"/>
              <input type="hidden" name="permissions[0].name" value="看" rel="0"/>看(show)
            </label>
            <label>
              <input class="cbr" type="checkbox" name="permissions[1].sn" value="save" checked="checked" rel="1"/>
              <input type="hidden" name="permissions[1].name" value="增" rel="1"/>增(save)
            </label>
            <label>
              <input class="cbr" type="checkbox" name="permissions[2].sn" value="delete" checked="checked" rel="2"/>
              <input type="hidden" name="permissions[2].name" value="删" rel="2"/>删(delete)
            </label>
            <label>
              <input class="cbr" type="checkbox" name="permissions[3].sn" value="view" checked="checked" rel="3"/>
              <input type="hidden" name="permissions[3].name" value="查" rel="3"/>查(view)
            </label>
            <label>
              <input class="cbr" type="checkbox" name="permissions[4].sn" value="edit" checked="checked" rel="4"/>
              <input type="hidden" name="permissions[4].name" value="改" rel="4"/>改(edit)
            </label>
          </div>
        </div>
      </div>
    </fieldset>
    <fieldset id="createNewPermissonInput">
      <h4 class="media-heading">动态新增</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <label class="control-label col-sm-4">名称</label>
        <div class="col-sm-6">
          <input type="text" name="_name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">操作名称</label>
        <div id="divOfOperateName" class="col-sm-6">
          <input id="operateName" type="text" name="_sn" class="form-control input-medium validate[required,maxSize[32]] required" maxlength="32"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">描述</label>
        <div class="col-sm-6">
          <input name="_description" maxlength="256" class="form-control input-medium"/>
        </div>
      </div>
      <div class="form-group">
        <div class="control-label col-sm-8">
          <button type="button" id="createNewPermission" class="btn btn-primary" style="width:160px;">新  增</button>
        </div>
      </div>
    </fieldset>
  </div>
   </div>   
  <div class="modal-footer">
    <button id="confirm" type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script type="text/javascript">
// var submitStatus = new Array();
// function checkValue(){
//     if($("#operateName").val()!=""&&$.trim($("#operateName").val()) != ''){
//         ajaxTodo("${contextPath}/management/security/module/checkPermissionSn/" + $("#operateName").val(), function(data) {
//             checkData(data,$("#operateName"),"模块名称不可重复",$("#divOfOperateName"),$("#createModuleForm"),submitStatus,"operateName");
//         });
//       }
// }
// function checkValue1(){
//     if($("#operateName").val()!=""&&$.trim($("#operateName").val()) != ''){
//         ajaxTodo("${contextPath}/management/security/module/checkPermissionSn/" + $("#operateName").val(), function(data) {
//             checkData1(data,$("#operateName"),"模块名称不可重复",$("#divOfOperateName"),$("#createModuleForm"));
//         });
//       }
// }
// $("#operateName").keyup(checkValue);
// $("#confirm").click(function(){
//     if(submitStatus.length>0){
//         checkValue1();
//     }else{
//         $("#createModuleForm").submit();
//     }
// });
</script>