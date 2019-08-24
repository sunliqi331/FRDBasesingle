optionDriverGauge = {
    //backgroundColor: '#1b1b1b',
    tooltip : {
        formatter: "{a} <br/>{c} {b}",
        show:false
    },
    toolbox: {
        show : false,
        feature : {
            mark : {show: true},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    series : [
        {
            name:'速度',
            type:'gauge',
            min:0,
            max:360,
            //splitNumber:11,
            radius: '50%',
            axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                    color: [[0.09, 'lime'],[0.82, '#1e90ff'],[1, '#ff4500']],
                    width: 3,
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 1
                }
            },
            axisLabel: {            // 坐标轴小标记
                textStyle: {       // 属性lineStyle控制线条样式
                    fontWeight: 'bolder',
                    color: '#fff',
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 1,
                    fontSize:8
                }
            },
            axisTick: {            // 坐标轴小标记
                length :8,        // 属性length控制线长
                lineStyle: {       // 属性lineStyle控制线条样式
                    color: 'auto',
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 1
                }
            },
            splitLine: {           // 分隔线
                length :10,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                    width:1,
                    color: '#fff',
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 1
                }
            },
            pointer: {           // 分隔线
                shadowColor : '#fff', //默认透明
                shadowBlur: 5,
                length: '60%'
            },
            title : {
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                    fontWeight: 'bolder',
                    fontSize: 10,
                    fontStyle: 'italic',
                    color: '#fff',
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 1
                }
            },
            detail : {
                backgroundColor: 'rgba(30,144,255,0.8)',
                //borderWidth: 1,
                borderColor: '#fff',
                //shadowColor : '#fff', //默认透明
                //shadowBlur: 5,
                offsetCenter: [0, '70%'],       // x, y，单位px
                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                    fontFamily: '微软雅黑',
                    fontWeight: 'bolder',
                    color: 'white',
                    fontSize:12
                },
                show:true
            },
            data:[{value: 40, name: ''}]
        }
    ]
};

// setInterval(function (){
//     option.series[0].data[0].value = (Math.random()*100).toFixed(2) - 0;
//     myChart.setOption(option);
// },2000)