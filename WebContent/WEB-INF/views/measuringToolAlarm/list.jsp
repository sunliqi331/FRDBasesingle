<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>量具报警管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>

<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li><a href="${contextPath}/measuringToolAlarm/list">量具报警管理</a></li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver">
            <div class="search_header">
              <i class="fa fa-minus search_small" onclick="searchHide()"></i>
              <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 量具报警查询条件
           </div>

            <div class="ishidden" >
              <form class="form-inline" method="post" action="${contextPath}/measuringToolAlarm/measuringToolAlarmData" data-target="table" onsubmit="return navTabSearch(this)">
                <ul class="searchcontent" style="margin-bottom:0px">
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">选择产线</label>
                    <select id="selectLine" name="search_EQ_measuringTool.mesProductline.id" data-placeholder="请选择产线"  class="form-control searchtext">
                      <option value="">全部</option>
                      <c:forEach var="mpl" items="${mesProductlineList }">
                        <option value="${mpl.id}">${mpl.linename}</option>
                      </c:forEach>
                    </select>
                  </li>
                  <%--<li class="form-group">--%>
                    <%--<label for="inputText" class="searchtitle">选择工序</label>--%>
                    <%--<select id="selectMesProductProcedure" name="search_EQ_measuringTool.mesProductProcedure.id" data-placeholder="请选择工序"  class="form-control searchtext">--%>
                      <%--<option value="">全部</option>--%>
                      <%--<c:forEach var="mpp" items="${mesProductProcedureList }">--%>
                        <%--<option value="${mpp.id }">${mpp.mesProduct.name }--${mpp.procedurename }</option>--%>
                      <%--</c:forEach>--%>
                    <%--</select>--%>
                  <%--</li>--%>

                  <li class="form-group">
                    <label for="inputText" class="searchtitle">选择类型</label>
                    <select id="selecType" name="search_EQ_measuringTool.type" data-placeholder="请选择工序"  class="form-control searchtext">
                      <option value="">全部</option>
                      <c:forEach var="dictionary" items="${Dictionary1 }">
                        <option value="${dictionary.name }">${dictionary.name}</option>
                      </c:forEach>
                    </select>
                  </li>

                  <li class="form-group">
                    <label for="inputText" class="searchtitle">量具状态</label>
                    <select id="selecIsenabled" name="search_EQ_measuringTool.isenabled" data-placeholder="请选择状态"  class="form-control searchtext">
                      <option value="">全部</option>
                      <option value="0">使用</option>
                      <option value="1">停用</option>
                      <option value="2">检修</option>
                    </select>
                  </li>

                  <li class="form-group">
                    <label for="inputText" class="searchtitle">量具名称</label>
                    <input id="searchName" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_measuringTool.name"/>
                  </li>
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">量具编号</label>
                    <input id="searchSn" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_measuringTool.sn"/>
                  </li>
                  <li class="form-group">
                    <input id="property" type="hidden" disabled>
                    <button id="search" type="submit" class="btn btn-info btn-search1">搜索</button>
                  </li>
                </ul>
              </form>
            </div>


          </div>
          <div id="toolBar">
            <div class="btn-group pull-left clearfix" >
              <%--编辑按钮--%>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="MeasuringToolAlarmList" data-url="${contextPath}/measuringToolAlarm/measuringToolAlarmData">
            <thead>
              <tr>
                <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="measuringTool.sn" width="100">量具编号</th>
                <th data-field="measuringTool.name" width="100">量具名称</th>
                <th data-field="measuringTool.type" width="100">量具类型</th>
                <th data-field="measuringTool.isenabledshow" width="100">量具状态</th>
                <th data-field="measuringTool.mesProductline.linename" width="100">所属产线</th>
                <th data-field="measuringTool.spcsiteName" width="100">所属站点</th>
                <th data-field="alarmtime" width="100">预警时间</th>
                <th data-field="endtime" width="100">报警时间</th>
                <th data-field="recordtime" width="150">记录时间</th>
                <th data-field="hours" width="20%">超出报警时长</th>
                <th data-field="statusbutton" width="150">报警状态</th>

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
  <c:set var="ModuleTitle" value="MeasuringToolAlarm" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>