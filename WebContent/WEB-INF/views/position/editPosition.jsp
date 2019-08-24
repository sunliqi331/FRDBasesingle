<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />

  <form method="post" action="${contextPath}/position/saveOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      
      <input type="hidden" name="id" value="${position.id}">
      <input type="hidden" name="companyinfo.id" value="${position.companyinfo.id}">
     <div class="pageFormContent" layoutH="58">
      <div class="row">
       <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>职位名称</label>
          <div id="divOfLinesn" class="col-sm-6">
            <input type="text" name="positionname" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="45" value="${position.positionname}"/>
          </div>
        </div>
      </div>
      </div>
      <div class="modal-footer">
        <button type="submit" class="btn btn-primary" >确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>

<!-- Modal -->
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="icon-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>