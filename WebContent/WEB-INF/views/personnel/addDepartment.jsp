<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<%-- <script src="${contextPath}/js/bootstrap.js"></script> --%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

  <form method="post" id="testForm" action="${contextPath}/personnel/saveDepartment/${userId}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58">
      <div class="row">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>工厂筛选</label>
        <div class="col-sm-6">
          <select id="factory" name="factory" class="form-control validate[required] chosen-select">
          <option value="0">${currentCompanyName }(当前公司)</option>
          <c:forEach var="companyinfos" items="${companyinfos}">
          <option value="${companyinfos.id}"> ${companyinfos.companyname }</option>
          </c:forEach>
          </select>
        </div>
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>部门名称</label>
        <div class="col-sm-6">
          <select id="department" name="department.id" data-placeholder="该工厂无部门" class="form-control validate[required] chosen-select">
          <option value="0">请选择部门</option>
          <!--  
          <c:forEach var="department" items="${department}">
          <option value="${department.id}"> ${department.name }</option>
          </c:forEach>
          -->
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
<script type="text/javascript">
$(document).ready(function(){
	$("#factory").change(function(event){
        $("#department").empty();
        //$("#department").append("<option value=''>全部</option>");
        $("#department").trigger("chosen:updated");
      if($("#factory").val()!=""){
          ajaxTodo("${contextPath}/personnel/findDepartment/"+$("#factory").val(),function(data){
          	$.each(data,function(idx,item){
          	      $("#department").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
          	    });
          	$("#department").trigger("chosen:updated");
          });
      }
    });
	$("#factory").trigger('change');
	
});


</script>