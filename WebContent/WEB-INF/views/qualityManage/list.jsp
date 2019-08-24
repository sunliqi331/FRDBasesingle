<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>质检图片管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<div id="container">
  <%@ include file="/WEB-INF/views/include.header.jsp"%>
  <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index">首页</a></li>
        <li>质检图片管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 质检图片查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/qualityCheck/qualityCheckImgsData" data-target="qualityCheckTable" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle" style="width:96px">质检图片名称</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_qualitynm" value="${param.search_LIKE_qualitynm}" />
              </div>
              <div class="form-group rangetime ">
                <label for="inputText" class="searchtitle">开始时间</label>
                  <div class="controls input-append date form_datetime1"
                    data-date="" data-date-format="yyyy-mm-dd" style="position: relative;"
                    data-link-field="begin">
                        <input id="begin" name="begin" class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;"
                       value="" readonly>
                        <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
	                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
	                          <i class="fa fa-th"></i></span>
	                          <input type="hidden" id="dtp_input1" value="" name="search_GTE_updatedate" />
                  </div>
              </div>
              <div class="form-group rangetime ">
                <label for="inputText" class="searchtitle">结束时间</label>
                
                  <div class="controls input-append date form_datetime2"
                    data-date="" data-date-format="yyyy-mm-dd" style="position: relative;"
                    data-link-field="end">
                    <input id="end" name="end" class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;"
                     value="" readonly>
                     <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                    <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                    <input type="hidden" id="dtp_input2" value="" name="search_LTE_updatedate" />
                  </div>
               
              </div>
              <button  id="searchDriverTypeBtn" type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div id="toolBar">
            <div class="btn-group pull-left">
              <a class="btn btn-default1" target="dialog" data-target="qualityCheckTable" rel="" href="${contextPath}/qualityCheck/addQualityCheckImage"> 
                <i class="fa fa-plus"></i> 
                <span>添加质检图片</span>
              </a> 
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="qualityCheckTable" rel="ids" href="${contextPath }/qualityCheck/deleteQualityImg" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>删除质检图片</span>
                </a>
              <a class="btn btn-default1" target="dialog" data-target="qualityCheckTable" refresh="true" rel="create_DriverType" href="${contextPath}/qualityCheck/addMuitiQualityCheckImage"> 
                <i class="fa fa-plus"></i> 
                <span>批量添加质检图片</span>
              </a> 
            </div>
          </div>
          <table class="table table-striped driver_type " id="qualityCheckTable" data-field="qualityCheckImgs" data-url="${contextPath}/qualityCheck/qualityCheckImgsData">
            <thead>
              <tr>
                <th data-field="Number" width="1%" data-align="center">序号   </th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="qualitynm" data-width="43%">质检图片名称</th>
                <th data-field="createdate" data-width="43%">图片上传日期</th>
                <th data-field="showPic" data-width="43%">质检图片(点击图片可查看大图)</th>
                
              </tr>
            </thead>
          </table>
        </div>
      </div>
    </div>
  </div>
  <!-- Modal -->
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
<c:set var="ParentTitle" value="QualityInspection"/>
<c:set var="ModuleTitle" value="qualityCheck"/>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
<style type="text/css">
 .modal-dialog {
   width:100% !important;
 }
 
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#searchDriverTypeBtn").click(function(){
			console.log("im clicked");
			console.log($(this).parent().html());
		});
		$.table.init('qualityCheckTable',{toolbar:'#toolBar',onClickCell:function(field, value, row, $element){
        	  if(field == 'showPic'){
          	  var op = {};
        	    op.title = "质检图片大图信息";
        	   console.log($(row.showPic).attr("data-url"));
        	     op.url = $(row.showPic).attr("data-url");
        	    op.destroyOnClose=true;
        	    // op.max=true;
        		// $.pdialog.open("propertyDetail",op);
        		$.pdialog.open("qualityImg",op);
          	  }
            }
		},function(data){
		})

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
</html>