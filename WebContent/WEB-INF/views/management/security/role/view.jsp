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
  
  explandBuilder.append("<td><span class=\"button chk checkbox_false_full setAll\"/>" + module.getName() + "</td>\n");
  explandBuilder.append("<td>\n");
  
  for(Permission permission : module.getPermissions()) {
    boolean isFind = false;
    for(RolePermission rolePermission : role.getRolePermissions()) {
      if (rolePermission.getPermission().getId().equals(permission.getId())) {
        explandBuilder.append("<div class='role_line'><span class=\"pmn\">" + permission.getName() + "&nbsp;</span><span class=\"button chk checkbox_true_full pmnc\"/></div>");
        isFind = true;
        break;
      }
    }
    
    if (isFind == false) {
      explandBuilder.append("<div class='role_line'><span class=\"pmn\">" + permission.getName() + "&nbsp;</span><span class=\"button chk checkbox_false_full pmnc\"/></div>");
    }
    moduleIndex[0] = moduleIndex[0] + 1;
  }
  
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

<!--
// top
$(document).ready(function(){
  //初始化treeTable
  var option = {
          theme:'default',
          expandLevel : 3
    };
    $('.treeTable').treeTable(option);
  
  // 初始化全选状态
  $(".setAll").each(function(){
      var $td = $(this).parent().nextAll("td");
    var notPickLength = $("span[class*=checkbox_false_full]", $td).length;
    if (notPickLength == 0) {
      $(this).attr("class", "button chk checkbox_true_full setAll");
    }
  });
});

//-->
</script>
<form action="#" class="form form-horizontal">
  <div class="pageFormContent" layoutH="56">
  <div class="row">
  <div class="form-group" >
      <label class="control-label col-sm-4">名称</label>
      <div class="col-sm-6">
        <input type="text" name="name" class="form-control input-medium required" value="${role.name }" readonly="readonly"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述</label>
      <div class="col-sm-6">
        <textarea name="description" class="form-control input-medium" maxlength="256" readonly="readonly">${role.description }</textarea>
      </div>
    </div>
    <hr class="hr-normal"/>
    <div class="row" style="margin-top: 15px;">
      <table class="treeTable list tree-table-striped" width="98%" style="margin-top:0">
        <tr>
          <th rowspan="100" class="treetable-th"></th>
          <th width="40%">模块名称</th>
          <th width="60%">操作权限</th>
        </tr>
        <%=uPermissonTree %>
      </table>
    </div>
  </div>
  </div>

  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
<script type="text/javascript">
// $(document).ready(function(){
	
// 	alert($("tr#40").Height());
// 	$(".default_vertline").each(function(){
// 		   $(this).css("height","100");
//  		 });	
	
//});
</script>
