    // // 网关MAC监控
    // function MacStatusMonitor(){
		// $.each(driverTimeArray,function(idx,item){
		// 	 var productMonitor = {};
		// 	 var _array = new Array();
		// 	 $.each(JSON.parse(item.data),function(_idx,_item){
		// 		 if(_item.name == 'category' && _item.value == 'ProductionProcess'){
		// 			 var chartDom = $("#"+item.chartId).find('.chart-container')[0];
		// 			 var pointNum = $("#"+item.chartId).find('.chart-container').attr("data-pointnum");
		// 			 var chartObj = echarts.getInstanceByDom(chartDom);
		// 			 if(chartObj){
		// 				 var option = chartObj.getOption();
		// 				var xAxisDataArray = option.xAxis[0].data;
		// 				 var tooltip = option.tooltip[0];
		// 				 var grid = option.grid[0];
		// 				 grid.right='9%';
		// 				 tooltip.trigger="item";
		// 				 $.each(option.series,function(__idx,__item){
		// 					 __item.smooth = false;
		// 					 __item.markPoint.symbolSize=[110,48];
		// 					 var codeKey = __item.itemStyle.formatter;
		// 					 if(!procedures[codeKey]){
		// 						 return false;
		// 					 }
		// 					 if((__item.data.length == 1 && __item.data[0] == 0) || __item.data.length >= pointNum){
		// 						 __item.data.splice(0,1);
		// 						 xAxisDataArray.splice(0,1);
		// 					 }
		// 					 var formatter = function(params, ticket, callback){
		// 			                var str = "";
		// 		                    str += params.seriesName;
		// 		                    str+= "<br />";
		// 		                    str += "测量值:"+params.value;
		// 		                    str+= "<br />";
		// 		                    str += "SN:"+procedures.sn;
		// 		                    str+= "<br />";
		// 		                    str += "时间:"+dateRangeUtil.UnixToFullDate(procedures[codeKey].checkTime,true,8);
		// 		                    return str;
		// 			            };
		// 					 var cusVal = {value:procedures[codeKey].value,label:{normal: {show: true,position: 'top'}},tooltip:{formatter:formatter}};
		// 					 __item.data.push(cusVal);
		// 					 __item.label = {
		// 						show : true,
		// 						postion: 'top',
		// 						formatter: '',
		// 						color:'#fff'
		// 					 };
		// 					 __item.symbolSize = "10";
		// 					 xAxisDataArray.push(dateRangeUtil.UnixToFullDate(procedures[codeKey].checkTime,true,8));
		// 				 });
    //
		// 				 setTimeout(chartObj.setOption(option), 500);
		// 			 }
		// 		 }
		//
		// 	 });
		//  });
    // }