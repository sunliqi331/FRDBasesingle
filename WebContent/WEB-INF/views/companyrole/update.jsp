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
  
  //module头名
  explandBuilder.append("<td><span ref=\"treeChk\" class=\"button chk checkbox_false_full\"/><span ref=\"allChk\" class=\"button chk checkbox_false_full setAll\"/>" + module.getName() + "</td>\n");
  explandBuilder.append("<td>\n");
  
  explandBuilder.append("<span class='inputValueRole'>");
  //module下的permission循环
  for(Permission permission : module.getPermissions()) {
	//给permission中的name加上checkbox
    explandBuilder.append("<div class='role_line'>"+permission.getName() + "&nbsp;<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr' ");
    boolean isFind = false;
    List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
    //循环公司角色role中的permission
    for(MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
      //如果公司中permission的id和module中的permission的id相同,则打钩
      if(rolePermission.getPermission().getId().equals(permission.getId())) {
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


public String role_tree2(Module module, MesCompanyRole role,MesCompanyRole role2, Integer[] moduleIndex) {
	StringBuilder explandBuilder = null;
	MesCompanyRolePermission oldPermission = new MesCompanyRolePermission();
	  if (module.getParent() != null) {
	    explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\" pId=\"" + module.getParent().getId() + "\">\n");
	  } else {
	    explandBuilder = new StringBuilder("<tr id=\"" + module.getId() + "\">\n");
	  }
	  
	  //module头名
	  String tempStr = "";
	  String tempStr2 = "";
	  
	  //module下的permission循环
	  for(Permission permission : module.getPermissions()) {
		//给permission中的name加上checkbox
	    boolean isFind = false;
		boolean isFind2 = false;
	    List<MesCompanyRolePermission> rolePermissions = new ArrayList<MesCompanyRolePermission>();
	    //循环当前登录用户role中的permission
	    for(MesCompanyRolePermission rolePermission : role.getRolePermissions()) {
	      //如果公司中permission的id和module中的permission的id相同,则显示出该项
	      if(rolePermission.getPermission().getId().equals(permission.getId())) {
	    	tempStr2 +="<div class='role_line'>"+ permission.getName() + "&nbsp;<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr' ";
	    	//tempStr2 +=  "checked='checked'/>";
	    	
	    	//tempStr2 += "<input type='hidden' name='rolePermissions[" + moduleIndex[0] + "].id' value='" + rolePermission.getId() + "'/>";
	    	//如果选中的公司角色中的permission的id和当前登录用户的permission的id相同则勾选
	    	for(MesCompanyRolePermission rolePermission2 : role2.getRolePermissions()) {
	    		if(rolePermission2.getPermission().getId().equals(permission.getId())) {
		    		//tempStr2 += permission.getName() + "<input type='checkbox' name='rolePermissions[" +  moduleIndex[0] + "].permission.id' value='" + permission.getId() + "' class='cbr' ";
			    	tempStr2 +=  "checked='checked'/></div>";
			    	//tempStr2 += "<input type='hidden' name='rolePermissions[" + moduleIndex[0] + "].id' value='" + rolePermission.getId() + "'/>";
		    		//tempStr2 +=  "/>";
		    		isFind2 = true;
		    		oldPermission = rolePermission2;
		    		break;
	    		}
	    	}
	    	tempStr2 += "<input type='hidden' name='rolePermissions[" + moduleIndex[0] + "].id' value='" + oldPermission.getId() + "'/></div>";
	        isFind = true;
	        break;
	      }
	    }
	    
	    if (isFind == true) {
			  tempStr =  "<td><span ref=\"treeChk\" class=\"button chk checkbox_false_full\"/><span ref=\"allChk\" class=\"button chk checkbox_false_full setAll\"/>" + module.getName() + "</td>\n";
			  tempStr += "<td>\n";
			  tempStr += "<span class='inputValueRole'>";
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
	    explandBuilder.append(role_tree2(o, role,role2, moduleIndex));
	  }
	  return explandBuilder.toString();
	}
%>
<%
  String t = (String)request.getAttribute("Admin");
  Module module2 = (Module)request.getAttribute("module"); 
  MesCompanyRole role2 = (MesCompanyRole)request.getAttribute("roles"); //当前登录用户角色
  MesCompanyRole role1 = (MesCompanyRole)request.getAttribute("mesCompanyRole");
  String uPermissonTree = null;
  if(t.equals("0")){
    uPermissonTree = role_tree(module2, role1, new Integer[]{0});
  }else{
    uPermissonTree = role_tree2(module2, role2,role1, new Integer[]{0});
  }
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

<form method="post" id="roleForm" action="${contextPath }/companyRole/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${mesCompanyRole.id }">
  <div class="pageFormContent" layoutH="56">
<div class="row">  
  <div class="form-group"></div>
  <input type="hidden" id="companyname" name="companyid" value="${mesCompanyRole.companyid}">
    <div class="form-group">
      <label class="control-label col-sm-4"><span class="require">*&nbsp;</span>角色名称：</label>
      <div id="divOfName" class="col-sm-6">
        <input type="text" id="name" name="name" class="form-control input-medium validate[required,maxSize[32]] required" maxlength="32" value="${mesCompanyRole.name }"/>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4">描述：</label>
      <div class="col-sm-6">
        <textarea name="description" class="form-control input-medium validate[maxSize[40]]" maxlength="40">${mesCompanyRole.description }</textarea>
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
    	ajaxTodo("${contextPath}/companyRole/checkName/"+$("#name").val()+"/"+$("#companyname").val(), function(data) {
            checkData(data,$("#name"),"角色名称不可重复",$("#divOfName"),$("#roleForm"),submitStatus1,"name");
        });
      }
  };
function checkValue1(){
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/companyRole/checkName/"+$("#name").val()+"/"+$("#companyname").val(), function(data) {
            checkData1(data,$("#name"),"角色名称不可重复",$("#divOfName"),$("#roleForm"));
        });
      }
  }; 
  $("#name").keyup(function(){
	  if($("#companyname").val() !=""){
		  checkValue();
	  }
	  });
$("#Btn").click(function(){
	 if(submitStatus1.length>0){
		 if($("#companyname").val() !=""){
	    	checkValue1();
		  }
	 }else{
	        $("#roleForm").submit();
	 }
});
</script>