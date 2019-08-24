<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>OEE</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp" %>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp" %>
 <div class="main-content">
    <ol class="breadcrumb">
      <li><i class="fa fa-home"></i><a href="${contextPath}/management/index"> 首页</a></li>
      <li>测点维护</li>
    </ol>
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar" style="border-color:#82a2ce">
          <div class="search_header">
             <i class="fa fa-pencil"></i> 填写OEE信息
           </div>
       </div>
      <div  style="padding: 20px 10px;border: 1px solid #ddd;">
        <form method="post" action="${contextPath}/company/create"
          id="formID" class="form form-horizontal"
          enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, subPageCallback);">
          <div class="pageFormContent" layoutH="58">
          <div class="listleft">
            <div class="form-group">
              <label for="inputText" class="control-label col-sm-4">选择计算设备</label>
              <div class="col-sm-6 select_nobg">
                <select id="analysisMethod" name="analysisMethod" data-placeholder="请选择设备"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="产线1" selected>产线1</option>
                  <option value="产线2">产线2</option>
                </select>
              </div>
            </div>
            <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">设备负荷时间
                    </label>
                    <div class="col-sm-6">
                      <div class="controls input-append date form_datetime1" data-date="" data-date-format="yyyy-mm-dd HH:mm:ss" data-link-field="dtp_input1">
                        <input class="form-control datetime" type="text" style="background: none;" value="" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input1" value="" name="startdate" />
                    </div>
             </div>
              <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">设备调整时间
                    </label>
                    <div class="col-sm-6">
                      <div class="controls input-append date form_datetime2" data-date="" data-date-format="yyyy-mm-dd HH:mm:ss" data-link-field="dtp_input2">
                        <input class="form-control datetime" type="text" style="background: none;" value="" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input2" value="" name="startdate" />
                    </div>
             </div>
           
            <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">日历工作时间
                    </label>
                    <div class="col-sm-6">
                      <div class="controls input-append date form_datetime3" data-date="" data-date-format="yyyy-mm-dd HH:mm:ss" data-link-field="dtp_input3">
                        <input class="form-control datetime" type="text" style="background: none;" value="" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input3" value="" name="startdate" />
                    </div>
             </div>
            </div>
            <div class="listright">
            <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">计划停机时间
                    </label>
                    <div class="col-sm-6">
                      <div class="controls input-append date form_datetime4" data-date="" data-date-format="yyyy-mm-dd HH:mm:ss" data-link-field="dtp_input4">
                        <input class="form-control datetime" type="text" style="background: none;" value="" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px; bottom:0; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input4" value="" name="startdate" />
                    </div>
             </div>
            <div class="form-group">
              <label for="inputText" class="control-label col-sm-4">实际加工周期</label>
              <div class="col-sm-6">
                <input id="registernum" type="text "
                 name="registernum"
                  class="form-control input-medium validate[required,maxSize[32]] required "
                  maxlength="32" value="" />
              </div>
            </div>
            <div class="form-group">
              <label for="inputText" class="control-label col-sm-4">理论加工周期</label>
              <div class="col-sm-6">
                <input id="registernum" type="text "
                 name="registernum"
                  class="form-control input-medium validate[required,maxSize[32]] required "
                  maxlength="32" value="" />
              </div>
            </div>
            <div class="form-group">
              <label for="inputText" class="control-label col-sm-4">实际加工周期</label>
              <div class="col-sm-6">
                <input id="registernum" type="text "
                 name="registernum"
                  class="form-control input-medium validate[required,maxSize[32]] required "
                  maxlength="32" value="" />
              </div>
            </div>
            
           </div>
           
           
            <div class="clearfix"></div>
            <div class="modal-footer"
              style="border-top: 0; text-align: center; width: 100%;">
              <button type="submit" class="btn btn-primary" id="oee_count"
                style="width: 200px;">开始计算</button>
            </div>
            <div class="oee" style="margin: 15px 0;border-top: 1px solid #5580bb; line-height:40px; font-weight: bold; color: #5580bb;display: none">
                  <div class="col-sm-3">时间开动率=20%</div>  
                  <div class="col-sm-3 ">性能开动率=20%</div> 
                  <div class="col-sm-3 ">合格品率=20%</div> 
                  <div class="col-sm-3 ">OEE=20%×20%×20%=80%</div>
           </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
</div>
<c:set var="ParentTitle" value="Company" />
<c:set var="ModuleTitle" value="CompanyRegister" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
  $(document).ready(function(){
	  $("select").chosen({search_contains:true});
  });
  $("#oee_count").click(function(){
	  $(".oee").show();  
  });
 
  $('.form_datetime1').datetimepicker({
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
  $('.form_datetime3').datetimepicker({
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
  $('.form_datetime4').datetimepicker({
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
</script>

</body>
</html>