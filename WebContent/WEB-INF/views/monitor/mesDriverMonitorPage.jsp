<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<body>
            <form class="form form-inline" method="post" action="${contextPath}/driver/driverData" data-target="table" onsubmit="return navTabSearch(this)">
             <div class="pageFormContent" layoutH="58">
     <div class="row">
      <div class="searchBar personnel_search" style="padding: 0px 15px;border:none;box-shadow: none;  width:auto; max-width:inherit ;" >
              <ul class="form-inline" style="padding:0px">
                <li class="form-group">
                <label for="inputText" class="searchtitle">选择工厂</label> 
                <select id="selectFactory" name="search_EQ_mesProductline.companyinfo.id" data-placeholder="请选择工厂" style="width:120px" class="form-control searchtext chosen-select">
                     <option value="">全部</option>
                      <c:forEach var="p" items="${companyinfos }">
                        <option value="${p.id }">${p.companyname }</option>
                      </c:forEach>
                </select>
                </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">选择产线</label> 
                <select id="selectLine" name="search_LIKE_mesProductline.linename" data-placeholder="请选择产线"  class="form-control searchtext chosen-select">
                <option value="">全部</option>
                </select>
              </li>
              <li class="form-group">
                <label for="inputText" class="searchtitle">设备类型</label> 
                <select id="searchType" name="search_LIKE_mesDrivertype.typename" data-placeholder="请选择设备类型" class="form-control searchtext chosen-select" >
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
        </div>
        <div style=" margin:0 15px 15px 15px">
        <table class="table table-striped" id="table" data-field="mesDrivers" data-url="${contextPath}/driver/driverData">
            <thead>
              <tr>
                 <th data-field="Number" width="50" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                  <input class="cbr checkboxCtrl" type="checkbox" group="ids">
                </th>
                <th data-field="sn" width="100">设备编号</th>
                <th data-field="name" width="100">设备名称</th>
                <th data-field="mesDrivertype.typename" width="100">设备类型</th>
                <th data-field="modelnumber" width="100">设备型号</th>
                <th data-field="mesProductline.linename" width="100">所在产线</th>
                <th data-field="mesProductline.companyinfo.companyname" width="100">所在工厂</th>
              </tr>
            </thead>
          </table>
          </div>
          </div></div>
                        <div class="modal-footer">
      <button type="button" id="inviteBtn" class="btn btn-primary">确定</button>
      <button type="button" class="btn btn-default"
        data-dismiss="modal">关闭</button>
    </div>
            </form>

<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript">

  $(document).ready(function() {
	  $("select").chosen({
	      search_contains : true
	  });
	  
	  $.table.init('table', {
	      singleSelect  : true,
	      queryParams: function (params) {
	    	  var _parames = $.extend({},$.table._op.queryParams(params),{ 
	    		  'search_EQ_mesProductline.companyinfo.id': $("#selectFactory").val(),
	    		  'search_LIKE_mesProductline.linename':$("#selectLine").val(),
	    		  'search_LIKE_mesDrivertype.typename':$("#searchType").val(),
	    		  'search_LIKE_name':$("#searchName").val(),
	    		  'search_LIKE_sn':$("#searchSn").val(),
	    		  pageNum: this.pageNumber
	    		 });
	               return _parames;
	        }
	    }, function(data) {
	    
	    });
  });
</script>
</body>

</html>