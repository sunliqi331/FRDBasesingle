<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

  <form method="post" action="${contextPath}/product/saveProduct" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <input type="hidden" name="id" value="${mesProduct.id}"/>
      <div class="pageFormContent" layoutH="58">
<%--       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">选择工厂</label>
          <div class="col-sm-6">
           <select name="companyinfo.id" class="form-control validate[required]">
                      <option value="${mesProduct.companyinfo.id}">${mesProduct.companyinfo.companyname}</option>
                      <c:forEach var="p" items="${company }">
                        <option value="${p.id }">${p.companyname }</option>
                      </c:forEach>
            </select>
          </div>
          </div> --%>
<!--        <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品编号</label> -->
<!--           <div class="col-sm-6"> -->
<%--             <input type="text" name="productnum" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${mesProduct.productnum}"/> --%>
<!--           </div> -->
<!--         </div> -->
    <div class="row">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="20" value="${mesProduct.name}"/>
          </div>
        </div>
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品型号</label>
          <div class="col-sm-6">
            <input type="text" name="modelnum" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="30" value="${mesProduct.modelnum}"/>
          </div>
        </div>
<!--         <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>是否统计产量</label> -->
<!--           <div class="col-sm-6"> -->
<!--                <input type="radio" id="yesProductionstatscode" name="productionstatscode" value="1" >是 -->
<!--                <input type="radio" id="noProductionstatscode" name="productionstatscode" value="0" >否 -->
<%--                <input type="hidden" id="productionstatscode" value="${mesProduct.productionstatscode}" disabled> --%>
<!--           </div> -->
<!--         </div> -->
<!--         <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>是否统计合格率</label> -->
<!--           <div class="col-sm-6"> -->
<!--                <input type="radio" id="yesQualifiedsstatscode" name="qualifiedsstatscode" value="1" >是 -->
<!--                <input type="radio" id="noQualifiedsstatscode" name="qualifiedsstatscode" value="0" >否 -->
<%--                <input type="hidden" id="qualifiedsstatscode" value="${mesProduct.qualifiedsstatscode}" disabled> --%>
<!--           </div> -->
<!--         </div> -->
      </div>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-primary">确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>

  <script> 
  $(document).ready(function($){
     $(".add_attribute label").first().css("margin-left","10px") 
     $(".driver_attribute").click(function(){
        $(".attributebg").slideDown(500); 
         });

//      if($("#productionstatscode").val()=="0"){
//     	 $("#noProductionstatscode").attr("checked","true");
//     	}else{
//     	 $("#yesProductionstatscode").attr("checked","true");
//     	}

//      if($("#qualifiedsstatscode").val()=="0"){
//     	 $("#noQualifiedsstatscode").attr("checked","true");
//     	}else{
//     	 $("#yesQualifiedsstatscode").attr("checked","true");
//     	}
  });
  </script>