<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
<!--
// top
jQuery(document).ready(function(){
     
  $(".assignRole").click(function(){
    var roleId = $(this).attr("id").split("submit_")[1];  
    var $roleRow = $("#organizationRoleRow_" + roleId);
    var priority = $("#priority_" + roleId, $roleRow).val();
  
    jQuery.ajax({
      type: 'POST',
      contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
      url: '${contextPath}/management/security/organization/create/organizationRole?organization.id=${organizationId}&role.id=' + roleId + '&priority=' + priority,
      error: function() { 
         alertMsg.error('分配角色失败！');
      },
      success: function() { 
        // 删除已分配
        var $remove = $roleRow.remove();
        var roleName = $remove.find("td").eq(0).text()
        // 添加分配
        $("#hasRoles").append("<tr><td class='col-sm-4'>" + roleName + "</td><td class='col-sm-8'><button class='btn btn-role'>" + priority + "</button></td></tr>");
        $('tr[class="selected"]', getCurrentNavtabRel()).find("td").eq(1).find("div").append(roleName + "  ");
      }
    });  
  });
    
});
//-->
</script>
<div class="form form-horizontal pageContent" layoutH="0">
  <fieldset>
    <h4 class="media-heading">组织已分配角色</h4>
    <hr class="hr-normal"/>
    <table class="list table-role" width="100%">
      <thead>
        <tr>
          <th class="col-sm-4">角色名称</th>
          <th class="col-sm-8">优先级（数值越小，优先级越高）</th>
        </tr>
      </thead>
      <tbody id="hasRoles">
        <c:forEach var="item" items="${organizationRoles}">
          <tr>
            <td class="col-sm-4">${item.role.name}</td>
            <td class="col-sm-8"><button class="btn btn-role">${item.priority}</button></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </fieldset>
  <fieldset>
    <h4 class="media-heading" style="margin-top:26px;">组织可分配角色</h4>
    <hr class="hr-normal"/>
    <table class="list table-role" width="100%">
      <thead>
        <tr>
          <th class="col-sm-4">角色名称</th>
          <th class="col-sm-8">优先级（数值越小，优先级越高）</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="item" items="${roles}">
        <tr id="organizationRoleRow_${item.id}">
            <td class="col-sm-4">${item.name}</td>
            <td class="col-sm-8">
              <input type="text" id="priority_${item.id}" name="priority" value="999" maxlength="3" class="btn btn-role">
              <button id="submit_${item.id}" class="btn btn-success assignRole">分配</button>
            </td>
          </tr>  
        </c:forEach>
      </tbody>
    </table>
  </fieldset>
</div>