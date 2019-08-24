	    // 全屏动作
	    function fullScreen(){
	    	isFullScreen = true;
	    	$("#sidebar").hide();
	    	$("header").hide();
	    	$("#container").find(".removeble").removeClass("main-content");
	    	$("#container").find(".breadcrumb").hide();
	    	$("#container").find(".searchBar").hide();
	    	//$("#minScreen").show();
	    	 $("#goBack").parent().show();
	    	$(".monitor_content").css("margin-top","0");
	    	//console.log($(window).height());
	    	$("#monitorPainter").css('height',$(window).height() - 20);
	    	$("#monitorPainter").css('width',$(window).width());
	    	// $("#monitorPainter").css("overflow-x","auto");
	    	$(".main-wrap").css({"top":"0px","left":"0px","overflow":"auto"});
	    	$(".main-body").css("padding","0 0 0 0");
	    }
	    function minScreen(){
	    	isFullScreen = false;
	    	$("#sidebar").show();
	    	$("header").show();
	    	$("#container").find(".removeble").addClass("main-content");
	    	$("#container").find(".breadcrumb").show();
	    	$("#container").find(".searchBar").show();
	    	$("#minScreen").hide();
	    	$(".main-wrap").css({"top":"40px","left":"15px"});
	    	$(".main-body").css("padding","15px 15px 15px 15px");
	    	$(".monitor_content").css("margin-top","20px");
	    	console.log($(window).height());
	    	console.log($(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-100);
	    	$("#monitor_Content").height($(window).height()-$(".header").height()-$(".breadcrumb").height()-$(".search_header").height()-$(".form-inline").height()-100);
	    	//$(".monitor_content").css({'max-height':($(window).height() - $(".searchBar").height() - parseFloat($(".searchBar").css("margin-bottom")) - parseFloat($(".main-body").css("padding")) - $(".breadcrumb").height() - $(".header").height() - 10)});
	    }
	    // 此方法无处调用
	 	function doChartsAjax(obj){
	 		$.ajax({
				url: contextPath + "/procedureMonitor/getJson_ajax",
				dataType:"JSON",
				type:"POST",
				data:{"monitors":obj},
				success:function(data){
					$.each(data,function(k,v){
  						var $chart = $("#"+ k +"").find('.chart-container');
  						//var opt = renderManager.generateChartsData(v.chartsType,v);
  						echarts.getInstanceByDom($chart[0]).setOption(JSON.parse(v));
  					});
				}
			});
	 	}
	 	
	 	function delMonitor(){
	 		if($("#monitorPainterList").find("option").length != 0 ){
		 		swal({
					title: "您正在删除监控页面！！！！！",
					type: "warning",
					showCancelButton: true,
					confirmButtonColor: '#DD6B55',
					confirmButtonText: '确认',
					cancelButtonText: "取消",
					closeOnConfirm: false
				},
				function(isConfirm){
					if(isConfirm){
			 			var id = $("#monitorPainterList").val();
			 			$.ajax({
							url: contextPath + "/procedureMonitor/delMonitor",
							dataType:"JSON",
							type:"POST",
							data:{"id":id},
							success:function(data){
								if(data.success == 0){
									swal("删除成功");
									$("#monitorPainterList").find("option[value='"+id+"']").remove();
									if($("#monitorPainterList").find("option").length != 0){
										$("#monitorPainterList").trigger("change");
									}else{
										$("#monitor_Content").empty();
										 $('#startMonitor').attr("disabled",'true');
									        $('#stopMonitor').attr("disabled",'true');
										$("#edit").attr('disabled',"true");
										 $("#del").attr('disabled',"true");
									}
								}else if(data.success == -1 ){
									//swal("您没有权限删除这个监控");
									swal({
										  title: "警告!",
										  text: "您没有权限删除这个监控",
										  imageUrl: contextPath + "/styles/img/emoji/kb-emoji-U+E107.png"
										});
								}else if(data.success == -2 ){
									//swal("还有人在看这个监控呐。。。。");
									swal({
										  title: "警告!",
										  text: "还有人在看这个监控呐",
										  imageUrl: contextPath + "/styles/img/emoji/kb-emoji-U+E107.png"
										});
								}
							}
						});
					}else{
						//$(item).attr("href",url);
					}
				});
	 			
	 			
	 			
	 		}else{
	 			swal("没有可以删除的监控");
	 		}
			
		}

		function monitorIndex(){
			if($("#currentUserId").val()!=$("#founderId").val()){
				swal("您没有权限修改这个页面！");
				return;
			}
			if($("#monitorPainterList").find("option").length != 0 ){
				swal({
					title: "您正在进入监控设计页面！！！！！",
					type: "warning",
					showCancelButton: true,
					confirmButtonColor: '#DD6B55',
					confirmButtonText: '确认',
					cancelButtonText: "取消",
					closeOnConfirm: false
				},
				function(isConfirm){
					if(isConfirm){
						var localObj = JSON.parse(localStorage.getItem("chartsData_"+$("#monitorPainterList").val()));
						localStorage.setItem("chartsData",JSON.stringify(localObj));
						var spcObj = JSON.parse(localStorage.getItem("spcData_"+$("#monitorPainterList").val()));
						localStorage.setItem("spcData",JSON.stringify(spcObj));
						var spcAnalysisobj = JSON.parse(localStorage.getItem("spcAnalysisData_"+$("#monitorPainterList").val()));
						localStorage.setItem("spcAnalysisData",JSON.stringify(spcAnalysisobj));

						window.location.href= contextPath + "/procedureMonitor/monitorIndex?id="+$("#monitorPainterList").val();
					}else{
						//$(item).attr("href",url);
					}
				});
				
			}
		}