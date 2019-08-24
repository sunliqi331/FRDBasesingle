<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
// top
jQuery(document).ready(function(){
     
    $(".deleteUserRole").click(function(){
      var userRoleId = $(this).attr("id").split("submit_")[1];
      jQuery.ajax({
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            url: '${contextPath}/personnel/delete/userCompanyRole/' + userRoleId,
            error: function() { 
           alertMsg.error('撤销角色失败！');
        },
        success: function() { 
            // 删除已分配
            var $remove = $("#userRoleRow_" + userRoleId).remove();
            var roleName = $remove.find("td").eq(0).text()
          // 添加分配
        var  $div = $('tr[class="selected"]').find("td").eq(6).find("div");
        var text = $div.text();
        $div.text(text.replace(roleName, ""));
        }
        });  
      
    });
});
</script>
<div class="form form-horizontal pageContent" layoutH="0" >
  <fieldset>
    <h4 class="media-heading">用户已分配角色</h4>
    <hr class="hr-normal"/>
    <table class="list table-role" width="100%">
      <thead>
        <tr>
          <th class="col-sm-4">角色名称</th>
          <th class="col-sm-8">操作</th>
        </tr>
      </thead>
      <tbody id="hasRoles">
        <c:if test="${empty userCompanyroles }">
          <tr>
            <td colspan="2" align="center" style="color:red;" class="col-sm-8">该用户还没有分配角色。</td>
          </tr>
        </c:if>
        <c:forEach var="item" items="${userCompanyroles}">
          <tr id="userRoleRow_${item.id}">
            <td class="col-sm-4">${item.mesCompanyRole.name}</td>
            <td class="col-sm-8">
              <button class="btn btn-role" >${item.priority}</button>
              <button id="submit_${item.id}" class="deleteUserRole btn btn-warning">撤销</button>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <br/>
  </fieldset>
</div>