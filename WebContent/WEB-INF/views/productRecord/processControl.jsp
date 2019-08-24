<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<div class="pageContent">
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
<script type="text/javascript">
$(document).ready(function(){
	//alert("1");
  $(".mes_see .control-label").append("：");	
	
});
</script>
