<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<link href="${contextPath}/styles/css/chosen.css" rel="stylesheet" />
<script type="text/javascript">

$("#search").click(function() {
	  if($("#searchname").val()==""&&$("#searchRealUsername").val()==""&&$("#searchphone").val()==""){
	    return;
	  }
      $("#username").val($("#searchname").val());
      var M = $("#searchname").val();
      $.table.setCurrent("userTable");
      $.table.refreshCurrent("${contextPath }/personnel/data2",{"username":$("#searchname").val(),
    	  "realname":$("#searchRealUsername").val(),"phone":$("#searchphone").val()});
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
    $.table.setCurrent("table");
    $("#inviteFrom").submit();
  })
</script>

  <form method="post" id="inviteFrom"
    action="${contextPath}/personnel/invite" class="form form-horizontal"
    onsubmit="return validateCallback(this, dialogReloadCallback);">
    <div class="pageFormContent" layoutH="58">
     <div class="row">
      <div class="searchBar personnel_search" style="padding: 0px 15px;border:none;box-shadow: none;  width:auto; max-width:inherit ;" >
	 
	   <div class="form-inline" style="padding:0px">
	      <input type="hidden" id="username"> 
	      <input type="hidden"
	        id="userids" name="userids">
	      <div class="form-group" style="padding:0">
	        <label for="inputText" class="searchtitle" >用户名</label>
	        
	          <input id="searchname" name="name" style="margin-left: 13px;"
	            class="form-control searchtext validate[maxSize[45]]"
	            maxlength="45"  />
	       </div>
	       <div class="form-group" style="padding:0">
	        <label for="inputText" class="searchtitle" >真实姓名</label>
	        
	          <input id="searchRealUsername" name="realUsername"
	            class="form-control searchtext validate[maxSize[45]]"
	            maxlength="45"  />
	       </div>
	      <div class="form-group" style="padding:0">
	        <label for="inputText" class="searchtitle" >手机号码</label>
	        
	          <input id="searchphone" name="phone"
	            class="form-control searchtext validate[maxSize[45]]"
	            maxlength="45"  />
	       </div>
	        
	         <button type="button" class="btn btn-info btn-search1 search_person" id="search"  >搜索</button>
         </div>
      </div>
      <div class="form-group" style=" margin:0 15px 15px 15px">
        <div class="" style=" margin-top: 0px;" >
          <table class="table table-striped" id="userTable" data-field="user"
            data-url="${contextPath }/personnel/data2/">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22"><input
                  class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="username" width="100">用户名</th>
                <th data-field="realname" width="100">姓名</th>
                <th data-field="phone" width="100">电话</th>
              </tr>
            </thead>
          </table>
        </div>
      </div>
      </div>
    </div>
    <div class="modal-footer">
      <button type="button" id="inviteBtn" class="btn btn-primary">确定</button>
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
<script type="text/javascript">
  $(document).ready(function($) {
	$.table.init('userTable',{toolbar : '#toolBar1'});
	  
    /* function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('userTable', {
      toolbar : '#toolBar1',
      queryParams: function (params) {
    	  var parames = $.extend({},$.table._op.queryParams(params),{ 
              username: $("#searchname").val(),
              phone:$("#searchphone").val(),
              pageNum: this.pageNumber
              });
               return parames;
        }
    }, function(data) {
      var $p = $('#userTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.user[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    }); */
  });
</script>