<%@ page import="com.its.common.entity.main.Module"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
  public String tree(Module module, String basePath) {
    StringBuilder builder = new StringBuilder();
    
    long pid = module.getParent() == null ? 0:module.getParent().getId();
    builder.append("{id:" + module.getId() +  ", pId:" + pid + 
        ", name:\"" + module.getName() + "\", url:\"" + basePath + "/management/security/module/data/" + module.getId() + "\", target:\"ajax\"},");
    
    for(Module o : module.getChildren()) {
      builder.append(tree(o, basePath));
    }
    return builder.toString();
  }
%>
<%
  Module module2 = (Module)request.getAttribute("module");
  String moduleTree = tree(module2, request.getContextPath());
  moduleTree = moduleTree.substring(0, moduleTree.length() - 1);
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
      var $list = $("#jbsxBox2moduleList");
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

var zNodes =[<%=moduleTree%>];
       
$(document).ready(function(){
  var t = $("#moduleTree");
  t = $.fn.zTree.init(t, setting, zNodes);
  t.expandAll(true); 
});
//-->
</script>
<div class="dr-menu">
  <ul id="moduleTree" class="ztree nav-title" data-target="table"></ul>
</div>