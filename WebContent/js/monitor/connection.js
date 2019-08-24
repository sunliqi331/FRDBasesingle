 // 建立监控链接
 function connectClient(){
	 monitorClient = $.monitorSocket.connect({connectUrl:contextPath + "/monitor",connectCallback:function(frame){
		if(null != connectClientStatus){
			// 清除定时器
			clearInterval(connectClientStatus);
			if(null == subscriber ){
				// 
				 subscriber = doSubscriber();
				 doingMonitor();
		     }
		 }
		 monitorClient.ws.onclose = function(event){
		 	 console.log("monitorClient的关闭时间 -- start");
			 unSubscribeMonitor();
			 monitorClient.disconnect(function(){
				 console.log("主动关闭monitor链接。");
			 });
			 console.log("丢失monitorClient链接。");
			 connectClientStatus = setInterval(connectClient,2000);
		 	 console.log("monitorClient的关闭时间 -- end");
		 }

	 }});
 };

 function connectSubscriber(){
 	 // 通信到ActiveMQ获取数据
	 monitorSubscriber = $.monitorSocket.connectRegular({connectUrl:monitorStomp,connectCallback:function(frame){
	 if(null != connectSubscriberStatus){
		 clearInterval(connectSubscriberStatus);
		 if(null == subscriber ){
		 	 // 重新开始描画监控画面
			 subscriber = doSubscriber();
			 // 开始监控
			 doingMonitor();
	     }
	 }
	 monitorSubscriber.ws.onclose = function(event){
		 unSubscribeMonitor();
		 monitorSubscriber.disconnect(function(){
			 console.log("lalalala");
		 });
		 console.log("丢失monitorSubscriber链接");
			 connectSubscriberStatus = setInterval(connectSubscriber,2000);
		 }		
	 }});
 };
 
 // 向Storm发送【SimpMessagingTemplate】 URL:/showMonitor/advise/protocal
 var unSubscribeMonitor = function(){
 	 // 设备数据 《- 监控内容的状态设置为-1 不监控
	 var driverData = {monitorId:$("#monitorPainterList").val(),status:'-1'};
	 // monitorClient.send("/driverMonitor/getMonitorInfo", {},JSON.stringify(driverData));
	 if(null != subscriber ){
	 	 // 不监控
		 subscriber.unsubscribe();
		 // 监控对象设置为空
		 subscriber = null;
     }
 };
 // 清楚浏览器缓存localStorage
 var cleanMonitor = function(){
		 //删除多个监控中的chartsData
		 // for (var key in localStorage){
		 // 	if (key.indexOf("_") != -1){
		 //  		localStorage.removeItem(key);
		 // 	}
		 // }
		 localStorage.removeItem("spcData");
		 localStorage.removeItem("spcAnalysisData");
	 	localStorage.removeItem("elementsInfo");
		localStorage.removeItem("connections");
		localStorage.removeItem("winId");
		localStorage.removeItem("chartsData");
		localStorage.removeItem("monitorTableData");
		localStorage.removeItem("components");
		localStorage.removeItem("bindingData");
		localStorage.removeItem("background");
		localStorage.removeItem("container");
 }