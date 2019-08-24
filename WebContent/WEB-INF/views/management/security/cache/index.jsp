<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>缓存管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body scroll="no">
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <div class="panelBar" style="text-align: center; margin-top: 15%">
        <ul class="toolBar">
          <shiro:hasPermission name="Cache:edit">
            <li class="img-cache " style=""><a class="delete" style=" border: 1px solid #6db6f2; " target="ajaxTodo" href="${contextPath }/management/security/cache/clear" title="确认要清空缓存?"><span>清空缓存</span></a></li>
          </shiro:hasPermission>
        </ul>
      </div>
      <table class="list" layoutH="0" width="100%" >
        <thead>
          <tr>
            <th style="text-align: center; font-size: 20px; letter-spacing: 3px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;清除系统所有缓存。</th>
          </tr>
        </thead>
        <tbody>
    
        </tbody>
      </table>
    </div>
  </div>  
</body>
<script src="${contextPath}/styles/dwz/js/dwz.core.js" type="text/javascript"></script>
<script src="${contextPath}/styles/dwz/js/dwz.ajax.js" type="text/javascript"></script>
<script src="${contextPath}/styles/assets/sweetalert/sweetalert-dev.js"></script>
<script src="${contextPath}/js/jquery.nicescroll.js"></script>
<script src="${contextPath}/js/common.js"></script>
<script>
$(document).ready(function($){
  $('#sidebar > ul > li[title="Security"] > a').click();
  $('#sidebar .sub > li[title="Cache"]').addClass('active');
});
</script>
</body>
</html>