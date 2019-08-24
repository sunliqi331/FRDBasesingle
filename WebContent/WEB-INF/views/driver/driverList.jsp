<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>设备管理</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />

</head>

<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>设备管理</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar search_driver clearfix">
           <div class="search_header"> 
            <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 设备查询条件
           </div>
          <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/driver/driverData" data-target="table" onsubmit="return navTabSearch(this)">
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
              <li class="form-group">
                <label for="inputText" class="searchtitle">设备类型</label> 
                <select id="searchType" name="search_LIKE_mesDrivertype.typename" data-placeholder="请选择设备类型" class="form-control searchtext" >
                     <option value="">全部</option>
                      <c:forEach var="p" items="${mesDrivertypes }">
                        <option value="${p.typename }">${p.typename }</option>
                      </c:forEach>
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">设备名称</label> 
                <input id="searchName" type="text" class="form-control searchtext" id="inputText" name="search_LIKE_name"/>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">设备编号</label> 
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
            <div class="btn-group pull-left">
            <shiro:hasPermission name="driverList:save">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true" rel="create_driver" href="${contextPath}/driver/addDriver?pagename=addDriver">
                <i class="fa fa-plus"></i>
                <span>添加设备</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:edit">
              <a class="btn btn-default1" target="dialog" data-target="table" refresh="true"  rel="edit_driver" href="${contextPath}/driver/findById/{slt_uid}?pagename=editDriver">
                <i class="fa fa-pencil"></i> 
                <span>修改设备</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:delete">
              <button class="btn btn-default1" id="deleteDriver"> 
                <i class="fa fa-remove"></i> 
                <span>删除设备</span>
              </button>
              </shiro:hasPermission>
              <shiro:hasPermission name="driverList:show">
              <a class="btn btn-default1" target="dialog" data-target="table" rel="view_driver" href="${contextPath }/driver/viewDriver/{slt_uid}"> 
                <i class="fa fa-eye"></i> 
                <span>查看设备信息</span>
              </a> 
              </shiro:hasPermission>
             <!--  <button id="xxxx" type="button" class="btn btn-info btn-search1">搜索</button> -->
<%--               <shiro:hasPermission name="driverList:viewPoint"> --%>
<%--               <a class="btn btn-default1" target="page" data-target="table" rel="view_driverPoint" href="${contextPath}/driver/driverPointList/{slt_uid}"> --%>
<!--                 <i class="fa fa-cogs"></i>  -->
<!--                 <span>配置测点</span> -->
<!--               </a> -->
<%--               </shiro:hasPermission> --%>
            <a id="deleteA" style="visibility:hidden" class="btn btn-default1 btn-tool" target="selectedTodo" data-target="table" rel="ids" href="${contextPath}/driver/deleteDriver"></a>
            </div>
          </div>
          <table class="table table-striped" id="table" data-field="mesDrivers" data-url="${contextPath}/driver/driverData">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="sn" width="100">设备编号</th>
                <th data-field="name" width="100">设备名称</th>
                <th data-field="mesDrivertype.typename" width="100">设备类型</th>
                <th data-field="modelnumber" width="100">设备型号</th>
                <th data-field="mesProductline.linename" width="100">所在产线</th>
                <th data-field="mesProductline.companyinfo.companyname" width="100">所在工厂</th>
                <th data-field="button" width="150">查看数据</th>
                <th data-field="configPoint" width="20%">配置测点</th>
                <th data-field="driverStatus" width="150">状态</th>
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
<script type="text/javascript">
function searchDriverData(obj){
  var op = {};
    op.title = "设备属性实时信息";
    op.destroyOnClose = true;
    op.url = "${contextPath}/driver/getDriverStatus/"+obj;
  $.pdialog.open("driverStatus",op);
  };
 
function configPoint(obj,aa){
  var op = {};
    op.title = "配置测点";
    op.url = "${contextPath}/driver/driverPointList/"+obj;
    var $this = $(aa);
    var $newPage = $this.parents('.main-body').after('<div class="main-body" style="display:none"></div>').next();
    var $topTitle = $('.main-content > .breadcrumb');
    $newPage.loadUrl(op.url, {}, function() {
      $newPage.pageswitch('show');
      $newPage.one('hide.pageswitch',function () {
        $topTitle.children('li:last-child').remove();
      });
      $topTitle.append('<li>' + op.title + '</li>');
    });
}
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

function getIds(){
    var ids = "";
    $("#table").find("tr.selected").each(function(i){
        var val = $(this).attr("data-uniqueid");
        ids += i==0 ? val : ","+val;
    });
    return ids;
}
$("#deleteDriver").click(function (){
	if(getIds()!=""){
		ajaxTodo("${contextPath}/driver/findAlarm/"+getIds(),function (data){
	        $.each(data, function(idx, item) {
	            if (item == 1) {
	                $("#deleteA").attr("title","该设备有告警信息,确认一起删除?");
	                $("#deleteA").trigger("click");
	            }else{
	                $("#deleteA").attr("title","确认要删除?");
	                $("#deleteA").trigger("click");
	            }
	        });
	    });
	} else {
		swal("错误", "请选择正确的信息。", "error");
	}
	
    
})


</script>

<c:set var="ParentTitle" value="Driver" />
<c:set var="ModuleTitle" value="driverList" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript">
$("#search").click(function (){
  $("#property").val("?numPerPage=10&pageNum=1&sortOrder=asc&search_EQ_mesProductline.companyinfo.id="+$('#selectFactory').val()+"&search_LIKE_mesProductline.linename="+$('#selectLine').val()+"&search_LIKE_mesDrivertype.typename="+$('#searchType').val()+"&search_LIKE_name="+$('#searchName').val()+"&search_LIKE_sn="+$('#searchSn').val()+"");
})

  $(document).ready(function() {
    $("select").chosen({
      search_contains : true
    });
    
    window.setInterval(function() {
    	if($.table.getCurrent().bootstrapTable("getData",true).length > 0){
    		$.each($("#table").find("tr:not(:first)"), function(idx,item) {
                ajaxTodo("${contextPath}/driver/driverStatus/" + $(this).attr("data-uniqueid"),
                    function(data) {
                      $("#table").bootstrapTable("updateCell", {
                        index : idx,
                        field : "driverStatus",
                        value : data.status,
                        triggered : false
                      });
                    },false)
             })
    	}
    	
      //      $.table.setCurrent("table");
      //      $.table.refreshCurrent("${contextPath}/driver/driverData"+$("#property").val());
    }, 10000);
  });
</script>
</body>

</html>