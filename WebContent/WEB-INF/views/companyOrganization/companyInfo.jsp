<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

      <div class="main-wrap">
          <table class="table table-striped" id="table" data-field="companyinfo" data-single-select="true" data-url="${contextPath }/companyOrganization/companyInfoData/${id}">
            <thead>
              <tr>
                <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-field="companyname" width="100">公司名称</th>
                <th data-field="address" width="100">公司地址</th>
                <th data-field="companyemail" width="100">公司邮箱</th>
                <th data-field="legalperson" width="100">法人</th>
                <th data-field="businesstype" width="100">所属行业</th>
                <th data-field="infotype" width="100">类型</th>
                <th data-field="companystatus" width="130">状态</th>
              </tr>
            </thead>
          </table>

      </div>

  <!-- Modal 
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
</script> -->
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
