console.log(contextPathJS);
console.log(seriesData);
console.log(seriesPercentListData);

document.write("<script language=javascript src='"+contextPathJS+"/js/analyse/echartProduct.js'><\/script>");
// 主方法
$(document).ready(function(){
//	console.log(lineOptionData);
    $("#maindiv").css('height',$(window).height());
    $("#maindiv").css('width',$(window).width());

    var maindata1 = echarts.init(document.getElementById('maindata1'));
    maindata1.clear();
    var obj1Text = '切割bar';
    var obj2Text = '切割line';
    var protext = "产品 切割件";
    var yAxisNm1 = "产品产量";
    var yAxisNm2 = "产品产量比例（%）";
    var xAxisData = new Array(
            '0时', '1时', '2时', '3时', '4时', '5时',
            '7时', '8时', '9时', '10时', '11时', '12时',
            '13时', '14时', '15时', '16时', '17时', '18时',
            '19时', '20时', '21时', '22时', '23时', '24时'
    );
    var seriesdata0 = new Array(
            1741.9, 977, 1742.2, 1431.1, 1636.2,
            1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1,
            1741.9, 977, 1742.2, 1431.1, 1636.2,
            1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1
    );
    seriesdata0 = seriesData;
    var seriesdata1 = new Array(
            49.8, 19, 68.9, 261.6, 212.6, 250.1,
            131.1, 92.1, 77.7, 38.1, 75.5, 99.7,
            49.8, 19, 68.9, 261.6, 212.6, 250.1,
            131.1, 92.1, 77.7, 38.1, 75.5, 99.7
    );
    seriesdata1 = seriesPercentListData;
    optionsForProduct[0].title.text = protext;
    optionsForProduct[0].legend.data = new Array(obj1Text, obj2Text);
    optionsForProduct[0].xAxis.data = xAxisData;
    optionsForProduct[0].yAxis[0].name = yAxisNm1;
    optionsForProduct[0].yAxis[1].name = yAxisNm2;

    optionsForProduct[0].series[0].data = seriesdata0;
    optionsForProduct[0].series[0].name = obj1Text;
    optionsForProduct[0].series[1].data = seriesdata1;
    optionsForProduct[0].series[1].name = obj2Text;

    maindata1.setOption(optionsForProduct[0]);

    /*
    var maindata2 = echarts.init(document.getElementById('maindata2'));
    maindata2.clear();
    maindata2.setOption(options[1]);

    var maindata3 = echarts.init(document.getElementById('maindata3'));
    maindata3.clear();
    maindata3.setOption(options[2]);
	
    var maindata4 = echarts.init(document.getElementById('maindata4'));
    maindata4.clear();
    maindata4.setOption(options[3]);

    var maindata5 = echarts.init(document.getElementById('maindata5'));
    maindata5.clear();
    maindata5.setOption(options[4]);

    var maindata6 = echarts.init(document.getElementById('maindata6'));
    maindata6.clear();
    maindata6.setOption(options[5]); 
    */
});