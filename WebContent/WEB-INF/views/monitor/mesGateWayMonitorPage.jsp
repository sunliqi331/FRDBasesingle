<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form class="form form-horizontal" method="post"
	action="${contextPath }/mesPoints/data" data-target="table"
	onsubmit="return navTabSearch(this)">
	<div class="pageFormContent" layoutH="58">
		<div class="row">
			<div class="form-group" style="margin: 0 15px 15px 15px">
				<div class="" style="margin-top: 0px;">
					<table class="table table-striped" id="table" data-field="mesgateways"
						data-url="${contextPath }/mesPoints/gateWayDate">
						<thead>
							<tr>
								<th data-field="Number" width="50" data-align="center">序号</th>
								<th data-checkbox="true" width="22"><input
									class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
								<th data-field="name" width="100">网关名称</th>
								<th data-field="mac" width="100">MAC地址</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<span style="float: left;" id="recordTime"></span>
			<button type="button" id="inviteBtn" class="btn btn-primary">确定</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
</form>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript">
  $(document).ready(function($) {
	  var gateway = "${dataMac}";
	  console.log(gateway);
	  var codekey = "${dataCodekey}";

	  $("#inviteBtn").click(function(){
		  console.log("绑定------start-");
		  var $dialog = $.pdialog.getUpper();
		  var params = $dialog.data("params");
		  if(params){
			  params = DWZ.jsonEval(params);
			  var rowData = $.table.getCurrent().bootstrapTable('getSelections')[0];
			  console.log(rowData.mac);
			  params.mac=rowData.mac;
		  }
		  $dialog.data("params",params);
		  // $dialog.data("params","{\"aa\":\"bb\"}");
		  $dialog.modal('hide');
          console.log("绑定------end-");
	  });
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    function init(){
    	$.table.init('table', {
    	      toolbar : '#toolBar1',
    	      singleSelect  : true,
    	      pageSize: 5,
   	          onClickRow:function(row, $element){
                 console.log($element);
              }
    	    }, function(data) {
    	    	console.log(data)
    	    	if(data && data.mesgateways.length == 1){
    	    		$.table.getCurrent().bootstrapTable('checkAll');
    	    	}
    	    });
    }
    init();
  });
  
</script>