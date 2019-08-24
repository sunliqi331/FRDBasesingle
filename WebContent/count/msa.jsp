<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="title" content="FRD - Demo">
<title>FRD - Demo</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<script type="text/javascript">
$(document).ready(function(){
	$("#analysisMethod").change(function(e) {
	    //alert($("#analysisMethod").val());
	    if ($("#analysisMethod").val() == "grr") {
	      $(".count_cg").css("display", "none");
	      $(".count_grr").css("display", "block");
	    } else {
	      $(".count_cg").css("display", "block");
	      $(".count_grr").css("display", "none");
	    }
	  });

	  $("#measureRange").change(function(e) {
	    //alert($("#analysisMethod").val());
	    if ($("#measureRange").val() == "rangeTime") {
	      $(".rangeList").css("display", "none");
	      $(".rangetime").css("display", "block");
	    } else if ($("#measureRange").val() == "rangeList") {
	      $(".rangeList").css("display", "block");
	      $(".rangetime").css("display", "none");
	    } else if ($("#measureRange").val() == "rangeSource") {
	      window.open('dataSource.html');
	      $(".rangeList").css("display", "none");
	      $(".rangetime").css("display", "none");
	    } else {
	      $(".rangeList").css("display", "none");
	      $(".rangetimeSource").css("display", "none");
	    }

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
});
  
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<body>
<div id="container">
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar" style="padding: 20px 10px;">
        <form method="post" action="${contextPath}/company/create"
          id="formID" class="form form-horizontal"
          enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, subPageCallback);">
          <div class="pageFormContent" layoutH="58">
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">分析方法
              </label>
              <div class="col-sm-8">
                <select id="analysisMethod" name="analysisMethod"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="cg" selected>Cg分析</option>
                  <option value="grr">Grr分析</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">检测设备
              </label>
              <div class="col-sm-8">
                <select id="parentname" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="检测设备A">检测设备A</option>
                  <option value="检测设备B">检测设备B</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">产品型号
              </label>
              <div class="col-sm-8">
                <select id="parentname" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="产品型号A">产品型号A</option>
                  <option value="产品型号B">产品型号B</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">产品工序
              </label>
              <div class="col-sm-8">
                <select id="parentname" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="GX1011">GX1011</option>
                  <option value="GX1012">GX1012</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">被测参数</label>
              <div class="col-sm-8">
                <select id="parentname" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="内径">内径</option>
                  <option value="GX1012">GX1012</option>
                </select>
              </div>
            </div>
            <div class="count_cg">
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">测量次数</label>
                <div class="col-sm-8">
                  <select id="parentname" name="parentname"
                    class="form-control validate[required]">
                    <option>请选择</option>
                    <option value="25">25</option>
                    <option value="50">50</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">统计标准</label>
                <div class="col-sm-8">
                  <select id="parentname" name="parentname"
                    class="form-control validate[required]">
                    <option>请选择</option>
                    <option value="4O">4O</option>
                    <option value="6O">6O</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">测量范围</label>
                <div class="col-sm-8">
                  <select id="measureRange" name="measureRange"
                    class="form-control validate[required]">
                    <option>请选择</option>
                    <option value="rangeData">最新数据</option>
                    <option value="rangeTime">时间段</option>
                    <option value="rangeList" selected>编号段</option>
                    <option value="rangeSource">数据源</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-sm-6 rangetime ">
                <label for="inputText" class="control-label col-sm-4">开始时间</label>
                <div class="col-sm-8">
                  <div class="controls input-append date form_datetime1"
                    data-date="" data-date-format="yyyy-mm-dd"
                    data-link-field="dtp_input1">
                    <input class="form-control datetime" type="text"
                      style="background: #fff;" value="" readonly> <span
                      class="add-on"
                      style="position: absolute; right: 48px; padding: 0px 9px; line-height: 32px"><i
                      class="fa fa-remove"></i></span> <span class="add-on"
                      style="position: absolute; right: 15px; padding: 0px 9px; line-height: 32px"><i
                      class="fa fa-th"></i></span>
                  </div>
                  <input type="hidden" id="dtp_input1" value="" />
                </div>
              </div>
              <div class="form-group col-sm-6 rangetime ">
                <label for="inputText" class="control-label col-sm-4">结束时间</label>
                <div class="col-sm-8">
                  <div class="controls input-append date form_datetime2"
                    data-date="" data-date-format="yyyy-mm-dd"
                    data-link-field="dtp_input2">
                    <input class="form-control datetime" type="text"
                      style="background: #fff;" value="" readonly> <span
                      class="add-on"
                      style="position: absolute; right: 48px; padding: 0px 9px; line-height: 32px"><i
                      class="fa fa-remove"></i></span> <span class="add-on"
                      style="position: absolute; right: 15px; padding: 0px 9px; line-height: 32px"><i
                      class="fa fa-th"></i></span>
                  </div>
                  <input type="hidden" id="dtp_input2" value="" />
                </div>
              </div>
              <div class="form-group col-sm-6 rangeList">
                <label for="inputText" class="control-label col-sm-4">开始编号</label>
                <div class="col-sm-8">
                  <input id="registernum" type="text" name="registernum"
                    class="form-control input-medium validate[required,maxSize[32]] required "
                    maxlength="32" value="" />
                </div>
              </div>
              <div class="form-group col-sm-6 rangeList">
                <label for="inputText" class="control-label col-sm-4">结束编号</label>
                <div class="col-sm-8">
                  <input id="registernum" type="text" name="registernum"
                    class="form-control input-medium validate[required,maxSize[32]] required "
                    maxlength="32" value="" />
                </div>
              </div>
              <!--<div class="form-group col-sm-6 rangeSource" >       
                              
                                <div type="button" class="btn btn-info driver_attribute" ><a href="dataSource.html" target="_blank" style="color:#fff"><i class="icon-ok"></i> 选择数据源</a></div>                              
                        </div>-->
            </div>
            <div class="count_grr">
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">评定方法</label>
                <div class="col-sm-8">
                  <select id="parentname" name="parentname"
                    class="form-control validate[required]">
                    <option>请选择</option>
                    <option value="最新数据（件、人、次）计算">最新数据（件、人、次）计算</option>
                    <option value="最新数据（件、次、人）计算">最新数据（件、次、人）计算</option>
                  </select>
                </div>
              </div>
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">工件数量</label>
                <div class="col-sm-8">
                  <input id="registernum" type="text" name="registernum"
                    class="form-control input-medium validate[required,maxSize[32]] required "
                    maxlength="32" value="" />
                </div>
              </div>
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">检验员数量</label>
                <div class="col-sm-8">
                  <input id="registernum" type="text" name="registernum"
                    class="form-control input-medium validate[required,maxSize[32]] required "
                    maxlength="32" value="" />
                </div>
              </div>
              <div class="form-group col-sm-6">
                <label for="inputText" class="control-label col-sm-4">每人</label>
                <div class="col-sm-8">
                  <div class="input-group">
					<input id="registernum" type="text" name="registernum"
                                class="form-control input-medium validate[required,maxSize[32]] required "
                                maxlength="32" value=""  />
                    <span class="input-group-addon">次/件</span>
				</div>
                </div>
              </div>
            </div>
            <div class="clearfix"></div>
            <div class="modal-footer"
              style="border-top: 0; text-align: center; width: 100%;">
              <button type="submit" class="btn btn-primary"
                style="width: 200px;">开始评定</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>
