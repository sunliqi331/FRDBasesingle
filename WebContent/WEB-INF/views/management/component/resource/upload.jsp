<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
<!--
function forbidden() {
	dialogAjaxDone('{"statusCode":"403", "message":"用户权限不足。", "navTabId":"", "forwardUrl":"", "callbackType":"closeCurrent"}');
} 
//-->
</script>
<style type="text/css" media="screen">
.fileQueue {
	height: 150px;
	overflow: auto;
	border: 1px solid #E5E5E5;
	margin-bottom: 10px;
}
.uploadify-queue-item {
  max-width: auto;
}
</style>

<div class="pageFormContent" style="padding: 0 15px 15px 15px;">
<input type="hidden" value="${storeType }" />
<input id="file_upload" type="file"
  uploaderOption="{
        'auto':false,
        'successTimeout':300,
        'swf':'${contextPath}/styles/uploadify/scripts/uploadify.swf',
        'overrideEvents' : ['onDialogClose'],
        'queueID':'fileQueue',
        'fileObjName':'files',
        'uploader':'${contextPath}/management/component/resource/upload;jsessionid=<%=session.getId()%>?storeType=${storeType }',
        'buttonText': '选择文件',
        'removeComplete': false,
        'fileTypeDesc':'支持的格式：',
        'fileSizeLimit':'1MB',
        'queueSizeLimit' : 10,
        'onSelectError':function(file, errorCode, errorMsg){
            switch(errorCode) {
                case -100:
                    swal('错误','上传的文件数量已经超出系统限制的'+$('#file_upload').uploadify('settings','queueSizeLimit')+'个文件！');
                    break;
                case -110:
                    swal('错误','文件 ['+file.name+'] 大小超出系统限制的'+$('#file_upload').uploadify('settings','fileSizeLimit')+'大小！');
                    break;
                case -120:
                    swal('错误','文件 ['+file.name+'] 大小异常！');
                    break;
                case -130:
                    swal('错误','文件 ['+file.name+'] 类型不正确！');
                    break;
            }
        },
        'onFallback':function(){
            swal('错误','您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。');
        },
        'onUploadError' : function(file, errorCode, errorMsg, errorString) {
        	if (errorCode == 403) {
        		forbidden();
        	}
            swal(file.name + '上传失败: ' + errorMsg + errorString);
        }
	}"
/>
<div id="fileQueue" class="fileQueue"></div>
<button class="btn btn-info  btn-info" onclick="$('#file_upload').uploadify('upload', '*');">开始上传</button>
<button class="btn btn-info  btn-danger" onclick="$('#file_upload').uploadify('cancel', '*');">取消上传</button>
</div>
