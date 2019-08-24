<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<script>
	$(document).ready(function(){
		$("#_qdasCategory").change(function(){
			  generateQdasSelect($(this).val());
		  });
		if($("#editId").val() != '0'){
			console.log($("#editId").val());
			var mesQdasCategory = '${mesQdasCategory}';
			var categoryId = JSON.parse(mesQdasCategory).id;
			$("#_qdasCategory").val(categoryId);
			generateQdasSelect(categoryId);
			$("#_id").val($("#editId").val());
		}else{
		  	generateQdasSelect($("#_qdasCategory").val());
		}
		  function generateQdasSelect(obj){
			  var option;
			  $.ajax({
				  url:'${contextPath}/qdas/generateQdasSelect',
				  data:{id:obj},
				  type:'POST',
				  async:false,
				  dataType:'JSON',
				  success:function(data){
					    $("#_id").empty();
			            $.each(data,function(idx,item){
			              option += "<option value='"+ item.id +"'>"+ item.name +"</option>";
			            });
			            $("#_id").append(option);
			            $("#_id").trigger("chosen:updated");
				  }
			  });
		  }
		 
		//表单提交提交时检测变量名是否重复
		$("#formID").submit(function(){
			var keyValue = $("#cbd").find("input").val().trim();
			if(keyValue && keyValue!=""){
				var flag = "";
				$.ajax({
					type : "POST",
					url : "${contextPath}/qdas/checkValueQualified",
					async : false,
					data : "keyValue="+keyValue+"&editId="+$("#editId").val(),
					dataType : "text",
					success : function(data){
						if(data){
							flag = data;
						}
					},
					error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
				});
				if("ok" != flag){
					swal("错误","检测到存在重复的变量名！","error");
					return false;
				}
			}
			return validateCallback(this, dialogReloadCallback);
		}); 
		
		  
	});
</script>

        <form method="post" id="formID" class="form form-horizontal" action="${contextPath}/qdas/addOrUpdate">
        <input type="hidden" id="editId" value="${id }">
        <div class="pageFormContent" layoutH="58">
              <div class="row">
        	<div class="form-group">
              <label for="id" class="control-label col-sm-4">参数类型
              </label>
               <div class="col-sm-6">
                <select name="mesqdascategory.id" id="_qdasCategory" data-placeholder="请选择参数"
                  class="form-control chosen-select">
                  <c:forEach items="${qdasCategoryList }" var="qdasCategory">
                  	<option value="${qdasCategory.id }">${qdasCategory.name }</option> 
                  </c:forEach>
                </select>
                </div>
            </div>
        	
            <div class="form-group">
              <label for="id" class="control-label col-sm-4">参数名称
              </label>
              <div class="col-sm-6">
                <select name="id" id="_id" data-placeholder="请选择参数"
                  class="form-control chosen-select">
                </select>
                </div>
            </div>
            <div class="form-group"  id="cbd">
              <label for="key" class="control-label col-sm-4">变量名
              </label>
              <div class="col-sm-6">
               	<input id="key" type="text" class="form-control input-medium validate[required]" name="keyfield" />
            </div>
            </div>
<!--               <button type="submit" class="btn btn-info btn-search1" -->
<!--                 style="width: 150px;">搜索</button> -->
                </div>
                </div>
      <div class="modal-footer">
          <button id="confirm" type="submit" class="btn btn-primary">确定</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
        </form>
        
