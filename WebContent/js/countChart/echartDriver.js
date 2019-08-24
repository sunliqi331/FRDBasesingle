var dataDriverRuntime = [];
var dataCount = 100;// 测试数据条数
var startTime = 1525835791000; // 2018/5/9 11:16:31
// startTime = 1537232656000
var categories = ['大族激光1', '大族激光2', '大族激光3', '大族激光4'].reverse();
categories = ['大族激光3'].reverse();
var types = [{
        name: '运行',
        color: 'green'
    },
    {
        name: '开机',
        color: 'yellow'
    },
    {
        name: '待机',
        color: 'gray'
    },
    {
        name: '关机',
        color: 'gray'
    },
];
 
// Generate mock data
 
// echarts.util.each(categories, function(category, index) {
//     var baseTime = startTime;
//     for (var i = 0; i < dataCount; i++) {
//         var typeItem = types[Math.round(Math.random() * (types.length - 1))];
//         var duration = Math.round(Math.random() * 10000);
//         data.push({
//             name: typeItem.name,
//             value: [
//                 index,
//                 baseTime,
//                 baseTime += duration,
//                 duration
//             ],
//             itemStyle: {
//                 normal: {
//                     color: typeItem.color
//                 }
//             }
//         });
//         baseTime += Math.round(Math.random() * 200);
//     }
// });
 
dataDriverRuntime = [
    {name: '运行',value: [0, 1525835791000, 1525835791000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
    {name: '停机',value: [0, 1525836391000, 1525836391000 + 600000, 600000],itemStyle:{normal: {color: 'red'}}},
    {name: '运行',value: [0, 1525836991000, 1525836991000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
    {name: '停机',value: [0, 1525837591000, 1525837591000 + 600000, 600000],itemStyle:{normal: {color: 'red'}}},
    
    // value 第一个参数: 设备 index; 
    //       第二个参数: 状态的开始时间; 
    //       第三个参数: 状态的结束时间; 
    //       第四个参数: 状态的持续时间; 
    
    
    {name: '运行',value: [1, 1525835791000, 1525835791000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
    {name: '运行',value: [1, 1525836391000, 1525836391000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
    {name: '停机',value: [1, 1525836991000, 1525836991000 + 600000, 600000],itemStyle:{normal: {color: 'red'}}},
    {name: '运行',value: [1, 1525837591000, 1525837591000 + 600000, 600000],itemStyle:{normal: {color: 'green'}}},
    
    ]
 
 
function renderItem(params, api) {
    var categoryIndex = api.value(0);
    var start = api.coord([api.value(1), categoryIndex]);
    var end = api.coord([api.value(2), categoryIndex]);
    var height = api.size([0, 1])[1] * 0.6;
 
    return {
        type: 'rect',
        shape: echarts.graphic.clipRectByRect({
            x: start[0],
            y: start[1] - height / 2,
            width: end[0] - start[0],
            height: height
        }, {
            x: params.coordSys.x,
            y: params.coordSys.y,
            width: params.coordSys.width,
            height: params.coordSys.height
        }),
        style: api.style()
    };
}
 
var option8 = {
    tooltip: {
        formatter: function(params) {
            console.log(params)
            return params.marker + params.name + ': ' + params.value[3] / 1000 + ' s';
        }
    },
    title: {
        text: '',
        left: 'center'
    },
    legend: {
        data: types,
        //bottom: 30,
        top:150,
        right: 50,
        selectedMode: false,
        show:false,
    },
    dataZoom: [{
        type: 'slider',
        filterMode: 'weakFilter',
        showDataShadow: false,
        top: 200,
        height: 10,
        start: 0,
        end: 100, 
        borderColor: 'transparent',
        backgroundColor: '#e2e2e2',
        handleIcon: 'M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7v-1.2h6.6z M13.3,22H6.7v-1.2h6.6z M13.3,19.6H6.7v-1.2h6.6z', // jshint ignore:line
        handleSize: 20,
        handleStyle: {
            shadowBlur: 6,
            shadowOffsetX: 1,
            shadowOffsetY: 2,
            shadowColor: '#aaa'
        },
        labelFormatter: ''
    }, {
        type: 'inside',
        filterMode: 'weakFilter'
    }],
    grid: {
        height: 30,
        weight:1500,
        left:50,
        right:50
    },
    xAxis: {
        type: 'time',
        min: startTime,
        scale: true,
        axisLine:{       //y轴
            show:false
          },
         offset : -3
//         axisLabel: {
//             formatter: function(val) {
//                 var d1 = new Date(val);
//                 return d1.format("yyyy-MM-dd hh:mm:ss");
////                 return Math.max(0, val - startTime);
//             }
//         }
    },
    yAxis: {
        data: categories,
        show:false
    },
    series: [
        {name: types[0].name, type: 'bar', data: [], color: 'red'},
        {name: types[1].name, type: 'bar', data: [], color: 'green'},
        {name: types[2].name, type: 'bar', data: [], color: 'yellow'},
        {
        type: 'custom',
        renderItem: renderItem,
        itemStyle: {
            normal: {
                opacity: 0.8
            }
        },
        encode: {
            x: [1, 2, 3],
            y: 0,
        },
        data: dataDriverRuntime
    }]
};


Date.prototype.format = function (format) {
           var args = {
               "M+": this.getMonth() + 1,
               "d+": this.getDate(),
               "h+": this.getHours(),
               "m+": this.getMinutes(),
               "s+": this.getSeconds(),
               "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
               "S": this.getMilliseconds()
           };
           if (/(y+)/.test(format))
               format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
           for (var i in args) {
               var n = args[i];
               if (new RegExp("(" + i + ")").test(format))
                   format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
           }
           return format;
       };