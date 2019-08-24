<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.its.common.utils.SecurityUtils"%>

 <%
  //pageContext默认是保存在page(本页面有效)范围
  pageContext.setAttribute("menuModule", SecurityUtils.getShiroUser().menuMap);
%>


<!--sidebar start-->
 <div class="sidebar-header1 t-center">

 <!-- <img class="text-logo" src="${contextPath}/styles/img/meslogo.png" width="128" alt="" /> -->
 <img class="text-logo" src="${contextPath}/styles/img/meslogo.png" style="height:41px;width:268px;" alt="" />
 <ul class="phone_top">  
  <li><a href="${contextPath}/logout"><i class="fa fa-power-off"></i> 注销</a></li> 
  <li><a href="${contextPath}/management/index"><i class="fa fa-cog"></i> 返回平台</a></li>
  <li><a href="${contextPath}/index"><i class="fa fa-home"></i> 返回首页</a></li>
 </ul>

  </div>
<div id="sidebar"  class="nav-collapse">
  <!--logo start-->
  <div class="sidebar-header t-center">

 <img class="text-logo" src="${contextPath}/styles/img/meslogo.png" style="height:41px;width:268px;" alt="" ><span style="color:#fff;display:none;">MES</span><span style="color:#fff;display:none;">平台</span>

  </div>
  <!--logo end-->
  <div id="sidebar_s">
    <div class="collapse">
      <div class="toggleCollapse"><div><span style="color:#fff">MES</span></div></div>
    </div>
  </div>
  <!-- sidebar menu start-->
  <ul class="sidebar-menu">
   <div class="log-arrow-up"></div>
    <c:forEach var="level1" items="${menuModule['menuModule'].children }">
      <li class="sub-menu" title="${level1.sn }">
        <a href="javascript:;" class="">
        <span class="arrow"></span>
          <c:choose>
            <c:when test="${level1.sn == 'Security' }">
              <i class="fa fa-bell"></i>
            </c:when>
            <c:when test="${level1.sn == 'System' }">
              <i class="fa fa-cog"></i>
            </c:when>
            <c:when test="${level1.sn == 'Component' }">
              <i class="fa fa-sitemap"></i>
            </c:when>
            <c:when test="${level1.sn == 'Company' }">
              <i class="fa fa-list"></i>
            </c:when>
            <c:when test="${level1.sn == 'Sample' }">
              <i class="fa fa-desktop"></i>
            </c:when>
             <c:when test="${level1.sn == 'FriendManage' }">
              <i class="fa fa-group"></i>
            </c:when>
            <c:when test="${level1.sn == 'myself' }">
              <i class="fa fa-user"></i>
            </c:when>
            <c:when test="${level1.sn == 'Driver' }">
              <i class="fa fa-wrench"></i>
            </c:when>
            <c:when test="${level1.sn == 'SubSystem' }">
              <i class="fa fa-cogs"></i>
            </c:when>
            <c:when test="${level1.sn == 'monitorManage' }">
              <i class="fa fa-video-camera"></i>
            </c:when>
            <c:when test="${level1.sn == 'points' }">
              <i class="fa fa-lightbulb-o"></i>
            </c:when>
            <c:when test="${level1.sn == 'Count' }">
              <i class="fa fa-area-chart"></i>
            </c:when>
             <c:when test="${level1.sn == 'Alarm' }">
              <i class="fa fa-bell"></i>
            </c:when>

            <c:otherwise>
              <i class="fa fa-th"></i>
            </c:otherwise>
          </c:choose>
          <span>${level1.name }</span>
          
        </a>
        <ul class="sub">
          <c:forEach var="level2" items="${level1.children }">
            <li title="${level2.sn }">
              <a class="" href="${contextPath}${level2.url }" target="_self">${level2.name }</a>
            </li>
          </c:forEach>
        </ul>
      </li>
    </c:forEach>
  </ul>
  <!-- sidebar menu end-->
</div>

<script  type="text/javascript">
//   $(document).ready(function(){
// 	 $(".tooltips").after("<span style='color:#fff'>MES</span>"); 
//   });
</script>
<!--sidebar end-->