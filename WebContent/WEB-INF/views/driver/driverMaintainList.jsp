<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>设备维护管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript">
  $(document).ready(function(){
	  $("select").chosen({search_contains:true});
  });
  </script>
</head>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index">首页</a></li>
        <li>设备维护管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
    <div class="searchBar search_driver">
      <div class="search_header">
        <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
        <i class="fa fa-search"></i> 设备维护查询条件
      </div>
      <div class="ishidden" >
          <form class="form-inline" method="post" action="${contextPath}/driverRepair/driverRepairData" data-target="table" onsubmit="return navTabSearch(this)">
            <div class="form-group">
                <label for="inputText" class="searchtitle">选择产线</label> 
                <select name="search_LIKE_mesDriver.mesProductline.linename" data-placeholder="请选择产线" class="form-control searchtext">
                      <option value="">请选择产线</option>
                      <c:forEach var="p" items="${mesProductlines }">
                        <option value="${p.linename }">${p.linename }</option>
                      </c:forEach>
                </select>
              </div>
              <div class="form-group">
                <label for="inputText" class="searchtitle">选择状态</label> 
                <select name="search_LIKE_status" data-placeholder="请选择状态" class="form-control searchtext">
                      <option value="">请选择状态</option>
                      <option value="未开始">未开始</option>
                      <option value="维修中">维修中</option>
                      <option value="已完成">已完成</option>
                </select>
              </div>
            <div class="form-group">
               <label for="inputText" class="searchtitle">设备名称</label>
               <input type="text" class="form-control searchtext" id="inputText" data-placeholder="请选择名称" name="search_LIKE_mesDriver.name" value="${param.search_LIKE_mesDriver.name}"/>
            </div>
            <div class="form-group">
               <label for="inputText" class="searchtitle">维护人员</label>
               <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_repairperson" value="${param.search_LIKE_repairperson}"/>
            </div>
            <button type="submit" class="btn btn-info btn-search1">搜索</button>
          </form>
          </div>
        </div>
        <div id="toolBar">
          <div class="btn-group pull-left">
          <shiro:hasPermission name="driverRepair:save">
               <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" refresh="true" rel="create_driverRepair" href="${contextPath }/driverRepair/addDriverMaintain?pagename=addDriverMaintain"> 
                <i class="fa fa-plus"></i> 
                <span>添加维护</span>
               </a>
               </shiro:hasPermission>
               <shiro:hasPermission name="driverRepair:edit">
               <a class="btn btn-default1 btn-tool" target="dialog" data-target="table" refresh="true" rel="edit_driverRepair" href="${contextPath }/driverRepair/findById/{slt_uid}?pagename=editDriverMaintain"> 
                <i class="fa fa-pencil"></i> 
                <span>修改维护</span>
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="driverRepair:delete">
                <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath }/driverRepair/deleteDriverRepair" title="确认要删除?">
                <i class="fa fa-remove"></i> 
                <span>删除维护</span>
             </a>        
             </shiro:hasPermission>
          </div>
        </div>
        <table class="table table-striped" id="table" data-field="mesDriverrepairs" data-url="${contextPath}/driverRepair/driverRepairData">
          <thead>
            <tr>
                 <th data-field="Number" width="2%" data-align="center">序号</th>
              <th data-checkbox="true" width="22">
              <input class="cbr checkboxCtrl" type="checkbox" group="ids">
              </th>
              <th data-field="mesDriver.sn" width="100">设备编号</th>
              <th data-field="mesDriver.name" width="100">设备名称</th>
              <th data-field="mesDriver.mesProductline.linename" width="100">产线</th>
              <th data-field="starttime" width="80">开始时间</th>
              <th data-field="endtime" width="100">结束时间</th>
              <th data-field="repairperson" width="100">维护人员</th>
              <th data-field="telnum" width="100">维护人员电话</th>
              <th data-field="repaircycle" width="100" >维护周期</th> 
              <th data-field="status" width="100">状态</th>
              <th data-field="repaircause" >维护原因</th>
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
  <c:set var="ParentTitle" value="Driver" />
  <c:set var="ModuleTitle" value="driverRepair" />
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
  <script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
  <script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
</body>
</html>