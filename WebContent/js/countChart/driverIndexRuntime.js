// 画面元素组装
function searchForDriverRuntime(outdata) {
	var serviceData = outdata.serviceData;
	var sumRunTimeFont = serviceData.sumRunTimeFont;
	var sumStandByTimeFont = serviceData.sumStandByTimeFont;
	var sumStopTimeFont = serviceData.sumStopTimeFont;
	
	$("#runtimefont").text(sumRunTimeFont);
	$("#standBytimefont").text(sumStandByTimeFont);
	$("#stoptimefont").text(sumStopTimeFont);

	var sumRunTimeFontPercent = serviceData.sumRunTimeFontPercent;
	var sumStandByTotalListPercent = serviceData.sumStandByTotalListPercent;
	var sumStopByTotalListPercent = serviceData.sumStopByTotalListPercent;
	var total = parseInt(sumRunTimeFontPercent) + parseInt(sumStandByTotalListPercent) + parseInt(sumStopByTotalListPercent);

	$("#runtimePercent").text(Math.round(sumRunTimeFontPercent / total * 10000) / 100.00 + "%");
	$("#standBytimePercent").text(Math.round(sumStandByTotalListPercent / total * 10000) / 100.00 + "%");
	$("#stoptimePercent").text(Math.round(sumStopByTotalListPercent / total * 10000) / 100.00 + "%");

    var driverAnalysisRuntimeArea = document.getElementById('driverAnalysisRuntimeArea');
    if(null != driverAnalysisRuntimeArea && 'undefined' != typeof(driverAnalysisRuntimeArea)) {
        // 产量饼 图
        var maindata8 = echarts.init(driverAnalysisRuntimeArea);
        maindata8.clear();
//        var serviceData = outdata.serviceData;
        if(0 == serviceData.driverRuntimeList.length)
            return false;
        option8.series[3].data = serviceData.driverRuntimeList;
        option8.xAxis.min = serviceData.driverRuntimeList[0].value[1];
        option8.yAxis.data = outdata.driverNm;
        maindata8.setOption(option8);
    }

    var driverTopTimeAnalyniseArea2 = document.getElementById('driverTopTimeAnalyniseArea2');
    if(null != driverTopTimeAnalyniseArea2 && 'undefined' != typeof(driverTopTimeAnalyniseArea2)) {
        // 产量饼 图
        var maindata5 = echarts.init(driverTopTimeAnalyniseArea2);
        maindata5.clear();
        option7.title.text = "状态对比";
        option7.series[0].data = [
            {value:serviceData.sumRunTimeFontPercent, name:'运行时间'},
            {value:serviceData.sumStandByTotalListPercent, name:'待机时间'},
            {value:serviceData.sumStopByTotalListPercent, name:'停机时间'}
        ];
        option7.legend.data =[
          '运行时间', '待机时间','停机时间'
        ];
        maindata5.setOption(option7);
    }

    var driverTopTimeAnalyniseBarArea2 = document.getElementById('driverTopTimeAnalyniseBarArea2');
    if(null != driverTopTimeAnalyniseBarArea2 && 'undefined' != typeof(driverTopTimeAnalyniseBarArea2)) {
        // 产量柱状图
        var maindata6 = echarts.init(driverTopTimeAnalyniseBarArea2);
        maindata6.clear();
        option22.series[0].data = serviceData.standbyTimeEchartList;
        option22.series[0].name = "待机";
        option22.series[1].data = serviceData.stopTimeEchartList;
        option22.series[1].name = "停机";
        option22.title.show = true;
        option22.title.text = "设备正常状态统计";
        option22.title.y = "bottom";
        option22.title.padding = [105, 0, 0, 220];
        maindata6.setOption(option22);
    }

    var driverTopTimeAnalyniseLineArea2 = document.getElementById('driverTopTimeAnalyniseLineArea2');
    if(null != driverTopTimeAnalyniseLineArea2 && 'undefined' != typeof(driverTopTimeAnalyniseLineArea2)) {
        // 产量折现图
        var maindata7 = echarts.init(driverTopTimeAnalyniseLineArea2);
        maindata7.clear();
        option23.series[0].data = serviceData.standbyTimeEchartList;
        option23.series[0].name =  "待机";
        option23.series[1].data = serviceData.stopTimeEchartList;
        option23.series[1].name = "停机";
        option23.title.show = true;
        option23.title.text = "设备正常状态统计";
        option23.title.y = "bottom";
        option23.title.padding = [105, 0, 0, 220];
        maindata7.setOption(option23);
    }

    return true;
}