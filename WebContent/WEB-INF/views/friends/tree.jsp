<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <ul class="sidebar-menu">
  <li class="myfriend">我的好友</li>
    <c:forEach var="friend" items="${friend}">
      <li class="sub-menu" title="${friend.friendid }">
        <a href="javascript:;" class="friend">
          <span><img src="${contextPath}/styles/img/head sculpture.jpg" />&nbsp&nbsp&nbsp${friend.friendname } </span>
          <span class="arrow"></span>
        </a>
      </li>
    </c:forEach>
  </ul>

