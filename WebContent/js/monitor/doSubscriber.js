		// 描画监控画面
		 var doSubscriber = function(){
			 
			return monitorSubscriber.subscribe("/topic/showMonitor/monitor/"+$("#monitorPainterList").val(),function(data){
					// console.log("监控各个画面元素组装----------------开始" + new Date());
					if(interval__.length != 0){
                        $.each(interval__,function(idx,inter){
                            $.each(inter,function(k,v){
                                clearInterval(k);
                                var c = document.getElementById(v);
                                // 定义为二维绘图
                                var ctx=c.getContext("2d");
                                if(null != ctx) {
                                    var datatype = $("#"+v).prev().attr("data-type");
                                    // 清空canvas区域的指定像素
                                    ctx.clearRect(0, 0, $("#"+v).height(), $("#"+v).width());
                                    c.width=$("#"+v).attr("data-width");
                                    c.height=$("#"+v).attr("data-height");
                                    if(datatype == 'Arc'){
                                        storageManager.renderArc(c,{lineWidth:$("#"+v).attr("data-lineWidth"),strokeStyle:$("#"+v).attr("data-strokeStyle"),fillStyle:$("#"+v).attr("data-fillStyle")});
                                    }else if(datatype == 'Rectangle'){	storageManager.renderRectangle(c,{lineWidth:$("#"+v).attr("data-lineWidth"),strokeStyle:$("#"+v).attr("data-strokeStyle"),fillStyle:$("#"+v).attr("data-fillStyle")});
                                    }
                                }
                            });
                        });
					}
					var prop = JSON.parse(data.body);

					var collectionProp = {};
					var procedurePropertyArray = new Array();
	                $.each(prop,function(idx,item) {
                        if(item.mesPointTypeKey == 'P_PROCEDURE'){
                            collectionProp["mesPointGateway"] = item.mesPointGateway;
                            collectionProp["driverId"] = item.driverId;
                            collectionProp["sn"] = item.sn;
                            collectionProp["mesProduct"] = item.mesProduct;
                            collectionProp["mesProductCompanyinfo"] = "";
                            collectionProp["mesProductModel"] = item.mesProductModel;
                            collectionProp["mesPointTypeKey"] = item.mesPointTypeKey;
                            collectionProp["mesProcedureName"] = item.mesProcedureName;
                            collectionProp["value"] = item.value;
                            collectionProp["productId"] = item.productId;
                            var procedureProperty = new Object();
                            procedureProperty["mesPointKey"] = item.mesPointKey;
                            procedureProperty["mesDriverPointsName"] = item.mesDriverPointsName;
                            procedureProperty["mesDriverProcedureId"] = item.mesDriverProcedureId;
                            procedureProperty["procedurePropertyName"] = item.procedurePropertyName;
                            procedureProperty["mesProductProcedureId"] = item.mesProductProcedureId;
                            procedureProperty["procedurePropertyId"] = item.procedurePropertyId +""; ;
                            procedureProperty["value"] = item.value;
                            procedureProperty["uploadTime"] = item.uploadTime;
                            procedureProperty["status"] = item.status;
                            procedureProperty["unit"] = item.unit;
                            procedurePropertyArray.push(procedureProperty);
                        }
	                });
	                collectionProp["procedureProperty"] = procedurePropertyArray;
	                var makedJson = JSON.parse(JSON.stringify(collectionProp));
	                var rs = new Array(); 
	                rs.push(makedJson);
	                // console.log("-----rs-----" + rs);
	                if(null == rs || 0 == rs.length || 
	                    rs[0] == null || 0 == rs[0]["procedureProperty"].length){
	                	rs = prop;
	                }
	                $.each(rs,function(idx,item){
					// $.each(prop,function(idx,item){
						var driverId = item.driverId;
						if(item.mesPointTypeKey == 'D_MONITOR' || item.mesPointTypeKey == 'D_STATUS'){
							var TextBind = $("div[widget-type='BindTextBox']");
							var TextBind_ = $("div[data-driverid='"+driverId+"'][data-codekey='"+item.mesPointKey+"']");
							if(TextBind_.length != 0){
								if(TextBind_.find("span").length != 0){
									if(item.mesPointTypeKey == 'D_STATUS' && item.name){
										TextBind_.find("span").text(item.name+(item.unit || ""));
									}else{
										TextBind_.find("span").text(item.value+(item.unit || ""));
									}
								}else{
									if(item.mesPointTypeKey == 'D_STATUS' && item.name){
										   TextBind_.find(".widget-word").append("<span>"+item.name+(item.unit || "")+"</span>");
									}else{
										TextBind_.find(".widget-word").append("<span>"+item.value+(item.unit || "")+"</span>");
									}
								}
									
							}
							var TextBind_canvas = $("canvas[data-driverid='"+item.driverId+"'][data-codekey='"+item.mesPointKey+"']");
							if(TextBind.hasOwnProperty("context")){
								// if(TextBind.length != 0){
								if(item.colorCode != undefined){
									/* if(item.colorCode.colorcode != ''){
										$("div[widget-type='Device'][data-url='getDeviceProperties_ajax?id="+ item.driverId +"']").css("background-color",item.colorCode.colorcode);
									}else{
										$("div[widget-type='Device'][data-url='getDeviceProperties_ajax?id="+ item.driverId +"']").css({"background":"none"});
									} */
									$("div[widget-type='Device'][data-url='getDeviceProperties_ajax?id="+ item.driverId +"']").css("background-color",item.colorCode);
									if(item.colorCode.companyfileId && item.colorCode.companyfileId != 0){
										$("div[widget-type='Device'][data-url='getDeviceProperties_ajax?id="+ item.driverId +"']").find('.device-image').attr("src",contextPath + "/company/showPic/"+item.colorCode.companyfileId);
										//$("div[widget-type='Device'][data-url='getDeviceProperties_ajax?id="+ item.driverId +"']").css({"background-image":'url("${contextPath}/company/showPic/'+item.colorCode.companyfileId+'")','background-repeat':'no-repeat','background-size':'100% 100%','-moz-background-size':'100% 100%'});
									}
								}
								if(item.mesPointTypeKey == 'D_STATUS' && item.name){
									  // TextBind_.find(".widget-word").append("<span>"+item.name+(item.unit || "")+"</span>");
								$(".prop_"+item.driverId+"_"+item.mesPointKey).children("span").text(item.name+(item.unit || ""));
								}else{
									//TextBind_.find(".widget-word").append("<span>"+item.value+(item.unit || "")+"</span>");
								$(".prop_"+item.driverId+"_"+item.mesPointKey).children("span").text(item.value+(item.unit || ""));
								}
							}
							if(TextBind_canvas.hasOwnProperty("context")){
								//if(TextBind_canvas.length != 0){
								if(item.colorCode != undefined){
									$.each(TextBind_canvas,function(idx,can_){
										var arcId = $(can_).attr("id");
										var dataType = $("#"+arcId).prev().attr("data-type");
										var c = document.getElementById(arcId);
										var ctx=c.getContext("2d");
										ctx.fillStyle=item.colorCode;
										ctx.fill();
										if(dataType == 'Arc'||'Rectangle' ==dataType){
										var flag = true;
				                        var width = c.width;
				                        var height = c.height;
				                        $("#"+arcId).attr("data-width",width);
				                        $("#"+arcId).attr("data-height",height);
				                        $("#"+arcId).attr("data-fillStyle",item.colorCode);
										var mark = setInterval(function(){
											var change_width,change_height;
											if(flag){
												change_width = width*1;
												change_height = height*1;
												// $("#"+arcId).attr("data-fillStyle",item.colorCode.colorcode.colorRgba(0.6));
												$("#"+arcId).attr("data-fillStyle",item.colorCode);
												flag = false;
											} else{
												change_width = width;
												change_height = height;
												$("#"+arcId).attr("data-fillStyle",item.colorCode);
												flag = true;
											} 
				                        	ctx.clearRect(0, 0, change_width, change_width);
											c.width=change_width;
											c.height=change_height;
											if(dataType =='Arc'){
					                          	storageManager.renderArc(
					                          			c,
					                          			{
					                          				lineWidth:$("#"+arcId).attr("data-lineWidth"),
					                          				strokeStyle:$("#"+arcId).attr("data-strokeStyle"),
					                          				fillStyle:$("#"+arcId).attr("data-fillStyle")
					                          			});
											}else if(dataType=='Rectangle'){
												storageManager.renderRectangle(
														c,
														{
															lineWidth:$("#"+arcId).attr("data-lineWidth"),
															strokeStyle:$("#"+arcId).attr("data-strokeStyle"),
															fillStyle:$("#"+arcId).attr("data-fillStyle")
														}
													);
											}
										},800);	
										var markObj = {};
										markObj[mark] = arcId;
										interval__.push(markObj);
										}
										
									});
									
									//TextBind.css("background-color",item.colorCode);
								}
							}
						}
						/* if(item.mesPointTypeKey == 'D_STATUS'){
							var TextBind = $("canvas[data-driverid='"+item.driverId+"'][data-codekey='"+item.mesPointKey+"']");
							if(TextBind.length != 0){
								if(item.colorCode != undefined){
									var c = document.getElementById(TextBind.attr("id"));
									var ctx=c.getContext("2d");
									ctx.fillStyle=item.colorCode;
									ctx.fill();
									//TextBind.css("background-color",item.colorCode);
								}
							}
						} */
						else if(item.mesPointTypeKey == 'P_PROCEDURE'){
							
							var sn = item.sn;
							var mesProduct = item.mesProduct;
							var procedureName = item.mesProcedureName;
							var mesProductCompanyinfo = item.mesProductCompanyinfo;
							var mesProductModel = item.mesProductModel;
							var value = item.value;
							var td = "<td>"+ mesProduct + "</td><td>"+ procedureName +"</td><td>"+ mesProductModel +"</td><td>"+ sn +"</td>";
							var procedures = {'sn':sn};
							$.each(item.procedureProperty,function(idx,item){
								var valObj = {};
								valObj.checkTime = item.uploadTime;
								valObj.value = item.value;
								valObj.status = item.status;
								valObj.unit = (item.unit || "");
								var TextBind = $("div[data-driverid='"+driverId+"'][data-codekey='"+item.mesPointKey+"']");
								if(TextBind.length != 0){
									if(TextBind.find("span").length != 0){
										TextBind.find("span").text(item.value+(item.unit || ""));
									}else
									   TextBind.find(".widget-word").append("<span>"+item.value+(item.unit || "")+"</span>");
								}
								procedures[item.mesPointKey] = valObj;
								/* if(item.status != '0'){
									td += "<td  style='background:#c00;color:#FFF'>" + item.value + "</td>";
								}else{
									td += "<td>" + item.value + "</td>";
								} */
							});
							$.each($("."+item.productId),function(idx,table){
								var tableId = table.id;
								/* if($(table).find("thead th").length != $(table).find("tbody tr td").length){
									var a =$(table).find("thead th").length;
												console.log(a+"-------");
									$(table).find("tbody tr td:eq(a)").remove();
								} */
								if($(table).find("thead th:gt(3)") != 0){
									var insertable = false;
									td = "<td>"+ mesProduct + "</td><td>"+ procedureName +"</td><td>"+ mesProductModel +"</td><td>"+ sn +"</td>";
									$.each($(table).find("thead th:gt(3)"),function(idx,item){
										var v = procedures[$(item).attr("data-value").split("-")[1]];
										if(v){
											if(!insertable){
												insertable = true;
											}
											if(v.status != '0' ){
												
											// td += "<td style='background:#c00;color:#FFF'>" + v.value + v.unit  + "</td>"
												td += "<td><font style='background:#c00;color:#FFF'>" + v.value + v.unit  + "</font></td>"
											
											}else{
												td += "<td>" +  v.value +v.unit+ "</td>";
											}
										} else{
											   td += "<td>"+" "+"</td>"}								
									});
									
									if(insertable){
										var row = $("<tr>"+ td + "</tr>");
										if($(table).find("tbody tr").length >= tableLine[tableId]){
											$(table).find("tbody tr:eq(0)").remove();
											
										}
											$(table).find("tbody").append(row);
									}
									
								}
							});

							$.each(processArray,function(idx,item){
								 var productMonitor = {};
								 var _array = new Array();
								 $.each(JSON.parse(item.data),function(_idx,_item){
									 if(_item.name == 'category' && _item.value == 'ProductionProcess'){
										 var chartDom = $("#"+item.chartId).find('.chart-container')[0];
										 var pointNum = $("#"+item.chartId).find('.chart-container').attr("data-pointnum");
										 var chartObj = echarts.getInstanceByDom(chartDom);
										 if(chartObj){
											 var option = chartObj.getOption();
											var xAxisDataArray = option.xAxis[0].data;
											// var xAxisDataArray = [];
											 var tooltip = option.tooltip[0];
											 var grid = option.grid[0];
											 grid.right='9%';
											 tooltip.trigger="item";
											 $.each(option.series,function(__idx,__item){
												 __item.smooth = false;
												 __item.markPoint.symbolSize=[110,48];
												 // __item.markLine.label.normal.position="top";
//												 __item.label.normal.position='top';
//												 __item.label.normal.show=true;
												 // __item.label.normal = {};
//												 __item.label.show=true;
//												 __item.label.position='top';

//												 var codeKey = __item.label.normal.formatter;
												 // var codeKey = __item.label.formatter;
												 var codeKey = __item.itemStyle.formatter;
												 if(!procedures[codeKey]){
													 return false;
												 }
												 if((__item.data.length == 1 && __item.data[0] == 0) || __item.data.length >= pointNum){
													 __item.data.splice(0,1);
													 xAxisDataArray.splice(0,1);
												 }
												 var formatter = function(params, ticket, callback){
										                var str = "";
									                    str += params.seriesName;
									                    str+= "<br />";
									                    str += "测量值:"+params.value;
									                    str+= "<br />";
									                    str += "SN:"+procedures.sn;
									                    str+= "<br />";
									                    str += "时间:"+dateRangeUtil.UnixToFullDate(procedures[codeKey].checkTime,true,8);
									                    return str;
										            };
//												 var cusVal = {value:procedures[codeKey].value,label:{normal: {show: true,position: 'top'}},itemStyle:{},tooltip:{formatter:formatter}};
												 var cusVal = {value:procedures[codeKey].value,label:{normal: {show: true,position: 'top'}},tooltip:{formatter:formatter}};
												 __item.data.push(cusVal);
												 __item.label = {
													show : true,
													postion: 'top',
													// formatter : procedures[codeKey].value
													// formatter: '{c}',
													formatter: '',
													color:'#fff'
												 };
//												 __item.label =  {
//										                normal: {
//										                    show: false,
//										                    position: 'top'
//										                }
//										            },
												 __item.symbolSize = "10";
												 //__item.markPoint = {};
												 xAxisDataArray.push(dateRangeUtil.UnixToFullDate(procedures[codeKey].checkTime,true,8));
											 });
											 var upTolerances = 0;
											 if (typeof (option.series[0].markLine.data[0]) != "undefined"){
												 upTolerances = parseFloat(option.series[0].markLine.data[0][0].value);
											 }
											 var downTolerances = 0;
											 if (typeof (option.series[0].markLine.data[1]) != "undefined"){
												 downTolerances = parseFloat(option.series[0].markLine.data[1][0].value);
											 }
//											 option.visualMap = {
//													    show:false,
//												        min: downTolerances,
//													    max: upTolerances,
												        //splitNumber: 5,
												        //color: ['#04bdeb','red', '#04bdeb'],
//												        textStyle: {
//												            color: '#fff'
//												        }
//												        pieces: [{
//												            lte: downTolerances,
//												            color: 'red'
//												        }, {
//												            gt: downTolerances,
//												            lte: downTolerances,
//												            color: 'green'
//												        }, {
//												            gt: downTolerances,
//												            color: 'red'
//												        }]
//											 };
// 											 var new_data_ = [];
// 											 var new_data1 = {};
// 											 var new_data2 = {};
// 											 new_data1.value= 300;
// 											 new_data2.value = 50;
// 											 new_data_.push(new_data1);
// 											 new_data_.push(new_data2);
// 											 option.series[0].data = new_data_;
											 var new_data = option.series;
											 var max = 0;
											 var min = 0;
											 for(var i = 0; i<new_data.length; i++){
											 	for (var j =0; j<new_data[i].data.length; j++ ){
											 		var data_value = parseFloat(new_data[i].data[j].value);
													if (i == 0 && j == 0){
														if (new_data[i].data[j] == 0){
															max = upTolerances;
															min = 0;
														} else {
															max = data_value;
															min = data_value;
														}
													} else {
														if (data_value > max){
															max = data_value;
														}
														if (data_value < min){
															min = data_value;
														}
													}
												}
											 }
											 if (max < upTolerances){
											 	max = upTolerances;
											 } else {
											 	max = max;
											 }
											 if (min > downTolerances){
											 	min = downTolerances;
											 } else {
											 	min = min;
											 }
											 option.yAxis[0].max = max;
											 option.yAxis[0].min = min;
											 
											 setTimeout(chartObj.setOption(option), 500);
											 // chartObj.setOption(option);
										 }
									 }
								 });

							 });

							// 工序数据超差报警
							var processAlterControlArray = $("canvas[data-procedurepropertyid]");
				            $.each(processAlterControlArray, function(idx,can_){
				            	var colorArray = ["green", "white"];
				            	var dataProductid = $(can_).attr("data-productid");
				            	var dataProductprocedureid = $(can_).attr("data-productprocedureid");
				            	var dataProcedurepropertyid = $(can_).attr("data-procedurepropertyid");
				            	// 判断是否为同一产品
			            		if(dataProductid == item.productId) {
					            	// 和MQ传递过来的数据进行产品，工序，工序属性的匹配判断判断
					            	$.each(item.procedureProperty,function(idx,item){
//					            		if(dataProductprocedureid == item.procedurePropertyId) {
//					            			if ("1" == item.status) {
//					            				colorArray = ["red", "white"];
//					            				return;
//					            			} else {
//					            				return;
//					            			}
//					            		}
				            			if(dataProductprocedureid == item.mesProductProcedureId) {
				            				if(dataProcedurepropertyid == item.procedurePropertyId) {
						            			if ("1" == item.status) {
						            				colorArray = ["red", "white"];
						            				return;
						            			} else {
						            				return;
						            			}
				            				}
				            			}
					            	});
			            		}

								var arcId = $(can_).attr("id");
								var dataType = $("#"+arcId).prev().attr("data-type");
								var c = document.getElementById(arcId);
								var ctx=c.getContext("2d");
								ctx.fillStyle= "red";
								ctx.fill();
								if(dataType == 'ArcVirtual'){
								var flag = true;
		                        var width = c.width;
		                        var height = c.height;
		                        $("#"+arcId).attr("data-width",width);
		                        $("#"+arcId).attr("data-height",height);
		                        $("#"+arcId).attr("data-fillStyle", colorArray[0]);
								var mark = setInterval(function(){
									var change_width,change_height;
									if(flag){
										change_width = width*1;
										change_height = height*1;
										// $("#"+arcId).attr("data-fillStyle",item.colorCode.colorcode.colorRgba(0.6));
										$("#"+arcId).attr("data-fillStyle", colorArray[0]);
										flag = false;
									} else{
										change_width = width;
										change_height = height;
										$("#"+arcId).attr("data-fillStyle", colorArray[1]);
										flag = true;
									} 
		                        	ctx.clearRect(0, 0, change_width, change_width);
									c.width=change_width;
									c.height=change_height;
									if(dataType =='ArcVirtual'){
			                          	storageManager.renderArc(
			                          			c,
			                          			{
			                          				lineWidth:$("#"+arcId).attr("data-lineWidth"),
			                          				strokeStyle:$("#"+arcId).attr("data-strokeStyle"),
			                          				fillStyle:$("#"+arcId).attr("data-fillStyle")
			                          			});
									}
								},800);	
								var markObj = {};
								markObj[mark] = arcId;
								interval__.push(markObj);
								}
								
							
				            });
						}
					});
				});
			// console.log("监控各个画面元素组装----------------结束" + new Date());
		 }