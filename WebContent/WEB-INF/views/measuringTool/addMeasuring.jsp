<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>

  <form method="post" id="measuringToolForm" action="${contextPath}/measuringTool/saveMeasuring" class="form form-horizontal" onsubmit="return validateCallback(this, dialogReloadCallback);">
      <div class="pageFormContent" layoutH="58">

            <div class="row">
                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>量具编号</label>
                    <div id="divOfSn" class="col-sm-6">
                        <input id="sn" type="text" name="sn" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>量具名称</label>
                  <div class="col-sm-6">
                    <input type="text" name="name" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="20"/>
                  </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>量具类型</label>
                    <div id="divOfType" class="col-sm-6 select_nobg">
                        <select id="type" name="type" class="form-control validate[required] required">
                            <option value="">请选择类型</option>
                            <c:forEach var="dictionary" items="${Dictionary1}">
                                <option value="${dictionary.name }">${dictionary.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>所属产线</label>
                    <div class="col-sm-6">
                        <select id="mesProductline" name="mesProductline.id" class="form-control validate[required]">
                            <option value="">请选择产线</option>
                            <c:forEach var="mpl" items="${mesProductlineList}">
                                <option value="${mpl.id}">${mpl.linename}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <%--<div class="form-group">--%>
                    <%--<label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>所属工序</label>--%>
                    <%--<div class="col-sm-6">--%>
                        <%--<select id="mesProductProcedure" name="mesProductProcedure.id" class="form-control validate[required]">--%>
                            <%--<option value="">请选择工序</option>--%>
                            <%--<c:forEach var="mpp" items="${mesProductProcedureList}">--%>
                                <%--<option value="${mpp.id}">${mpp.mesProduct.name }--${mpp.procedurename}</option>--%>
                            <%--</c:forEach>--%>
                        <%--</select>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>校验时间</label>
                    <div class="col-sm-6">
                        <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd" data-link-field="testingtime">
                            <input class="form-control datetime validate[required]" type="text" style="background: none;" value="" readonly>
                            <span class="add-on" style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;">
                                <i class="fa fa-remove"></i>
                            </span>
                            <span class="add-on" style="position: absolute; bottom:0; right: 15px; padding: 5px 7px;">
                                <i class="fa fa-th"></i>
                            </span>
                        </div>
                        <input type="hidden" id="testingtime" value="" name="testingtime" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>开始日期</label>
                    <div class="col-sm-6">
                        <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd" data-link-field="starttime">
                            <input class="form-control datetime validate[required]" type="text" style="background: none;" value="" readonly>
                            <span class="add-on" style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;">
                                <i class="fa fa-remove"></i>
                            </span>
                            <span class="add-on" style="position: absolute; bottom:0; right: 15px; padding: 5px 7px;">
                                <i class="fa fa-th"></i>
                            </span>
                        </div>
                        <input type="hidden" id="starttime" value="" name="starttime" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>结束日期</label>
                    <div class="col-sm-6">
                        <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd" data-link-field="endtime">
                            <input class="form-control datetime validate[required]" type="text" style="background: none;" value="" readonly>
                            <span class="add-on" style="position: absolute; bottom:0; right: 44px; padding: 5px 7px;">
                                <i class="fa fa-remove"></i>
                            </span>
                            <span class="add-on" style="position: absolute; bottom:0; right: 15px; padding: 5px 7px;">
                                <i class="fa fa-th"></i>
                            </span>
                        </div>
                        <input type="hidden" id="endtime" value="" name="endtime" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>预警小时数</label>
                    <div class="col-sm-6">
                        <input type="text" name="days" class="form-control input-medium validate[required,maxSize[45]] required" maxlength="20"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>使用状态</label>
                    <div class="col-sm-6">
                        <input type="radio" id="qy" name="isenabled" value="0" checked="checked" /><label for="qy">启用&nbsp;</label>
                        <input type="radio" id="ty" name="isenabled" value="1" /><label for="ty">停用&nbsp;</label>
                        <input type="radio" id="jx" name="isenabled" value="2" /><label for="jx">检修&nbsp;</label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">状态描述</label>
                    <div class="col-sm-6">
                        <textarea name="description" class="form-control input-medium textarea-scroll" cols="29" rows="3" maxlength="100"></textarea>
                    </div>
                </div>

            </div>
      </div>

      <div class="modal-footer">
        <button id="confirm" type="submit" class="btn btn-primary">确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>

<script type="text/javascript">
    $('.form_datetime').datetimepicker({
//        language:  'zh-CN',
//        format: 'yyyy-mm-dd',
//        weekStart: 1,
//        todayBtn:'linked',
//        autoclose: 1,
//        todayHighlight: 1,
//        startView: 2,
//        forceParse: 0,
//        showMeridian: 1,
//        minView: 2

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

<script type="text/javascript">
    var submitStatus = new Array();
    function checkValue(){
        if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
            ajaxTodo("${contextPath}/measuringTool/checkSn/" + "/"+$("#sn").val(), function(data) {
                checkData(data,$("#sn"),"量具编号不可重复",$("#divOfSn"),$("#measuringToolForm"),submitStatus,"sn");
            });
        }
    }
    function checkValue1(){
        if($("#sn").val()!=""&&$.trim($("#sn").val()) != ''){
            ajaxTodo("${contextPath}/measuringTool/checkSn/" + "/"+$("#sn").val(), function(data) {
                checkData1(data,$("#sn"),"量具编号不可重复",$("#divOfSn"),$("#measuringToolForm"));
            });
        }
    }
    $("#sn").keyup(checkValue);
    $("#confirm").click(function(){
        if(submitStatus.length>0){
            checkValue1();
        }else{
            $("#measuringToolForm").submit();
        }
    });

</script>