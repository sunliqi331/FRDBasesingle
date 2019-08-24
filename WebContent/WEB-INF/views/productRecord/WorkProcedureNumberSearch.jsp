<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>工序号查询</title>
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
        <li>工序号查询</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver clearfix">
           <div class="search_header">
           <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
<!--              <i class="fa fa-search"></i> 工序号查询查询条件 -->
            <a class="work_search " href="${contextPath}/statistics/productionRecordPage">工件号查询</a>
             <a class="work_search active" href="${contextPath}/statistics/productionRecordPage2">工序号查询</a>
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/driver/driverData" data-target="table" onsubmit="return navTabSearch(this)">
              <ul class="searchcontent search_work">
               
              <li class="form-group">
                <label for="inputText" class="searchtitle">选择产线</label> 
                <select id="selectLine" name="search_LIKE_mesProductline.linename" data-placeholder="请选择产线"  class="form-control searchtext">
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">产品型号</label> 
                <select name="search_LIKE_mesDrivertype.typename" data-placeholder="请选择设备类型" class="form-control searchtext" >
                      <option></option>
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">工序号</label> 
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name"/>
              </li>
               <li class="form-group">
                <label for="inputText" class="searchtitle">开始时间</label>
                <div class="controls input-append date form_datetime" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text" style="background: #fff;; border-color: #c8ced6; width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" name="search_GTE_creattime" />
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">结束时间</label>
                <div class="controls input-append date form_datetime2" style="position: relative;" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dtp_input2">
                  <input class="form-control datetime" type="text" style="background: #fff;; border-color: #c8ced6;width:100%" value="" readonly> 
                  <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                  <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input2" value="" name="search_LTE_creattime" />
              </li>
              <li class="form-group">
              <button id="search" type="submit" class="btn btn-info btn-search1">搜索</button>
        </li>
        </ul>
            </form>
            </div>
          </div>
        <%--   <div id="toolBar">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_sendAlarm" href="${contextPath}/sendAlarm/addSendAlarm">
                <i class="fa fa-plus"></i>
                <span>过程控制</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:delete">
              <a class="btn btn-default1" target="dialog" data-target="table" rel="ids" href="${contextPath }/sendAlarm/deleteMany" title="确认要删除?"> 
                <i class="fa fa-remove"></i> 
                <span>过程特性</span>
              </a> 
              </shiro:hasPermission>
            </div>
          </div> --%>
          <table class="table table-striped" id="table" data-field="position" data-url="${contextPath}/position/data">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="name" width="100">工序号</th>
                <th data-field="name" width="100">工序名称</th>
                <th data-field="name" width="100">下道工序名称</th>
                <th data-field="name" width="100">毛坯外型尺寸</th>
                <th data-field="name" width="100">每坯可制作数</th>
                <th data-field="name" width="100">周转装具</th>
                <th data-field="name" width="100">上产车间</th>
                <th data-field="name" width="100">过程控制</th>
                <th data-field="name" width="100">过程特性</th>
                <th data-field="name" width="100">客户名称</th>
                <th data-field="name" width="100">零件名称</th>
                <th data-field="name" width="100">客户图号</th>
                <th data-field="name" width="100">版本日期</th>
                <th data-field="name" width="100">公司图号</th>
                <th data-field="name" width="100">设备名称/型号</th>
                <th data-field="name" width="100">设备编号</th>
                <th data-field="name" width="100">夹具名称</th>
                <th data-field="name" width="100">夹具图号</th>
                <th data-field="name" width="100">材料牌号</th>
                <th data-field="name" width="100">材料规格</th>
                <th data-field="name" width="100">下料重量</th>
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
<c:set var="ParentTitle" value="Production" />
<c:set var="ModuleTitle" value="ProductRecord" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>