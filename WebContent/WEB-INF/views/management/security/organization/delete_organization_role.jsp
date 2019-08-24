<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
<!--
// top
jQuery(document).ready(function(){
     
    $(".deleteOrganizationRole").click(function(){
      var organizationRoleId = $(this).attr("id").split("submit_")[1];
      jQuery.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            url: '${contextPath}/management/security/organization/delete/organizationRole/' + organizationRoleId,
            error: function() { 
           alertMsg.error('删除角色关联失败！');
        },
        success: function() { 
            // 删除已分配
            var $remove = $("#organizationRoleRow_" + organizationRoleId).remove();
            var roleName = $remove.find("td").eq(0).text()
          // 添加分配
        var  $div = $('tr[class="selected"]', getCurrentNavtabRel()).find("td").eq(1).find("div");
        var text = $div.text();
        $div.text(text.replace(roleName, ""));
        }
        });  
      
    });
});
//-->
</script>
<div class="form form-horizontal pageContent" layoutH="0" >
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
          <tr id="organizationRoleRow_${item.id}">
              <td class="col-sm-4">${item.role.name}</td>
              <td class="col-sm-8">
                <button  class="btn btn-role" >${item.priority}</button>
                <button id="submit_${item.id}" class="deleteOrganizationRole btn btn-warning">撤销</button>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </fieldset>
</div>