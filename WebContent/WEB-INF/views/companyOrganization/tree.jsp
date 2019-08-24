<%@page import="com.its.frd.entity.Companyinfo"%>
<%@ page import="com.its.frd.entity.Department"%>
<%@ page import="com.its.frd.service.CompanyinfoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
/**
  public String tree(Companyinfo companyinfo, String basePath) {
    StringBuilder builder = new StringBuilder();
    long pid = companyinfo.getParentid() == null ? 0:companyinfo.getParentid();
    builder.append("{id:" + companyinfo.getId() +  ", pId:" + pid + 
        ", name:\"" + companyinfo.getCompanyname() + "\", url:\"" + basePath + "/productline/productlineData/" + companyinfo.getId() + "\", target:\"ajax\"},");
    
    for(Companyinfo o : companyinfo.getSonCompanyinfo()) {
      builder.append(tree(o, basePath));
    }
    return builder.toString();
  }
*/
%>
<%
  String companyOrganizationInfoTree = (String)request.getAttribute("companyOrganizationInfo");
%>

<script type="text/javascript">
<!--
var setting = {
  view: {
    //showIcon: false
  },
  data: {
    simpleData: {
      enable:true,
      idKey: "id",
      pIdKey: "pId",
      rootPId: ""
    }
  },
  callback: {
    onClick: function(event, treeId, treeNode) {
      var $list = $("#jbsxBox2lineList");
      var rel = treeNode.url; //节点url
      var id = treeNode.id; //节点id
      
      //如果点击的是公司
      if(id.substr(0,1)=='C'){
    	  $("#userInfoTable").hide();
    	  $("#factoryInfoTable").show();
    	  $("#departmentInfoTable").show();
    	  $("#userInfoLabel").html("");
    	  $("#factoryInfoLabel").html("工厂信息");
    	  $("#departmentInfoLabel").html("部门信息");
    	  flushTable("factoryinfo","factoryinfos","${contextPath}/companyOrganization/factoryInfoData/"+id);
    	  flushTable("departmentinfo","departmentinfos","${contextPath}/companyOrganization/departmentInfoData/"+id);
    	  
      //如果点击的是工厂
      }else if(id.substr(0,1)=='F'){
    	  $("#userInfoTable").hide();
    	  $("#factoryInfoTable").show();
    	  $("#departmentInfoTable").show();
    	  $("#userInfoLabel").html("");
    	  $("#factoryInfoLabel").html("子工厂信息");
    	  $("#departmentInfoLabel").html("部门信息");
    	  flushTable("factoryinfo","factoryinfos","${contextPath}/companyOrganization/factoryInfoData/"+id);
    	  flushTable("departmentinfo","departmentinfos","${contextPath}/companyOrganization/departmentInfoData/"+id);
      
      //如果点击的是部门
      }else if(id.substr(0,1)=='D'){
    	  $("#factoryInfoTable").hide();
    	  $("#departmentInfoTable").show();
    	  $("#userInfoTable").show();
    	  $("#userInfoLabel").html("人员信息");
    	  $("#factoryInfoLabel").html("");
    	  $("#departmentInfoLabel").html("子部门信息");
   	  flushTable("departmentinfo","departmentinfos","${contextPath}/companyOrganization/departmentInfoData/"+id);
    	  flushTable("userinfo","userinfos","${contextPath}/companyOrganization/userInfoData/"+id);
          
	  //如果点击的是用户
      }else if(id.substr(0,1)=='U'){
    	  $("#factoryInfoTable").hide();
    	  $("#departmentInfoTable").hide();
    	  $("#userInfoTable").show();
    	  $("#userInfoLabel").html("人员详细信息");
    	  $("#factoryInfoLabel").html();
    	  $("#departmentInfoLabel").html();
    	  flushTable("userinfo","userinfos","${contextPath}/companyOrganization/userInfoData/"+id);
      }
    }
  }  
};

var zNodes =[<%=companyOrganizationInfoTree%>];

$(document).ready(function(){
  var t = $("#companyOrganizationInfoTree");
  t = $.fn.zTree.init(t, setting, zNodes);
  t.expandAll(true); 
  var nodes = t.getNodes();
  var pnode = nodes[0];
  t.selectNode(pnode);
});

//填充表格数据（tableid:表格id tabField:表格date-field; url:访问的url）
function flushTable(tableid,tabField,url){
	console.log("------");
	var $table = $("#"+tableid);
	$table.bootstrapTable('destroy');
    $table.attr("data-field",tabField);
    $table.attr("data-url",url);
    $.table.init(tableid, {});
}

//-->
</script>
<div class="dr-menu">
  <ul id="companyOrganizationInfoTree" class="ztree nav-title" data-target="table"></ul>
</div>