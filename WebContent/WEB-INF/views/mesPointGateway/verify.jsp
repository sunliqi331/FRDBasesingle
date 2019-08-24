<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
  var temp = /[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/;

  $("#addBtn").click(function() {
            var macs = new Array();
            macs = $("#mac").val().split("-");
            /* alert(macs); */
            if (macs.length != 6) {
              swal("错误","输入的mac地址格式不正确，\n请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error");
              return false;
            } else {
              for (var s = 0; s < 6; s++) {
                var temp = parseInt(macs[s], 16);
                if (isNaN(temp)) {
                  swal("错误","输入的mac地址格式不正确，\n请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error");
                  return false;
                } else {
                  if (temp > 255 || temp < 0) {
                    swal("错误","输入的mac地址格式不正确，\n请以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）!","error");
                    return false;
                  } else {
                	  if(s==5){
                    $("#addForm").submit();
                	  }
                  }
                }
              }
            }
          });
</script>

<form method="post" id="addForm" action="${contextPath}/mesPointGateway/verifyGateway" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
       <div class="pageFormContent" layoutH="58">
  <div class="row"> 
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>MAC地址</label>
          <div class="col-sm-6">
            <input type="text" name="mac" id="mac" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45" placeholder="例如：F4-8E-38-A5-B5-D2"/>
          </div>
        </div>
        <!-- 
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>网关名称</label>
          <div class="col-sm-6">
            <input type="text" name="name" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div> 
        -->
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>验证码</label>
          <div class="col-sm-6">
            <input type="text" name="macCode" class="form-control input-medium validate[maxSize[45],required] required" maxlength="45"/>
          </div>
        </div>
        <!-- <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>虚拟网关</label>
          <div class="col-sm-6">
            <select name="virtualFlag" class="form-control">
            	<option value="NO">否</option>
            	<option value="YES">是</option>
            </select>
          </div>
        </div> -->

    </div>
    </div>
  <div class="modal-footer">
    <button type="button" id="addBtn" class="btn btn-primary">确定</button>
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
  </div>
</form>
