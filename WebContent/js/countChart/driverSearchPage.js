// 画面元素组装
function searchForDriver(outdata) {
	
	option.series[0].itemStyle.normal.label.formatter = '{c}' + '%';
	
    // 产量饼 图
    var factoryTotalCompareArea = document.getElementById('factoryTotalCompareArea');
    if(null != factoryTotalCompareArea && 'undefined' != typeof(factoryTotalCompareArea)) {
        // 产量饼 图
        var maindata1 = echarts.init(factoryTotalCompareArea);
        maindata1.clear();
        option.series[0].radius = [20, 110];
        option.series[0].center = ['25%', '50%'];
        option.series[0].roseType = 'radius';
        option.series[0].data = [
            {value:12.3, name:'工厂A'},
            {value:22.1, name:'工厂B'},
            {value:32.5, name:'工厂C'},
        ]
    	option.series[0].itemStyle.normal.color =  function(params) {
            // build a color map as your need.
            var colorList = [
                '#0098d9','#c12e34','#e6b600'
            ];
            return colorList[params.dataIndex]
        };

        option.series[0].itemStyle.normal.label.position = 'inner';
        maindata1.setOption(option);
    }


    // 产量饼 图
    var lineTotalCompareArea = document.getElementById('lineTotalCompareArea');
    if(null != lineTotalCompareArea && 'undefined' != typeof(lineTotalCompareArea)) {
        // 产量饼 图
        var maindata2 = echarts.init(lineTotalCompareArea);
        maindata2.clear();
        option.series[0].radius = [20, 110];
        option.series[0].center = ['25%', '50%'];
        option.series[0].roseType = 'radius';
        option.series[0].data = [
            {value:12.3, name:'工厂A'},
            {value:22.1, name:'工厂B'},
            {value:52.5, name:'工厂C'},
        ]

        maindata2.setOption(option);
    }

    // 产量饼 图
    var driverTotalCompareArea = document.getElementById('driverTotalCompareArea');
    if(null != driverTotalCompareArea && 'undefined' != typeof(driverTotalCompareArea)) {
        var maindata3 = echarts.init(driverTotalCompareArea);
        maindata3.clear();
        option.series[0].radius = [20, 110];
        option.series[0].center = ['25%', '50%'];
        option.series[0].roseType = 'radius';
        option.series[0].data = [
            {value:12.3, name:'工厂A'},
            {value:42.1, name:'工厂B'},
            {value:22.5, name:'工厂C'},
        ]
        maindata3.setOption(option);
    }



    // 产量折现图
    var driverEnergyLineSubArea = document.getElementById('driverEnergyLineSubArea');
    if(null != driverEnergyLineSubArea && 'undefined' != typeof(driverEnergyLineSubArea)) {
        var maindata4 = echarts.init(driverEnergyLineSubArea);
        //maindata4.clear();
        //option21.xAxis[0].data = xA;
        option21.xAxis[0].data = ['数控机床', '折弯机床', '冲压机床','数控机床', '折弯机床', '冲压机床','数控机床', '折弯机床', '冲压机床', '冲压机床'];
        //option21.series[0].data = outdata.seriesData;
        option21.series[0].data = ['20','50','20','10','30','50','70','60','50','30'];
        option21.series[0].areaStyle = {normal: {}};
        maindata4.setOption(option21);

    }

    var driverTopTimeAnalyniseArea = document.getElementById('driverTopTimeAnalyniseArea');
    if(null != driverTopTimeAnalyniseArea && 'undefined' != typeof(driverTopTimeAnalyniseArea)) {
        // 产量饼 图
        var maindata5 = echarts.init(driverTopTimeAnalyniseArea);
        maindata5.clear();
//        option.series[0].radius = [20, 110];
//        option.series[0].center = ['25%', '50%'];
//        option.series[0].roseType = 'radius';
        option7.series[0].data = [
            {value:12.3, name:'工厂A'},
            {value:22.1, name:'工厂B'},
            {value:32.5, name:'工厂C'},
        ]
    	option7.series[0].itemStyle.normal.color =  function(params) {
            // build a color map as your need.
            var colorList = [
                '#0098d9','#c12e34','#e6b600'
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
        maindata6.setOption(option2);
    }


    var driverTopTimeAnalyniseLineArea = document.getElementById('driverTopTimeAnalyniseLineArea');
    if(null != driverTopTimeAnalyniseLineArea && 'undefined' != typeof(driverTopTimeAnalyniseLineArea)) {
        // 产量折现图
        var maindata7 = echarts.init(driverTopTimeAnalyniseLineArea);
        maindata7.clear();
//        option21.xAxis[0].data = xA;
//        option21.series[0].data = outdata.seriesData;
        maindata7.setOption(option21);
    }



    return true;
}