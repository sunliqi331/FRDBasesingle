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
    var moduleId = ${module.id };
    $.checkbox.initCheckbox('#updateModuleForm' + moduleId);
    var $fieldset = $('#updateNewPermissonInput' + moduleId);
    
    var $name = $("input[name=_name]",$fieldset);
    var $sn = $("input[name=_sn]",$fieldset);
    var $description = $("input[name=_description]",$fieldset);  
     

    $("#operateName").keyup(checkValue);
    $('#udateNewPermission' + moduleId).click(function(){
    	checkValue();
      if(submitStatus.length>0){
         checkValue();
     }else{
         CreateNewPermission();
     }
    });
    
    function CreateNewPermission(){
//     $('#udateNewPermission' + moduleId).click(function(event){
      var nameValidate = !$name.validationEngine('validate');
      var shortNameValidate = !$sn.validationEngine('validate');
      var descriptionValidate = !$description.validationEngine('validate');
      
      // 验证
      if (!nameValidate || !shortNameValidate || !descriptionValidate) {
        return false;
      }
      
      var $toNewPermission = $("#updateModuleForm" + moduleId + " div.toNewPermission");
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
    
    $('#updateModuleForm' + moduleId).submit(function(event){
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
<form id="updateModuleForm${module.id }" method="post" action="${contextPath }/management/security/module/update" class="form form-horizontal" onsubmit="">
  <input type="hidden" name="id" value="${module.id }"/>
  <div id="updateModuleFormContent${module.id }" class="pageFormContent form-horizontal" layoutH="58">
    <div class="row">
    <fieldset>
    <br/>
      <h4 class="media-heading">模块信息</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <label class="control-label col-sm-4">名称</label>
        <div class="col-sm-6">
          <input type="text" name="name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64" value="${module.name }"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">优先级</label>
        <div class="col-sm-6">
          <input type="text" name="priority" class="validate[required,custom[integer],min[1],max[999]] required" maxlength="3" style="width: 80px;" value="${module.priority }"/>
          <span class="info">（越小越靠前）</span>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">URL</label>
        <div class="col-sm-6">
          <input type="text" name="url" class="form-control input-medium validate[required,maxSize[256]] required"  maxlength="256" alt="以#、/或者http开头" value="${module.url }"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">授权名称</label>
        <div class="col-sm-6">
          <input type="text" name="sn" class="form-control input-medium validate[required,maxSize[32]] required"  maxlength="32" value="${module.sn }"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">模块类名</label>
        <div class="col-sm-6">
          <input type="text" name="className" class="form-control input-medium" maxlength="256" value="${module.className }"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">描述</label>
        <div class="col-sm-6">
          <textarea name="description" cols="29" rows="3" maxlength="256" class="form-control input-medium textarea-scroll">${module.description }</textarea>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">父模块</label>
        <div class="col-sm-6">
          <input name="parent.id" value="${module.parent.id }" type="hidden"/>
          <input type="text" name="parent.name" class="required" readonly="readonly" value="${module.parent.name }" class="input-medium"/>
          <a class="btnLook" href="${contextPath}/management/security/module/lookupParent/${module.id}" lookupGroup="parent" mask="true" title="更改父模块" width="400"><i class="fa fa-search"></i> 查找带回</a>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <h4 class="media-heading">自定义授权</h4>
      <hr class="hr-normal"/>
        <div class="form-group">
          <div class="col-lg-12">
            <div class="checkbox toNewPermission">
              <c:forEach var="p" items="${module.permissions }" varStatus="s">
                <label class="check_label">
                  <input class="cbr" type="checkbox" name="permissions[${s.index}].sn" value="${p.sn}" checked="checked" rel="${s.index}"/>
                  <input class="cbr" type="hidden" name="permissions[${s.index}].id" value="${p.id}" rel="${s.index}"/>
                  <input class="cbr" type="hidden" name="permissions[${s.index}].module.id" value="${p.module.id}" rel="${s.index}"/>
                  <input class="cbr" type="hidden" name="permissions[${s.index}].name" value="${p.name}" rel="${s.index}"/>
                  <input class="cbr" type="hidden" name="permissions[${s.index}].description" value="${p.description}" rel="${s.index}"/>
                  ${p.name}(${p.sn})
                </label>
              </c:forEach>
            </div>
          </div>
        </div>
    </fieldset>
    <fieldset id="updateNewPermissonInput${module.id }">
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
          <input type="text" name="_description" class="form-control input-medium" maxlength="256"/>
        </div>
      </div>
      <div class="form-group">
        <div class="control-label col-sm-8">
          <button type="button" id="udateNewPermission${module.id }" class="btn btn-primary" style="width:160px;">新  增</button>
        </div>
      </div>
    </fieldset>
  </div>
  </div>
      
  <div class="modal-footer">
    <button type="submit" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>