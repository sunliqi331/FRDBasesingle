<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
$("#search").click(function() {
      $("#username").val($("#searchname").val());
      var M = $("#searchname").val();
      $.table.setCurrent("userTable");
      var param = $("#inviteFrom").serializeObject();
      $.table.refreshCurrent("${contextPath }/sendAlarm/data2/",param,function(){
        $("#userTable").bootstrapTable("checkBy",{field:'username',values:[M]});
      });
    });
  $("#inviteBtn").click(function() {
    $.each($("#userTable").find("tr.selected"),  function(idx, item) {
      var M = $("#userids").val();
        var D = $(this).attr("data-uniqueid");
        if(M.length < 1){
          $("#userids").val($(this).attr("data-uniqueid"));
        }else if(M.indexOf(D) >= 0){
            return false;
        }else{
          $("#userids").val(M+","+$(this).attr("data-uniqueid"));
        }
        
    });
    if($("#userids").val().length>0){
        $.table.setCurrent("table");
        $("#inviteFrom").submit();
    }else{
    	swal("错误","请选择要发送的用户","error");
    }
  })
</script>

  <form method="post" id="inviteFrom" action="${contextPath}/sendAlarm/saveOrUpdate" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent form-horizontal clearfix" layoutH="58" style="padding:0 15px" >
     <h4 class="media-heading" >选择人员</h4>
        <hr class="hr-normal"/>
      <div class="searchBar " style="padding: 0px 15px 15px 15px; margin: -10px -15px 10px -15px; width:auto; border:none; max-width:inherit ;" >
	   <div class="form-inline" style="padding-top: 0">
	      <input type="hidden" id="username">
	      <input type="hidden" id="userids" name="userids">
	      <div class="form-group">
	        <label for="inputText" class="searchtitle" >真实姓名</label>
	          <input id="searchname" name="realname" class="form-control searchtext " maxlength="45"  />
	       </div>
	      <div class="form-group">
	        <label for="inputText" class="searchtitle" >电话</label>
	          <input id="searchphone" name="phone" class="form-control searchtext" maxlength="45"  />
	       </div>
	         <button type="button" class="btn btn-info btn-search1 search_person" id="search"  >搜索</button>
         </div>
          <div class="form-group" style=" margin-bottom: 0">
        <div class="" style=" margin-top: -10px; margin-left: 15px;" >
         <table class="table table-striped" id="userTable" data-field="user" data-url="${contextPath }/sendAlarm/data2/" >
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="username" width="100">用户名</th>
                <th data-field="realname" width="100">姓名</th>
              </tr>
            </thead>
          </table>
          </div>
      </div>
      </div>
     
          
     <h4 class="media-heading" >选择告警类型</h4>
        <hr class="hr-normal"/>   
    
    <div class="form-group">
      <label for="inputText" class="control-label col-sm-3"><span class="require">*&nbsp;</span>告警类型</label>
      <div class="col-sm-6 select_nobg">
        <select id="alarttype" name="alarttype" multiple="multiple" data-placeholder="请选择类型" class="form-control validate[required] chosen-select">
          <option value="driver">设备</option>
          <option value="product">产品</option>
        </select>
      </div>
    </div> 
    </div>
    <div class="modal-footer" style="margin-top: 0px">
      <button type="button" id="inviteBtn" class="btn btn-primary">确定</button>
      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
  </form>


<script type="text/javascript">
  $(document).ready(function($) {
    $.table.init('userTable', {
      toolbar : '#toolBar1'
    }, function(data) {
    });
  });
</script>