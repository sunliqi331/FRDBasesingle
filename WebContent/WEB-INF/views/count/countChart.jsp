<%@ page language="java" contentType="text/html; charset=UTF-8"
    trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>countChart</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<!-- <link href="${contextPath }/styles/assets/bootstrap-table/bootstrap-table.css" rel="stylesheet"> -->
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css"
    rel="stylesheet" type="text/css" />
<link href="${contextPath}/styles/css/animate.css" rel="stylesheet" />
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<link href="${contextPath}/styles/css/countChart/countChart.css"
    rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<script src="${contextPath}/js/dateRangeUtil.js"></script>
<script src="${contextPath}/js/jquery.js"></script>
<script src="${contextPath}/js/bootstrap.js"></script>

<link href="${contextPath}/js/layui/css/layui.css" rel="stylesheet" />
<script type="text/javascript">
var contextPathGet = "${contextPath}";

var arr2 = ["1%","2%","3%","4%","5%","6%","7%","8%","9%","10%","11%","12%"];
var arr3 = ["1%","2%","3%","4%","5%","6%","7%","8%","9%","10%","11%","12%"];
var indexI2 = 0;
var indexI3 = 0;

</script>
</head>
<body>
    <div id="container">
        <%@ include file="/WEB-INF/views/include.header.jsp"%>
        <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
        <div class="main-content">
            <ol class="breadcrumb">
                <li><i class="fa fa-home"></i><a
                    href="${contextPath}/management/index"> 首页</a></li>
                <li>统计</li>
            </ol>
            <div class="main-wrap">
                <div class="main-body" style="overflow-x:auto !important;">
                    <ul class="tabs clearfix">
                        <li style="margin-left: -1px"><a class="tab2" href="#tab2">产品</a></li>
                        <li style="margin-left: -1px"><a class="tab3" href="#tab3">设施</a></li>
                    </ul>
                    <div class="tab_container chart_tab">
                        <div id="tab2" class="tab_content"
                            style="display: none; padding-bottom: 15px; min-height: 1700px;width:1647px;">
                            <div class="driver_info">
                                <form class="form-inline" method="post"
                                    action="${contextPath}/companyRole/data" data-target="table">
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">产品名称</label> <select
                                            name="modelnum" id="modelnum" class="form-control searchtext"
                                            style="padding-right: 50px">
                                            <!--  <option id="AllModelnum" value="">全部</option> -->
                                            <c:forEach var="p" items="${mesProduct}">
                                                <option value="${p.modelnum }">${p.name }</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">批次号</label> <select
                                            name="productBatchid" id="productBatchid"
                                            class="form-control searchtext" style="padding-right: 50px">
                                        </select>
                                    </div>

                                    <!-- 时间选择器  --------------------------------------------start---->
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">时间选择</label> <select
                                            name="search_time" id="search_time"
                                            class="form-control searchtext">
                                            <option value="1">本日</option>
                                            <option value="2">本周</option>
                                            <option value="3">本月</option>
                                            <option value="4">本年</option>
                                            <!-- <option value="define_time">自定义时间段</option> -->
                                        </select>
                                    </div>
                                    <div class="form-group rangetime" style="width: 305px;">
                                        <label for="inputText" class="searchtitle"
                                            style="float: left; padding-top: 4px;">开始时间</label>
                                        <div class="controls input-append date form_datetime1"
                                            style="position: relative" data-date=""
                                            data-date-format="yyyy-mm-dd hh:ii:ss"
                                            data-link-field="begin">
                                            <input id="dtp_input1s" class="form-control datetime"
                                                type="text" style="background: #fff;" value="" readonly>
                                            <span class="add-on"
                                                style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-remove"></i>
                                            </span> <span class="add-on"
                                                style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-th"></i>
                                            </span>
                                        </div>
                                        <input type="hidden" id="begin" value="" name="starttime" />
                                    </div>
                                    <div class="form-group rangetime" style="width: 305px;">
                                        <label for="inputText" class="searchtitle"
                                            style="float: left; padding-top: 4px;">结束时间</label>
                                        <div class="controls input-append date form_datetime2"
                                            style="position: relative" data-date=""
                                            data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">
                                            <input class="form-control datetime " type="text"
                                                style="background: #fff;" value="" readonly> <span
                                                class="add-on"
                                                style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-remove"></i>
                                            </span> <span class="add-on"
                                                style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-th"></i>
                                            </span>
                                        </div>
                                        <input type="hidden" id="end" value="" />
                                    </div>
                                    <!-- 时间选择器  --------------------------------------------end---->
                                    <a class="btn btn-default1" id="searchForEcharts" href="#">
                                        <i class="fa fa-bar-chart"></i> <span>搜索</span>
                                    </a>
                                </form>

                            </div>
                            <!-- 产品数据 -->
                            <div id="tab2Ddiv" class="tab2Ddiv">
                                <font class="tab2DdivFont">产品统计</font>
                                <!-- 所选产品和产品型号 -->
                                <div class="product">
                                    <div class="modelnumSelectedArea">
                                        <font id="modelnumSelected" class="modelnumSelected"></font>
                                    </div>
                                    <div style="margin-top: 7%;">
                                        <font class="productNo">产品型号：<span id="productNo"></span></font>
                                    </div>
                                </div>
                                <!-- 所选时间区域 -->
                                <div class="productMid">
                                    <div class="productMidTime">
                                        <font class="startTimeForS" id="startTimeForS"></font>
                                    </div>
                                    <div class="productMidTime">
                                        <font class="modelnumSelected" id="monthSel">0</font> <font
                                            class="productMidTimeConst">月</font> <font
                                            class="modelnumSelected" id="daySel">0</font> <font
                                            class="productMidTimeConst">天</font> <font
                                            class="modelnumSelected" id="hourSel">5</font> <font
                                            class="productMidTimeConst">小时</font> <font
                                            class="modelnumSelected" id="minutesSel">18</font> <font
                                            class="productMidTimeConst">分</font> <font
                                            class="modelnumSelected" id="secondsSel">25</font> <font
                                            class="productMidTimeConst">秒</font>
                                    </div>
                                </div>
                                <!-- 产品总数和产品合格数 -->
                                <div class="productOKNGCountArea">
                                    <div style="margin-top: 3%;">
                                        <div style="float: left;">
                                            <font id="productCount" class="productCountFont">产品总数：</font>
                                        </div>
                                        <div class="productCountAreaFont"
                                            id="productCountAreaFontTotal">19728</div>
                                    </div>
                                    <div style="padding-top: 10%; margin-top: 12%;">
                                        <div style="float: left; width: 120px; text-align: right;">
                                            <font id="productCount" class="productCountFont">合格数：</font>
                                        </div>
                                        <div class="productCountAreaFont" id="productCountAreaFontOk">11500</div>
                                    </div>
                                </div>
                                <!-- 产品合格率 -->
                                <div class="productPassArea">
                                    <div style="float: left; line-height: 100px;">
                                        <font id="productPassArea" class="productPassAreaFont">合格率：</font>
                                    </div>
                                    <div class="productCountAreaFontImg"
                                        style="padding: 0.2% 10% 0.2% 10%; margin-left: 4%; margin-top: 5%;">
                                        <font id="productCountAreaFontImgFont"
                                            class="productCountAreaFontImgFont">68.7%</font>
                                    </div>
                                </div>
                            </div>
                            <div class="titleMarks1 ibox" id="productCountAreaUp">
                                <div class="ibox-tools" style="z-index: 99; margin-top: 1%;">
                                    <a class="collapse-link"> <i class="fa fa-chevron-up"></i></a>
                                </div>
                                <div>
                                    <img src="${contextPath}/styles/img/titleMark.png" /><font
                                        id="productCountTitle" class="productCountFont2">产品合格率对比图</font>
                                </div>

                                <!-- 产品图标数据 -->
                                <div id="productCountArea1"
                                    style="float: left; margin-top: 0.8%;">
                                    <div id="mainData1" class="maindata1 ibox-content"></div>
                                    <div id="mainData2" class="maindata2 ibox-content"></div>
                                    <div id="mainData3" class="maindata3 ibox-content"></div>
                                </div>

                                <div id="productCountArea2" class="chartLeftArea">
                                    <div id="mainData4" class="maindata4 ibox-content"></div>
                                    <div id="mainData5" class="maindata5 ibox-content"></div>
                                </div>

                            </div>
                            <!-- 产品批次图标数据-->
                            <div id="productCountArea3" class="ibox">
                                <div class="ibox-tools"
                                    style="z-index: 99; margin-top: 1%; margin-right: 2%;">
                                    <a class="collapse-link"> <i class="fa fa-chevron-up"></i>
                                    </a>
                                </div>
                                <div class="titleMarks1">
                                    <div>
                                        <img src="${contextPath}/styles/img/titleMark.png" /><font
                                            id="productCountTitle2" class="productCountFont2">产品各批次数据分析图</font>
                                    </div>
                                </div>
                                <div id="mainData6" class="maindata1 ibox-content"
                                    style="float: left"></div>
                                <div id="mainData7" class="maindata6 ibox-content"
                                    style="float: left"></div>
                            </div>
                        </div>

                        <div id="tab3" class="tab_content "
                            style="display: none; padding-bottom: 15px; min-height: 100px !important;width: 1647px;">
                            <div class="driver_info">
                                <form class="form-inline" method="post" action="#"
                                    data-target="table" onsubmit="return navTabSearch(this)">

                                    <input type="hidden" id="chooseCompany" value="${company.id}" />
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">选择工厂</label>
                                        <select id="chooseFactory" class="form-control searchtext">
                                            <option value="">全部</option>
                                            <c:forEach var="p" items="${companyinfos}">
                                                <option value="${p.id }">${p.companyname}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">选择产线</label> <select
                                            id="chooseProductLine" class="form-control searchtext">
                                            <option value="">全部</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">选择设备</label> <select
                                            name="ChooseDriver" id="ChooseDriver"
                                            class="form-control searchtext">
                                            <option value="">全部</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputText" class="searchtitle">统计类型</label> <select
                                            name="EnergyType" id="EnergyType"
                                            class="form-control searchtext">
                                            <option value="electric">耗电量</option>
                                            <option value="water">耗水量</option>
                                            <option value="gas">耗气量</option>
                                            <option value ="runtime">运行时间</option>
                                        </select>
                                    </div>

                                    <!-- 时间选择器  --------------------------------------------start---->
                                    <div class="form-group" id="searchTimeTabArea3">
                                        <label for="inputText" class="searchtitle">时间选择</label> <select
                                            name="search_timeTab3" id="search_timeTab3"
                                            class="form-control searchtext">
                                            <option value="1">本日</option>
                                            <option value="2">本周</option>
                                            <option value="3">本月</option>
                                            <option value="4">本年</option>
                                            <!-- <option value="define_time">自定义时间段</option> -->
                                        </select>
                                    </div>
                                    <div class="form-group rangetime" style="width: 305px;" id="timeSelectTab3">
                                        <label for="inputText" class="searchtitle"
                                            style="float: left; padding-top: 4px;">运行日期</label>
                                        <div class="controls input-append date form_datetime3"
                                            style="position: relative" data-date=""
                                            data-date-format="yyyy-mm-dd"
                                            data-link-field="beginTab3">
                                            <input id="dtp_input1s" class="form-control datetime"
                                                type="text" style="background: #fff;" value="" readonly>
                                            <span class="add-on"
                                                style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-remove"></i>
                                            </span> <span class="add-on"
                                                style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-th"></i>
                                            </span>
                                        </div>
                                        <input type="hidden" id="beginTab3" value="" name="timeSelectTabVal" />
                                    </div>

                                    <div class="form-group rangetime" style="width: 305px;" id="startTimeTab3">
                                        <label for="inputText" class="searchtitle"
                                            style="float: left; padding-top: 4px;">开始时间</label>
                                        <div class="controls input-append date form_datetime1"
                                            style="position: relative" data-date=""
                                            data-date-format="yyyy-mm-dd hh:ii:ss"
                                            data-link-field="begin">
                                            <input id="dtp_input1s" class="form-control datetime"
                                                type="text" style="background: #fff;" value="" readonly>
                                            <span class="add-on"
                                                style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-remove"></i>
                                            </span> <span class="add-on"
                                                style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-th"></i>
                                            </span>
                                        </div>
                                        <input type="hidden" id="begin" value="" name="starttime" />
                                    </div>
                                    <div class="form-group rangetime" style="width: 305px;" id="endTimeTab3">
                                        <label for="inputText" class="searchtitle"
                                            style="float: left; padding-top: 4px;">结束时间</label>
                                        <div class="controls input-append date form_datetime2"
                                            style="position: relative" data-date=""
                                            data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="end">
                                            <input class="form-control datetime " type="text"
                                                style="background: #fff;" value="" readonly> <span
                                                class="add-on"
                                                style="position: absolute; right: 29px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-remove"></i>
                                            </span> <span class="add-on"
                                                style="position: absolute; right: 0px; bottom: 0; border-color: #c8ced6; padding: 5px 7px;">
                                                <i class="fa fa-th"></i>
                                            </span>
                                        </div>
                                        <input type="hidden" id="end" value="" />
                                    </div>
                                    <!-- 时间选择器  --------------------------------------------end---->

                                    <div class="form-group countBut">
                                        <a class="btn btn-default1"
                                            id="searchFosearchForEchartsForDriverrEcharts" href="#">
                                            <i class="fa fa-bar-chart"></i> <span>设备分析图表</span>
                                        </a>
                                        <button id="exportExcel" type="button" class="btn btn-info"
                                            style="float: right; margin-left: 20px; display: none;">导出表格</button>
                                        <button id="search2" type="button" class="btn btn-info"
                                            style="float: right; margin-left: 20px; display: none;">搜索</button>
                                        <div style="clear: both"></div>
                                    </div>
                                    <iframe id="ifile" style="display: none"></iframe>
                                </form>
                            </div>

                            <!-- 单一设备区 -->
                            <div style="display:none;" id="driverAnalysis" class="driverAnalysis">
                                <font class="tab3DdivFont" id="countTypeTab3">耗电量</font>
                                <!-- 所选产品和产品型号 -->
                                <div class="product">
                                    <div class="modelnumSelectedArea">
                                        <font id="modelnumSelectedTab3" class="modelnumSelected">数控机床A1</font>
                                    </div>
                                    <div style="margin-top: 7%;">
                                        <font class="productNo" id="productNoTab3">T1SDT0004</font>
                                    </div>
                                </div>

                                <div class="driverAnalysisMid">
                                    <div class="dirverMidTime" style="width: 30%;">
                                        <font class="startTimeForS" id="startTimeForSTab3">2018-07-12
                                            14:10:17 - 2018-07-12 14:28:18</font>
                                    </div>
                                    <div class="dirverMidTime" style="width: 25%;">
                                        <font class="modelnumSelected" id="monthSelTab3">0</font> <font
                                            class="productMidTimeConst">月</font> <font
                                            class="modelnumSelected" id="daySelTab3">0</font> <font
                                            class="productMidTimeConst">天</font> <font
                                            class="modelnumSelected" id="hourSelTab3">5</font> <font
                                            class="productMidTimeConst">小时</font> <font
                                            class="modelnumSelected" id="minutesSelTab3">18</font> <font
                                            class="productMidTimeConst">分</font> <font
                                            class="modelnumSelected" id="secondsSelTab3">25</font> <font
                                            class="productMidTimeConst">秒</font>
                                    </div>
                                    <div class="heightLine heightLineMargin-right"
                                        style="margin-top: 14px;"></div>
                                    <div class="driverWegTotal tableCellMiddel" style="width: 40%;">
                                        <font class="driverWebMidTimeConst">耗电量合计：</font> <font
                                            class="driverWebMidt" id="sumValueTab3">19728</font> <font
                                            class="productMidTimeConst">KWH</font>
                                    </div>
                                </div>

                                <div class="driverAnalysisArea">
                                    <div class="driverAnalysisMidAreaItem"
                                        id="driverTopTimeAnalyniseArea"></div>
                                    <div class="driverAnalysisMidAreaItem de_margin_top"
                                        id="driverTopTimeAnalyniseBarArea"></div>
                                    <div class="driverAnalysisMidAreaItem de_margin_top"
                                        id="driverTopTimeAnalyniseLineArea"></div>

                                    <div class="de_margin_width">
                                        <font class="wegFontBottom">耗电量明细：</font>
                                    </div>
                                    <div class="de_margin_top_1">
                                        <ul class="ui" id="Ulist">
                                            <li class="font_style_00b0f0_white" style="width: 70px;">时间</li>
                                            <li>1时</li>
                                            <li>2时</li>
                                            <li>3时</li>
                                            <li>4时</li>
                                            <li>5时</li>
                                            <li>6时</li>
                                            <li>7时</li>
                                            <li>8时</li>
                                            <li>9时</li>
                                            <li>10时</li>
                                            <li>11时</li>
                                            <li>12时</li>
                                            <li>13时</li>
                                            <li>14时</li>
                                            <li>15时</li>
                                            <li>16时</li>
                                            <li>17时</li>
                                            <li>18时</li>
                                            <li>19时</li>
                                            <li>20时</li>
                                            <li>21时</li>
                                            <li>22时</li>
                                            <li>23时</li>
                                            <li>24时</li>
                                        </ul>
                                        <ul class="ui" id="Ulist2">
                                            <li class="font_style_00b0f0_white" style="width: 70px;">耗电量</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                            <li>150</li>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <!-- 设备运行状态area -->
                            <div class="driverAnalysisRuntime" id="driverAnalysisRuntime" style="display:none;">
                              <font class="tab3DdivFont" id="countTypeTab3">设备状态</font>
                                <!-- 所选产品和产品型号 -->
                                <div class="product">
                                    <div class="modelnumSelectedArea">
                                        <font id="modelnumSelectedTab4" class="modelnumSelected">数控机床A1</font>
                                    </div>
                                    <div style="margin-top: 7%;">
                                        <font class="productNo" id="productNoTab4">T1SDT0004</font>
                                    </div>
                                </div>

                                <div class="driverAnalysisMid" style="padding-left: 1.8%;">
                                    <div class="dirverMidTime" style="width: 100px;display:none;">
                                        <font class="startTimeForS">查询总时间：</font>
                                    </div>
                                    <div class="dirverMidTime" style="width: 22%;display:none;">
                                        <font class="modelnumSelected" id="monthSelTab4">0</font> <font
                                            class="productMidTimeConst">月</font> <font
                                            class="modelnumSelected" id="daySelTab4">0</font> <font
                                            class="productMidTimeConst">天</font> <font
                                            class="modelnumSelected" id="hourSelTab4">5</font> <font
                                            class="productMidTimeConst">小时</font> <font
                                            class="modelnumSelected" id="minutesSelTab4">18</font> <font
                                            class="productMidTimeConst">分</font> <font
                                            class="modelnumSelected" id="secondsSelTab4">25</font> <font
                                            class="productMidTimeConst">秒</font>
                                    </div>
                                    <div class="driverWegTotal tableCellMiddel">
                                        <div class="driverRuntimeFontArea">
                                          <div class="driverRuntimeFontAreaGreen" id="runtimePercent">98.1%</div>
                                          <div class="table_cell width_defind" id="runtimefont">运行：608分52秒</div>
                                          <div class="driverRuntimeFontAreaYellow" id="standBytimePercent">98.1%</div>
                                          <div class="table_cell width_defind" id="standBytimefont">待机：608分52秒</div>
                                          <div class="driverRuntimeFontAreaRed" id="stoptimePercent">98.1%</div>
                                          <div class="table_cell width_defind" id="stoptimefont">停机：608分52秒</div>
                                        </div>
                                    </div>
                                </div>
                              <div class="driverAnalysisMidAreaItemRuntime" id="driverAnalysisRuntimeArea"></div>

                              <div class="driverAnalysisArea">
                                  <div class="driverAnalysisMidAreaItem"
                                      id="driverTopTimeAnalyniseArea2"></div>
                                  <div class="driverAnalysisMidAreaItem de_margin_top"
                                      id="driverTopTimeAnalyniseBarArea2"></div>
                                  <div class="driverAnalysisMidAreaItem de_margin_top"
                                      id="driverTopTimeAnalyniseLineArea2"></div>
                              </div>
                            </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    </div>
    <c:set var="ParentTitle" value="Count" />
    <c:set var="ModuleTitle" value="countChart" />
    <%@ include file="/WEB-INF/views/com_foot.jsp"%>
</body>
</html>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript"
    src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript"
    src="${contextPath }/styles/echarts/echarts.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script src="${contextPath}/js/content.js"></script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<script src="${contextPath}/js/countChart/echartProduct.js"></script>
<script src="${contextPath}/js/countChart/countChart.js"></script>
<script src="${contextPath}/js/countChart/timeUtils.js"></script>
<script src="${contextPath}/js/countChart/utils.js"></script>
<script src="${contextPath}/js/countChart/indexpage.js"></script>
<!-- 设备统计分析用js -->
<script src="${contextPath}/js/countChart/driverIndexPage.js"></script>

<script src="${contextPath}/js/countChart/driverIndexRuntime.js"></script>
<script src="${contextPath}/js/countChart/echartDriver.js"></script>

<script src="${contextPath}/js/layui/layui.js"></script>
<script>
//一般直接写在一个js文件中
layui.use(['layer', 'form'], function(){
  var layer = layui.layer
  ,form = layui.form;
  
  layer.msg('开启统计分析模块');
});

layui.use('element', function(){
      var element = layui.element;
      // 模块点击监听
	  element.on('tab(demo)', function(data){
	    console.log(data);
	    layer.msg('选择了下一设备。');
	  });

});
</script> 