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
        <li><i class="fa fa-home"></i><a href="${contextPath}/management/index">首页</a></li>
        <li><a href="${contextPath}/measuringTool/list">量具管理</a></li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver">
            <div class="search_header">
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
              <i class="fa fa-plus search_small" onclick="searchShow()"></i>
               <i class="fa fa-search"></i> 量具查询条件
            </div>
            <div class="ishidden" >
              <form class="form-inline" method="post" action="${contextPath}/measuringTool/measuringData" data-target="table" onsubmit="return navTabSearch(this)">
                <ul class="searchcontent" style="margin-bottom:0px">
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">选择工厂</label>
                    <select id="selectFactory" name="search_EQ_mesProductline.companyinfo.id" data-placeholder="请选择工厂"  class="form-control searchtext">
                      <option value="">全部</option>
                      <c:forEach var="p" items="${companyinfos }">
                        <option value="${p.id }">${p.companyname }</option>
                      </c:forEach>
                    </select>
                  </li>
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">选择产线</label>
                    <select id="selectLine" name="search_LIKE_mesProductline.linename" data-placeholder="请选择产线"  class="form-control searchtext">
                      <option value="">全部</option>
                    </select>
                  </li>
                  <%--<li class="form-group">--%>
                    <%--<label for="inputText" class="searchtitle">选择工序</label>--%>
                    <%--<select id="selectMesProductProcedure" name="search_EQ_mesProductProcedure.id" data-placeholder="请选择工序"  class="form-control searchtext">--%>
                    <%--<option value="">全部</option>--%>
                    <%--<c:forEach var="mpp" items="${mesProductProcedureList }">--%>
                    <%--<option value="${mpp.id }">${mpp.mesProduct.name }--${mpp.procedurename }</option>--%>
                    <%--</c:forEach>--%>
                    <%--</select>--%>
                  <%--</li>--%>
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">量具名称</label>
                    <input id="searchName" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name"/>
                  </li>
                  <li class="form-group">
                    <label for="inputText" class="searchtitle">量具编号</label>
                    <input id="searchSn" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_sn"/>
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
            <shiro:hasPermission name="Measuring:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_product" href="${contextPath}/measuringTool/addMeasuring" >
                <i class="fa fa-plus"></i> 
                <span>添加量具</span>
              </a> 
              </shiro:hasPermission>

              <shiro:hasPermission name="Measuring:edit">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="edit_product" href="${contextPath}/measuringTool/findById/{slt_uid}" >
                <i class="fa fa-pencil"></i>
                <span>编辑量具</span>
              </a>
              </shiro:hasPermission>

              <shiro:hasPermission name="Measuring:delete">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/measuringTool/deleteMeasuring" title="确认要删除?">
                <i class="fa fa-remove"></i>
                <span>删除量具</span>
              </a>
              </shiro:hasPermission>

            </div>
          </div>
          <table class="table table-striped" id="table" data-field="measuringToolList" data-url="${contextPath}/measuringTool/measuringData">
            <thead>
              <tr>
                <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="sn" width="100">量具编号</th>
                <th data-field="name" width="100">量具名称</th>
                <th data-field="type" width="100">量具类型</th>
                <th data-field="mesProductline.linename" width="100">所属产线</th>
                <th data-field="spcsiteName" width="100">所属站点</th>
                <th data-field="testingtime" width="100">量具校验时间</th>
                <th data-field="starttime" width="100">开始使用时间</th>
                <th data-field="endtime" width="150">结束使用时间</th>
                <th data-field="days" width="20%">预警小时数</th>
                <th data-field="hours" width="20%">使用天数</th>
                <th data-field="isenabledshow" width="150">使用状态</th>
                <%--<th data-field="statusbutton" width="150">报警显示</th>--%>
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
  <c:set var="ModuleTitle" value="Measuring" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script type="text/javascript">

      $("#selectFactory").change(function(event){
          $("#selectLine").empty();
          $("#selectLine").append("<option value=''>全部</option>");
          $("#selectLine").trigger("chosen:updated");
          if($("#selectFactory").val()!=""){
              ajaxTodo("${contextPath}/productline/getProductlineByCompanyid/"+$("#selectFactory").val(),paintDrivers);
          }
      });
      function paintDrivers(data){
          $.each(data,function(idx,item){
              $("#selectLine").append("<option value='"+ item.linename +"'>"+ item.linename +"</option>");
          });
          $("#selectLine").trigger("chosen:updated");
      };

  </script>
</body>
</html>