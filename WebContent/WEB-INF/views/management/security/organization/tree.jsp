<%@page import="com.its.common.entity.main.Organization"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
  public String tree(Organization organization, String basePath) {
    StringBuilder builder = new StringBuilder();
    
    long pid = organization.getParent() == null ? 0:organization.getParent().getId();
    builder.append("{id:" + organization.getId() +  ", pId:" + pid + 
        ", name:\"" + organization.getName() + "\", url:\"" + basePath + "/management/security/organization/data/" + organization.getId() + "\", target:\"ajax\"},");
    
    for(Organization o : organization.getChildren()) {
      builder.append(tree(o, basePath));
    }
    return builder.toString();
  }
%>
<%
  Organization organization2 = (Organization)request.getAttribute("organization");
  String orgTree = tree(organization2, request.getContextPath());
  orgTree = orgTree.substring(0, orgTree.length() - 1);
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
      var $list = $("#jbsxBox2organizationList");
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

var zNodes =[<%=orgTree%>];
       
$(document).ready(function(){
  var t = $("#orgTree");
  t = $.fn.zTree.init(t, setting, zNodes);
  t.expandAll(true);
});
//-->
</script>
<div class="dr-menu">
  <ul id="orgTree" class="ztree nav-title" data-target="table"></ul>
</div>