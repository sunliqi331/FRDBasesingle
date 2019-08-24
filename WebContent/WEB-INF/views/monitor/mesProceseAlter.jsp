<%@ page language="java" contentType="text/html; charset=UTF-8"
	trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="form-group">
    <label class="control-label col-sm-4">产品：</label>
    <div class="col-sm-6">
        <div class="select select-white">
            <span class="form-control placeholder">切割件</span>
            <ul>
                <li data-value="109" data-attr="">切割件</li>
                <li data-value="115" data-attr="">NEW法兰制造</li>
                <li data-value="143" data-attr="">上驱模块</li>
                <li data-value="145" data-attr="">5毫米钢管</li>
                <li data-value="146" data-attr="">壳体</li>
                <li data-value="147" data-attr="">变速器总成下线</li>
                <li data-value="148" data-attr="">变速器总成泄露测试改造</li>
                <li data-value="149" data-attr="">变速器壳体装配</li>
                <li data-value="150" data-attr="">立加产品1</li>
                <li data-value="151" data-attr="">立加产品2</li>
                <li data-value="152" data-attr="">卧加产品1</li>
                <li data-value="153" data-attr="">卧加产品2</li>
                <li data-value="154" data-attr="">北汽刹车盘</li>
                <li data-value="155" data-attr="">独立悬架</li>
                <li data-value="156" data-attr="">测试影像器件</li>
                <li data-value="157" data-attr="">PLC立加1</li>
                <li data-value="158" data-attr="">PLC立加2</li>
                <li data-value="159" data-attr="">检测产品</li>
            </ul>
            <input type="hidden" name="productId" value="109" data-attr="">
        </div>
    </div>
</div>

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