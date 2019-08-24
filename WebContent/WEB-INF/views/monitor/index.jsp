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
<title>FRD - Demo</title>

<%@ include file="/WEB-INF/views/com_head.jsp"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<link href="${contextPath }/styles/css/jquery-ui.min.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery.contextMenu.css" rel="stylesheet">
<link href="${contextPath }/styles/colorpicker/css/bootstrap-colorpicker.css" rel="stylesheet">
<link href="${contextPath }/styles/colorpicker/css/bootstrap-colorpicker.css.map" rel="stylesheet">
<link href="${contextPath }/styles/css/colorpicker/evol-colorpicker.css" rel="stylesheet">
<link href="${contextPath }/styles/css/editable-widget.css" rel="stylesheet">
<link href="${contextPath }/styles/css/monitor.css" rel="stylesheet">
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<%-- <link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" /> --%>

<script type="text/javascript">
	var contextPath = "${contextPath }";
	$(document).ready(function(){
	});
</script>
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
<![endif]-->

<!-- Fav and touch icons -->
<link rel="shortcut icon" href="img/favicon.png">

<!--[if lt IE 9]>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
<![endif]-->

</head>

<body  class="" style="background: #fff">


<input type="hidden" id="loginUser" value="<shiro:principal />" />
<input type="hidden" id="monitorPainterId" value="${monitorPainter.id }" />
<input type="hidden" id="isSaved" value="" />
<%-- <input type="hidden" id="biaoqian" value="${contextPath}"/> --%>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content edit">
     <ol class="breadcrumb" style="background:#d8f0ff; height:41px; margin:0;line-height:40px;    border-radius: 0;">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>监控设计</li>
      </ol>
      <ul class="nav navbar-nav" id="menu-layoutit">
        <li>
          <div class="btn-group" data-toggle="buttons-radio">
            <button type="button" id="edit" class="btn btn-primary active"><i class="fa fa-edit"></i>编辑</button>
            <button type="button" class="btn btn-primary" id="devpreview"><i class="fa fa-eye"></i>预览</button>
            <a class="btn btn-primary" refresh="true" role="button" target="dialog" href="${contextPath}/procedureMonitor/toSaveMonitor?id=${monitorPainter.id }"><i class="fa fa-save"></i>保存</a>
            <button class="btn btn-primary" href="#clear" id="clear"><i class="fa fa-trash-o"></i>清空</button>
            <button class="btn btn-primary"  id="clear_menu"><i class="fa fa-arrow-left"></i>移出左侧菜单</button>
            <button class="btn btn-primary"  id="clear_menu_out"><i class="fa fa-arrow-right"></i>移回左侧菜单</button>
          </div>
        </li>
      </ul>
      
      <div class="sidebar-nav">
        <ul class="nav nav-list" id="sideBar" aria-multiselectable="true">
          <li class="" >
          
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
             
            </div>
            <h5 class="nav-header" title="拖拽数据图表"><i class="fa fa-area-chart"></i>&nbsp;&nbsp;数据图表</h5>
            <div class="sub clearfix">
              <div class="lyrow ui-draggable" widget-type="Chart">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/bar.jpg">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/bar.jpg">
                  <div class="chart-container" data-type="bar" style="height:100%"></div>
                </div>
              </div>
              <div class="lyrow ui-draggable" widget-type="Chart">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/line.jpg">
                </div>
                <div class="view" style="opacity : 30">
                  <img class="image" src="${contextPath }/styles/img/line.jpg">
                  <div class="chart-container" data-type="line" style="height:100%"></div>
                </div>
              </div>
              <div class="lyrow ui-draggable" widget-type="Chart">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/pie.jpg">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/pie.jpg">
                  <div class="chart-container" data-type="pie" style="height:100%"></div>
                </div>
              </div>
              <div class="lyrow ui-draggable" widget-type="Chart" >
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/pieempty.png">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/pieempty.png">
                  <div class="chart-container" data-pietype="pie" data-type="pie" style="height:100%"></div>
                </div>
              </div>
              <div class="lyrow ui-draggable" widget-type="Chart" title="能耗仪表盘">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/gauge.jpg">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/gauge.jpg">
                  <div class="chart-container" data-type="gauge" style="height:100%"></div>
                </div>
              </div>
              <div class="lyrow ui-draggable" widget-type="Chart" title="设备状况图表">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/profilebk.png">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/profilebk.png">
                  <div class="chart-container" driver-runtime="dri" data-type="bar" style="height:100%"></div>
                </div>
              </div>
              <!-- 
              <div class="lyrow ui-draggable" widget-type="TextBox" title="设备属性仪表盘">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                  <img class="image" src="${contextPath }/styles/img/gauge.jpg">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/gauge.jpg">
                  <div class="chart-container" data-type="gauge" style="height:100%"></div>
                </div>
              </div>-->
            </div>
          </li>
            <!-- 2019-05-14 slq 添加spc控件 -->
            <li class="" >
                <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>

                </div>
                <h5 class="nav-header" title="拖拽表格"><i class="fa fa-bar-chart"></i>&nbsp;&nbsp;SPC监控</h5>
                <div class="sub clearfix">
                    <div class="lyrow ui-draggable" widget-type="Spc">
                        <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                        <div class="preview">
                            <img class="image" src="${contextPath }/styles/img/spc/spc.jpg" title="spc散点图">
                        </div>
                        <div class="view">
                            <img class="image" src="${contextPath }/styles/img/spc/spc.jpg" title="spc散点图">
                            <div class="chart-container" data-type="line" style="height:100%"></div>
                        </div>
                    </div>

                    <div class="lyrow ui-draggable" widget-type="SpcAnalysis">
                        <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                        <div class="preview">
                            <img class="image" src="${contextPath }/styles/img/line.jpg" title="spc分析(CP,CPK,PP,PPK)">
                        </div>
                        <div class="view">
                            <img class="image" src="${contextPath }/styles/img/line.jpg" title="spc散点图">
                            <div class="chart-container" data-type="line" style="height:100%"></div>
                        </div>
                    </div>
                </div>
            </li>
          
          <li class="" >
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
             
            </div>
            <h5 class="nav-header" title="拖拽表格"><i class="fa fa-table"></i>&nbsp;&nbsp;产品表格</h5>
            <div class="sub clearfix">
              <div class="monitorTable ui-draggable" widget-type="MonitorTable">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                	<img class="image" src="${contextPath }/styles/img/monitorTable.png" style="width:32px">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/monitorTable.png">
                  <div class="monitorTable-container"  style="height:100%"><table class="table" id=""><thead></thead><tbody></tbody></table></div>
                </div>
              </div>
            </div>
          </li>
          
          <li class="" >
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
             
            </div>
            <h5 class="nav-header" title="拖拽表格"><i class="fa fa-table"></i>&nbsp;&nbsp;设备运行状态</h5>
            <div class="sub clearfix">
              <div class="monitorTable ui-draggable" widget-type="MonitorTable">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                    <img class="image" src="${contextPath }/styles/img/driverRuntime.jpg" style="width:32px">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/driverRuntime.jpg">
                  <div class="monitorTable-container"  style="height:100%"><table class="table" id=""><thead></thead><tbody></tbody></table></div>
                </div>
              </div>
            </div>
          </li>
          
          <li class="" >
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
<!--               <div class="popover fade right"> -->
<!--                 <div class="arrow"></div> -->
<!--                 <h3 class="popover-title">功能</h3> -->
<!--                 <div class="popover-content">拖拽设备</div> -->
<!--               </div> -->
            </div>
            <h5 class="nav-header" title="拖拽设备"><i class="fa fa-gear"></i>&nbsp;&nbsp;设备列表</h5>
            <div class="sub clearfix">
            	<c:forEach items="${imageList}" var="image" varStatus="status">
            		 <div class="box ui-draggable ldriver" widget-type="Device" device-type="${status.count }">
		                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
		                  <div class="preview">
		                    <div class="" id="">
		                      <img class="image_driver" width="30px" height="30px" src="${contextPath }/company/showPic/${image.id }" alt="">
		                    </div>
		                  </div>
		                  <div class="view">
		                    <div class=" jtk-node" id="">
		                      <img class="device-image image_driver" src="${contextPath }/company/showPic/${image.id }" alt="">
		                    </div>
		                  </div>
		             </div>
            	</c:forEach>
            
            </div>
            </li>
            <li>
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
<!--               <div class="popover fade right"> -->
<!--                 <div class="arrow"></div> -->
<!--                 <h3 class="popover-title">功能</h3> -->
<!--                 <div class="popover-content">拖拽设备</div> -->
<!--               </div> -->
            </div>
            <h5 class="nav-header" title="设备告警"><i class="fa fa-warning"></i>&nbsp;&nbsp;设备告警</h5>
            <div class="sub clearfix">
              <div class="AlarmTable ui-draggable" widget-type="AlarmTable">
                <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                <div class="preview">
                	<img class="image" src="${contextPath }/styles/img/monitorTable.png" style="width:32px">
                </div>
                <div class="view">
                  <img class="image" src="${contextPath }/styles/img/monitorTable.png">
                  <div class="AlarmTable-container"  style="height:100%"><table class="table" id=""><thead></thead><tbody></tbody></table></div>
                </div>
              </div>
            </div>
            </li>

            <li>
                <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
                    <!--               <div class="popover fade right"> -->
                    <!--                 <div class="arrow"></div> -->
                    <!--                 <h3 class="popover-title">功能</h3> -->
                    <!--                 <div class="popover-content">拖拽设备</div> -->
                    <!--               </div> -->
                </div>
                <h5 class="nav-header" title="量具告警"><i class="fa fa-warning"></i>&nbsp;&nbsp;量具告警</h5>
                <div class="sub clearfix">
                    <div class="AlarmTableMT ui-draggable" widget-type="AlarmTableMT">
                        <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i>删除</a>
                        <div class="preview">
                            <img class="image" src="${contextPath }/styles/img/monitorTable.png" style="width:32px">
                        </div>
                        <div class="view">
                            <img class="image" src="${contextPath }/styles/img/monitorTable.png">
                            <div class="AlarmTableMT-container"  style="height:100%"><table class="table" id=""><thead></thead><tbody></tbody></table></div>
                        </div>
                    </div>
                </div>
            </li>
          
          <li class="" >
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
<!--               <div class="popover fade right"> -->
<!--                 <div class="arrow"></div> -->
<!--                 <h3 class="popover-title">功能</h3> -->
<!--                 <div class="popover-content">可选组件</div> -->
<!--               </div> -->
            </div>
            
            <!--updatedBy:xsq  -->
            
            <h5 class="nav-header" title="可选组件"><i class="fa fa-check-square-o"></i>&nbsp;&nbsp;可选组件</h5>
            <div class="sub">
            	<!-- updatedBy:xsq 7.5 -->

                <!-- 文本框监控控件 -->
                <div class="component ui-draggable" widget-type="TextBox" style="border:0px solid black">
                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                  <div class="preview" >
                    <div class="textbox" id="">
                      <img title="文本" width="24px" height="24px" src="${contextPath }/styles/img/component-txt.png" />
                    </div>
                  </div>
                  <div class="view">
                    <div class="textbox" id="">
                      <p class="widget-word"><span>Sample Text</span></p>
                    </div>
                  </div>
                </div>

                <!-- 图片监控控件 -->
                 <div class="component ui-draggable" widget-type="ImageUpload">
                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                  <div class="preview">
                    <img title="图片" width="24px" height="24px" src="${contextPath }/styles/img/component-img.png" />
                  </div>
                  <div class="view">
                    <img class="device-image" title="图片" src="${contextPath }/styles/img/component-img-big.png" alt="">
                  </div>
                </div>

                <!-- 时间监控控件 -->
                 <div class="component ui-draggable" widget-type="Timer">
                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                  <div class="preview">
                    <img alt="时钟" src="${contextPath }/styles/img/component-time.png" />
                  </div>
                  <div class="view">
                    	<div class="textbox" id="">
                      <p class="widget-word"></p>
                    </div>
                  </div>
                </div>

                <!-- 矢量监控控件 -->
                <div class="clearfix" style="margin-top:40px">
                <c:set var="extendsImgs">
                	Arrow,Rectangle,Triangle,Arc,Pentagon,5Stars
                </c:set>
                <c:forEach items="${extendsImgs }" var="extendsImg" begin="1" end="16">
	                <div class="component ui-draggable" widget-type="Canvas">
	                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
	                  <div class="preview">
	                    <img title="${extendsImg }" alt="矢量组件" src="${contextPath }/styles/img/extends-img-${extendsImg }.png" />
	                  </div>
	                  <div class="view">
	                    <img class="canvas" data-type="${extendsImg }" src="${contextPath }/styles/img/extends-img-big-${extendsImg }.png" alt="">
	                    <canvas></canvas>
	                  </div>
	                </div>
				</c:forEach>

                <c:set var="extendsImgs1">
                    ArcReal
                </c:set>
                <c:forEach items="${extendsImgs1 }" var="extendsImg1" begin="0" end="2">
                    <div class="component ui-draggable" widget-type="CanvasGateWay">
                      <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                      <div class="preview">
                        <img title="${extendsImg1 }" alt="矢量组件" src="${contextPath }/styles/img/extends-img-${extendsImg1 }.png" />
                      </div>
                      <div class="view">
                        <img class="canvas" data-type="${extendsImg1 }" src="${contextPath }/styles/img/extends-img-big-${extendsImg1 }.png" alt="">
                        <canvas></canvas>
                      </div>
                    </div>
                </c:forEach>

                <c:set var="extendsImgs1">
                    ArcVirtual
                </c:set>
                <c:forEach items="${extendsImgs1 }" var="extendsImg1" begin="0" end="2">
                    <div class="component ui-draggable" widget-type="CanvasProceseAlter">
                      <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                      <div class="preview">
                        <!-- <img title="${extendsImg1 }工序监控控件" alt="矢量组件" src="${contextPath }/styles/img/extends-img-${extendsImg1 }.png" /> -->
                        <img title="${extendsImg1 }工序监控控件" alt="矢量组件" src="${contextPath }/styles/img/alermlight.gif" />
                      </div>
                      <div class="view">
                        <img class="canvas" data-type="${extendsImg1 }" src="${contextPath }/styles/img/greenalerm.gif" alt="">
                        <canvas></canvas>
                      </div>
                    </div>
                </c:forEach>

                <c:set var="extendsImgs1">
                    Qualified
                </c:set>
                <c:forEach items="${extendsImgs1 }" var="extendsImg1" begin="0" end="2">
                    <div class="component ui-draggable" widget-type="CanvasQualified">
                        <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                        <div class="preview">
                            <img title="${extendsImg1 }合格标识" alt="矢量组件" src="${contextPath }/styles/img/extends-img-${extendsImg1 }.png" />
                        </div>
                        <div class="view">
                            <img class="canvas" data-type="${extendsImg1 }" src="${contextPath }/styles/img/extends-img-big-Rectangle.png" alt="">
                            <canvas></canvas>
                        </div>
                    </div>
                </c:forEach>
				</div>

				<div class="clearfix" style="margin-top:10px">
                <c:forEach begin="2" end="12" varStatus="st">
                <div class="component ui-draggable" widget-type="Image">
                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                  <div class="preview">
                    <img alt="水管" style="height:30px;" src="${contextPath }/styles/img/waterPipe(${st.index }).gif" />
                  </div>
                  <div class="view">
                    <img class="device-image" src="${contextPath }/styles/img/waterPipe(${st.index }).gif" alt="">
                  </div>
                </div>
                </c:forEach>
                <c:forEach begin="1" end="13" varStatus="st">
                <div class="component ui-draggable" widget-type="Image">
                  <a href="#close" class="remove label label-danger"><i class="fa fa-remove"></i></a>
                  <div class="preview">
                    <img alt="水管" style="height:30px;" src="${contextPath }/styles/img/connector_${st.index }.png" />
                  </div>
                  <div class="view">
                    <img class="device-image" src="${contextPath }/styles/img/connector_${st.index }.png" alt="">
                  </div>
                </div>
                </c:forEach>
                </div>
            </div>
           
          </li>
          <li class="open" >
            <div class="pull-right popover-info"><i class="fa fa-angle-down"></i>
<!--               <div class="popover fade right"> -->
<!--                 <div class="arrow"></div> -->
<!--                 <h3 class="popover-title">功能</h3> -->
<!--                 <div class="popover-content">可选图形</div> -->
<!--               </div> -->
            </div>
            <h5 class="nav-header" title="可选图形"><i class="fa fa-arrows"></i>&nbsp;&nbsp;可选图形</h5>
            <div class="sub">
                <div class="option-grid">
                  <ul class="clearfix" id="graphics">
                    <li class="option-cell" grap-type="arrow,Flowchart">
                      <div>
                        <img src="${contextPath }/styles/img/line1.jpg" alt="">
                        </div>
                    </li>
                    <li class="option-cell" grap-type="arrow,Straight">
                      <div>
                       <img src="${contextPath }/styles/img/line2.jpg" alt="">
                      </div>
                    </li>
                    <li class="option-cell" grap-type="line,Flowchart">
                      <div>
                         <img src="${contextPath }/styles/img/line3.jpg" alt="">
                        </div>
                    </li>
                    <li class="option-cell"  grap-type="arrow,Bezier">
                      <div>
                         <img src="${contextPath }/styles/img/line4.jpg" alt="">
                        </div>
                    </li>
                    <li class="option-cell"  grap-type="line,Bezier">
                      <div>
                         <img src="${contextPath }/styles/img/line5.jpg" alt="">
                        </div>
                    </li>
                  </ul>
                </div>
                
                <!-- updatedBy:xsq 6.29-->
                
                <div class="option-grid">
                  <div id="colorPicker" class="input-group colorpicker-component">
                  	<lable for="lineColor">线条颜色：</lable>
                    <input type="text" value="#00AABB" id="lineColor" />
                    <!--  <span class="input-group-addon"></span> -->
                  </div>
                </div>
                
                  <!-- updatedBy:xsq 6.29-->
                  
                <div class="option-grid"> 
                 <div id="lineWidth" class="input-group">
                  	 <sapn font-size:12px;color:#154a75>连线宽度：</sapn>
                  	 <input id="line" type="text" value="3"/>
                  </div>
                </div>
               <!--  <div class="option-grid">
                  <div id="colorPicker" class="input-group colorpicker-component">
                  </div>
                </div> -->
            </div>
          </li>
        </ul>
      </div>
      
      
      
      
      <!--/.sidebar-nav-->
      <div style="position: absolute;overflow: auto;">
      	 <div class="demo ui-sortable" id="canvas" style="display:block;" widget-type="Container">
	     </div>
      </div>
	     
      
     
      <!--/.demo-->
      <div style="overflow-y:scroll;width:252px" class="tool-bar">
        <div class="collapse-toggle">
          <i class="fa fa-angle-double-right" aria-hidden="true"></i>
        </div>
        <div class="widget" style="margin-top: 15px">
        </div>
      </div>
    </div><!-- /.main-content -->
  </div>
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" data-style role="dialog">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
			<div class="pageContent"></div>
        </div>
      </div>
    </div>
  </div>
</script>
<c:set var="ParentTitle" value="monitorManage" />
<c:set var="ModuleTitle" value="monitorDesginer" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
<script type="text/javascript" src="${contextPath }/js/chart-data.js"></script>
<script type="text/javascript" src="${contextPath }/js/jquery.contextMenu.js"></script>
<script type="text/javascript" src="${contextPath }/js/jsPlumb-2.1.5.js"></script>
<script type="text/javascript" src="${contextPath }/styles/colorpicker/js/bootstrap-colorpicker.js"></script>
<script type="text/javascript" src="${contextPath }/styles/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<%-- <script type="text/javascript" src="${contextPath }/js/evol-colorpicker.js"></script> --%>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script type="text/javascript" src="${contextPath }/js/editable-widget.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.index.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.render.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor.connector.js"></script>
<script type="text/javascript" src="${contextPath }/js/monitor/driverGauge.js"></script>

<script src="${contextPath}/js/countChart/echartDriver.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
	  /* $(".ldriver").hover(function(){
		  $(this).css("background-color","gray");
	  }); */
	  var r = window.location.search;
	  var companyId = '${companyId}';
	  console.log(companyId);
	  $.each($("#companyIdUrl").find('a'),function(idx,item){
		var url =  $(item).attr("href");
		  $(item).click(function(){
			  if($("#isSaved").val() != 'true' && $.trim($(".demo").html()) != '' && url.indexOf(companyId) == -1){
			  	$(item).attr("href","javascript:void(0)");
			  	swal({
					title: "您正在切换公司,当前页面可能尚未保存,此页面上的操作会丢失！！！！！",
					type: "warning",
					showCancelButton: true,
					confirmButtonColor: '#DD6B55',
					confirmButtonText: '确认',
					cancelButtonText: "取消",
					closeOnConfirm: false
				},
				function(isConfirm){
					if(isConfirm){
						localStorage.removeItem("elementsInfo");
						localStorage.removeItem("connections");
						localStorage.removeItem("winId");
						localStorage.removeItem("chartsData");
						localStorage.removeItem("monitorTableData");
						localStorage.removeItem("components");
						localStorage.removeItem("bindingData");
						localStorage.removeItem("background");
                        localStorage.removeItem("spcData");
                        localStorage.removeItem("spcAnalysisData");
						window.location.href=url;
					}else{
						$(item).attr("href",url);
					}
				});
			  }else{
				  window.location.href=url;
			  }
		  });
	  });
	  $('[href$="toMonitor"]').click(function(){
		  var $that = $(this);
		  var url =  $(this).attr("href");
		  if($("#isSaved").val() != 'true' && $.trim($(".demo").html()) != ''){
			  $that.attr("href","javascript:void(0)");
			  swal({
					title: "您正在进入监控页面,当前页面可能尚未保存,此页面上的操作可能会丢失！！！！！",
					type: "warning",
					showCancelButton: true,
					confirmButtonColor: '#DD6B55',
					confirmButtonText: '确认',
					cancelButtonText: "取消",
					closeOnConfirm: false
				},
				function(isConfirm){
					if(isConfirm){
						window.location.href=url;
					}else{
						$that.attr("href",url);
					}
				});
		  }else{
			  window.location.href=url;
		  }
	  });
	  $(window).bind('beforeunload',function(){  
		
	    }); 
	 // $(window).unbind('beforeunload'); 
	  
	 $("#clear_menu").click(function () { 
		 $(this).hide();
		$("#clear_menu_out").show();
		$("#sidebar").width(0).css("z-index","-555");		
		$(".main-content").css("left","0");
		 console.log((parseFloat($(".demo").css("width"))+250)+"px");
		$(".demo").parent().css("width",(parseFloat($(".demo").parent().css("width"))+220)+"px");
		if ( $("div").hasClass("collapsed-left") ){
		  $(".sidebar-nav").animate({left: '-220px'}, "500");		
		}
		else{
		  $(".sidebar-nav").animate({left: '0'}, "500").addClass("side-left");	
		}
		//$(".sidebar-nav").css("left","0").addClass("side-left");
	});
	 $("#clear_menu_out").click(function () { 
		 $(this).hide();
		$("#clear_menu").show();
		$(".main-content").animate({left: '+220px'}, "500");
		if ( $("div").hasClass("collapsed-left") ){
			$(".sidebar-nav").animate({left: '0'}, "500");		
			}
		else{
		  $(".sidebar-nav").animate({left: '+220px'}, "500").removeClass("side-left");	
		}
		$(".demo").parent().css("width",(parseFloat($(".demo").parent().css("width"))-220)+"px");		
		//alert(1);
		$("#sidebar").width(220).css("z-index","99999");
		
				
		
		
	});
	 /* var selected_begin_index,selected_end_index;
	 $("#canvas").on("click",".ui-draggable",function(e){
		 var _selectable= $(this).parent();
		 if(!e.ctrlKey && !e.shiftKey){  //没有按下Ctrl或Shift键
             if(!$(this).hasClass("ui-selected")){
                 _selectable.children(".ui-draggable").removeClass("ui-selected");
             }
             $(this).addClass("ui-selected");
             //selected_begin_index=_selectable.children("li").index(this);
         }else if(e.ctrlKey && !e.shiftKey){ //只按下Ctrl键
        	 if($(this).hasClass("ui-selected"))
        		 $(this).removeClass("ui-selected");
             else
                 $(this).addClass("ui-selected");
             //selected_begin_index=_selectable.children("li").index(this);
         }
	 });
	 $(document).bind('click', function(e) {  
         var e = e || window.event; //浏览器兼容性   
         var elem = e.target || e.srcElement;  
         while (elem) { //循环判断至跟节点，防止点击的是div子元素   
             if (elem.className && elem.className.indexOf('ui-selected') != -1 && elem.className.indexOf('ui-draggable') != -1) {  
                 return;  
             }  
             elem = elem.parentNode;  
         }  
         $("#canvas").find(".ui-draggable").removeClass('ui-selected'); //点击的不是div或其子元素   
     });  */ 
	 
  }); 
 </script>
</body>
<style>
#canvas_fileupload{
  float:left;
  margin-right:5px;
}
button[type="submit"]{
 margin-top:2px
}
img:hover {
    opacity:0.4;
}
.ui-selected{
	border: 1px solid #4FA1E6;
}
</style>
</html>