<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>好友管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<%-- zTree --%>
<link href="${contextPath}/styles/ztree/css/zTreeStyle.css" rel="stylesheet" type="text/css" media="screen" />
</head>
<body scroll="no">
<input type="hidden" id="fromUser" value="<shiro:principal />" />
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i>
        <a href="${contextPath}/management/index"> 首页</a></li>
        <li>好友管理</li>
      </ol>
      <div class="main-wrap">
        <%-- <div class="tree-list-friend" layoutH="5" id="jbsxBox2friendTree">
          <c:import url="/friends/tree" />
        </div>
         <div class="collapse-trigger">
          <div class="collapse-trigger-inner">
            <span class="fa fa-outdent"></span>
          </div>
        </div> --%>
        <div class="main-body"  layoutH="0" id="jbsxBox2friendList"
          class="unitBox">
          <%--           <c:import url="/friends/list"/> --%>
          <a id="refreshJbsxBox2friendTree" rel="jbsxBox2friendTree" target="ajax" href="${contextPath}/friends/tree" style="display: none;"></a>
          <div class="">
            <div class="searchBar">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 好友查询条件
           </div>
           <div class="ishidden" >
              <form class="form-inline" method="post" action="${contextPath }/friends/friendPageData" data-target="table" onsubmit="return navTabSearch(this)">
                 <div class="form-group">
                  <label for="inputText" class="searchtitle">好友姓名</label>
                  <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_friendname" value="${param.search_LIKE_friendname}" />
                </div>
                <button type="submit" class="btn btn-info  btn-search1">搜索</button>
              </form>
              </div>
            </div>
            <div id="toolBar">
              <div class="btn-group pull-left">
              <shiro:hasPermission name="FriendList:save">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="create" refresh="true" mask="true" href="${contextPath }/friends/addfriend?pagename=addFriend">
                  <i class="fa fa-plus"></i> 
                  <span>添加好友</span>
                </a> 
                </shiro:hasPermission>
                <shiro:hasPermission name="FriendList:delete">
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids"  href="${contextPath }/friends/delFriendById/{slt_uid}" title="确认要删除?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除好友</span>
                </a>
                </shiro:hasPermission>
                 <a class="btn btn-default1 btn-tool" target="dialog"  refresh="true" onclick="javascript:$('#invitationSpan').html('')" data-target="table" rel="friendInvitation" href="${contextPath }/friends/toAcceptPage"> 
                  <i class="fa fa-eye"></i> 
                  <span>邀请消息</span> <span id="invitationSpan"></span>
                </a>
              </div>
            </div>
            <table class="table table-striped" id="table" data-field="friends"  data-url="${contextPath }/friends/friendPageData">
              <thead>
                <tr>
                   <th data-field="Number" width="22" data-align="center">序号</th>
                  <th data-checkbox="true" width="22">
 				  <input class="cbr checkboxCtrl" type="checkbox" group="ids"> 
                  </th>
                  <th data-field="username" width="100">用户名</th>
                  <th data-field="friendname" width="100">好友姓名</th>
                  <th data-field="phone"width="100">手机号</th> 
                  <th data-field="email" width="100">好友邮箱</th>                                                    
                  <!-- <th data-field="groupid" width="100">群组Id</th> -->
                </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
<!-- Modal -->
<script type="text/template" id="dialogTemp">
<div class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <i class="fa fa-edit">
          <span class="modal-title">Modal title</span>
        </i>
      </div>
      <div class="modal-body unitBox">
      </div>
    </div>
  </div>
</div>
</script>
<c:set var="ParentTitle" value="FriendManage" />
<c:set var="ModuleTitle" value="FriendList" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/styles/ztree/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>
<script src="${contextPath }/js/sockjs.js"></script>
<script src="${contextPath }/js/stomp.js"></script>
<script type="text/javascript">
	var chatClient = $.monitorSocket.connect({connectUrl:"${contextPath}/chat",connectCallback:connectCallBack});
	function connectCallBack(frame){
		chatClient.subscribe("/user/"+ $("#fromUser").val() +"/invitation", function(data){
			 var num = parseInt($("#invitationSpan").html() == '' ? 0 : $("#invitationSpan").html());
			 num++;
			 $("#invitationSpan").html(num);
			 $("#invitationSpan").addClass("friend_tips");
		    	console.log(JSON.parse(data.body).length);
		    	var invitations = JSON.parse(localStorage.getItem("invitations"));
		    	if(null == invitations){
		    		invitations = new Array();
		    	}
		    	invitations.push(JSON.parse(data.body));
		    	localStorage.setItem("invitations",JSON.stringify(invitations));
		    });
		 chatClient.subscribe("/doChat/chat/invitations", function(data){
			 if(JSON.parse(data.body).length != 0){
				 $("#invitationSpan").html(JSON.parse(data.body).length);
				    $("#invitationSpan").addClass("friend_tips");
			 }
		    });
	}
</script>
</body>
</html>