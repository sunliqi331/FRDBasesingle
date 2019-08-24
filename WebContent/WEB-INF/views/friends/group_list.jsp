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
        <li>讨论组管理</li>
      </ol>
      <div class="main-wrap fold-wrap">
        <%-- <div class="tree-list-friend" layoutH="5" id="jbsxBox2friendTree">
          <c:import url="/friends/tree" />
        </div>
         <div class="collapse-trigger">
          <div class="collapse-trigger-inner">
            <span class="fa fa-outdent"></span>
          </div>
        </div> --%>
        <div class="main-body" style="left:0px" layoutH="0" class="unitBox">
          <div class="">
            <div class="searchBar">
            <div class="search_header">
                <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 讨论组查询条件
           </div>
           <div class="ishidden" >
              <form class="form-inline" method="post" action="${contextPath }/friends/groupPageData" onsubmit="return navTabSearch(this)">
                <div class="form-group">
                  <label for="inputText" class="searchtitle">讨论组名称</label>
                   <input type="text" class="form-control searchtext" maxlength="20" id="inputText" name="search_LIKE_groupname">
                </div>
                <button type="submit" class="btn btn-info  btn-search">搜索</button>
              </form>
              </div>
            </div>
            <div id="toolBar">
              <div class="btn-group pull-left">
               <shiro:hasPermission name="group:save">
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="create" mask="true" href="${contextPath }/friends/addgroup?pagename=addgroup">
                  <i class="fa fa-plus"></i> 
                  <span>添加讨论组</span>
                </a> 
                </shiro:hasPermission>
                 <shiro:hasPermission name="group:delete">
                <a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table" rel="ids"  href="${contextPath }/friends/quitGroupById/{slt_uid}" title="确认要退出?"> 
                  <i class="fa fa-remove"></i> 
                  <span>删除讨论组</span>
                </a>
                </shiro:hasPermission>
                <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" rel="edit" mask="true" href="${contextPath }/friends/addgroup?pagename=addfriends&groupId={slt_uid}"> 
                  <i class="fa fa-user-plus"></i> 
                  <span>添加成员</span>
                </a>
              </div>
            </div>
            <table class="table table-striped" id="table" data-field="groups"  data-single-select="true" data-url="${contextPath }/friends/groupPageData">
              <thead>
                <tr>
                     <th data-field="Number" width="2%" data-align="center">序号</th>
                  <th data-checkbox="true" width="22">
                  	<!-- <input class="cbr checkboxCtrl" type="checkbox" group="ids"> -->
                  </th>
                  <th data-field="name" width="100">讨论组名称</th>
                  <th data-field="createTime" width="100">创建时间</th>
                  <th data-field="createUserName" width="130">创建人</th>
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
<c:set var="ModuleTitle" value="group" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/styles/ztree/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>
<script src="${contextPath }/js/sockjs.js"></script>
<script src="${contextPath }/js/stomp.js"></script>
<script type="text/javascript">
	/* var chatClient = $.monitorSocket.connect({connectUrl:"${contextPath}/chat",connectCallback:connectCallBack});
	function connectCallBack(frame){
		chatClient.subscribe("/user/"+ $("#fromUser").val() +"/invitation", function(data){
			 var num = parseInt($("#invitationSpan").html() == '' ? 0 : $("#invitationSpan").html());
			 num++;
			 $("#invitationSpan").html(num);
			 $("#invitationSpan").addClass("friend_tips");
		    	console.log(JSON.parse(data.body).length);
		    	localStorage.setItem("invitation",data.body);
		    });
		 chatClient.subscribe("/doChat/chat/invitations", function(data){
			 if(JSON.parse(data.body).length != 0){
				 $("#invitationSpan").html(JSON.parse(data.body).length);
				    $("#invitationSpan").addClass("friend_tips");
			 }
		    });
	} */
</script>
</body>
</html>