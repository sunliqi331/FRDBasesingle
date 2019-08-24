		// 画面初始化
		//  1.清除浏览器缓存
		//  2.获取监控内容【{contextPath}/procedureMonitor/getPainter?id="+obj.val()】
		//    A.DB中的保存的监控画面HTML放到localStorage当中
		//    B.并对【demo】层进行CSS追加
		var loadPage = function(obj){
			// 清除浏览器缓存
            cleanMonitor();
			// procedureMonitor/getPainter 获取监控内容
			$.ajax({
				url: contextPath + "/procedureMonitor/getPainter?id="+obj.val(),
				dataType:"JSON",
				type:"POST",
				success:function(data){
					/* 2019-05-15 slq spc数据 */
					if (data.id !=""){
						localStorage.setItem("monitorId", data.id);
					}
					if (data.spcData !=""){
						localStorage.setItem("spcData_"+obj.val(), data.spcData);
						localStorage.setItem("spcData", data.spcData);
					}
					if (data.spcAnalysisData !=""){
						localStorage.setItem("spcAnalysisData_"+obj.val(), data.spcAnalysisData);
						localStorage.setItem("spcAnalysisData", data.spcAnalysisData);
					}

					if(data.elementsInfo != "")
						localStorage.setItem("elementsInfo",data.elementsInfo);
					if(data.connections != "")
						localStorage.setItem("connections",data.connections);
					if(data.winId != "")
						localStorage.setItem("winId",data.winId);

					// [chartId,data]
					// [data:chartsType, name, category, productId, productProcedureId, procedurePropertyId, pointsNum, procedurePropertyIds, _procedurePropertyIds]
					if(data.chartsData != "")
						localStorage.setItem("chartsData_"+obj.val(),data.chartsData);
						localStorage.setItem("chartsData",data.chartsData);

					// [chartId,data]
					// [data:product, procedure, mesProcedureProperties, mesProcedureProperties, mesProcedureProperties, lineCount]
					if(data.monitorTableData != "")
		 				localStorage.setItem("monitorTableData",data.monitorTableData);

					// [chartId, elementDiv] elementDiv: html的内容
					if(data.components != "")
						localStorage.setItem("components",data.components);

					if(data.bindingData != "")
						localStorage.setItem("bindingData",data.bindingData);

					// container:【chartId，size，background，backgroundRepeat，background-color，backgroundPosition，backgroundSize】
					if(data.container != ""){
						localStorage.setItem("container",data.container);
						var containerData = JSON.parse(data.container);
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
								
								//$(".demo").css({width:item.size[0],height:item.size[1],background:item.background});
							});
						}
					}
					//if(data.background != "")
						//localStorage.setItem("background",data.background);

					// 把数据库中的domContent内容【html】，添加到【monitor_Content】当中
					$("#monitor_Content").html(data.domContent);

                    //2018-12-18 zq strat 监控画面load时将告警框的行数重新存在localStorage缓存中
					if(data.domContent.split(" line_")[1]){
                        var lineCount = data.domContent.split(" line_")[1].charAt(0);
                        localStorage.setItem(JSON.parse(localStorage.getItem("components"))[0].chartId + "_lineCount", lineCount);
                    }
                    //2018-12-18 zq end

					//$(".monitor_content").width($("#monitor_Content").width());
					var monitorHeight = $(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-100;
					console.log("窗体高度减去header .breadcrumb .serarch .form-inline 的结果:" + monitorHeight)
					console.log($(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-100);
					var mainContent = $("#canvas");
					mainContent.removeClass("main-content demo");
					//$("#monitor_Content").css("background",data.background)
					// 【monitor.index.js】中定义变量，把数据库中的【monitor_painter】映射为JS对象
					// 重置其中的ContainerData, ChartsData, CanvasData, NowDate, MonitorTableData对象
					storageManager.restoreContainerData();
		  			console.log("=================restoreContainerData========start");
		  			// console.log($("#monitor_Content").html());
		  			console.log("================restoreContainerData=========end");
					storageManager.restoreChartsData();
		  			console.log("=================restoreChartsData========start");
		  			// console.log($("#monitor_Content").html());
		  			console.log("================restoreChartsData=========end");
					storageManager.restoreCanvasData($(".component", $(".demo")));
		  			console.log("=================restoreCanvasData========start");
		  			// console.log($("#monitor_Content").html());
		  			console.log("================restoreCanvasData=========end");
					storageManager.restoreNowDate($(".component", $(".demo")));
		  			console.log("=================restoreNowDate========start");
		  			// console.log($("#monitor_Content").html());
		  			console.log("================restoreNowDate=========end");
					storageManager.restoreMonitorTableData();
		  			console.log("=================restoreMonitorTableData========start");
		  			// console.log($("#monitor_Content").html());
		  			console.log("================restoreMonitorTableData=========end");

					storageManager.restoreSpcData();
					console.log("=================restoreSpcData========start");
					// console.log($("#monitor_Content").html());
					console.log("================restoreSpcData=========end");

					storageManager.restoreSpcAnalysisData();
					console.log("=================restoreSpcAnalysisData========start");
					// console.log($("#monitor_Content").html());
					console.log("================restoreSpcAnalysisData=========end");

					$(".remove").remove();
					$(".ui-resizable-handle").remove();

		  			$("#monitorPainter").height($(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-100);
		  			//$(".monitor_content").height($(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-95);
		  			console.log("=========================start");
		  			console.log($("#monitor_Content").html());
		  			console.log("=========================end");
		  			$(".monitor_content").addClass("loadClass");
		  			 $.each( $("#monitor_Content").find(".monitorTable-container").find("table"),function(idx,item){
		  				var length = 0;
		  				$.each($(item).find("tbody > tr"),function(_idx,_item){
		  					length ++;
		  				});
		  				tableLine[item.id] = length;
		  			 })
		  			console.log("monitor_content中的tableline内容打印：");
		  			console.log(tableLine);
				}
			});
		 };