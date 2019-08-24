<%@page import="com.its.common.entity.main.Permission"%>
<%@page import="com.its.common.entity.main.Module"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<%!
public String role_tree(Module module, Integer[] moduleIndex) {
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
    explandBuilder.append("<div class='role_line'>"+permission.getName() + "&nbsp;<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr'/></div>");
    moduleIndex[0] = moduleIndex[0] + 1;
  }
  explandBuilder.append("</span>\n");
  
  explandBuilder.append("</td>\n");
  explandBuilder.append("</tr>\n");

  for(Module o : module.getChildren()) {
    explandBuilder.append(role_tree(o, moduleIndex));
  }
  
  return explandBuilder.toString();
}
%>
<%
  Module module2 = (Module)request.getAttribute("module");
  String cPermissonTree = role_tree(module2, new Integer[]{0});
%>
<script type="text/javascript">

// top
$(document).ready(function(){
  initRolePage('#roleCreateTree');
  $.checkbox.initCheckbox('.pageFormContent');
});

</script>

<form method="post" id="roleForm" action="${contextPath }/management/security/role/create" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <div layouth="56" class="pageFormContent" >
    <div class="form-group"></div>
    <div class="form-group">
      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>角色名称</label>
      <div id="divOfName" class="col-sm-6">
        <input type="text" id="name" name="name" class="form-control input-medium validate[required,maxSize[64] required" maxlength="64"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述</label>
      <div class="col-sm-6">
        <textarea name="description" class="form-control input-medium textarea-scroll" cols="29" rows="3" maxlength="40"></textarea>
      </div>
    </div>
    <hr class="hr-normal"/>
    <div class="row">
      <table id="roleCreateTree" class="treeTable list tree-table-striped" width="98%" style="margin-top:0">
        <tr>
          <th rowspan="100" class="treetable-th"></th>
          <th width="40%">模块名称</th>
          <th width="50%">操作权限</th>
        </tr>
        <%=cPermissonTree %>
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
function checkValue(){
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
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