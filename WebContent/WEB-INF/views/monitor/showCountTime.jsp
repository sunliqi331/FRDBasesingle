<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<form class="form form-horizontal" method="post"
	action="${contextPath }/mesPoints/data" data-target="table"
	onsubmit="return navTabSearch(this)">
	<input type="hidden" id="bindType" value="${bindType }">
	<div class="pageFormContent" layoutH="58">
		<div class="row">
			<div class="searchBar personnel_search"
				style="padding: 0px 15px; border: none; box-shadow: none; width: auto; max-width: inherit;">

				<div class="form-inline" >

					<div class="form-group">
						<label for="timeCount" class="searchtitle">选择时间：</label>
						<div class="controls input-append date form_datetime2"
							style="position: relative;" data-date=""
							data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
							<input class="form-control datetime" type="text" id="countTimeText"
								style="background: #fff;; border-color: #c8ced6; width: 100%"
								value="" readonly> <span class="add-on"
								style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
								<i class="fa fa-remove"></i>
							</span> <span class="add-on"
								style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
								<i class="fa fa-th"></i>
							</span>
						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" id="countTimeBtn" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	</div>
</form>
<script type="text/javascript">
$(document).ready(function(){
	 $('.form_datetime2').datetimepicker({
	        language : 'zh-CN',
	        format : 'yyyy-mm-dd hh:ii:ss',
	        weekStart : 1,
	        todayBtn : 'linked',
	        autoclose : 1,
	        todayHighlight : 1,
	        startView : 2,
	        forceParse : 0,
	        showMeridian : 1,
	      //minView: 2

	      });
	
	 $("#countTimeBtn").click(function(){
		  var $dialog = $.pdialog.getUpper();
		  var params = $dialog.data("params");
		  if(params){
			  params = DWZ.jsonEval(params);
			  params.countTime = $("#countTimeText").val();
		  }
		  $dialog.data("params",params);
		 // $dialog.data("params","{\"aa\":\"bb\"}");
		  $dialog.modal('hide');
	  });
});
</script>
