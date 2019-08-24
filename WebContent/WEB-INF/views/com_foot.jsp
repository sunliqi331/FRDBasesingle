
<%@ page pageEncoding="UTF-8"%>
<script
	src="${contextPath}/styles/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"
	charset="utf-8"></script>
<script
	src="${contextPath}/styles/validationEngine/js/jquery.validationEngine-2.6.4.js"
	charset="utf-8"></script>
<script
	src="${contextPath}/styles/uploadify/scripts/jquery.uploadify.min.js"
	charset="utf-8"></script>
<script src="${contextPath}/styles/dwz/js/dwz.core.js"
	type="text/javascript"></script>
<script src="${contextPath}/styles/dwz/js/dwz.ajax.js"
	type="text/javascript"></script>
<script src="${contextPath}/styles/dwz/js/dwz.database.js"
	type="text/javascript"></script>
<script src="${contextPath}/js/bootstrap.js"></script>
<script src="${contextPath }/js/jquery-ui.js"></script>
<script
	src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script
	src="${contextPath}/styles/datetimepicker/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="${contextPath}/js/uikit.table.js"></script>
<script src="${contextPath}/styles/assets/bootstrap-table/bootstrap-table.js"></script>
<script src="${contextPath}/styles/assets/bootstrap-table/bootstrap-table-export.js"></script>
<script src="${contextPath}/styles/tableExport/tableExport.js"></script>
<script src="${contextPath}/styles/tableExport/jquery.base64.js"></script>
<script src="${contextPath}/styles/assets/bootstrap-table/bootstrap-table-zh-CN.js"></script>
<script src="${contextPath}/styles/assets/sweetalert/sweetalert-dev.js"></script>
<script src="${contextPath}/js/uikit.cbr.js"></script>
<script src="${contextPath}/js/uikit.dialog.js"></script>
<script src="${contextPath}/js/jquery.loadTemplate-1.4.4.js"></script>
<script src="${contextPath}/js/jquery.nicescroll.min.js"></script>
<script src="${contextPath}/js/jquery.nicescroll.plus.js"></script>
<script src="${contextPath}/js/common.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.blockUI.js"></script>

<script> 
  $(document).ready(function($){
	 
	//if(document.body.clientWidth>767){ 	
    $('#sidebar > ul > li[title="${ParentTitle}"] > a').click();
    //}
    $('#sidebar .sub > li[title="${ModuleTitle}"]').addClass('active').prepend("<span class='sub_arrow'></span>");
 
  });
  </script>