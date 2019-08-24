<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

  <form method="post" action="${contextPath}/product/saveProduct" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      <div class="pageFormContent" layoutH="58">
<!--        <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品编号</label> -->
<!--           <div class="col-sm-6"> -->
<!--             <input type="text" name="productnum" class="form-control input-medium validate[required,maxSize[]] required" maxlength=""/> -->
<!--           </div> -->
<!--         </div> -->
    <div class="row">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="20"/>
          </div>
        </div>
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>产品型号</label>
          <div class="col-sm-6">
            <input type="text" name="modelnum" class="form-control input-medium validate[required,custom[onlyLetterNumber],maxSize[45]] required" maxlength="30"/>
          </div>
        </div>
<!--        <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>是否统计产量</label> -->
<!--           <div class="col-sm-6"> -->
<!--                <input type="radio" name="productionstatscode" value="1" >是 -->
<!--                <input type="radio" name="productionstatscode" value="0" checked>否 -->
<!--           </div> -->
<!--         </div> -->
<!--        <div class="form-group"> -->
<!--           <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>是否统计合格率</label> -->
<!--           <div class="col-sm-6"> -->
<!--                <input type="radio" name="qualifiedsstatscode" value="1" >是 -->
<!--                <input type="radio" name="qualifiedsstatscode" value="0" checked>否 -->
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

  });
  </script>