<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<%-- <script src="${contextPath}/js/bootstrap.js"></script> --%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script type="text/javascript">
// $("#positionSelect").change(function(){
// 	alert();
// 	alert($(this).val());
// 	if($(this).val() == "无职位"){
// 		 $("#mesCompanyPosition").append("<option value='0'>"+"无职位"+"</option>");
// 	}
// 	if($(this).val() == "有职位"){
// 		ajaxTodo("${contextPath}/personnel/getMesCompanyPosition",choosePosition);
// 	}
// });
// function choosePosition(){
// 	$.each(data,function(idx,item){
//         $("#mesCompanyPosition").append("<option value='"+ item.id +"'>"+ item.positionname +"</option>");
//     });
// };
// $(":radio[name = 'position']").click(function(){
// 	if($(this).val() == "无职位") {
// 		alert("无");
// 	}
// 	if($(this).val() == "有职位") {
// 		alert("有");
// 	}
// })
</script>

  <form method="post" id="testForm" action="${contextPath}/personnel/savePosition/${userId}" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58">
    <input type="hidden" name="user.id" value="${userId}">
	 <!--  <div class="form-group">
	  <label for="inputText" class="control-label col-sm-4">有无职位</label>
	  <div class="col-sm-6">
	    <label><input name="position" type="radio" value=""/>无职位</label>
	    <label><input name="position" type="radio" value=""/>有职位</label>
	    </div>
	    </div> -->
<!--       <div class="form-group">
        <label for="inputText" class="control-label col-sm-4">有无职位</label>
        <div class="col-sm-6">
          <select id="positionSelect" class="form-control validate[required]">
          <option value="无职位">无职位</option>
          <option value="有职位">有职位</option>
          </select>
        </div>
      </div> -->
     <div class="row">
      <div class="form-group">
        <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>职位名称</label>
        <div class="col-sm-6">
          <select id="mesCompanyPosition" name="mesCompanyPosition.id" class="form-control validate[required]">
          <option value="0">请选择职位</option>
          <c:forEach var="cposition" items="${cposition}">
          <option value="${cposition.id}"> ${cposition.positionname }</option>
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