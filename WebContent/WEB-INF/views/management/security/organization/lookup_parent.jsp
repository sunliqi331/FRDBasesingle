<%@page import="com.its.common.entity.main.Organization"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%!
  public String tree(Organization organization, Long organizationId) {
    if (organization.getChildren().isEmpty()) {
      return "";
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("<ul class='tree'>" + "\n");
    for(Organization m : organization.getChildren()) {
      // 不显示自己及子元素
      if (organizationId.equals(m.getId())) {
        continue;
      }
      buffer.append("<li class='tree-folder'><div class='tree-folder-header'><div class='tree-folder-content'><a href=\"javascript:\" onclick=\"$.bringBack({id:'" + m.getId() + "', name:'" + m.getName() + "'})\">" + m.getName() + "</a>"+ "</div>" + "</div>"+"\n");
      buffer.append(tree(m, organizationId));
      buffer.append("</li>" + "\n");
    }
    buffer.append("</ul>" + "\n");
    return buffer.toString();
  }
%>
<%
  Organization organization = (Organization)request.getAttribute("organization");
  Long organizationId = (Long)request.getAttribute("id");
%>
  <div class="pageFormContent" layoutH="58">
    <ul>
      <li class="tree-folder"><div class="tree-folder-header"><a href="javascript:"><%=organization.getName() %></a></div>
        <%=tree(organization, organizationId) %>
      </li>
    </ul>
  </div>
 
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>