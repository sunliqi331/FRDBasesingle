<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<input type="hidden" id="sn" name="analyzeSearch.productionSn" value="${sn }">
<input type="hidden" id="_rowKey" name="rowKey" value="${rowKey }">
<input type="hidden" id="_procedureId" name="analyzeSearch.productProcedureId" value="${procedureId }">
          <table class="table table-striped" id="property_table" data-field="mesProcedureProperty" data-url="${contextPath}/statistics/getPropertyRecordList">
            <thead>
               <tr>
                  <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <!-- <th data-field="keyid" width="100">工序属性id</th> -->
                <th data-field="propertyname" width="100">工序属性名称</th>
                <th data-field="mesPoints.name" width="100">测点名称 </th>
                <th data-field="mesProductProcedure.procedurenum" width="100">所属工序</th>
                <th data-field="controlWay" width="100">测量方法</th>
                <th data-field="checkTime" width="100">时间</th>
                <th data-field="uppervalues" width="100">上公差</th>
                <th data-field="lowervalues" width="100">下公差</th>
                <th data-field="standardvalues" width="100">标准值</th>
                <th data-field="checkValue" width="100">检测值</th>
                <th data-field="mesPoints.units" width="100">单位 </th>
              </tr>
            </thead>
          </table>
          
          <script>
          $.table.init("property_table", {
        	  pagination:false,
        	  queryParams: function (params) {
        		  params["analyzeSearch.productionSn"] = $("#sn").val();
        		  params["rowKey"] = $("#_rowKey").val();
        		  params["analyzeSearch.productProcedureId"] = $("#_procedureId").val();
                       return params;
                }
        	  });
          
          
          </script>