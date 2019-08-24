<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<div class="pageContent">
  <form method="post" action="#" class="form form-horizontal">
      <div class="pageFormContent" layoutH="58">
       <div class="row mes_see">
       <c:choose>
       <c:when test="${have}">
      <c:forEach var="p" items="${xxx}">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">${p.key}</label>
          <div class="col-sm-6">
            <input type="text" class="form-control" readonly="readonly" value="${p.value}"/>
          </div>
        </div>
      </c:forEach>
       </c:when>
       <c:otherwise>
       
       <div class="driver_data">暂无数据</div>
       </c:otherwise>
       </c:choose>
      </div>
      </div>
      <div class="modal-footer" style="margin-bottom:-10px">
        <button id="closeButton" type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>
  <br/>
</div>
<script type="text/javascript">
$(document).ready(function(){
  $(".mes_see .control-label").append("："); 
});
$("#closeButton").click(function (){
    //parent.location.reload();
})
</script>