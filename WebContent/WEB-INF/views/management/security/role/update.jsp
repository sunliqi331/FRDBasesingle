<%@page import="com.its.common.entity.main.RolePermission"%>
<%@page import="com.its.common.entity.main.Permission"%>
<%@page import="com.its.common.entity.main.Module"%>
<%@page import="com.its.common.entity.main.Role"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
public String role_tree(Module module, Role role, Integer[] moduleIndex) {
  StringBuilder explandBuilder = null;
  
  if (module.getParent() != null) {
    explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\" pId=\"" + module.getParent().getId() + "\">\n");
  } else {
    explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\">\n");
  }
  
  explandBuilder.append("<td><span ref=\"treeChk\" class=\"button chk checkbox_false_full\"/><span ref=\"allChk\" class=\"button chk checkbox_false_full setAll\"/>" + module.getName() + "</td>\n");
  explandBuilder.append("<td>\n");
  
  explandBuilder.append("<span class='inputValueRole'>");
  for(Permission permission : module.getPermissions()) {
    explandBuilder.append("<div class='role_line'>"+permission.getName() + "&nbsp;<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr' ");
    boolean isFind = false;
    for(RolePermission rolePermission : role.getRolePermissions()) {
      if (rolePermission.getPermission().getId().equals(permission.getId())) {
        explandBuilder.append("checked='checked'/></div>");
        explandBuilder.append("<input type='hidden' name='rolePermissions[" + moduleIndex[0] + "].id' value='" + rolePermission.getId() + "'/>");
        isFind = true;
        break;
      }
    }
    
    if (isFind == false) {
      explandBuilder.append("/></div>");
    }
    moduleIndex[0] = moduleIndex[0] + 1;
  }
  explandBuilder.append("</span>\n");
  
  explandBuilder.append("</td>\n");
  explandBuilder.append("</tr>\n");

  for(Module o : module.getChildren()) {
    explandBuilder.append(role_tree(o, role, moduleIndex));
  }
  
  return explandBuilder.toString();
}
%>
<%
  Module module2 = (Module)request.getAttribute("module");
  Role role2 = (Role)request.getAttribute("role");
  String uPermissonTree = role_tree(module2, role2, new Integer[]{0});
%>
<script type="text/javascript">

// top
$(document).ready(function(){
  initRolePage('#roleUpdateTree');
  $.checkbox.initCheckbox('.pageFormContent');
  // 初始化全选状态
  $(".setAll").each(function(){
    var $td = $(this).parent().nextAll("td");
    var $inputSpan = $(".inputValueRole", $td);
    var allCheckBoxLength = $("input[type=checkbox]", $inputSpan).length;
    var checkedLength = $("input:checked", $inputSpan).length; 
    if (allCheckBoxLength == checkedLength) {
      $(this).attr("class", "button chk checkbox_true_full setAll");
    }
  });
});
//
</script>
<form method="post" id="roleForm" action="${contextPath }/management/security/role/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${role.id }">
  <div class="pageFormContent" layoutH="56">
    <div class="form-group"></div>    
    <div class="form-group">
      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>角色名称</label>
      <div id="divOfName" class="col-sm-6">
        <input type="text" id="name" name="name" class="form-control input-medium validate[required,maxSize[64] required" maxlength="64" value="${role.name }"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述</label>
      <div class="col-sm-6">
        <textarea name="description" class="form-control input-medium validate[maxSize[40]" maxlength="40">${role.description }</textarea>
      </div>
    </div>
    <hr class="hr-normal"/>
    <div class="row">
      <table id="roleUpdateTree" class="treeTable list tree-table-striped" width="98%" style="margin-top:0">
        <tr>
          <th rowspan="100" class="treetable-th"></th>
          <th width="40%">模块名称</th>
          <th width="60%">操作权限</th>
        </tr>
        <%=uPermissonTree %>
      </table>
    </div>
  </div>
  
  <div class="modal-footer">
    <button type="button" id="Btn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script type="text/javascript">
var submitStatus1 = new Array();
var oldName = $("#name").val();
function checkValue(){
	if($("#name").val()==oldName){
		$("#divOfName").find("div.parentFormformID").remove();
		$("#divOfName").find("div.snformError").remove();
		submitStatus2.length=0;
	}else if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/management/security/role/checkName/"+$("#name").val(), function(data) {
            checkData(data,$("#name"),"角色名称不可重复",$("#divOfName"),$("#roleForm"),submitStatus1,"name");
        });
      }
  };
function checkValue1(){
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/management/security/role/checkName/"+$("#name").val(), function(data) {
            checkData1(data,$("#name"),"角色名称不可重复",$("#divOfName"),$("#roleForm"));
        });
      }
  }; 
  $("#name").keyup(checkValue);
$("#Btn").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue1();
	 }else{
	        $("#roleForm").submit();
	 }
});
</script>