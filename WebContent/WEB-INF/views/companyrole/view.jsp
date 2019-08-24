<%@page import="com.its.frd.entity.MesCompanyRolePermission"%>
<%@page import="com.its.common.entity.main.Permission"%>
<%@page import="com.its.common.entity.main.Module"%>
<%@page import="com.its.frd.entity.MesCompanyRole"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
public String role_tree(Module module, MesCompanyRole role, Integer[] moduleIndex) {
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
    for(MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
      if (rolePermission.getPermission().getId().equals(permission.getId())) {
        explandBuilder.append("<span class=\"pmn\">" + permission.getName() + "</span><span class=\"button chk checkbox_true_full pmnc\"/>");
        isFind = true;
        break;
      }
    }
    
    if (isFind == false) {
      explandBuilder.append("<span class=\"pmn\">" + permission.getName() + "</span><span class=\"button chk checkbox_false_full pmnc\"/>");
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

public String role_tree2(Module module, MesCompanyRole role, Integer[] moduleIndex) {
    StringBuilder explandBuilder = null;
      
      if (module.getParent() != null) {
        explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\" pId=\"" + module.getParent().getId() + "\">\n");
      } else {
        explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\">\n");
      }
      
      //module头名
      String tempStr =  "<td><span ref=\"treeChk\" class=\"button chk checkbox_false_full\"/><span ref=\"allChk\" class=\"button chk checkbox_false_full setAll\"/>" + module.getName() + "</td>\n";
      tempStr += "<td>\n";
      tempStr += "<span class='inputValueRole'>";
      String tempStr2 = "";
      
      //module下的permission循环
      for(Permission permission : module.getPermissions()) {
        //给permission中的name加上checkbox
        boolean isFind = false;
        List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
        //循环公司角色role中的permission
        for(MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
          //如果公司中permission的id和module中的permission的id相同,则打钩
          if(rolePermission.getPermission().getId().equals(permission.getId())) {
            tempStr2 += permission.getName() + "<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr' ";
            tempStr2 +=  "checked='checked'/>";
            tempStr2 += "<input type='hidden' name='rolePermissions[" + moduleIndex[0] + "].id' value='" + rolePermission.getId() + "'/>";
            
            isFind = true;
            break;
          }
        }
        
        if (isFind == false) {
            tempStr = "";
        }
        moduleIndex[0] = moduleIndex[0] + 1;
      }
      
      //添加行
      explandBuilder.append(tempStr);
      explandBuilder.append(tempStr2);
      
      explandBuilder.append("</span>\n");
      
      explandBuilder.append("</td>\n");
      explandBuilder.append("</tr>\n");

      for(Module o : module.getChildren()) {
        explandBuilder.append(role_tree2(o, role, moduleIndex));
      }
      return explandBuilder.toString();
    }
%>
<%
  Module module2 = (Module)request.getAttribute("module");
  MesCompanyRole role2 = (MesCompanyRole)request.getAttribute("role");
  String uPermissonTree = role_tree2(module2, role2, new Integer[]{0});
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
  <div class="form-group">
      <label class="control-label col-sm-4">名称：</label>
      <div class="col-sm-6">
        <input type="text" name="name" class="form-control input-medium required" value="${role.name }" readonly="readonly"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述：</label>
      <div class="col-sm-6">
        <textarea name="description" class="form-control input-medium" maxlength="256" readonly="readonly">${role.description }</textarea>
      </div>
    </div>
    <hr class="hr-normal"/>
    <div class="row">
      <table class="treeTable list" width="98%" style="margin-top:0">
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
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
