<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
  <div class="pageFormContent form form-horizontal module_see " style="padding-bottom:0">
    <fieldset>
      <h4 class="media-heading">模块信息</h4>
      <hr class="hr-normal"/>
      <div class="form-group">
        <label class="control-label col-sm-4">名称</label>
        <div class="col-sm-6">
          <input type="text" name="name" class="form-control input-medium required" value="${module.name }" readOnly=readOnly/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">优先级</label>
        <div class="col-sm-6">
          <input type="text" name="priority" class="required" value="${module.priority }" readOnly=readOnly  style="width: 80px;"/>
          <span class="info">（越小越靠前）</span>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">URL</label>
        <div class="col-sm-6">
          <input type="text" name="url" class="form-control input-medium required" value="${module.url }" readOnly=readOnly/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">授权名称</label>
        <div class="col-sm-6">
          <input type="text" name="sn" class="form-control input-medium required"  value="${module.sn }" readOnly=readOnly/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-sm-4">描述</label>
        <div class="col-sm-6">
          <textarea name="description" cols="29" rows="3" maxlength="256" class="form-control input-medium textarea-scroll" readOnly=readOnly>${module.description }</textarea>
        </div>
      </div>
    </fieldset>
    <fieldset>
      <h4 class="media-heading">自定义授权</h4>
      <hr class="hr-normal" style="margin-bottom:5px" />
      <c:forEach var="p" items="${module.permissions }" varStatus="s">
        <fieldset>
          <h5>${s.count }.${p.name }</h5>
<%--           <h5>${p.name }</h5> --%>
         <div class="module_bottom"> 
          <div class="form-group">
            <label class="control-label col-sm-4">名称</label>
            <div class="col-sm-6">
              <input type="text" name="_name" class="form-control input-medium required" value="${p.name }" readOnly=readOnly/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-sm-4">操作名称</label>
            <div class="col-sm-6">
              <input type="text" name="_sn" class="form-control input-medium required" value="${p.sn }" readOnly=readOnly/>
            </div>
          </div>
          <div class="form-group">
            <label class="control-label col-sm-4">描述</label>
            <div class="col-sm-6">
              <input name="_description" maxlength="256" class="form-control input-medium" value="${p.description }" readOnly=readOnly/>
            </div>
          </div>
         </div>
        </fieldset>
      </c:forEach>
    </fieldset>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
  <script type="text/javascript">
jQuery(document).ready(function(){
	$(".module_bottom:last").css("border","none").css("margin","0px");
	
});

</script>
