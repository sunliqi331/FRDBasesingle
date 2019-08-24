<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<div class="pageContent">
<table class="table table-striped" id="table" data-field="position" data-url="${contextPath}/position/data">
            <thead>
              <tr>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <th data-field="name" width="100">产品/过程参数</th>
                <th data-field="name" width="100">规范/公差</th>
                <th data-field="name" width="100">特殊特性</th>
                <th data-field="name" width="100">测量方法</th>
                <th data-field="name" width="100">检验频次</th>
                <th data-field="name" width="100">控制方法</th>
              </tr>
            </thead>
          </table>
</div>
