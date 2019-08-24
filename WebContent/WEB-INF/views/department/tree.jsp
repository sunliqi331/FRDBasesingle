<%@ page import="com.its.frd.entity.Department"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
  public String tree(Department department, String basePath) {
    StringBuilder builder = new StringBuilder();
    
    long pid = department.getDepartment() == null ? 0:department.getDepartment().getId();
    builder.append("{id:" + department.getId() +  ", pId:" + pid + 
        ", name:\"" + department.getName() + "\", url:\"" + basePath + "/department/data/" + department.getId() + "\", target:\"ajax\"},");
    
    for(Department o : department.getDepartments()) {
      builder.append(tree(o, basePath));
    }
    return builder.toString();
  }
%>
<%
  Department department2 = (Department)request.getAttribute("department");
  String departmentTree = tree(department2, request.getContextPath());
  departmentTree = departmentTree.substring(0, departmentTree.length() - 1);
%>
<script type="text/javascript">
<!--
var setting = {
  view: {
	  dblClickExpand: true,//双击展开
      showLayer: false,
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
      var $list = $("#jbsxBox2departmentList");
      var rel = treeNode.url;
      $.table.setCurrent($(event.currentTarget).attr("data-target") || $(event.currentTarget).parents('[data-target]').attr("data-target"));
      rel && $.table.refreshCurrent(rel,null,function(data){
        // 表格数据更新后，同时更新需要更新的链接
        $('[dynamic-url]', $list).each(function(){
          var $this = $(this);
          var dynamicUrl = $this.attr('dynamic-url');
          if (dynamicUrl && data[dynamicUrl]) {
            var urlAttr = $this.is('form') ? 'action' : 'href';
            var matchs = $this.attr(urlAttr).match(/[\w\/]+\//);
            matchs && ($this.attr(urlAttr, matchs[0] + data[dynamicUrl]));
          }
        });
      });
      if ($(window).width() < 480) {
        $(event.currentTarget).closest('.fold-wrap').addClass('folded');
      }
      event.preventDefault();
    }
  }  
};

var zNodes =[<%=departmentTree%>];
$(document).ready(function(){
  var t = $("#departmentTree");
  t = $.fn.zTree.init(t, setting, zNodes);
  t.expandAll(true); 
  var nodes = t.getNodes(); 
  var pnode = nodes[0];
  t.selectNode(pnode);
});
</script>
<div class="dr-menu">
  <ul id="departmentTree" class="ztree nav-title" data-target="table"></ul>
</div>