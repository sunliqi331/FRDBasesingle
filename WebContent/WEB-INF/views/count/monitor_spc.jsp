<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="title" content="FRD - Demo">
    <title>SPC-分析</title>
    <%@ include file="/WEB-INF/views/com_head_spc.jsp"%>
    <link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
    <link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
    <%--<script src="${contextPath}/styles/assets/html2canvas/html2canvas.js"></script>--%>
    <%--<script src="${contextPath}/styles/assets/html2canvas/html2canvas.svg.js"></script>--%>
    <%--<script src="${contextPath}/styles/echarts/echarts.min.js"></script>--%>
    <style>
        #container{
            /*max-height: 450px;*/
        }
        .main-content{
            top: 0;
            /*max-height: 450px;*/
        }
        .main-content .main-wrap{
            /*max-height:450px;*/
            top: 0;
        }
        .modal-footer{
            border-top:0;
        }
    </style>
</head>
<script type="text/javascript">
    $(document).ready(function(){
        // $("#formID .chosen-container-single").css("width","135px");
        // SPC检索出来的数据删除
        $("#data-del-Btn").click(function(){
            if($.table.getSelectedId().length == 0){
                swal("警告","请选择需要删除的数据！","warning");
                return;
            }
            if($.table.getSelectedId().length > 5){
                swal("警告","请选择5条以下的数据进行删除","warning");
                return;
            }
            swal({
                    title: "确定删除数据？",
                    type: "warning",
                    showCancelButton: true,
                    confirmButtonColor: '#DD6B55',
                    confirmButtonText: '确认',
                    cancelButtonText: "取消",
                    closeOnConfirm: true
                },
                function(isConfirm){
                    if (isConfirm){
                        $.blockUI({
                            baseZ:99999,
                            message:"<h1>请稍等......</h1>",
                            onBlock:function(){
                                var query = {};
                                $.each($("#formID").serializeArray(),function(){
                                    query[this.name] = this.value;
                                });
                                query["rowKeyDelFlg"] = "0";

                                var delString = "";
                                var arrays = $('#analyzeTable').bootstrapTable("getSelections");
                                $.each(arrays, function(){
                                    delString = delString + this.rowKey + ","
                                })
                                query["rowKeyList"] = delString;
                                // delHBaseDate
                                $.table.setCurrent($("#formID").attr('data-target'));
                                $.table.refreshCurrent("${contextPath}/statistics/delHBaseDate", query,function(data){
                                    //$.table.getCurrent().bootstrapTable("insertRow",{index:0,row:{productName:"aaaa"}});
                                    $.unblockUI();
                                    swal("数据删除完毕。");
                                });
                            }
                        });
                    }
                });

        });
        $("#data-confirm-Btn").click(function(){
            alert(5555);
            return false;
            if($.table.getSelectedId().length == 0){
                swal("您未选择任何数据");
                return;
            }
            var values = new Array();
            var arrays = $.table.getCurrent().bootstrapTable("getSelections");
            $.each(arrays, function(){
                values.push(this.productionSn);
            })
            $.unique(values.sort());
            if($.table.getSelectedId().length != ($("#subNum").val()*$("#subRange").val())){
                swal("您选择的数据不符合要求,总条数应为："+($("#subNum").val()*$("#subRange").val()));
                return;
            }
            /* if(values.length > 1){
                swal("您选择的数据不符合要求,存在不同的产品序列号");
                return;
            } */
            $("#data-confirm").attr("href","${contextPath }/statistics/analyseSpcDataPage");
            $("#data-confirm").trigger("click");

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
        var subRangeOption;
        for(var i = 2;i <= 25; i++){
            subRangeOption += "<option value="+ i +">"+ i +"个</option>"
        }
        $("#subRange").append(subRangeOption);
        var subNumOption;
        for(var i = 2;i <= 100; i++){
            subNumOption += "<option value="+ i +">"+ i +"组</option>"
        }
        $("#subNum").append(subNumOption);
        $("select").chosen({search_contains:true});
        //通过产品ID获取该产品下的工序，并动态产生select
        function generateProcedureSelect(productId,callback){
            var option = "";
            $.ajax({
                url:"${contextPath}/statistics/generateProductProcedureSelect/"+productId,
                dataType:"JSON",
                type:"POST",
                success:function(data){
                    $("#productProcedureId").empty();
                    $.each(data,function(idx,item){
                        option += "<option value='"+ item.id +"'>"+ item.procedurename +"</option>";
                    });
                    $("#productProcedureId").append(option);
                    $("#productProcedureId").trigger("change");
                    $("#productProcedureId").trigger("chosen:updated");
                    if(callback)
                        callback($("#productProcedureId").val());
                },
                error:function(jqXHR, textStatus, errorThrown){
                    console.log(textStatus);
                }
            });
        }
        /*   $("#productId").change(function(event){
              $("#productProcedureId").empty();
              $("#productProcedureId").append("<option value=''>请选择</option>");
              $("#productProcedureId").trigger("chosen:updated");
              if($("#productId").val() != ""){
                  ajaxTodo("${contextPath}/statistics/generateProductProcedureSelect/"+$("#productId").val(),function(data){
				 $.each(data,function(idx,item){
					  $("#productProcedureId").append("<option value='"+item.id+"'>"+item.procedurename +"</option>")
				 });
				 $("productProcedureId").trigger("chosen:updated");
			  });
		  }
	  }); */



        function generateProcedurePropertySelect(procedureId,callback){
            var option = "";
            $.ajax({
                url:"${contextPath}/statistics/generateProcedurePropertySelect/"+procedureId,
                dataType:"JSON",
                type:"POST",
                success:function(data){
                    $("#procedurePropertyId").empty();
                    $.each(data,function(idx,item){
                        option += "<option value='"+ item.id +"'>"+ item.propertyname +"</option>";
                    });
                    $("#procedurePropertyId").append(option);
                    $("#procedurePropertyId").trigger("change");
                    $("#procedurePropertyId").trigger("chosen:updated");
                    if(callback)
                        callback();
                }	,
                error:function(jqXHR, textStatus, errorThrown){
                    console.log(textStatus);
                }
            });

        }
        $("#productId").change(function(){
            generateProcedureSelect($(this).val());
            //generateProcedurePropertySelect($(this).val());
        }) ;
        generateProcedureSelect($("#productId").val(),generateProcedurePropertySelect);
        $("#productProcedureId").change(function(){
            generateProcedurePropertySelect($(this).val());
        });
        $("#searchBtn").click(function(){
            $("#formID").submit();
        });
        $("#formID").submit(function(e){
            if($("#mesDriverId").val() == 0 || $("#productId").val() == 0 || $("#productProcedureId").val() == 0 || $("#procedurePropertyId").val() == 0){
                swal("请选择筛选条件");
                return false;
            }
            // 双环传动公司的spc数据取消时间限制。
            if("${companyId}" == "511") {
                if(($("#begin").val() == '' || $("#end").val() == '')){
                    swal("时间段未选择");
                    return false;
                }
                //当子组频率是连续取值时，时间段限制在12小时以内
                var twelveHours = 43560000;
                var tTemp = Date.parse($("#end").val())-Date.parse($("#begin").val());//所选时间段毫秒数
                if(tTemp<=0){ //结束时间不能小于开始时间
                    return false;
                }
                if($("#subSeq").val() == "0"){
                    if(tTemp > twelveHours){
                        swal("warning", "时间区间过长", "warning");
                        return false;
                    }
                }
            }
            var query = {};
            $.each($("#formID").serializeArray(),function(){
                query[this.name] = this.value;
            });

            $.table.setCurrent($("#formID").attr('data-target'));
            $.table.refreshCurrent($("#formID").attr('action'), query,function(data){
                //$.table.getCurrent().bootstrapTable("insertRow",{index:0,row:{productName:"aaaa"}});
            });
            return false;
        });
        $.table.init('analyzeTable', {
            pagination:false,
            sidePagination:'server',
            showExport: true,                     //是否显示导出
            exportDataType: "all",
            exportOptions:{
                ignoreColumn:[0]
            },
        });
        // generateProcedureSelect($("#product").val(),generateProcedurePropertySelect($("#procedure").val()));
    });

    //扫描spc最新数据
    var doSubscriber = function(){
        console.info("doSubscriber start");
        return monitorSubscriber.subscribe("/topic/showMonitor/monitor/spc",function(data){
            console.log("监控各个画面元素组装----------------开始" + new Date());
            var prop = JSON.parse(data.body);
            console.log(prop);
            console.log("监控各个画面元素组装----------------结束" + new Date());
        });
    }
</script>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<body>
<div id="container">
    <%--<%@ include file="/WEB-INF/views/include.header.jsp" %>--%>
    <%--<%@ include file="/WEB-INF/views/include.sidebar.jsp" %>--%>
    <div class="main-content removeble" style="left:0;bottom:80px;">
        <%--<ol class="breadcrumb" style="background: #f1f2f7; height:41px; margin:0;line-height:40px;    border-radius: 0; display:block;">--%>
            <%--<li ><i class="fa fa-home"></i>--%>
                <%--<a href="${contextPath}/management/index"> 首页</a></li>--%>
            <%--<li >统计分析SPC</li>--%>
        <%--</ol>--%>
        <div class="main-wrap">
            <div class="main-body">
                <div class="searchBar search_driver" style="margin-bottom: 15px;" >
                    <div class="search_header">
                        <i class="fa fa-minus search_small" onclick="searchHide()"></i>
                        <i class="fa fa-plus search_small" onclick="searchShow()"></i>
                        <i class="fa fa-search"></i> SPC-X图分析条件
                    </div>
                    <div class="ishidden" style="margin-bottom:5px" >
                        <form method="post" action="${contextPath}/statistics/analyseDataSearch" data-target="analyzeTable"
                              id="formID" class="form form-inline">

                            <div class="form-group">
                                <label for="mesDriverId" class="searchtitle">检测设备
                                </label>

                                <select id="mesDriverId" name="mesDriverId" data-placeholder="请选择检测设备"
                                        class="form-control validate[required]">
                                    <c:forEach items="${checkDrivers }" var="map">
                                        <optgroup label="${map.key }">
                                            <c:forEach items="${map.value }" var="checkDriver">
                                                <option value="${checkDriver.id }">${checkDriver.name }</option>
                                            </c:forEach>
                                        </optgroup>
                                    </c:forEach>
                                </select>

                            </div>
                            <div class="form-group">
                                <label for="productId" class="searchtitle">产品名称
                                </label>

                                <select id="productId" name="productId" data-placeholder="请选择产品"
                                        class="form-control validate[required]">
                                    <c:forEach items="${products }" var="product">
                                        <option value="${product.id }">${product.name }</option>
                                    </c:forEach>
                                </select>

                            </div>
                            <div class="form-group">
                                <label for="productProcedureId" class="searchtitle">产品工序
                                </label>

                                <select id="productProcedureId" name="productProcedureId" data-placeholder="请选择工序"
                                        class="form-control validate[required]">
                                </select>

                            </div>
                            <div class="form-group">
                                <label for="procedurePropertyId" class="searchtitle">被测参数</label>

                                <select id="procedurePropertyId" name="procedurePropertyId" data-placeholder="请选择工序参数"
                                        class="form-control validate[required]">
                                </select>

                            </div>
                            <!--             <div class="count_cg clearfix"> -->
                            <div class="form-group">
                                <label for="testCount" class="searchtitle">子组大小</label>

                                <select id="subRange" name="subRange" data-placeholder="请选择子组大小"
                                        class="form-control validate[required]">

                                </select>

                            </div>
                            <div class="form-group">
                                <label for="statisticalStandard" class="searchtitle">子组频率</label>

                                <select id="subSeq" name="subSeq" data-placeholder="请选择子组频率"
                                        class="form-control validate[required]">
                                    <option value="0">连续取值</option>
                                    <option value="2">每隔2小时</option>
                                    <option value="4">每隔4小时</option>
                                    <option value="6">每隔6小时</option>
                                    <option value="8">每隔8小时</option>
                                    <option value="10">每隔10小时</option>
                                    <option value="12">每隔12小时</option>
                                    <option value="14">每隔14小时</option>
                                    <option value="16">每隔16小时</option>
                                    <option value="18">每隔18小时</option>
                                    <option value="20">每隔20小时</option>
                                    <option value="22">每隔22小时</option>
                                    <option value="24">每隔24小时</option>
                                    <option value="26">每隔26小时</option>
                                    <option value="28">每隔28小时</option>
                                    <option value="30">每隔30小时</option>
                                    <option value="32">每隔32小时</option>
                                    <option value="34">每隔34小时</option>
                                    <option value="36">每隔36小时</option>
                                </select>

                            </div>
                            <div class="form-group">
                                <label for="subNum" class="searchtitle">子组数量</label>

                                <select id="subNum" name="subNum" data-placeholder="请选择子组数量"
                                        class="form-control validate[required]">

                                </select>

                            </div>
                            <div class="form-group rangetime ">
                                <label for="inputText" class="searchtitle">开始时间</label>

                                <div class="controls input-append date form_datetime1"
                                     data-date="" data-date-format="yyyy-mm-dd" style="position: relative;"
                                     data-link-field="begin">
                                    <input id="begin" name="begin" class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;"
                                           value="" readonly>
                                    <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                                    <span class="add-on" style="position: absolute; right: 0px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                                </div>

                            </div>
                            <div class="form-group rangetime ">
                                <label for="inputText" class="searchtitle">结束时间</label>

                                <div class="controls input-append date form_datetime2"
                                     data-date="" data-date-format="yyyy-mm-dd" style="position: relative;"
                                     data-link-field="end">
                                    <input id="end" name="end" class="form-control datetime" type="text" style="background: #fff;border-color: #c8ced6;"
                                           value="" readonly>
                                    <span class="add-on" style="position: absolute; right: 29px; bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span>
                                    <span class="add-on" style="position: absolute; right: 0px;  bottom:0;border-color:#c8ced6; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                                </div>

                            </div>
                            <div class="form-group rangeList" style="margin-top:5px">
                                <label for="inputText" class="searchtitle">设备编号</label>

                                <input id="registernum" type="text" name="productionSn"
                                       class="form-control input-medium validate[required,maxSize[32]] required "
                                       maxlength="32" style="width:135px" value="" />

                            </div>
                            <div class="form-group" style="margin-top:5px;">
                                <button class="btn btn-primary" id="submit">确定</button>
                            </div>
                            <!--<div class="form-group col-sm-6 rangeSource" >

                                              <div type="button" class="btn btn-info driver_attribute" ><a href="dataSource.html" target="_blank" style="color:#fff"><i class="icon-ok"></i> 选择数据源</a></div>
                                      </div>-->




                            <%--<a type="submit" id="searchBtn" class="btn btn-info btn-search1"--%>
                               <%--style="width: 150px;margin-top: 5px;">数据检索</a>--%>


                        </form>
                    </div>
                </div>
                <%--<div id="toolBar clearfix " style="margin-bottom:10px;">--%>
                    <%--<div class="btn-group">--%>
                        <%--<a class="btn btn-default1" id="data-confirm-Btn" >--%>
                            <%--<i class="fa fa-check"></i>--%>
                            <%--数据确认--%>
                        <%--</a>--%>
                        <%--<a style="display: none" class="btn btn-primary" id="data-confirm" role="button" target="page" rel="calculate" ></a>--%>
                    <%--</div>--%>
                    <%--<shiro:hasPermission name="spc:delete">--%>
                        <%--<div class="btn-group">--%>
                            <%--<a class="btn btn-default1" id="data-del-Btn" >--%>
                                <%--<i class="fa fa-close"></i>--%>
                                <%--数据删除--%>
                            <%--</a>--%>
                            <%--<a style="display: none" class="btn btn-primary" id="data-confirm" role="button" target="page" rel="calculate" ></a>--%>
                        <%--</div>--%>
                    <%--</shiro:hasPermission>--%>
                <%--</div>--%>
                <%--<table class="table table-striped" id="analyzeTable" data-field="cgAnalyzeData">--%>
                    <%--<thead>--%>
                    <%--<tr>--%>
                        <%--<th data-field="Number" width="2%" data-align="center">序号</th>--%>
                        <%--<th data-checkbox="true" width="22">--%>
                            <%--<input class="cbr checkboxCtrl" type="checkbox" group="ids">--%>
                        <%--</th>--%>
                        <%--<th data-field="productName" width="100">产品名称</th>--%>
                        <%--<th data-field="productionSn" width="100">序列号</th>--%>
                        <%--<th data-field="mesDriverName" width="100">测量设备</th>--%>
                        <%--<th data-field="productProcedureName" width="100">工序名称</th>--%>
                        <%--<th data-field="procedurePropertyName" width="100">参数名称</th>--%>
                        <%--<th data-field="value" width="150">参数值</th>--%>
                        <%--<th data-field="time" width="150">上传时间</th>--%>
                    <%--</tr>--%>
                    <%--</thead>--%>
                    <%--<!--  <tbody>--%>
                         <%--<tr data-index="0" data-uniqueid="73">--%>
                           <%--<td>1230</td>--%>
                           <%--<td>1231</td>--%>
                           <%--<td>1232</td>--%>
                           <%--<td>1233</td>--%>
                           <%--<td>1234</td>--%>
                           <%--<td>1235</td>--%>
                           <%--<td>1236</td>--%>
                         <%--</tr>--%>
                     <%--</tbody> -->--%>
                <%--</table>--%>
            </div>
        </div>



    </div>
</div>

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
<%--<c:set var="ParentTitle" value="Count"/>--%>
<%--<c:set var="ModuleTitle" value="spc"/>--%>
<%--<%@ include file="/WEB-INF/views/com_foot.jsp"%>--%>
<%--<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>--%>
<%--<script type="text/javascript" src="${contextPath }/js/canvas2image.js"></script>--%>
<%--<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>--%>
<!-- MQ通信引用 -->
<%--<script type="text/javascript" src="${contextPath }/js/monitor.index.js"></script>--%>
<%--<script type="text/javascript" src="${contextPath }/js/monitor.render.js"></script>--%>
<%--<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>--%>

<%--<script src="${contextPath}/js/dateRangeUtil.js"></script>--%>
<%--<script src="${contextPath }/js/sockjs.js"></script>--%>
<%--<script src="${contextPath }/js/stomp.js"></script>--%>
</body>
</html>
