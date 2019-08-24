// 画面元素组装
function searchForProduct(outdata) {
	if("1" == outdata.dataflg)
		return false;
    var proSeletedTxt = outdata.productNm;
    $("#productCountTitle").text(proSeletedTxt + "合格率对比图");
    $("#productCountTitle2").text(proSeletedTxt + "各批次数据分析图");
    judgeDate();
    console.log("startDate:" + startDate);
    console.log("endDate:" + endDate);

    $("#startTimeForS").text(startDate + " - " + getNowFormatDate("1"));
    console.log(minusTime(startDate, getNowFormatDate("1"), "1"));

    var totalCount = parseInt(outdata.totalDisplay);
    var okCount = parseInt(outdata.okDisplay);
    if("undefined" == typeof(outdata.totalDisplay) || 0 === totalCount){
       $("#productCountAreaFontTotal").text("--")
    } else {
    	$("#productCountAreaFontTotal").text(totalCount)
    }
    if("undefined" == typeof(outdata.totalDisplay) || 0 === okCount) {
        $("#productCountAreaFontOk").text("--")
    } else {
        $("#productCountAreaFontOk").text(okCount)
    }

    // 产量饼 图
    var maindata1 = echarts.init(document.getElementById('mainData1'));
    maindata1.clear();
    if("undefined" == typeof(outdata.totalDisplay) || 0 === totalCount) {
        $("#productCountAreaFontImgFont").text("--");
        option.series[0].data =[
            {value: "0", name:'合格'},
            {value: "0", name:'不合格'}
        ];
    } else {
        var num = parseFloat(okCount); 
        var total = parseFloat(totalCount); 
        var coutRs = (Math.round(num / total * 10000) / 100.00 + "%");
        $("#productCountAreaFontImgFont").text(coutRs);

    	var okPercent = (Math.round(num / total * 10000) / 100.00);
    	var ngPercent = (Math.round((total - num) / total * 10000) / 100.00);
        option.series[0].data =[
            {value: okPercent, name:'合格'},
            {value: ngPercent, name:'不合格'}
        ];
    }
    maindata1.setOption(option);

    // 产量柱状图
    var maindata2 = echarts.init(document.getElementById('mainData2'));
    maindata2.clear();
    var xA = JSON.parse(outdata.xAxisData);
    option2.xAxis[0].data = xA;
    option2.series[0].data = outdata.seriesData;
    maindata2.setOption(option2);

    // 产量折现图
    var maindata3 = echarts.init(document.getElementById('mainData3'));
    maindata3.clear();
    option21.xAxis[0].data = xA;
    option21.series[0].data = outdata.seriesData;
    maindata3.setOption(option21);

    // 产品产量对比
    var maindata4 = echarts.init(document.getElementById('mainData4'));
    maindata4.clear();
    option3.yAxis[0].data = xA;
    option3.yAxis[1].data = xA;
    option3.series[0].data = JSON.parse(outdata.seriesDataComMax);
    option3.series[1].data = outdata.seriesData;
    if("2" == $("#search_time").val()) {
        option3.series[0].barWidth = 45;
        option3.series[1].barWidth = 45;
    } else if("3" == $("#search_time").val()) {
        option3.series[0].barWidth = 10;
        option3.series[1].barWidth = 10;
    } else {
        option3.series[0].barWidth = 30;
        option3.series[1].barWidth = 30;
    }
    option3.title.text = proSeletedTxt + "产量对比";
    maindata4.setOption(option3);

    // 产品合格率对比
    var maindata5 = echarts.init(document.getElementById('mainData5'));
    maindata5.clear();
    option4.yAxis[0].data = xA;
    option4.yAxis[1].data = xA;
    // option4.series[0].data = JSON.parse(outdata.seriesDataComMax);
    option4.series[0].data = outdata.okPercentMaxList;
    // option4.series[1].data = outdata.seriesData;
    option4.series[1].data = outdata.percentMaxList;
    indexI2 = 0;
    arr2 = JSON.parse(outdata.seriesDataOkPercent);
    option4.title.text = proSeletedTxt + "合格率对比率";
    maindata5.setOption(option4);

    if("0" != $("#productBatchid").val()){
      $("#productCountArea3").hide();
      return true;
    } else {
      $("#productCountArea3").show();
    }

    // 产品各批次合格率饼图
    var mainData6 = echarts.init(document.getElementById('mainData6'));
    mainData6.clear();
    if("undefined" == typeof(outdata.totalDisplay) || 0 === totalCount) {
        option5.series[0].data =[
            {value: "0", name:''},
        ];
    } else {
//        var num = parseFloat(okCount);
//        var total = parseFloat(totalCount); 
//        var coutRs = (Math.round(num / total * 10000) / 100.00 + "%");
//        $("#productCountAreaFontImgFont").text(coutRs);

//    	var okPercent = (Math.round(num / total * 10000) / 100.00);
//    	var ngPercent = (Math.round((total - num) / total * 10000) / 100.00);
        // option5.series[0].data = "[" + JSON.parse(outdata.batchIdXvalues) + "]";
    	option5.legend.data = outdata.batchIdYvalues;
        option5.series[0].data = outdata.batchIdXvalues;
//    	option5.series[0].data =[
//            {value: okPercent, name:'合格'},
//            {value: ngPercent, name:'不合格'}
//        ];
    }
    option5.title.text = proSeletedTxt + "各批次产量对比图";
    mainData6.setOption(option5);

    // 产品各批次产量
    if(nullCheck(outdata.seriesDataComMaxForBatch)){
        var maindata7 = echarts.init(document.getElementById('mainData7'));
        maindata7.clear();
        option6.title.text = proSeletedTxt + "各批次产量柱状对比图";
        option6.yAxis[0].data = outdata.batchIdYvalues;
        option6.yAxis[1].data = outdata.batchIdYvalues;
        option6.series[0].data = JSON.parse(outdata.seriesDataComMaxForBatch);
        option6.series[0].itemStyle.normal.color = "#ffd8bb";
        option6.series[0].itemStyle.normal.label.textStyle = "#f19149";
        option6.series[1].itemStyle.normal.color = "#f19149";
        option6.series[1].itemStyle.normal.label.textStyle = "#f19149";
        option6.series[1].data = outdata.batchXvalueLis;// JSON.parse(outdata.seriesDataComMaxForBatch);
        indexI3 = 0;
        arr3 = outdata.batchXvalueLis;
        maindata7.setOption(option6);
    }

    return true;
}