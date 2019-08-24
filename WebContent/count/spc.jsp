<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<div id="container">
  <div class="main-wrap">
    <div class="main-body">
      <div class="searchBar" style="padding: 20px 10px;">
        <form method="post" action="${contextPath}/company/create"
          id="formID" class="form form-horizontal"
          enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, subPageCallback);">
          <div class="pageFormContent" layoutH="58">
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">产线</label>
              <div class="col-sm-8">
                <select id="analysisMethod" name="analysisMethod"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="产线1" selected>产线1</option>
                  <option value="产线2">产线2</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">产品型号</label>
              <div class="col-sm-8">
                <select id="analysisMethod" name="analysisMethod"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="产品型号1" selected>产品型号1</option>
                  <option value="产品型号2">产品型号2</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">评定方法</label>
              <div class="col-sm-8">
                <select id="analysisMethod" name="analysisMethod"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="产线1" selected>方法1</option>
                  <option value="产线2">方法2</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">子组大小</label>
              <div class="col-sm-8">
                <select id="parentname" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6">
              <label for="inputText" class="control-label col-sm-4">子组类型</label>
              <div class="col-sm-8">
                <select id="childType" name="parentname"
                  class="form-control validate[required]">
                  <option>请选择</option>
                  <option value="连续">连续</option>
                  <option value="随机">随机</option>
                  <option value="datasource">数据源</option>
                </select>
              </div>
            </div>
            <div class="form-group col-sm-6 spc_count">
              <label for="inputText" class="control-label col-sm-4">子组数量</label>
              <div class="col-sm-8">
                <input id="registernum" type="text "
                  style="width: 70%; float: left" name="registernum"
                  class="form-control input-medium validate[required,maxSize[32]] required "
                  maxlength="32" value="" /><span style="line-height: 34px">&nbsp;个工件</span>
              </div>
            </div>
            <div class="form-group col-sm-6 ">
              <label class="control-label col-sm-4"> <input
                type="checkbox" name="time" value="${p.sn}" checked="checked" />&nbsp;时间段
              </label>
              <div class=" col-sm-8 ">
                <div class="controls input-append date form_datetime1"
                  data-date="" data-date-format="yyyy-mm-dd"
                  data-link-field="dtp_input1">
                  <input class="form-control datetime" type="text"
                    placeholder="开始时间" style="background: #fff;" value="" readonly>
                  <span class="add-on"
                    style="position: absolute; right: 48px; padding: 0px 9px; line-height: 32px"><i
                    class="fa fa-remove"></i></span> <span class="add-on"
                    style="position: absolute; right: 15px; padding: 0px 9px; line-height: 32px"><i
                    class="fa fa-th"></i></span>
                </div>
                <input type="hidden" id="dtp_input1" value="" />
              </div>
            </div>
            <div class="form-group col-sm-6">
              <div class="controls input-append date form_datetime2 col-sm-8"
                data-date="" data-date-format="yyyy-mm-dd"
                data-link-field="dtp_input2">
                <input class="form-control datetime" type="text"
                  placeholder="结束时间" style="background: #fff;" value="" readonly>
                <span class="add-on"
                  style="position: absolute; right: 48px; padding: 0px 9px; line-height: 32px"><i
                  class="fa fa-remove"></i></span> <span class="add-on"
                  style="position: absolute; right: 15px; padding: 0px 9px; line-height: 32px"><i
                  class="fa fa-th"></i></span>
              </div>
              <input type="hidden" id="dtp_input2" value="" />
            </div>
            <div class="table-responsive spc_table">
              <table class="table ">
                <thead>
                  <tr style="background: #f6f7f8;">
                    <th width="100">序号</th>
                    <th width="100">工序1</th>
                    <th width="100">工序2</th>
                    <th width="100">工序3</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>1</td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}"
                        checked="checked" />&nbsp;段长
                    </label></td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}"
                        checked="checked" />&nbsp;段长
                    </label></td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}"
                        checked="checked" />&nbsp;段长
                    </label></td>
                  </tr>
                  <tr>
                    <td>2</td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}" />&nbsp;内径
                    </label></td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}" />&nbsp;内径
                    </label></td>
                    <td><label class="check_label"> <input
                        class="check_line" type="checkbox"
                        name="permissions[${s.index}].sn" value="${p.sn}" />&nbsp;内径
                    </label></td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="clearfix"></div>
            <div class="modal-footer"
              style="border-top: 0; text-align: center; width: 100%;">
              <button type="submit" class="btn btn-primary"
                style="width: 200px;">开始评定</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
<script type="text/javascript">
  $("#childType").change(function(e) {
    //alert($("#analysisMethod").val());
    if ($("#childType").val() == "datasource") {
      window.open('dataSource.html');
      //$(".count_cg").css("display","none"); 
      //$(".count_grr").css("display","block");  
    }

  });
  $('.form_datetime1').datetimepicker({
    language : 'zh-CN',
    format : 'yyyy-mm-dd hh:ii:ss',
    weekStart : 1,
    todayBtn : 'linked',
    autoclose : 1,
    todayHighlight : 1,
    startView : 2,
    forceParse : 0,
    showMeridian : 1,
  //minView: 2

  });

  $('.form_datetime2').datetimepicker({
    language : 'zh-CN',
    format : 'yyyy-mm-dd hh:ii:ss',
    weekStart : 1,
    todayBtn : 'linked',
    autoclose : 1,
    todayHighlight : 1,
    startView : 2,
    forceParse : 0,
    showMeridian : 1,
  //minView: 2

  });
</script>