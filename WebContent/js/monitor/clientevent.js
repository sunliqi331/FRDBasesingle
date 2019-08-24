$(document).ready(function(){
	initExportContextMenu();
	console.log(monitorStomp);
	
	// 建立和MQ的链接
	connectClient();
	// 订阅MQ的消息
	connectSubscriber();
	// 弹出窗高宽重新计算
	$(window).resize(function() {
		 //$(".monitor_content").css({'height':$(window).height() - 30});
	});
	
	// 开始监控事件
	 $("#startMonitor").click(function(){
		 // monitorClient.send("/showMonitor/monitorPage", {},JSON.stringify($("#monitorPainterList").val()));
		 // monitorClient.send("/showMonitor/monitorPage", {},$("#monitorPainterList").val());
		 // getNewAlarmInfo();
	 	 // $.contextMenu：右键菜单
	 	 // 销毁右键菜单内容
		 $.contextMenu("destroy"); //销毁右键点击事件
		 // 
		 interval = setInterval(function(){
		 	    // 【monitor.index.js】中定义变量，把数据库中的【monitor_painter】映射为JS对象
		 	    // 重置其中的ChartsData对象
		 		storageManager.restoreChartsData(true);
				storageManager.restoreSpcData(true);
				storageManager.restoreSpcAnalysisData(true);
		 },2500);
		 $("#tt").append("-"+$("#monitorPainterList").find("option:selected").text());
		 if(!$(this).attr("disabled")){
			 console.log("window.location.href url:"+window.location.href);
			 // 全屏操作
			 fullScreen();
			 // 描画监控内容
			 subscriber = doSubscriber();
			 if($("#monitorPainterList").find("option").length == 0 ){
				 return;
			 }
			 doingMonitor();
	
		 }
		 $("#maxScreen").hide();
	 }); 

	 if($("#monitorPainterList").find("option").length != 0 ){
		 	loadPage($("#monitorPainterList")); 
	 }else{
		 $('#startMonitor').attr('disabled','true');
		 $('#stopMonitor').attr('disabled',"true");
		 $("#edit").attr('disabled',"true");
		 $("#del").attr('disabled',"true");
	 }
	 
	 $("#monitorPainterList").change(function(){
		 if($("#monitorPainterList").val()=='' || $("#monitorPainterList").val()==null){
			 return;
		 }
		 // 验证改用户是否可以有权限操作，改监控画面。
		 $.ajax({
      	        type : "POST",
      	        async:false,
      	        url : contextPath + "/procedureMonitor/getUserName",
      	        data: "monitorPainterId="+$("#monitorPainterList").val(),
      	        dataType : "text", 
      	        success : function(data) {
      	        	if(data!="error"){
      	        		$("#founder").html(data.split(",")[0]);
      	        		$("#founderId").val(data.split(",")[1]);
      	        	}
      	        },
      			error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
      	 });
	    unSubscribeMonitor()
        $('#startMonitor').removeAttr("disabled");
        //$('#stopMonitor').removeAttr("disabled");
  	    loadPage($(this));
	 });
	 
	 $("#stopMonitor").click(function(){
         // 清除定时器
		 clearInterval(interval);
		 clearInterval(interval_);

		 //2018-12-25 zq start 注释
		 // clearInterval(timerId);
		 //end

		 $.each(interval__,function(idx,intr){
			 $.each(intr,function(k,v){
				 clearInterval(k);
			 });
		 });
		 if(!$(this).attr("disabled")){
		     $(this).attr("disabled",'true');
			 unSubscribeMonitor();
			 $('#startMonitor').removeAttr("disabled");
			 $('#startMonitor').removeAttr("disabled");
		 }
		 $("#maxScreen").hide();
	 });
	 
	    $("#sendTemplate").click(function(){
	    	 monitorClient.send("/driverMonitor/sendTemplate", {},"");
	    });
	    $("#screenArea").hover(function(){
			 console.log($("#container").find(".removeble").hasClass("main-content"));
			 if(!$("#container").find(".removeble").hasClass("main-content")){
			 	$("#minScreen").show();

			 }else{
				 if($("#startMonitor").attr("disabled") == 'disabled'){
					 $("#maxScreen").show();
				 }
			 }
		 },function(){
			 console.log($("#container").find(".removeble").hasClass("main-content"));
			 if(!$("#container").find(".removeble").hasClass("main-content")){
				 $("#minScreen").hide();
				
			 }else{
				 if($("#startMonitor").attr("disabled") == 'disabled'){
				 	$("#maxScreen").hide();
				 
				 }
			 }
		 });
	    $("#downarea").hover(function(){
	    	if(!$("#container").find(".removeble").hasClass("main-content")){
			 
			 	$("#down_button").show();
			 	
			 }else{
				 if($("#startMonitor").attr("disabled") == 'disabled'){
					
					 $("#down_button").show();
				 }
			 }
		 },function(){
			 console.log($("#container").find(".removeble").hasClass("main-content"));
			 if(!$("#container").find(".removeble").hasClass("main-content")){
				 
				 $("#down_button").hide();
			 }else{
				 if($("#startMonitor").attr("disabled") == 'disabled'){
			
				 	$("#down_button").hide();
				 }
			 }
		 });

	    // 监控画面最小化
	    $("#minScreen").click(function(){
	    	minScreen();
	    	initExportContextMenu();//重新开启右键点击事件
	    	//$("#maxScreen").show();
	    });
	    // 监控画面最大化
	    $("#maxScreen").click(function(){
	    	$.contextMenu("destroy"); //销毁右键点击事件
	    	fullScreen();
	    	$("#maxScreen").hide();
	    });
	    
	    var currentWindowsSize = $(window).height();
		   $(window).resize(function(e){
			   if(isFullScreen){
				   /* if($(window).height() > currentWindowsSize){
					   $("#monitor_Content").css('height',$(window).height());
				   }else{
					   $("#monitor_Content").css('height',$(window).height()-35);
				   } */
				   $(".monitor_content").css('height',$(window).height() );
			   }
		   });
	    $(document).keydown(function(e){
	    	if(e.key == 'F11' && isFullScreen){
	    		
	    	}
	    });

	 	
	 	$("#del").click(function(){
	 		delMonitor();
	 	});

	 	$("#goBack").click(function(){
			minScreen();
			$(this).parent().hide();
		});
	 	
});

//******************设置圆点闪烁**********************************

/* <script> 
	 $(document).ready(
		function test1(){
	  		var c1 = document.getElementById('c1');
	  		var cxt = c1.getContext('2d');
	  		var r = 50;
	  		var c = 1;
	  		var t1 = window.setInterval(function(){
	  			if(r==50){
	  			cxt.clearRect(100,100,500,500);
	  			cxt.fillStyle = "#FF0000";
	  			cxt.beginPath();
	  			cxt.arc(300,300,r,0,Math.PI*2,true);
	  			cxt.closePath();
	  			cxt.fill();
	  				r-=10;
	  			}else if(r==40){
	  			cxt.clearRect(100,100,500,500);
	  			cxt.fillStyle = "#4f6128";
	  			cxt.beginPath();
	  			cxt.arc(300,300,r,0,Math.PI*2,true);
	  			cxt.closePath();
	  			cxt.fill();
	  				r+=10;
	  		
	  			}
	  			
	  			
	  			
	  			console.log(r);
	  		},200);
	
	}) 
  </script> */