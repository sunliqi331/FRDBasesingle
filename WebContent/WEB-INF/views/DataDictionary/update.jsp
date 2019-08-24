<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

<form method="post" id="dicForm" action="${contextPath}/DataDictionary/update" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
  <input type="hidden" name="id" value="${DataDictionary.id}"/>
  <input type="hidden" name="type" value="${DataDictionary.type}"/>
  <c:if test="${DataDictionary.type == 'ITEM' }">
  <input type="hidden" name="parent.id" value="${DataDictionary.parent.id}"/>
  </c:if>
  <div class="pageFormContent" layoutH="58">
  <div class="row">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>名称</label>
      <div id="divOfName"class="col-sm-5">
        <input type="text" id="name" name="name" class="form-control input-medium validate[required,maxSize[64]] required" maxlength="64" value="${DataDictionary.name}"/>
      </div>
    </div>
<!--     <div class="form-group"> -->
<!--       <label for="inputText" class="control-label col-sm-4">序号</label> -->
<!--       <div class="col-sm-5"> -->
<%--         <input type="text" name="id" class="form-control input-medium validate[required,maxSize[20]] required" maxlength="20" value="${DataDictionary.id}"/> --%>
<!--       </div> -->
<!--     </div> -->
    <c:choose>
    <c:when test="${param.pid != null }">
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">内容</label>
      <div class="col-sm-5">
        <input type="text" name="value" class="form-control input-medium validate[maxSize[256]]" maxlength="256" value="${DataDictionary.value}"/>
      </div>
    </div>  
    </c:when>
    <c:otherwise>
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-4">描述</label>
      <div class="col-sm-5">
        <input type="text" name="value" class="form-control input-medium validate[maxSize[40]]" maxlength="40" value="${DataDictionary.value}"/>
      </div>
    </div>  
    </c:otherwise>
  </c:choose>  
   </div>
   </div>
    <div class="modal-footer">
      <button type="button" id="Btn" class="btn btn-primary"><i class="icon-save"></i> 保存</button>
      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
 
</form>

<script type="text/javascript">
var oldName = $("#name").val();
var submitStatus1 = new Array();
function checkValue(){
	if($("#name").val()==oldName){
		$("#divOfName").find("div.parentFormformID").remove();
		$("#divOfName").find("div.snformError").remove();
		submitStatus1.length=0;
	}else if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/DataDictionary/checkName/"+$("#name").val(), function(data) {
            checkData(data,$("#name"),"字典名称不可重复",$("#divOfName"),$("#dicForm"),submitStatus1,"name");
        });
      }
  };
function checkValue2(){
	if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
    	ajaxTodo("${contextPath}/DataDictionary/checkName/"+$("#name").val(), function(data) {
            checkData1(data,$("#name"),"字典名称不可重复",$("#divOfName"),$("#dicForm"));
        });
      }
  }; 
  $("#name").keyup(checkValue);
$("#Btn").click(function(){
	 if(submitStatus1.length>0){
	    	checkValue2();
	 }else{
	        $("#dicForm").submit();
	 }
});
</script>