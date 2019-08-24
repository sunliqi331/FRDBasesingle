<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
  <form method="post" id="testForm" action="${contextPath}/friends/acceptInvitation" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58">
      <div class="row">
      <div class="form-group">
        <label for="member" class="control-label col-sm-3">发来邀请的好友</label>
        <div class="col-sm-8">
        <select id="member" name="friendIds" class="form-control" data-placeholder="无新的好友邀请..." multiple>
        	<option value=""></option>
        	<c:forEach items="${chatMessages}" var="chatMessage">
        		<option value="${chatMessage.friendid }" selected>${chatMessage.fromUser }</option>
        	</c:forEach>
        </select>
<!--         发来好友邀请 -->
        </div>
      </div>
     </div>
     </div>
    <div class="modal-footer">
      <button type="submit" class="btn btn-primary">确定</button>
      <button type="button" class="btn btn-default"
        data-dismiss="modal">关闭</button>
    </div>
  </form>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script>
	$(document).ready(function(){
		$("#member").chosen({search_contains:true});
		$("#member").next().css("width","350px");
		$(".search-field .default").css("width","136px");
		
	});
</script>