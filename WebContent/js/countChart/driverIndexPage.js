// 画面元素组装
function searchForDriver(outdata) {
    // 单一设备区域----------------------start
    var serviceData = outdata.serviceData;
    if(!nullCheck(serviceData.sumVaue)) {
        $("#driverAnalysis").hide()
        return false;
    }
    $("#driverAnalysis").show();

    //judgeDate();
    console.log("startDate:" + startDate);
    console.log("endDate:" + endDate);

    $("#startTimeForSTab3").text(startDate + " - " + getNowFormatDate("1"));
    console.log(minusTime(startDate, getNowFormatDate("1"), "2"));

    // 选择的设备
    var driverSeletedVal = jQuery("#ChooseDriver  option:selected").text();
    // 选择统计类型
    var countTypeKind = jQuery("#EnergyType  option:selected").text();

    if(nullCheck(countTypeKind)) {
        $("#countTypeTab3").text(countTypeKind);
        $(".driverWebMidTimeConst").text(countTypeKind + "合计：")
    }

    if(nullCheck(driverSeletedVal))
    $("#modelnumSelectedTab3").text(driverSeletedVal);

    $("#productNoTab3").text(outdata.driverSn);


    
    // 总能耗
//    var sumVal = serviceData.sumVaue;
//    if(null != sumVal &&0 != sumVal) {
//    	sumVal = sumVal
        $("#sumValueTab3").text(serviceData.sumVaue);
//    }

//    $("#sumValueTab3").text(serviceData.timeCount[serviceData.timeCount.length - 1]);
    
    // 单一设备区域----------------------end


//    option.series[0].itemStyle.normal.label.formatter = '{c}' + '%';
//    
//    // 产量饼 图
//    var factoryTotalCompareArea = document.getElementById('driverTopTimeAnalyniseArea');
//    if(null != factoryTotalCompareArea && 'undefined' != typeof(factoryTotalCompareArea)) {
//        // 产量饼 图
//        var maindata1 = echarts.init(factoryTotalCompareArea);
//        maindata1.clear();
//        option.series[0].radius = [20, 110];
//        option.series[0].center = ['25%', '50%'];
//        option.series[0].roseType = 'radius';
//        option.series[0].data = [
//            {value:12.3, name:'工厂A'},
//            {value:22.1, name:'工厂B'},
//            {value:32.5, name:'工厂C'},
//        ]
//        option.series[0].data = serviceData.topCount;
//        option.series[0].itemStyle.normal.color =  function(params) {
//            // build a color map as your need.
//            var colorList = [
//                '#0098d9','#c12e34','#e6b600'
//            ];
//            return colorList[params.dataIndex]
//        };
//
//        option.series[0].itemStyle.normal.label.position = 'inner';
//        maindata1.setOption(option);
//    }
//
//
//    // 产量饼 图
//    var lineTotalCompareArea = document.getElementById('lineTotalCompareArea');
//    if(null != lineTotalCompareArea && 'undefined' != typeof(lineTotalCompareArea)) {
//        // 产量饼 图
//        var maindata2 = echarts.init(lineTotalCompareArea);
//        maindata2.clear();
//        option.series[0].radius = [20, 110];
//        option.series[0].center = ['25%', '50%'];
//        option.series[0].roseType = 'radius';
//        option.series[0].data = [
//            {value:12.3, name:'工厂A'},
//            {value:22.1, name:'工厂B'},
//            {value:52.5, name:'工厂C'},
//        ]
//
//        maindata2.setOption(option);
//    }
//
//    // 产量饼 图
//    var driverTotalCompareArea = document.getElementById('driverTotalCompareArea');
//    if(null != driverTotalCompareArea && 'undefined' != typeof(driverTotalCompareArea)) {
//        var maindata3 = echarts.init(driverTotalCompareArea);
//        maindata3.clear();
//        option.series[0].radius = [20, 110];
//        option.series[0].center = ['25%', '50%'];
//        option.series[0].roseType = 'radius';
//        option.series[0].data = [
//            {value:12.3, name:'工厂A'},
//            {value:42.1, name:'工厂B'},
//            {value:22.5, name:'工厂C'},
//        ]
//        maindata3.setOption(option);
//    }
//
//
//
//    // 产量折现图
//    var driverEnergyLineSubArea = document.getElementById('driverEnergyLineSubArea');
//    if(null != driverEnergyLineSubArea && 'undefined' != typeof(driverEnergyLineSubArea)) {
//        var maindata4 = echarts.init(driverEnergyLineSubArea);
//        //maindata4.clear();
//        //option21.xAxis[0].data = xA;
//        option21.xAxis[0].data = ['数控机床', '折弯机床', '冲压机床','数控机床', '折弯机床', '冲压机床','数控机床', '折弯机床', '冲压机床', '冲压机床'];
//        //option21.series[0].data = outdata.seriesData;
//        option21.series[0].data = ['20','50','20','10','30','50','70','60','50','30'];
//        option21.series[0].areaStyle = {normal: {}};
//        maindata4.setOption(option21);
//
//    }

    var driverTopTimeAnalyniseArea = document.getElementById('driverTopTimeAnalyniseArea');
    if(null != driverTopTimeAnalyniseArea && 'undefined' != typeof(driverTopTimeAnalyniseArea)) {
        // 产量饼 图
        var maindata5 = echarts.init(driverTopTimeAnalyniseArea);
        maindata5.clear();
//        option.series[0].radius = [20, 110];
//        option.series[0].center = ['25%', '50%'];
//        option.series[0].roseType = 'radius';
        option7.series[0].data = [
            {value:"12.3", name:'工厂A'},
            {value:"22.1", name:'工厂B'},
            {value:"32.5", name:'工厂C'},
        ]
        option7.series[0].data = serviceData.topCount;
        option7.legend.data = serviceData.barLegendVal;
        option7.series[0].itemStyle.normal.color =  function(params) {
            // build a color map as your need.
            var colorList = [
                "#e6b600","#c12e34","#4cd5f4","#0098d9","#c12e34"
            ];
            return colorList[params.dataIndex]
        };

        option7.series[0].itemStyle.normal.label.position = 'inner';
        maindata5.setOption(option7);
    }

    var driverTopTimeAnalyniseBarArea = document.getElementById('driverTopTimeAnalyniseBarArea');
    if(null != driverTopTimeAnalyniseBarArea && 'undefined' != typeof(driverTopTimeAnalyniseBarArea)) {
        // 产量柱状图
        var maindata6 = echarts.init(driverTopTimeAnalyniseBarArea);
        maindata6.clear();
//        var xA = JSON.parse(outdata.xAxisData);
//        option2.xAxis[0].data = xA;
//        option2.series[0].data = outdata.seriesData;
        option2.series[0].data = serviceData.timeCount;
//        if("1" != $("#search_timeTab3").val()){
        	option2.xAxis[0].data = serviceData.timeChinese;
//        }
        if(nullCheck(countTypeKind)) {
            option2.title.show = true;
            option2.title.text = countTypeKind + "柱状图";
        }

        maindata6.setOption(option2);
    }


    var driverTopTimeAnalyniseLineArea = document.getElementById('driverTopTimeAnalyniseLineArea');
    if(null != driverTopTimeAnalyniseLineArea && 'undefined' != typeof(driverTopTimeAnalyniseLineArea)) {
        // 产量折现图
        var maindata7 = echarts.init(driverTopTimeAnalyniseLineArea);
        maindata7.clear();
//        option21.xAxis[0].data = xA;
//        option21.series[0].data = outdata.seriesData;
        option21.series[0].data = serviceData.timeCount;
//        if("1" != $("#search_timeTab3").val()){
        	option21.xAxis[0].data = serviceData.timeChinese;
//        }
        if(nullCheck(countTypeKind)) {
        	option21.title.show = true;
        	option21.title.text = countTypeKind + "折线图";
        }
        maindata7.setOption(option21);
    }

    // 能耗明细列表
    var timeCountArray = serviceData.timeCount;
    var timeChineseArray = serviceData.timeChinese;

    $("#Ulist").empty();
    $("#Ulist2").empty();
    for(var i = -1 ;i< timeCountArray.length;i++){
        if(-1 === i) {
          $("#Ulist").append("<li class='font_style_00b0f0_white' style='width: 70px;'>时间</li>");
          $("#Ulist2").append("<li class='font_style_00b0f0_white' style='width: 70px;'>耗电量</li>");
        } else {
            var hour = i + 1;
            // $("#Ulist").append("<li>" + hour + "时</li>");
            if("4" == $("#search_timeTab3").val()){
                $("#Ulist").append("<li style='width:113px !important;'>" + timeChineseArray[i] + "</li>");
                $("#Ulist2").append("<li style='width:113px !important;'>"+timeCountArray[i]+"</li>");
            } else {
                $("#Ulist").append("<li>" + timeChineseArray[i] + "</li>");
                $("#Ulist2").append("<li>"+timeCountArray[i]+"</li>");
            }

        }
    }
    if("1" == $("#search_timeTab3").val()){
        $(".de_margin_top_1").width(1136);
    } else if("2" == $("#search_timeTab3").val()){
        $(".de_margin_top_1").width(432);
    } else if("3" == $("#search_timeTab3").val()){
        $(".de_margin_top_1").width(1364);
    } else if("4" == $("#search_timeTab3").val()){
        $(".de_margin_top_1").width(1494);
    }

    return true;
}