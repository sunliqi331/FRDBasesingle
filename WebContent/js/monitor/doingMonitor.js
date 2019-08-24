		 // 运行监控
		 var doingMonitor = function(){
			
			processArray = [];
			 $.each( $(".monitorTable-container").find("table"),function(idx,item){
				 $(item).find("tbody").empty();
			})
			// 去除【停止监控】的disabled属性
			$('#stopMonitor').removeAttr("disabled");
			// 从数据库获取的监控集合，拿到monitorId监控ID，状态并设置为0
			var driverData = {monitorId:$("#monitorPainterList").val(),status:'0'};
			var array = new Array();
			var array_p = new Array();
			// 设备监控集合
			var driverMonitor = {};
			
			var countMonitor = {};
			// 获取部件的HTML元素，拿到检测点driverId，并临时保存
			$.each($(".prop-textbox > .widget-word"),function(idx,item){
				var driverId = $(item).find("li:first").attr("class").split("_")[1];
				var _array = new Array();
				$.each($(item).find("li"),function(_idx,_item){
					var propId = $(_item).attr("class").split("_")[2];
					_array.push(propId);
				});
				driverMonitor[driverId] = _array
			 });
			 var productMonitor = {};
			 // 遍历监控talbe的内容，并保存到productMonitor结合中。
			$.each($(".monitorTable-container").find("table"),function(idx,item){
				 //var productMonitor = {};
				 var _array = new Array();
				 var k = "";
				 $.each($(item).find("th"),function(i,itm){
					 var name = $(itm).attr("data-value").split("-")[0];
					 var value = $(itm).attr("data-value").split("-")[1];
					
					 if(name == 'mesProcedureProperties'){
						 _array.push(value);
					 }else{
						 if(k == "" && name == 'product'){
							 k += value;
						 }else if(name == 'procedure'){
							 k += "-"+value
						 }
					 }
				 })
				 productMonitor[k] = _array
			});

			 // 便利VIEW中的所有文本框内容
			 $.each($(".view > .textbox"),function(idx,item){
			 	 // 设备ID
				 var driverId = $(item).attr("data-driverId");
				 // 网关
				 var gatewayId = $(item).attr("data-mac");
				 // 测点ID
				 var codekey = $(item).attr("data-codekey");
				 // 测点类型名称
				 var pointTypeName = $(item).attr("data-pointTypeName");
				 // 测点ID
				 var propertyId = $(item).attr("data-propertyId");
				 // 工序ID
				 var procedureId = $(item).attr("data-procedureId");
				 // 产品ID
				 var productId = $(item).attr("data-productId");
				// 监控单位
				 var units = $(item).attr("data-units");
				 // 计数时间
				 var countTime = $(item).attr("data-countTime");
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 console.log("---------pointTypeName--------" + pointTypeName);
				 
				 console.log("---------gatewayId--------" + gatewayId);
				 console.log("---------gatewayId--------" + gatewayId);
				 console.log("---------gatewayId--------" + gatewayId);
				 
				 console.log("---------codekey--------" + codekey);
				 console.log("---------codekey--------" + codekey);
				 console.log("---------codekey--------" + codekey);

				 if(pointTypeName != undefined && ""!=pointTypeName){
					 if(pointTypeName == 'P_PROCEDURE'){
					 	 // 过滤产线监控中的【测点ID+工序ID】,
						 if(productMonitor.hasOwnProperty(productId+"-"+procedureId)){
							 productMonitor[productId+"-"+procedureId].push(codekey);
						 }else{
							 var _array = new Array();
							 _array.push(codekey);
							 productMonitor[productId+"-"+procedureId] = _array;
						 }
					 	 // 当测点类型是水，煤气，电能，P计数，D计数的时候
					 }else if(pointTypeName == 'WATER' || pointTypeName == 'GAS' || pointTypeName == 'ELECTRIC' || pointTypeName == 'P_COUNT'|| pointTypeName == 'D_COUNT'){
						 var obj = {'unit':units,'countTime':countTime};
						 obj[pointTypeName] = codekey;
						 if( countMonitor.hasOwnProperty(gatewayId)){
							 countMonitor[gatewayId].push(obj);
						 }else{
							 var _array = new Array();
							 _array.push(obj);
							 countMonitor[gatewayId] = _array;
						 }
					 }else{
						// 上面以外的情况，从产线集合中，过滤某一产线ID
						 if(driverMonitor.hasOwnProperty(driverId)){
							 driverMonitor[driverId].push(codekey);
						 }else{
							 var _array = new Array();
							 _array.push(codekey);
							 driverMonitor[driverId] = _array;
						 }
					 }
				 }
			 });
			 // 遍历view中的canvas内容
			$.each($(".view > canvas"),function(idx,item){
				 // 追加产线ID的
				 var driverId = $(item).attr("data-driverId");
				 var codekey = $(item).attr("data-codekey");
				 var pointTypeName = $(item).attr("data-pointTypeName");
				 if(pointTypeName != undefined && ""!=pointTypeName){
				 	 // 测点类型为【D_STATUS】的时候
					 if(pointTypeName == 'D_STATUS'){
					 	 // 该产品ID不在产品集合中的时候
						 if(!driverMonitor.hasOwnProperty(driverId)){
							 var _array = new Array();
							 driverMonitor[driverId] = _array;
						 }
						driverMonitor[driverId].push(codekey);
					 }
				 }
			});
			// 从浏览器缓存中获取【chartsData】数据
			 var chartsData = localStorage.getItem("chartsData_"+$("#monitorPainterList").val());
			// 转换为JSON对象
			var chartsDataArray = JSON.parse(chartsData);
			// 该对象不为NULL的情况下
			if(chartsDataArray){
				 $.each(chartsDataArray,function(idx,item){
					 $.each(JSON.parse(item.data),function(_idx,_item){
					 	 // chartsData的子元素是【category：ProductionProcess】
						 if(_item.name == 'category' && _item.value == 'ProductionProcess'){
						 	 // 加入运行集合中
							 processArray.push(item);
						 } else if(_item.name == 'category' && _item.value == 'DriverStatusAnalysis') {
							 driverTimeArray.push(item);
						 }
					 });
				 });
			 }
				 //var productMonitor = {};
			 // 遍历工序集合
			$.each(processArray,function(idx,item){
				 //var productMonitor = {};
				 var _array = new Array();
				 var k = "";
				$.each(JSON.parse(item.data),function(_idx,_item){
					 // 如果是工序ID
					 if(_item.name == '_procedurePropertyIds'){
						 _array.push(_item.value);
					 }else{
						 if(_item.name == 'productId'){
							 if(k.indexOf("-") == -1){
								 k += _item.value;
							 }else{
								 k = _item.value + k;
							 }
						 }
						 if(_item.name == 'productProcedureId'){
								 k += "-"+_item.value;
						 }
						 
					 }
					 
					 /* 
					 
					 
					 var checkPoints ={};
					 if(_item.name == 'productId'){
						 productMonitor.productId = _item.value;
					 }
					 if(_item.name == 'productProcedureId'){
						 productMonitor.procedureId = _item.value;
					 }
					 if(_item.name == '_procedurePropertyIds'){
						 checkPoints.mesPointKey =  _item.value;
						 _array.push(checkPoints);
					 } */
				});
				// 产线监控内容中如含有k
				if(productMonitor.hasOwnProperty(k)){
					// 合并监控集合和工序ID
					$.merge(productMonitor[k],_array)
				}else{
					productMonitor[k] = _array;
				}
				//productMonitor.productCheckPoints = _array;
				 //array_p.push(productMonitor);
			 });
			 console.log("设备监控集合-driverMonitor：");
			 console.log(driverMonitor);
			 console.log("产线监控集合-productMonitor：");
			 console.log(productMonitor);

			 var driverMonitors = new Array();
			 $.each(driverMonitor,function(driverId,keys){
				 var checkKeys = new Array();
				 $.each(keys,function(idx,key){
					 checkKeys.push({mesPointKey:key});
				 })
				 // 手机产品ID和测点
				 driverMonitors.push({mesDriverId:driverId,driverCheckPoints:checkKeys})
			 });

			 var productMonitors = new Array();
			 $.each(productMonitor,function(pp,keys){
				 var checkKeys = new Array();
				 $.each(keys,function(idx,key){
					 checkKeys.push({mesPointKey:key});
				 })
				 // 收集产线ID及其测点
				 productMonitors.push({productId:pp.split("-")[0],procedureId:pp.split("-")[1],productCheckPoints:checkKeys})
			 });
			 // 产品/产线情报设置到产品数据Object中
			 driverData.driverMonitors = driverMonitors;
			 driverData.productMonitors = productMonitors;

			 if(0 == Object.keys(countMonitor).length) {
				 console.log("无文本框类的统计测点。");
			 }
			 console.log("getCountPoint请求count参数：" + JSON.stringify(countMonitor));
			 // 
			 interval_ = setInterval(function(){
				if(0 != Object.keys(countMonitor).length) {
			    	$.ajax({
						url: contextPath + "/procedureMonitor/getCountPoint",
						dataType:"JSON",
						type:"POST",
						data:{"countPoints":JSON.stringify(countMonitor)},
						success:function(data){
							// console.log(data);
							$.each(data,function(k,v){
								var p = $("div[data-mac='"+ k.split("_")[0] +"'][data-codekey='"+ k.split("_")[1] +"'][data-countTime='"+ k.split("_")[2] +"']").find("p");
							
								if(p.find("span").length != 0){
									p.find("span").html(v);
								}else{
									p.append("<span>"+v+"</span>");
								}
							});
						}
					});
				}
				//获取设备状态数据
			    MacStatusMonitor();
				//获取产品信息
				productInfo();
			    //获取设备最新告警数据
			    TimeSeter();
				//获取量具最新告警数据
				TimeSeterMT();
			 	},15000);
			 console.log(JSON.stringify(driverData));
			 // 向MQ发送，当前画面需要实时推送的产品和设备的测点信息
			 if(!$.isEmptyObject(driverData)){
			 	monitorClient.send("/driverMonitor/getMonitorInfo", {},JSON.stringify(driverData));
			 }
				$('#startMonitor').attr('disabled',"true");
		 };	