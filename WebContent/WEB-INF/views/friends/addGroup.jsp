<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
  <form method="post" id="testForm" action="${contextPath}/friends/addGroup/${userId}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  	<input type="hidden" id="groupId" name="id" value="${group.id }">
    <div class="pageFormContent" layoutH="58">
    <div class="row">
      <div class="form-group">
        <label for="groupName" class="control-label col-sm-4"><span class="require">*&nbsp;</span>讨论组名称</label>
        <div class="col-sm-6">
        <input id="groupName" name="name" value="${group.name }" class="form-control validate[required,maxSize[64] required"/>
        </div>
      </div>
      <div class="form-group">
        <label for="member" class="control-label col-sm-4"><span class="require">*&nbsp;</span>讨论组成员</label>
        <div class="col-sm-6">
        <select id="member" name="userIds" class="form-control" data-placeholder="Choose a user..." multiple>
        	<option value=""></option>
        	<c:forEach items="${friendsList}" var="friend">
        		<option value="${friend.friendid }">${friend.friendname }</option>
        	</c:forEach>
        </select>
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
		
		if($("#groupId").val() != 0){
			$("#groupName").attr("readonly","readonly");
			var selectedIds = JSON.parse('${selectedIds}');
			console.log(selectedIds);
			$("#member").val(selectedIds);
			$("#member").trigger("chosen:updated");
		}
	});
</script>