<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>量具管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>

<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>s
        <li><a href="${contextPath}/measuringTool/list">量具操作日志</a></li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver">
            <div class="search_header">
              <i class="fa fa-minus search_small" onclick="searchHide()"></i>
              <i class="fa fa-plus search_small" onclick="searchShow()"></i>
              <i class="fa fa-search"></i> 量具日志查询条件
            </div>
            <div class="ishidden" >
              <form class="form-inline" method="post" action="${contextPath}/measuringOperationlog/data" data-target="table" onsubmit="return navTabSearch(this)">

                <div class="form-group">
                  <label for="inputText" class="searchtitle" style="width:96px">量具编号</label>
                  <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_.sn" value="${param.search_LIKE_.sn}" />
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
                    <input type="hidden" id="dtp_input1" value="" name="search_GTE_createTime" />
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
                    <input type="hidden" id="dtp_input2" value="" name="search_LTE_createTime" />
                  </div>
                </div>

                <div class="form-group">
                  <label for="inputText" class="searchtitle" style="width:96px">量具名称</label>
                  <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_.name" value="${param.search_LIKE_.name}" />
                </div>

                <div class="form-group">
                  <label for="inputText" class="searchtitle" style="width:76px">操作人</label>
                  <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_username" value="${param.search_LIKE_username}" />
                </div>

                <button  id="searchDriverTypeBtn" type="submit" class="btn btn-info btn-search1">搜索</button>
              </form>
            </div>
          </div>

          <div id="toolBar">
            <div class="btn-group pull-left clearfix" >
              <%--编辑按钮--%>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="measuringToolList" data-url="${contextPath}/measuringOperationlog/data">
            <thead>
              <tr>
                <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="sn" width="100">量具编号</th>
                <th data-field="name" width="100">量具名称</th>
                <th data-field="username" width="100">操作人员</th>
                <th data-field="message" width="100">操作日志</th>
                <th data-field="createTime" width="100">操作时间</th>
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
  <c:set var="ParentTitle" value="MeasuringTool" />
  <c:set var="ModuleTitle" value="measuringOperationlog" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
  <script type="text/javascript">
      $(document).ready(function(){
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

</body>
</html>