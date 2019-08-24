<%@page import="com.its.frd.entity.Companyinfo"%>
<%@ page import="com.its.frd.entity.Department"%>
<%@ page import="com.its.frd.service.CompanyinfoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
  public String tree(Companyinfo companyinfo, String basePath) {
    StringBuilder builder = new StringBuilder();
    long pid = companyinfo.getParentid() == null ? 0:companyinfo.getParentid();
    builder.append("{id:" + companyinfo.getId() +  ", pId:" + pid + 
        ", name:\"" + companyinfo.getCompanyname() + "\", url:\"" + basePath + "/company/data3/" + companyinfo.getId() + "\", target:\"ajax\"},");
    
    for(Companyinfo o : companyinfo.getSonCompanyinfo()) {
      builder.append(tree(o, basePath));
    }
    return builder.toString();
  }
%>
<%
Companyinfo companyinfo = (Companyinfo)request.getAttribute("companyinfo");
  String companyinfoTree = tree(companyinfo, request.getContextPath());
  companyinfoTree = companyinfoTree.substring(0, companyinfoTree.length() - 1);
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
      var $list = $("#jbsxBox2factoryList");
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

var zNodes =[<%=companyinfoTree%>];
       
$(document).ready(function(){
  var t = $("#companyinfoTree");
  t = $.fn.zTree.init(t, setting, zNodes);
  t.expandAll(true); 
  var nodes = t.getNodes(); 
  var pnode = nodes[0];
  t.selectNode(pnode);
});
//-->
</script>
<div class="dr-menu">
  <ul id="companyinfoTree" class="ztree nav-title" data-target="table"></ul>
</div>