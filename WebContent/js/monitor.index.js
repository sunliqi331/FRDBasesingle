var winId  = localStorage.getItem("winId") == null ? 0 : localStorage.getItem("winId");
var resizeReg = /<div[\s\w\"\=\-\:\;]*ui-resizable-handle[\s\w\"\=\-\:\;]*\>\<\/div\>/g;
//var contextPath = "/FRDBase";
// 时间格式化
Date.prototype.format = function(format) {
	
	/*
	* 使用例子:format="yyyy-MM-dd hh:mm:ss";
	*/
	var o = {
	"M+" : this.getMonth() + 1, // month
	"d+" : this.getDate(), // day
	"H+" : this.getHours(), // hour
	"m+" : this.getMinutes(), // minute
	"s+" : this.getSeconds(), // second
	"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
	"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
	format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
	- RegExp.$1.length));
	}

	for (var k in o) {
	if (new RegExp("(" + k + ")").test(format)) {
	format = format.replace(RegExp.$1, RegExp.$1.length == 1
	? o[k]
	: ("00" + o[k]).substr(("" + o[k]).length));
	}
	}
	
	return format;
	}
var tmp;
var PaintStyle = function() {
	
	this.strokeStyle = '#000';
	this.lineWidth = 4;
	
}
var gSelectedGraphic = {
	graphicType: 'arrow',
	connectorType: 'Flowchart',
	paintStyle: new PaintStyle()
};
$(window).resize(function() {
	
	//$(".demo").css("min-height", $(window).height() -95);
});

/**
 * @description
 * register click event of remove button,
 * remove corresponding elements and its localstorage data
 */
function removeElm() {
	
	$(".demo").on("click", ".remove", function(e) {
		
		e.preventDefault();
		var $thisElement = $(this).parent();

		var _this_id = $thisElement.attr("id");
		if (_this_id) {
			storageManager.deleteLocal('elementsInfo', _this_id, 'elementId');
			storageManager.deleteLocal('components', _this_id,'chartId');
			storageManager.deleteLocal('chartsData', _this_id,'chartId');
			storageManager.deleteLocal('monitorTableData', _this_id,'chartId');
			storageManager.deleteLocal('AlarmTableData', _this_id,'chartId');
            // storageManager.deleteLocal('AlarmTableMTData', _this_id,'chartId');


        }

		var endpoints = instance.getEndpoints($thisElement.find(".jtk-node"));
		if (endpoints) {
			for(var i = 0;i < endpoints.length; i++){
				instance.deleteEndpoint(endpoints[i]);
			}
		}

		var bindId = $thisElement.editableWidget('getBindId');
		if (bindId) storageManager.deleteLocal('bindingData', bindId);

		$thisElement.editableWidget('destroy');
		$thisElement.remove();

		if (!$(".demo .lyrow, .demo .box, .demo .component,.demo .monitorTable,.demo .AlarmTable,.demo .AlarmTableMT").length > 0) {
			clearDemo()
		}
		
	})
	
}
/**
 * @description clear all data
 */
function clearDemo() {
	
	$(".demo").empty();
	$(".demo").css({background:'none'});
	storageManager.removeAll();
	
}
function removeMenuClasses() {
	
	$("#menu-layoutit li button").removeClass("active")
	
}

var repaintEverything = function(){
	
	instance.repaintEverything();
	
}
function initContainer(){
	
	var colors = {
    	'red': '#FF0000',
        'black': '#000000',
        'white': '#ffffff',
        'default': '#777777',
        'primary': '#337ab7',
        'success': '#5cb85c',
        'info': '#5bc0de',
        'warning': '#f0ad4e',
        'danger': '#d9534f', "aliceblue": "#f0f8ff",
         "antiquewhite": "#faebd7",
         "aqua": "#00ffff",
         "aquamarine": "#7fffd4",
         "azure": "#f0ffff",
         "beige": "#f5f5dc",
         "bisque": "#ffe4c4",
         "black": "#000000",
         "blanchedalmond": "#ffebcd",
         "blue": "#0000ff",
         "blueviolet": "#8a2be2",
         "brown": "#a52a2a",
         "burlywood": "#deb887",
         "cadetblue": "#5f9ea0",
         "chartreuse": "#7fff00",
         "chocolate": "#d2691e",
         "coral": "#ff7f50",
         "cornflowerblue": "#6495ed",
         "cornsilk": "#fff8dc",
         "crimson": "#dc143c",
         "cyan": "#00ffff",
         "darkblue": "#00008b",
         "darkcyan": "#008b8b",
         "darkgoldenrod": "#b8860b",
         "darkgray": "#a9a9a9",
         "darkgreen": "#006400",
         "darkkhaki": "#bdb76b",
         "darkmagenta": "#8b008b",
         "darkolivegreen": "#556b2f",
         "darkorange": "#ff8c00",
         "darkorchid": "#9932cc",
         "darkred": "#8b0000",
         "darksalmon": "#e9967a",
         "darkseagreen": "#8fbc8f",
         "darkslateblue": "#483d8b",
         "darkslategray": "#2f4f4f",
         "darkturquoise": "#00ced1",
         "darkviolet": "#9400d3",
         "deeppink": "#ff1493",
         "deepskyblue": "#00bfff",
};
	
	$(".demo").parent().css({"left":200,"width":$(window).width()-420-250,"height":$(window).height() - 91});
	$(".demo").css("min-height", $(window).height() - 105);
	$(document).bind('click', function(e) {
		
        var e = e || window.event; //浏览器兼容性
        var elem = e.target || e.srcElement;
        while (elem) { //循环判断至跟节点，防止点击的是div子元素
            if (elem.className && typeof elem.className == 'string' && elem.className.indexOf('ui-selected') != -1 && elem.className.indexOf('ui-draggable') != -1) {
                return;
            }
            elem = elem.parentNode;
        }
        $("#canvas").find(".ui-draggable").removeClass('ui-selected'); //点击的不是div或其子元素
        $("#canvas").find(".ui-draggable").blur();
        
	});
	$(".demo").droppable({
		accept: ".sidebar-nav .lyrow, .sidebar-nav .box, .sidebar-nav .component, .sidebar-nav .monitorTable ,.sidebar-nav .AlarmTable, .sidebar-nav .AlarmTableMT",
		activeClass: "ui-state-hover",
		hoverClass: "ui-state-active",
		drop: function( event, ui ) {
			
			winId ++;
			var $this = $(this);
			var temp = ui.helper.clone().css({
				left: function(index, value) {
					
					return parseFloat(value) - 200;
				},
				top: function(index, value) {
					
					return parseFloat(value) + 32;
				},
				hidefocus:"true",
				outline:0
				//opacity:0
			}).appendTo($this);
			temp.attr("tabindex",0);
			if (ui.helper.hasClass('lyrow')) {
				temp.width(200);
				temp.attr('id', 'charts' + winId);
				temp.draggableElement(null, function(){
					
					var $chart = $('.chart-container', temp);
					//
					if ($chart.length) {
						//	
						$chart.prev().width($chart.parent().parent().width());
						$chart.prev().height($chart.parent().parent().height());
						//	echarts.getInstanceByDom($chart[0]).resize();
						var instance_ = echarts.getInstanceByDom($chart[0]);
						if(instance_){
							instance_.resize();
						}
					}
					
				}, storageManager.saveComponentsData).editableWidget();

				storageManager.saveComponentsData(temp);
			} else if (ui.helper.hasClass('box')) {

				temp.attr('id', 'flowchartWindowOuter' + winId);
				temp.find('.jtk-node').attr('id', 'flowchartWindow' + winId);

				temp.draggableElement(repaintEverything, repaintEverything, function(){
					
					repaintEverything();
					storageManager.bindingJspplumb(temp);
					
				})
				.editableWidget('bindData')
				.on('bindTextCreate', storageManager.initBindText);

				instance.batch(function () {
					
					_addEndpoints("Window" + winId, ["LeftMiddle", "RightMiddle"], ["TopCenter", "BottomCenter"]);
				});
				storageManager.bindingJspplumb(temp);
			} else if (ui.helper.hasClass('monitorTable')) {
				temp.width(260);
				temp.attr('id', 'monitorTable' + winId);
				temp.draggableElement(null, function(){
					
					var $table = $('.monitorTable-container', temp);
					if ($table.length) {
					//	
						$table.prev().width($table.parent().parent().width());
						$table.prev().height($table.parent().parent().height());
					//	echarts.getInstanceByDom($chart[0]).resize();
					}
					
				}, storageManager.saveComponentsData).editableWidget();
				storageManager.saveComponentsData(temp);
			}else if (ui.helper.hasClass('AlarmTableMT')) {
                temp.width(260);
                temp.attr('id', 'AlarmTableMT' + winId);
                temp.draggableElement(null, function(){

                    var $table = $('.AlarmTableMT-container', temp);
                    if ($table.length) {
                        //
                        $table.prev().width($table.parent().parent().width());
                        $table.prev().height($table.parent().parent().height());
                        //	echarts.getInstanceByDom($chart[0]).resize();
                    }

                }, storageManager.saveComponentsData).editableWidget();
                storageManager.saveComponentsData(temp);
            }
			else if(ui.helper.hasClass('AlarmTable')){
				temp.width(260);
				temp.attr('id', 'AlarmTable' + winId);
				temp.draggableElement(null, function(){
					
					var $table = $('.AlarmTable-container', temp);
					if ($table.length) {
					//	
						$table.prev().width($table.parent().parent().width());
						$table.prev().height($table.parent().parent().height());
					//	echarts.getInstanceByDom($chart[0]).resize();
					}
					
				}, storageManager.saveComponentsData).editableWidget();
				storageManager.saveComponentsData(temp);
			}else {
				temp.attr('id', 'component' + winId);
				var tmp = $('.canvas', temp);

				if(tmp.length != 0){
					tmp.hide();
                    var w_h;
                    if(tmp.attr('data-type') == "Arc"){
						w_h = [temp.width()-150,temp.height()-8];//temp.editableWidget('getSize');
                    }else{
						w_h = [temp.width(),temp.height()];//temp.editableWidget('getSize');
                    }
					var $canvas = tmp.next();
					$canvas.attr("id","canvas_"+winId);
					var ele = document.getElementById("canvas_"+winId);
					ele.width=w_h[0];
					ele.height=w_h[1];
					var ctx=ele.getContext("2d");
					ctx.clearRect(0, 0, ele.width, ele.height);
					$canvas.width = ele.width;
					storageManager["render"+tmp.attr('data-type')](ele);
					if(tmp.attr('data-type') == "Arc" ||
							tmp.attr('data-type') == "ArcReal" ||
							tmp.attr('data-type') == "ArcVirtual" ||
							tmp.attr('data-type') == "Qualified" ||
							"Rectangle"==tmp.attr('data-type')){
						temp.tooltip({
							items:"div",
			                content:function(){
			                	
			                	return "<p>网关:"+(ele.getAttribute('data-mac') || "") +"</p></br> <p>测点:"+ (ele.getAttribute('data-codekey') || "")+"</p>"
			                },
			                track: true,
			              });
					}

					if(tmp.attr('data-type') == "ArcVirtual"){
						temp.tooltip({
							items:"div",
			                content:function(){
			                	return "<p><b>绑定产品:</b>"+(ele.getAttribute('data-product-name') || "") +"</p></br>" +
			                			" <p><b>绑定工序:</b>"+ (ele.getAttribute('data-productProcedure-name') || "")+"</p></br>" +
			                			" <p><b>绑定工序属性:</b>"+ (ele.getAttribute('data-procedureProperty-name') || "")+"</p>"
			                },
			                track: true,
			              });
					}

                    if(tmp.attr('data-type') == "Qualified"){
                        temp.tooltip({
                            items:"div",
                            content:function(){
                                return "<p><b>绑定产品:</b>"+(ele.getAttribute('data-product-name') || "") +"</p>"
                            },
                            track: true,
                        });
                    }

					//storageManager.renderTriangle(ele);

				}
				if(ui.helper.attr("widget-type") == 'Timer'){
					temp.find(".widget-word").text(storageManager.nowDate());
					ui.helper.attr("data-formatter","yyyy-MM-dd HH:mm:ss");
					var timer = setInterval(function(){
							temp.find(".widget-word").text(storageManager.nowDate());
					},1000);
					temp.data("timer",timer);
				}
				if(ui.helper.attr("widget-type") == 'TextBox'){
					var text = temp.find(".widget-word").parent();
					temp.css("height","23px");
					temp.tooltip({
						items:"div",
		                content:function(){
		                	
		                	return "<p>网关:"+(text.attr('data-mac') || "") +"</p></br> <p>测点:"+ (text.attr('data-codekey') || "")+"</p>"
		                },
		                track: true,
		              });
				}
				temp.draggableElement(null, function(){
					
					var $table = $('.device-image', temp);
					if ($table.length) {
					//	
						$table.width($table.parent().parent().width());
						$table.height($table.parent().parent().height());
					//	echarts.getInstanceByDom($chart[0]).resize();
					}
					
				}, storageManager.saveComponentsData).editableWidget();
				storageManager.saveComponentsData(temp);
			}
			localStorage.setItem("winId",winId);
			
		}
	});

	$(".sidebar-nav .lyrow, .sidebar-nav .box, .sidebar-nav .component, .sidebar-nav .monitorTable , .sidebar-nav .AlarmTable, .sidebar-nav .AlarmTableMT").draggable({
		helper: "clone"
	});
	$(".demo").on("click", ".configuration > a", function(e) {
		
		e.preventDefault();
		var t = $(this).parent().next().next().children();
		$(this).toggleClass("active");
		t.toggleClass($(this).attr("rel"))
		
	});
	$(".demo").on("click", ".configuration .dropdown-menu a", function(e) {
		
		e.preventDefault();
		var t = $(this).parent().parent();
		var n = t.parent().parent().next().next().children();
		t.find("li").removeClass("active");
		$(this).parent().addClass("active");
		var r = "";
		t.find("a").each(function() {
			r += $(this).attr("rel") + " "
		});
		t.parent().removeClass("open");
		n.removeClass(r);
		n.addClass($(this).attr("rel"))
		
	});

	$("#edit").click(function() {
		
		$(".main-content").removeClass("collapsed-right collapsed-left");
		$(".main-content").addClass("edit");
		if ( $("div").hasClass("side-left") ){
		$(".sidebar-nav").animate({left: '0'}, "500");
		}
		else{
		$(".sidebar-nav").animate({left: '+220px'}, "500");
		}

		removeMenuClasses();
		$(this).addClass("active");
		
		return false;
	});
	$("#clear").click(function(e) {
	swal({
          title: "确定要清空吗？",
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: '#DD6B55',
          confirmButtonText: '确认',
          cancelButtonText: "取消",
          closeOnConfirm: false
        },
        function(isConfirm){
          if (isConfirm){
        	e.preventDefault();
      		clearDemo()
      		swal("","已清空","success");
          }
        });
	});
	$("#devpreview").click(function() {
		
		$(".main-content").removeClass("edit");
		//$(".main-content").addClass("collapsed-right collapsed-left");
		$(".main-content").addClass("collapsed-right collapsed-left");
		$(".sidebar-nav").animate({left: '-220px'}, "500");
		removeMenuClasses();
		$(this).addClass("active");
		
		return false;
	});
	$(".nav-header").click(function() {
		
		var $this = $(this);
		var $parent = $this.parent();
		var $last = $parent.siblings('.open');
		$('.sub', $last).slideUp(200,function(){
			$last.removeClass("open");
		});
		var sub = $this.next();
		if (sub.is(":visible")) {
		  sub.slideUp(200, function(){
			$parent.removeClass("open");
		  });
		} else {
		  sub.slideDown(200, function(){
			$parent.addClass("open");
		  });
		}
		
	});
	$('#graphics').on('click', 'li', function(){
		
		var grapType = $(this).attr('grap-type');
		grapType = grapType && grapType.split(',');
		if (grapType.length == 2) {
			gSelectedGraphic.graphicType = grapType[0];
			gSelectedGraphic.connectorType = grapType[1];
		}
		$(this).siblings().removeClass('active').end().addClass('active');
		
		
	});
	//updatedBy:xsq 7.6 连线颜色
	$('#colorPicker').find("input").colorpicker({colorSelectors: colors}).on('changeColor',function(event){
		
		var paintStyle = new PaintStyle();
		var lineWidth = $("#lineWidth").find("input").val();
		paintStyle.strokeStyle =  event.color;
		paintStyle.lineWidth = lineWidth;
		gSelectedGraphic.paintStyle = paintStyle;

		/*if(lineWidth != '' && lineWidth != null && lineWidth != 'undefined'){
			$("#lineWidth").find("input").spinner({
					min: 1,
			        max: 8,
			       stop: function () {
			    	   var line = $("#line");
			    	   paintStyle.lineWidth = line.val();
			    	   gSelectedGraphic.paintStyle = paintStyle;
			       }
			});
		}*/
		
	});

	//updatedBy:xsq 6.29 连线粗细
	$("#lineWidth").find("input").spinner({
        min: 1,
        max: 8,
       stop: function () {
    	   
    	   var line = $("#line");
    	   var paintStyle = new PaintStyle();
    	   var colorVal = $('#colorPicker').find("input").val();
    	   paintStyle.lineWidth = line.val();
    	   paintStyle.strokeStyle =  colorVal;
    	   gSelectedGraphic.paintStyle = paintStyle;
    	   /*if(colorVal != '' && colorVal != null && colorVal != 'undefined'){
    		   $('#colorPicker').find("input").colorpicker({colorSelectors: colors}).on('changeColor',function(event){
    				paintStyle.strokeStyle =  event.color;
    				gSelectedGraphic.paintStyle = paintStyle;
    			});
    	   }*/
    	   
        }
    });

	$('.collapse-toggle').each(function(i, el) {
		
		var $trigger = $(this);
		var $p = $trigger.parent('.tool-bar');
		$trigger.on('click', function(event){
			
			if($p.hasClass("ishide")){
				$(".demo").parent().css({width:$(".demo").parent().width()-250});
				$p.removeClass("ishide");
			}else{
				$p.addClass("ishide");
				$(".demo").parent().css({width:$(".demo").parent().width()+250});
			}
			$('.main-content').toggleClass('collapsed-right');
			
			return false;
		});
		
	});
	$('.demo').editableWidget('updateSize').on("change",function(){
		
		var that = this;
		storageManager.saveContainerData($(that));
		
	});
	storageManager.saveContainerData($('.demo'));
	$('#menu-layoutit').draggable({
		containment: ".main-content",
	});
	initContextMenu();
	
}
// 右键菜单功能
function initContextMenu() {
	
	$.contextMenu({
		selector: '.widget-element',
		callback: function(key, options) {
			
			
			if (key.match(/bringForward|sendBackward|bringFront|sendBack|copyitem/).length) {
				options.$trigger.layers('changeLayer', key);
			}
		},
		items: {
			bringForward: {
				name: '上移一层',
				icon: 'fa-level-up',
				disabled: function(key, opt) {
					
					return opt.$trigger.layers('isFront') ? true : false;
				}
			},
			sendBackward: {
				name: '下移一层',
				icon: 'fa-level-down',
				disabled: function(key, opt) {
					
					return opt.$trigger.layers('isBottom') ? true : false;
				}
			},
			bringFront: {
				name: '置于顶层',
				icon: 'fa-long-arrow-up',
				disabled: function(key, opt) {
					
					return opt.$trigger.layers('isFront') ? true : false;
				}
			},
			sendBack: {
				name: '置于底层',
				icon: 'fa-long-arrow-down',
				disabled: function(key, opt) {
					
					return opt.$trigger.layers('isBottom') ? true : false;
				}
			},
			copyitem: {
				name: '复制控件',
				icon: 'fa-copy',
				callback: function(key, opt) {
					// $(".demo").trigger('droppable');

					var copyBeforeId = opt.$trigger[0].id;
					if(null != copyBeforeId && '' != copyBeforeId) {
						var copyBeforeObj = $("#"+copyBeforeId+"");
						//var copyitem = copyBeforeObj.clone(true).attr('id', 'component' + (parseInt(opt.$trigger[0].id.replace("component", "")) + 1));
						/*
						winId++;
						var copyitem = copyBeforeObj.clone(true).attr('id', 'component' + winId);
						var zindex = parseInt(copyitem.css('z-index'));
						copyitem.css('z-index', zindex + 1);
						var left = parseInt(copyitem.css('left')) - 200;
						var top = parseInt(copyitem.css('top')) + 32;
						copyBeforeObj.after(copyitem).addClass('context-menu-active');
						
						if (copyitem.hasClass('context-menu-active')) {
							copyBeforeObj.removeClass('context-menu-active');
						}
						*/
						var copyitem;
						cloneItem(copyitem, copyBeforeObj);
						
						/*
						if (!copyBeforeObj.hasClass('context-menu-active')) {
							copyBeforeObj.addClass('context-menu-active');
						} */
					}

					return true;
				}
			},
			sep1: {
				type: "cm_separator",
				visible: function(key, opt) {
					
					var flag1 = /Device|Chart|MonitorTable|AlarmTable|AlarmTableMT|TextBox/.test(this.editableWidget('getElementType')) ? true : false;
					var flag2 = (this.editableWidget('getElementType') == 'Canvas'
						|| this.editableWidget('getElementType') == 'CanvasGateWay'
							|| this.editableWidget('getElementType') == 'CanvasProceseAlter'
                        	|| this.editableWidget('getElementType') == 'CanvasQualified'
							) && 
					(this.find(".canvas").attr("data-type") == "Arc"
					|| this.find(".canvas").attr("data-type") == "ArcReal"
						|| this.find(".canvas").attr("data-type") == "ArcVirtual"
                    	|| this.find(".canvas").attr("data-type") == "Qualified")? true : false;
					var flag3 = this.editableWidget('getElementType') == 'Canvas' && this.find(".canvas").attr("data-type") == "Rectangle"? true : false;

					return flag1 || flag2 || flag3;
				}
			},
			bindData: {
				name: '绑定数据',
				icon: 'fa-database',
				callback: function(key, opt) {
					
					var options = {};
					switch (this.editableWidget('getElementType')) {
						case 'Device':
							options.title = '绑定设备数据';
							options.type = 'Device';
						break;
						case 'Chart':
							options.title = '选择图形数据源';
							options.type = 'Chart';
						break;
						case 'MonitorTable':
							options.title = '选择表格数据源';
							options.type = 'MonitorTable';
						break;
						case'AlarmTable':
							options.title = '选择表格数据源';
							options.type = 'AlarmTable';
							break;
                        case'AlarmTableMT':
                            options.title = '选择表格数据源';
                            options.type = 'AlarmTableMT';
                            break;
						case 'TextBox':
							options.title = '选择测点数据源';
							options.type = 'TextBox';
						break;
						case 'Canvas':
							options.title = '选择测点数据源';
							options.type = 'Canvas';
						break;
						case 'CanvasGateWay':
							options.title = '选择网关';
							options.type = 'CanvasGateWay';
						break;
						case 'CanvasProceseAlter':
							options.title = '选择工序';
							options.type = 'CanvasProceseAlter';
						break;
                        case 'CanvasQualified':
                            options.title = '选择产品';
                            options.type = 'CanvasQualified';
                            break;
						case 'Spc':
							options.title = 'Spc-X图分析条件';
							options.type = 'Spc';
							break
						case 'SpcAnalysis':
							options.title = 'Spc过程能力分析(CP,CPK,PP,PPK)';
							options.type = 'SpcAnalysis';
							break
						default:
						break;
					}

					renderManager.renderDialog(opt.$trigger, options);
					
				},
				visible: function(key, opt) {
					
					var flag1 = /Device|Chart|Spc|SpcAnalysis|MonitorTable|AlarmTable|AlarmTableMT|TextBox/.test(this.editableWidget('getElementType')) ? true : false;
					var flag2 = (this.editableWidget('getElementType') == 'Canvas'
						|| this.editableWidget('getElementType') == 'CanvasGateWay'
							|| this.editableWidget('getElementType') == 'CanvasProceseAlter'
                    		|| this.editableWidget('getElementType') == 'CanvasQualified') && (
							this.find(".canvas").attr("data-type") == "Arc"
							|| this.find(".canvas").attr("data-type") == "ArcReal"
							|| this.find(".canvas").attr("data-type") == "ArcVirtual"
                            || this.find(".canvas").attr("data-type") == "Qualified") ? true : false;
					var flag3 = this.editableWidget('getElementType') == 'Canvas' && this.find(".canvas").attr("data-type") == "Rectangle"? true : false;

					return flag1 || flag2 || flag3;
				}
			},
			aa: {
				name: '解绑数据',
				icon: 'fa-chain-broken',
				visible: function(key, opt) {
					
					if((this.find("canvas").length != 0 && this.find("canvas").attr("data-mac")) || (this.find(".widget-word").length != 0 && this.find(".widget-word").parent().attr("data-mac"))){
						return /TextBox|Canvas/.test(this.editableWidget('getElementType')) ? true : false;
					}
					//统计图显示解绑功能
					if((this.find("canvas").length !=0 && (this.attr("id").indexOf("charts") != -1))){
                        return true;
					}
					
				},
				callback: function(key, opt) {
					
					//解绑统计图上边的数据
                    if((this.find("canvas").length !=0 && (this.attr("id").indexOf("charts") != -1))){
                        this.find("div[class='chart-container']").empty();
                        this.find("div[class='chart-container']").removeAttr("style");
                        this.find("div[class='chart-container']").removeAttr("data-url");
                        this.find("div[class='chart-container']").removeAttr("_echarts_instance_");
                        this.find("div[class='chart-container']").css("height","100%");
                        this.find("img[class='image']").removeAttr("style");
                        this.css("height","220px");
                    }
					var can = this.find("canvas");
					var txt = this.find(".widget-word");
					if(can.length != 0 && can.attr("data-mac")){
						can.removeAttr("data-mac");
						can.removeAttr("data-driverid");
						can.removeAttr("data-pointtypename");
						can.removeAttr("data-codekey");
						can.removeAttr("data-propertyid");
					}
					if(txt.length != 0 && txt.parent().attr("data-mac")){
						var txtP = txt.parent();
						txtP.removeAttr("data-mac");
						txtP.removeAttr("data-driverid");
						txtP.removeAttr("data-pointtypename");
						txtP.removeAttr("data-codekey");
						txtP.removeAttr("data-propertyid");
						txtP.removeAttr("data-units");
						//data-mac="00-14-97-14-3A-8B" data-driverid="372" data-pointtypename="D_MONITOR" data-codekey="5310" data-propertyid="0" data-units="毫米"
					}
					
				}
			}
		}
	});
	
}

$.fn.draggableElement = function(ondrag,onresize,onchange) {
	
	var x,y;
	var $this = this;
	this.draggable({
		draggable:'disable',
		snap: true,
		snapTolerance: 10,
		scroll: true,
		containment: ".demo",
		drag: function(e, ui) {
			
			ondrag && ondrag($(this));
			var left = parseFloat(ui.helper.css("left")).toFixed(2)*1;
			var tmpX = left - x;
			x = left;
			var top = parseFloat(ui.helper.css("top")).toFixed(2)*1;
			var tmpY = top - y;
			y = top;
			//y = parseFloat(ui.helper.css("top")).toFixed(2)*1 - y;
			$(this).editableWidget('updatePosition');
            $.each($(this).siblings(".ui-selected"),function(idx,item){
            	var sideX = parseFloat($(item).css("left")).toFixed(2);
            	var sideY = parseFloat($(item).css("top")).toFixed(2);
            	$(item).css({left:(sideX*1+tmpX)+"px",top:(sideY*1+tmpY)+"px"});
            	$(item).trigger('change');
            	instance.repaintEverything();
            });
            

		},
		grid:[20,20],
		start:function(event, ui){
			
			var _drag_ele=ui.helper;
			x=parseFloat(_drag_ele.css('left')).toFixed(2)*1;
			y=parseFloat(_drag_ele.css('top')).toFixed(2)*1;
			
		},
		stop: function(e, t) {
			
			$(this).trigger('change');
			$(this).parent().children(".ui-draggable").removeClass("ui-selected");
			
		}
	})
	.resizable({
		resize: function( event, ui ) {
			
			onresize && onresize($(this));
			$(this).editableWidget('updateSize');
			if(ui.helper.attr("widget-type") == 'Canvas'
				|| ui.helper.attr("widget-type") == 'CanvasGateWay'
					|| ui.helper.attr("widget-type") == 'CanvasProceseAlter'
                	|| ui.helper.attr("widget-type") == 'CanvasQualified'){
				var id = ui.helper.find("canvas").attr("id");
				var e = $("#"+id);
				var extendsImg = ui.helper.find(".canvas").attr('data-type');
				var ele = document.getElementById(id);
				var w_h = [ui.helper.width(),ui.helper.height()];//$(this).editableWidget('getSize');
				ele.width=w_h[0];
				ele.height=w_h[1];
				var ctx=ele.getContext("2d");
				ctx.clearRect(0, 0, ele.width, ele.height);
                //canvas.width = ele.width;
				storageManager["render"+extendsImg](ele,{lineWidth:e.attr("data-lineWidth"),strokeStyle:e.attr("data-strokeStyle"),fillStyle:e.attr("data-fillStyle")});
			}
			//storageManager.renderTriangle(ele);
			
		},
		stop: function( event, ui ) {
			
			$(this).trigger('change');

		}
	})
	.on('change', function() {
		onchange && onchange($(this));
	}).on('click',function(e){
		
		var _selectable= $(this).parent();
		 if(!e.ctrlKey && !e.shiftKey){  //没有按下Ctrl或Shift键
          if(!$(this).hasClass("ui-selected")){
        	 var a = $(this).css('draggable');

              _selectable.children(".ui-draggable").removeClass("ui-selected");
             //$(this).addClass("ui-selected");
              $(this).addClass("ui-selected");
          }
          //selected_begin_index=_selectable.children("li").index(this);
          $(this).focus();
      }else if(e.ctrlKey && !e.shiftKey){ //只按下Ctrl键
     	 if($(this).hasClass("ui-selected")){
     		 $(this).blur();
     		 $(this).removeClass("ui-selected");
     	 }
          else{
        	  $(this).focus();
        	  $(this).addClass("ui-selected");
          }
          //selected_begin_index=_selectable.children("li").index(this);
      }
		 
	}).keydown(function(e){
		
		
		var fixed = 1;
		if(e.shiftKey){
			fixed = 10;
		}
		//$.each($(this).parent(),function(idx,item){
			var oriPosition = $(this).editableWidget("getPosition");
			var oriLeft = oriPosition[0];
			
			var oriTop = oriPosition[1];
			
			var position = [oriLeft,oriTop];
			if(e.which == 37){
				position[0] = oriLeft - fixed;
			}else if(e.which == 39){
				position[0] = oriLeft + fixed;
			}else if(e.which == 38){
				position[1] = oriTop - fixed;
			}else if(e.which == 40){
				position[1] = oriTop + fixed;
			}
			$(this).css({
	            left: (position[0] || 0) + 'px',
	            top: (position[1] || 0) + 'px'
	        });
		    $(this).editableWidget('updatePosition').trigger('change');
		//});
		    
	});
	
	return this;
}


//监控画面数据库数据加载
var storageManager = (function(){
	
	var Container = function(id,size, background, name,description){
		
		this.chartId = id;
		this.size = size;
		this.background = background;
		this.name = name;
		this.description = description;
		// this.sourceData = '';
		
	};
	var ElementInfo = function(){
		
		var elementId;
		var elementDiv;
		var flowchartWindow;
		var currentDivId;
		var connector;
		
	};
	ElementInfo.prototype = {			
		setElementId: function(elementId){
			
			this.elementId = elementId;
			
		},
		getElementId: function(){
			
			return this.elementId;
		},
		setElementDiv: function(elementDiv){
			
			this.elementDiv = elementDiv;
		},
		getElementDiv: function(){
			
			return this.elementDiv;
		},
		getFlowchartWindow: function(){
			
			return this.flowchartWindow;
		},
		setFlowchartWindow: function(flowchartWindow){
			
			this.flowchartWindow = flowchartWindow;
		},
		setConnector: function(connector){
			
			this.connector = connector;
		},
		getConnector: function(){
			
			return this.connector;
		}
	};
	var ConnectionInfo = function(){
		
		var sourceId;
		var targetId;
		var uuid;
		var connector;
		var sourcePosition;
		var targetPosition;
		var lineType;
	};
	ConnectionInfo.prototype = {
		getSourceId: function(){
			
			return this.sourceId;
		},
		setSourceId: function(sourceId){
			
			this.sourceId = sourceId;
		},
		getTargetId: function(){
			
			return this.targetId;
		},
		setTargetId: function(targetId){
			
			this.targetId = targetId;
		},
		getUuid: function(){
			
			return this.uuid;
		},
		setUuid: function(uuid){
			
			this.uuid = uuid;
		},
		getConnector: function(){
			
			return this.connector;
		},
		setConnector: function(connector){
			
			this.connector = connector;
		},
		getSourcePosition: function(){
			
			return this.sourcePosition;
		},
		setSourcePosition: function(sourcePosition){
			
			this.sourcePosition = sourcePosition;
		},
		getTargetPosition: function(){
			
			return this.targetPosition;
		},
		setTargetPosition: function(targetPosition){
			
			this.targetPosition = targetPosition;
		},
		setLineType: function(lineType){
			
			this.lineType = lineType;
		},
		getLineType: function(){
			
			return this.lineType;
		}
	};

	var BindingDataInfo = function(id, styles, props){
		
		this.id = id;
		this.styles = styles;
		this.props = props;
		// this.sourceData = '';
	}

	var ComponentInfo = function(id, eleDiv){
		
		this.chartId = id;
		this.elementDiv = eleDiv;
		// this.sourceData = '';
	}

	var ChartDataInfo = function(id, data) {
		
		this.chartId = id;
		this.data = data;
	}

	/**
	 * @description check if localStorage is supported
	 */
	function supportstorage() {
		
		if (typeof window.localStorage=='object')
			return true;
		else
			return false;
	}


	function restoreComponentsData() {
		
		var components = JSON.parse(localStorage.getItem("components"));
		if (components) {
			var $p = $(".demo");
			for (var i = 0; i < components.length; i++) {
				$p.append(components[i].elementDiv);
			}
			$(".lyrow", $p)
				.removeClass('ui-resizable')
				.draggableElement(null, function(){
					
					if(echarts.getInstanceByDom($(this).find('.chart-container')[0])){
						echarts.getInstanceByDom($(this).find('.chart-container')[0]).resize();
					}
				}, storageManager.saveComponentsData)
				.editableWidget();
				//.on('resize', );

			var $component = $(".component", $p)
				.draggableElement(null, null, storageManager.saveComponentsData)
				.editableWidget();
			var temp = $("canvas",$component);
			if(temp.length != 0){
				storageManager.restoreCanvasData($component);
			}
			if($component.length > 0){
				storageManager.restoreNowDate($component);
			}
			$(".monitorTable", $p)
			.draggableElement(null, null, storageManager.saveComponentsData)
			.editableWidget();
			$(".AlarmTable", $p)
			.draggableElement(null, null, storageManager.saveComponentsData)
			.editableWidget();
            $(".AlarmTableMT", $p)
                .draggableElement(null, null, storageManager.saveComponentsData)
                .editableWidget();
		}
		
	}

	function saveBindText(target){
		
		var _id = target.editableWidget('getBindId');
		var _styles = target.editableWidget('getBindTextBox').attr('style');
		var _props = target.editableWidget('getSelectedProperties');
		var bindingDataInfo = new BindingDataInfo(_id, _styles, _props);

		syncToLocal('bindingData', bindingDataInfo);
		
	}

	function restoreBindText(target) {
		
		var bindingData = JSON.parse(localStorage.getItem("bindingData"));
		if (bindingData != null && bindingData.length != 0) {
			for (var i = 0; i < bindingData.length; i++) {
				var item = bindingData[i];
				if(item.id == target.editableWidget('getBindId')) {
					target.editableWidget('getBindTextBox').attr('style', item.styles);
					target.editableWidget('updateSelectedProperties', item.props);
				}
			}
		}
		
	}

	function syncToLocal(field, obj, key) {
		
		var localObj = JSON.parse(localStorage.getItem(field));
		var isExist = false;
		key = key || 'id';
		if (localObj != null && localObj.length != 0) {
			for (var i = 0; i < localObj.length; i++) {
				if (typeof obj == 'string') {
					if(localObj[i][key] == obj) {
						localObj.splice(i,1);
						break;
					}
				} else {
					if(localObj[i][key] == obj[key]){
						localObj[i] = obj;
						isExist = true;
						break;
					}
				}
			}
		}
		if (!isExist && (typeof obj != 'string')) {
			if(localObj == null){
				localObj = [];
			}
			localObj.push(obj);
		}
		localStorage.setItem(field,JSON.stringify(localObj));
		
	}

	return {

		renderTriangle:function(ele,opt){
			
			var ctx=ele.getContext("2d");
			ctx.beginPath();
			ctx.moveTo(ele.width/2,13);
			ctx.lineWidth = (opt && opt.lineWidth) || 1;
			ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
			ctx.fillStyle = (opt && opt.fillStyle) || "white";
			ctx.lineTo(ele.width-13,ele.height-13);
			ctx.lineTo(13,ele.height-13);
			ctx.closePath();//填充或闭合 需要先闭合路径才能画
			ctx.stroke();
			ctx.fill();
			
    },
    renderRectangle:function(ele,opt){
    	
		var ctx=ele.getContext("2d");
		ctx.beginPath();
		ctx.lineWidth = (opt && opt.lineWidth) || 1;
		ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
		ctx.rect(5,5,ele.width-10,ele.height-10);
		ctx.stroke();
		ctx.fill();
		
    },
    renderArc:function(ele,opt){
    	
    	var ctx=ele.getContext("2d");
    	ctx.beginPath();
    	ctx.lineWidth = (opt && opt.lineWidth) || 1;
    	ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
    	var r1 = ele.width/2;
    	var r2 = ele.height/2;
    	ctx.arc(r1,r2,r1 < r2 ? (r1 - 10) < 0 ? r1: (r1 - 10) : (r2 - 10) < 0 ? r2 : (r2 - 10) ,0,2*Math.PI);
    	ctx.stroke();
    	ctx.fill();
    	
    },
    renderArcReal:function(ele,opt){
    	
    	var ctx=ele.getContext("2d");
    	ctx.beginPath();
    	ctx.lineWidth = (opt && opt.lineWidth) || 1;
    	ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
    	var r1 = ele.width/2;
    	var r2 = ele.height/2;
    	ctx.arc(r1,r2,r1 < r2 ? (r1 - 10) < 0 ? r1: (r1 - 10) : (r2 - 10) < 0 ? r2 : (r2 - 10) ,0,2*Math.PI);
    	ctx.stroke();
    	ctx.fill();
    	
    },
    renderArcVirtual:function(ele,opt){
    	
    	var ctx=ele.getContext("2d");
    	ctx.beginPath();
    	ctx.lineWidth = (opt && opt.lineWidth) || 1;
    	ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
    	var r1 = ele.width/2;
    	var r2 = ele.height/2;
    	ctx.arc(r1,r2,r1 < r2 ? (r1 - 10) < 0 ? r1: (r1 - 10) : (r2 - 10) < 0 ? r2 : (r2 - 10) ,0,2*Math.PI);
    	ctx.stroke();
    	ctx.fill();
    	
    },

	renderQualified:function(ele,opt){

		var ctx=ele.getContext("2d");
		ctx.beginPath();
		ctx.lineWidth = (opt && opt.lineWidth) || 1;
		ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
		ctx.rect(5,5,ele.width-10,ele.height-10);
		ctx.stroke();
		ctx.fill();

	},

    renderPentagon:function(ele,opt){
    	
    	var ctx=ele.getContext("2d");
    	ctx.beginPath();
    	ctx.lineWidth = (opt && opt.lineWidth) || 1;
    	ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
    	var w = ele.width;
    	var r2 = ele.height;
    	x0 = w / 2, y0 = 0;
    	x1 = w, y1 = w * Math.tan(36*2*Math.PI/360) / 2;
    	x2 = w / 2 + w / 4 / Math.cos(36*2*Math.PI/360), y2 = w / 4 / (Math.cos(36*2*Math.PI/360) * Math.tan(18*2*Math.PI/360));
    	x3 = w / 2 - w / 4 / Math.cos(36*2*Math.PI/360), y3 = w / 4 / (Math.cos(36*2*Math.PI/360) * Math.tan(18*2*Math.PI/360));
    	x4 = 0, y4 = w * Math.tan(36*2*Math.PI/360) / 2;
    	ctx.moveTo(x0,y0+5);
		ctx.lineTo(x1-5,y1);
		ctx.lineTo(x2,y2);
		ctx.lineTo(x3,y3);
		ctx.lineTo(x4+5,y4);
		ctx.closePath();//填充或闭合 需要先闭合路径才能画
		ctx.stroke();
		ctx.fill();
		
    },
    render5Stars:function(ele,opt){
    	
    	var ctx=ele.getContext("2d");
    	ctx.beginPath();
    	ctx.lineWidth = (opt && opt.lineWidth) || 1;
    	ctx.strokeStyle = (opt && opt.strokeStyle) || "black";
		ctx.fillStyle = (opt && opt.fillStyle) || "white";
    	var r=ele.width/2,R=r/2 ,x=r,y=r,rot=180;
    	for(var i=0; i<5; i++){
    		ctx.lineTo(Math.cos((18+i*72-rot)/180*Math.PI)*R+x,-Math.sin((18+i*72-rot)/180*Math.PI)*R+y);

    		ctx.lineTo(Math.cos((54+i*72-rot)/180*Math.PI)*r+x,-Math.sin((54+i*72-rot)/180*Math.PI)*r+y);
            }
    	ctx.closePath();
    	ctx.stroke();
    	ctx.fill();
    	
    },
		nowDate : function(formatter){
			
			var d = new Date();
			//
			var month = d.getMonth()+1;
			var day = d.getDate();
			var h = d.getHours();
			var m = d.getMinutes();
			var s = d.getSeconds();
			var time = (h<10 ? '0' : '')+h + ":" + (m<10 ? '0' : '')+m + ":" + (s<10 ? '0' : '')+s;
			var output = d.getFullYear() + '-' +    (month<10 ? '0' : '') + month + '-' +    (day<10 ? '0' : '') + day +" "+ time ;
			if(formatter){
				return d.format(formatter);
			}
			
			return d.format("yyyy-MM-dd HH:mm:ss");
		},
		removeAll: function(){
			
			if (supportstorage()){
				localStorage.removeItem("elementsInfo");
				localStorage.removeItem("connections");
				localStorage.removeItem("winId");
				localStorage.removeItem("components");
				localStorage.removeItem("bindingData");
				localStorage.removeItem("chartsData");
				localStorage.removeItem("monitorTableData");
				localStorage.removeItem("AlarmTableData");
				localStorage.removeItem("spcData");
				localStorage.removeItem("spcAnalysisData");
                // localStorage.removeItem("AlarmTableMTData");
                localStorage.removeItem("background");
				localStorage.removeItem("container");
			}
			
		},
		createConnectionInfo: function(){
			
			return new ConnectionInfo();
		},
		  restoreCanvasData: function($component){
			
	    	$.each($component,function(idx,tmp){
	    		var cas = $(tmp).find(".canvas");
				if(cas.length != 0){
					var w_h = [$(tmp).width(),$(tmp).height()];//$(tmp).editableWidget('getSize');
					var extendsImg = cas.attr("data-type");
					var $canvas = $(tmp).find('canvas');
					var ele = document.getElementById($canvas.attr("id"));
					ele.width=w_h[0];
					ele.height=w_h[1];
					var ctx=ele.getContext("2d");
					ctx.clearRect(0, 0, ele.width, ele.height);
					ele.width = ele.width;
					storageManager["render"+extendsImg](ele,{lineWidth:$canvas.attr("data-lineWidth"),strokeStyle:$canvas.attr("data-strokeStyle"),fillStyle:$canvas.attr("data-fillStyle")});
				}

			});
	    	
	    },
	    restoreNowDate: function($component){
	    	
	    	$.each($component,function(idx,tmp){
	    		if($(tmp).attr("widget-type") == 'Timer'){
	    			$(tmp).find(".widget-word").text(storageManager.nowDate($(tmp).attr("data-formatter")));
					var timer = setInterval(function(){
						$(tmp).find(".widget-word").text(storageManager.nowDate($(tmp).attr("data-formatter")));
					},1000);
					$component.data("timer",timer);
				}

			});
	    	
	    },
		generateConnectionInfoList: function(obj){
			
			var connectionInfoList = new Array();
			if(obj){
				$.each(obj,function(idx,connection){
					
					var connectionInfo = new ConnectionInfo();
					connectionInfo.setSourceId(connection.sourceId);
					connectionInfo.setTargetId(connection.targetId);
					connectionInfo.setUuid(typeof connection.uuid == 'string' ? JSON.parse(connection.uuid) : connection.uuid);
					connectionInfo.setSourcePosition(connection.sourcePosition);
					connectionInfo.setTargetPosition(connection.targetPosition);
					// connectionInfo.setConnector(connection.connector);
					connectionInfo.setLineType(typeof connection.lineType == 'string' ? JSON.parse(connection.lineType) : connection.lineType);
					connectionInfoList.push(connectionInfo);
					
				});
			}
			
			return connectionInfoList;
		},
		generateElemetInfoList: function(obj){
			
			var elemetInfoList = new Array();
			if(obj){
				$.each(obj,function(idx,item){
					var elementInfo = new ElementInfo();
					elementInfo.setElementId(item.elementId);
					elementInfo.setElementDiv(item.elementDiv);
					elementInfo.setFlowchartWindow(item.flowchartWindow);
					elemetInfoList.push(elementInfo);
				});
			}
			
			return elemetInfoList;
		},
		bindingJspplumb: function(obj){
			
			var endpoints = instance.getEndpoints(obj.find(".jtk-node"));//获取块元素的所有连接点
			if (!endpoints) return;

			var _id = $(obj).attr("id");
			var _element = $(obj).prop("outerHTML").replace(resizeReg, '');
			var domId = endpoints[0].getElement().id;

			var elementInfo = new ElementInfo();
			elementInfo.setElementId(_id);
			elementInfo.setElementDiv(_element);
			elementInfo.setFlowchartWindow(domId);

			syncToLocal('elementsInfo', elementInfo, 'elementId')
			
		},
		initBindText: function(e) {
			
			var target = $(e.currentTarget);
			target.editableWidget('getBindTextBox').draggableElement(null, null, function(){
				saveBindText(target);
			});
			restoreBindText(target);
			target.editableWidget('getBindTextBox').editableWidget();
			
		},
		saveComponentsData: function($ele) {
			
			var _id = $ele.attr("id");
			var _eleDiv = $ele.prop("outerHTML").replace(resizeReg, '');
			var chartsInfo = new ComponentInfo(_id, _eleDiv);
			syncToLocal('components', chartsInfo,'chartId');
			
		},
		saveContainerData: function($ele) {
			
			//var background = $ele.css("background-image")+" "+$ele.css("background-repeat") + " " + $ele.css("background-attachment") + " "+$ele.css("background-clip") + " " + $ele.css("background-origin") + " " +  $ele.css("background-size") + " " + $ele.css("background-position") + " " + $ele.css("background-color");
			var background = {};
			background["background-image"]=$ele.css("background-image");
			background.backgroundRepeat=$ele.css("background-repeat");
			background["background-color"]=$ele.css("background-color");
			background.backgroundPosition=$ele.css("backgroundPosition");
			background.backgroundSize=$ele.css("backgroundSize");
			
			var chartsInfo = new Container(1,[
			                                parseFloat(parseFloat($ele.css('width')).toFixed(2)),
			                                parseFloat(parseFloat($ele.css('height')).toFixed(2))
			                            ], background, "demo","demo");
			syncToLocal('container', chartsInfo,'chartId');
			
		},
		restoreData: function(){
			
			if (supportstorage()) {

				restoreComponentsData();
				storageManager.restoreChartsData();
				storageManager.restoreMonitorTableData();
				storageManager.restoreAlarmTableData();
                storageManager.restoreAlarmTableMTData();
                storageManager.restoreExtendsImg();
				storageManager.restoreContainerData();
				storageManager.restoreSpcData();
				storageManager.restoreSpcAnalysisData();
				_layouthistory = JSON.parse(localStorage.getItem("elementsInfo"));
				if(_layouthistory){
					var layout = storageManager.generateElemetInfoList(_layouthistory);
					$.each(layout,function(idx,elementInfo){
						$(".demo").append(elementInfo.getElementDiv());
						_addEndpoints(elementInfo.getFlowchartWindow().split("flowchart")[1], ["LeftMiddle", "RightMiddle"], ["TopCenter", "BottomCenter"]);
						// elementDraggable($("#"+elementInfo.getElementId()));
						$("#"+elementInfo.getElementId())
						.draggableElement(repaintEverything, repaintEverything, function(target){
							repaintEverything();
							storageManager.bindingJspplumb(target);
						})
						.editableWidget('bindData')
						.on('bindTextCreate', storageManager.initBindText);
					})
					var connections = storageManager.generateConnectionInfoList(JSON.parse(localStorage.getItem("connections")));
					localStorage.removeItem("connections");
					$.each(connections,function(idx,item){
						var graphicInfo = item.getLineType();
						var connection = instance.connect({
							uuids: item.getUuid(),
							editable: true,
							type: graphicInfo.graphicType,
							connector: graphicInfo.connectorType,
							paintStyle: graphicInfo.paintStyle
						});
					});
				}
				/*setTimeout(function(){
				
					storageManager.restoreMonitorBackground();
				},600);*/

			}
			
		},
		deleteLocal: function(field, obj, key){
			
			syncToLocal(field, obj, key);
		},
		saveChartsData: function(id, data) {
			
			var _data = JSON.stringify(data);
			var chartDataInfo = new ChartDataInfo(id, _data);
			syncToLocal('chartsData', chartDataInfo,"chartId");
			
		},
		saveMonitorTableData: function(id, data) {
			
			var _data = JSON.stringify(data);
			var chartDataInfo = new ChartDataInfo(id, _data);
			syncToLocal('monitorTableData', chartDataInfo,"chartId");
			
		},
		saveAlarmTableData: function(id, data) {
			
			var _data = JSON.stringify(data);
			var chartDataInfo = new ChartDataInfo(id, _data);
			syncToLocal('AlarmTableData', chartDataInfo,"chartId");
			
		},
        saveAlarmTableMTData: function(id, data) {

            var _data = JSON.stringify(data);
            var chartDataInfo = new ChartDataInfo(id, _data);
            syncToLocal('AlarmTableMTData', chartDataInfo,"chartId");

        },
		saveSpcAnalysisData: function (id, data){
			var _data = JSON.stringify(data);
			var chartDataInfo = new ChartDataInfo(id, _data);
			syncToLocal('spcAnalysisData', chartDataInfo,"chartId");
		},
		saveSpcData: function (id, data){
			var _data = JSON.stringify(data);
			var chartDataInfo = new ChartDataInfo(id, _data);
			syncToLocal('spcData', chartDataInfo,"chartId");
		},
		// 从缓存中获取【container】，并将其内容回复到【.demo】当中
		restoreContainerData:function(){
			
			var containerData = JSON.parse(localStorage.getItem("container"));
			if(containerData){
				$.each(containerData,function(idx,item){
					$(".demo").css({width:item.size[0],height:item.size[1]});
					if(typeof(item.background) == "string"){
						$(".demo").css("background",item.background);
					}else{
						$.each(item.background,function(k,v){
							$(".demo").css(k,v);
						});
					}
				});
			}
			
		},
		restoreChartsData: function(interval) {
			
			var chartsData = JSON.parse(localStorage.getItem("chartsData_"+$("#monitorPainterList").val()));
			if (typeof ($("#monitorPainterList").val()) == "undefined"){
				if (localStorage.getItem("chartsData") == ""){
					chartsData = "";
				} else {
					chartsData = JSON.parse(localStorage.getItem("chartsData"));
				}

			}
			if (chartsData && chartsData.length) {
				for (var i = 0; i < chartsData.length; i++) {
					var item = chartsData[i],
						$chart = $('#' + item.chartId).find('.chart-container'),
						data = JSON.parse(item.data);
					var flag = true;
					$.each(data,function(idx,_item){
						 if(interval){
							 if(_item.name == 'category' && _item.value == 'ProductionProcess'){
								 flag = false;
							 }
						 }
					});
					if(flag){
						renderManager.drawCharts($chart, data);
					}
				}
			}
			
		},
		restoreSpcData: function(interval){
			var spcData = JSON.parse(localStorage.getItem("spcData_"+$("#monitorPainterList").val()));
			if (typeof ($("#monitorPainterList").val()) == "undefined"){
				spcData = JSON.parse(localStorage.getItem("spcData"));
			}
			if (spcData && spcData.length) {
				for (var i = 0; i < spcData.length; i++) {
					var item = spcData[i],
						$chart = $('#' + item.chartId).find('.chart-container'),
						data = JSON.parse(item.data);
					var flag = true;
					$.each(data,function(idx,_item){
						if(interval){
							if(_item.name == 'category' && _item.value == 'ProductionProcess'){
								flag = false;
							}
						}
					});
					if(flag){
						renderManager.drawSpcCharts($chart, data);
					}
				}
			}
		},
		restoreSpcAnalysisData: function(interval){
			var spcAnalysisData = JSON.parse(localStorage.getItem("spcAnalysisData_"+$("#monitorPainterList").val()));
			if (typeof ($("#monitorPainterList").val()) == "undefined"){
				spcAnalysisData = JSON.parse(localStorage.getItem("spcAnalysisData"));
			}
			// var monitorId = localStorage.getItem("monitorId");
			var monitorId = $("#monitorPainterList").val();
			if (spcAnalysisData && spcAnalysisData.length) {
				for (var i = 0; i < spcAnalysisData.length; i++) {
					var item = spcAnalysisData[i],
						$chart = $('#' + item.chartId).find('.chart-container'),
						data = JSON.parse(item.data);
					var chartId_data = {
						name: "chartId",
						value: item.chartId,
					}
					var monitorId_data = {
						name: "monitorId",
						value: monitorId,
					}
					data.push(chartId_data);
					data.push(monitorId_data);
					var flag = true;
					$.each(data,function(idx,_item){
						if(interval){
							if(_item.name == 'category' && _item.value == 'ProductionProcess'){
								flag = false;
							}
						}
					});
					if(flag){
						renderManager.drawSpcCharts($chart, data);
					}
				}
			}
		},
		restoreExtendsImg:function(){

		},
		restoreMonitorTableData: function() {
			
			var monitorTableData = JSON.parse(localStorage.getItem("monitorTableData"));
			if (monitorTableData && monitorTableData.length) {
				for (var i = 0; i < monitorTableData.length; i++) {
					var item = monitorTableData[i],
					$monitorTable = $('#' + item.chartId).find('.table'),
					data = JSON.parse(item.data);
				    renderManager.drawMonitorTable($monitorTable,data);
				    $('#' + item.chartId).editableWidget('updateLineCount');
				}
			}
			
		},
		restoreAlarmTableData: function() {
			
			var AlarmTableData = JSON.parse(localStorage.getItem("AlarmTableData"));
			if (AlarmTableData && AlarmTableData.length) {
				for (var i = 0; i < AlarmTableData.length; i++) {
					var item = AlarmTableData[i],
					$AlarmTable = $('#' + item.chartId).find('.table');
					var lineCount = localStorage.getItem(item.chartId+"_lineCount");
					var headData = ["行号","工厂","产线","设备","日期","类型","报警ID","信息","状态"];
				    renderManager.drawAlarmTable($AlarmTable,headData,lineCount);
				    //$('#' + item.chartId).editableWidget('updateLineCount');
				}
			}
			
		},
        restoreAlarmTableMTData: function() {

            var AlarmTableData = JSON.parse(localStorage.getItem("AlarmTableMTData"));
            if (AlarmTableData && AlarmTableData.length) {
                for (var i = 0; i < AlarmTableData.length; i++) {
                    var item = AlarmTableData[i],
                        $AlarmTable = $('#' + item.chartId).find('.table');
                    var lineCount = localStorage.getItem(item.chartId+"_lineCount");
                    var headData = ["序号","量具编号","量具名称","量具类型","量具状态","所属产线","所属站点","预警时间","报警时间","超出报警时长","报警信息","显示"];
                    renderManager.drawAlarmTable($AlarmTable,headData,lineCount);
                    //$('#' + item.chartId).editableWidget('updateLineCount');
                }
            }

        },
		restoreMonitorBackground:function(){
			
			if(localStorage.getItem("background")){
				$("#canvas").css("background",localStorage.getItem("background"));
			}
			
		}
	}
	
})();

$("#monitor").click(function(e){
	$(".demo").load("${contextPath}/procedureMonitor/toMonitor",function(){
		
	});
});

var loadMonitorSelect = function(id){
	
	$("#productionLineList").empty();
	$.ajax({
			url:"${contextPath}/procedureMonitor/getProductionLineByCompanyId?companyId="+id,
			dataType:"JSON",
			type:"GET",
			success:function(data){
				$.each(data,function(idx,item){
					$("#productionLineList").append("<option value='"+item.id+"'>"+item.linename+"</option>");

				});
			}
		});
	
}
$("#companyList").change(function(){
	
	loadMonitorSelect($(this).val());
});

var fileName = 'test.html';
function downloadFile(fileName, content){
	
	var aLink = document.createElement('a');
	var blob = new Blob([content]);
	var evt = document.createEvent("HTMLEvents");
	evt.initEvent("click", false, false);//initEvent 不加后两个参数在FF下会报错
	aLink.download = fileName;
	aLink.href = URL.createObjectURL(blob);
	aLink.dispatchEvent(evt);
	
}
// ------------------------右键复制------------------------------------------------------------------------
function cloneItem(temp, copyBeforeItem) {
	temp = copyBeforeItem.clone().css({
		left: function(index, value) {
			
			return parseFloat(value) - 200;
		},
		top: function(index, value) {
			
			return parseFloat(value) + 32;
		},
		hidefocus:"true",
		outline:0
		//opacity:0
	// }).appendTo(copyBeforeItem);
	});
	temp.attr("tabindex",0);
	
	winId ++;

	if (copyBeforeItem.hasClass('lyrow')) {
		temp.width(200);
		temp.attr('id', 'charts' + winId);
		temp.draggableElement(null, function(){
			
			var $chart = $('.chart-container', temp);
			//
			if ($chart.length) {
				//	
				$chart.prev().width($chart.parent().parent().width());
				$chart.prev().height($chart.parent().parent().height());
				//	echarts.getInstanceByDom($chart[0]).resize();
				var instance_ = echarts.getInstanceByDom($chart[0]);
				if(instance_){
					instance_.resize();
				}
			}
			
		}, storageManager.saveComponentsData).editableWidget();

		storageManager.saveComponentsData(temp);
	} else if (copyBeforeItem.hasClass('box')) {

		temp.attr('id', 'flowchartWindowOuter' + winId);
		temp.find('.jtk-node').attr('id', 'flowchartWindow' + winId);

		temp.draggableElement(repaintEverything, repaintEverything, function(){
			
			repaintEverything();
			storageManager.bindingJspplumb(temp);
			
		})
		.editableWidget('bindData')
		.on('bindTextCreate', storageManager.initBindText);

		instance.batch(function () {
			
			_addEndpoints("Window" + winId, ["LeftMiddle", "RightMiddle"], ["TopCenter", "BottomCenter"]);
		});
		storageManager.bindingJspplumb(temp);
	} else if (copyBeforeItem.hasClass('monitorTable')) {
		temp.width(260);
		temp.attr('id', 'monitorTable' + winId);
		temp.draggableElement(null, function(){
			
			var $table = $('.monitorTable-container', temp);
			if ($table.length) {
			//	
				$table.prev().width($table.parent().parent().width());
				$table.prev().height($table.parent().parent().height());
			//	echarts.getInstanceByDom($chart[0]).resize();
			}
			
		}, storageManager.saveComponentsData).editableWidget();
		storageManager.saveComponentsData(temp);
	}else if(copyBeforeItem.hasClass('AlarmTable')){
		temp.width(260);
		temp.attr('id', 'AlarmTable' + winId);
		temp.draggableElement(null, function(){
			
			var $table = $('.AlarmTable-container', temp);
			if ($table.length) {
			//	
				$table.prev().width($table.parent().parent().width());
				$table.prev().height($table.parent().parent().height());
			//	echarts.getInstanceByDom($chart[0]).resize();
			}
			
		}, storageManager.saveComponentsData).editableWidget();
		storageManager.saveComponentsData(temp);
	}else if(copyBeforeItem.hasClass('AlarmTableMT')){
        temp.width(260);
        temp.attr('id', 'AlarmTableMT' + winId);
        temp.draggableElement(null, function(){

            var $table = $('.AlarmTableMT-container', temp);
            if ($table.length) {
                //
                $table.prev().width($table.parent().parent().width());
                $table.prev().height($table.parent().parent().height());
                //	echarts.getInstanceByDom($chart[0]).resize();
            }

        }, storageManager.saveComponentsData).editableWidget();
        storageManager.saveComponentsData(temp);
    }
	else {
		temp.attr('id', 'component' + winId);
		var tmp = $('.canvas', temp);

		if(tmp.length != 0){
			tmp.hide();
			var w_h = [temp.width(),temp.height()];//temp.editableWidget('getSize');
			var $canvas = tmp.next();
			$canvas.attr("id","canvas_"+winId);
			var ele = document.getElementById("canvas_"+winId);
			ele.width=w_h[0];
			ele.height=w_h[1];
			var ctx=ele.getContext("2d");
			ctx.clearRect(0, 0, ele.width, ele.height);
			$canvas.width = ele.width;
			storageManager["render"+tmp.attr('data-type')](ele);
			if(tmp.attr('data-type') == "Arc" ||
					tmp.attr('data-type') == "ArcReal" ||
					// tmp.attr('data-type') == "ArcVirtual" ||
					"Rectangle"==tmp.attr('data-type')){
				temp.tooltip({
					items:"div",
	                content:function(){
	                	
	                	return "<p>网关:"+(ele.getAttribute('data-mac') || "") +"</p></br> <p>测点:"+ (ele.getAttribute('data-codekey') || "")+"</p>"
	                },
	                track: true,
	              });
			}

			//storageManager.renderTriangle(ele);

		}
		if(copyBeforeItem.attr("widget-type") == 'Timer'){
			temp.find(".widget-word").text(storageManager.nowDate());
			copyBeforeItem.attr("data-formatter","yyyy-MM-dd HH:mm:ss");
			var timer = setInterval(function(){
					temp.find(".widget-word").text(storageManager.nowDate());
			},1000);
			temp.data("timer",timer);
		}
		if(copyBeforeItem.attr("widget-type") == 'TextBox'){
			var text = temp.find(".widget-word").parent();
			temp.css("height","23px");
			temp.tooltip({
				items:"div",
                content:function(){
                	
                	return "<p>网关:"+(text.attr('data-mac') || "") +"</p></br> <p>测点:"+ (text.attr('data-codekey') || "")+"</p>"
                },
                track: true,
              });
			
			var fontSizeBefore = $(copyBeforeItem.find(".view")).children().css('font-size');
			if(fontSizeBefore) {
				$(temp.find(".view")).children().css('font-size', fontSizeBefore);
			}
			var lineHeight = $(copyBeforeItem.find(".view")).children().css('line-height');
			if(lineHeight) {
				$(temp.find(".view")).children().css('line-height',  lineHeight);
			}

			var widthBefore = copyBeforeItem.css('width');
			if(widthBefore){
				temp.css('width', widthBefore);
			}
			var heightBefore = copyBeforeItem.css('height');
			if(heightBefore){
				temp.css('height', heightBefore);
			}
			
			var fontFamliyBefore = copyBeforeItem.css('font-family');
			if(fontFamliyBefore) {
				temp.css('font-family', fontFamliyBefore);
				// $(".widget-group widget-fontfamily")
				// var fontFamilyId = temp[0].id + "_fontfamily";
				$("#"+temp[0].id + "_fontfamily").val(fontFamliyBefore);
			}

		}
		temp.draggableElement(null, function(){
			
			var $table = $('.device-image', temp);
			if ($table.length) {
			//	
				$table.width($table.parent().parent().width());
				$table.height($table.parent().parent().height());
			//	echarts.getInstanceByDom($chart[0]).resize();
			}
			
		}, storageManager.saveComponentsData).editableWidget();
		storageManager.saveComponentsData(temp);
	}
	localStorage.setItem("winId",winId);
	
	var fontFamliyBefore = copyBeforeItem.css('font-family').replace(/\"/g, "");
	if(fontFamliyBefore) {
		temp.css('font-family', fontFamliyBefore);
		// $(".widget-group widget-fontfamily")
		// var fontFamilyId = temp[0].id + "_fontfamily";
		$("#"+temp[0].id + "_fontfamily").val(fontFamliyBefore);
	}

	temp.removeClass('context-menu-active');

	// cp
	copyBeforeItem.after(temp);
}