var optionsForProduct = [
    // 第一个graph
    {
        // backgroundColor: '#FFFFFF',
        title: {
            //text: '产品 切割件',//-------------------------------------------------------------【需要设置】
            textStyle: {
                fontSize: 14,
                color: 'white'
            }
        },

        tooltip: { // 提示框组件
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
                type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend: {
            //data: ['切割bar', '切割line'], //----------------------------------------------【需要设置】
            textStyle: {
                fontSize: 14,
                color: 'white'
            },
            top: '18'
        },
        grid: {
            left: '3%',
            right: '5%',
            bottom: '3%',
            containLabel: true,
            show: false // 网格边框是否显示，上和右边框 
        },
        toolbox: {
            feature: {
                dataView: {
                    show: false,
                    readOnly: false
                }, // 数据试图是否在控件中显示
                //magicType: {show: true, type: ['stack', 'tiled']},
                //restore: {show: true},
                saveAsImage: {
                    show: true
                }
            }
        },
        xAxis: {
            type: 'category',
            color : 'white',
            boundaryGap: true, // 坐标轴两边留白
            splitLine: { // 网格线 x轴对应的是否显示
                show: false
            },
            axisLine : {
                lineStyle : {
                    color : 'white'
                } 
            }
            /*
            ,data: [
                '0时', '1时', '2时', '3时', '4时', '5时',
                '7时', '8时', '9时', '10时', '11时', '12时',
                '13时', '14时', '15时', '16时', '17时', '18时',
                '19时', '20时', '21时', '22时', '23时', '24时'//----------------------------------------------------【需要设置】
            ]*/
        },

        yAxis: [ // 双y坐标轴
            {
                //name: '产品产量',
                type: 'value',
                splitLine: { // 网格线 y轴对应的是否显示
                    show: false
                },
                axisLine : {
                    lineStyle : {
                        color : 'white'
                    } 
                },
                axisLabel: {
                    formatter: '{value}'
                }
            },
            {
                //name: '产品产量比例（%）',
                //nameLocation: 'start',
                splitLine: { // 网格线 y轴对应的是否显示
                    show: false
                },
                //min: 0,
                //max: 300, // growing rate upper limit
                type: 'value',
                //top:10,
                inverse: false,
                axisLine: {
                    lineStyle: {
                        color: 'white'
                    }
                }
            }
        ],	

        series: [
            {
                //name: '切割bar',
                type: 'bar',
                //color: '#00BFFF',
                //stack: '总量',
                markPoint: {
                    data: [
                        {
                            type: 'max',
                            name: '最大值'
                        },
                        {
                            type: 'min',
                            name: '最小值'
                        }
                    ]
                },
                markLine: {
                    lineStyle : {
                        color: 'white'
                    },
                    data: [{
                        type: 'average',
                        name: '平均值'
                    }]
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        color: 'white'
                    }
                }
                /*
                ,data: [
                    1741.9, 977, 1742.2, 1431.1, 1636.2,
                    1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1,
                    1741.9, 977, 1742.2, 1431.1, 1636.2,
                    1447, 1711.7, 1921.2, 2609.6, 3332.6, 3647.3, 2498.1//--------------------------------------【需要设置】
                ] */
            },
            {
                //name: '切割line',
                type: 'line',
                yAxisIndex: 1, // yAxisIndex 1 表示第二个y轴，默认为0
                //color: '#FFD700',
                //stack: '总量',
                markPoint: {
                    data: [{
                            type: 'max',
                            name: '最大值'
                        },
                        //{type : 'min', name : '最小值'}
                    ]
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top',
                        color: 'white'
                    }
                }
                /*
                ,data: [
                    49.8, 19, 68.9, 261.6, 212.6, 250.1,
                    131.1, 92.1, 77.7, 38.1, 75.5, 99.7,
                    49.8, 19, 68.9, 261.6, 212.6, 250.1,
                    131.1, 92.1, 77.7, 38.1, 75.5, 99.7//------------------------------------------------------【需要设置】
                ] */
            }
        ]
    },

    //第4个graph
    {
        // backgroundColor: '#FFFFFF',
        title: {
            text: 'Cargo Structure Percentage',//---------------------------------------------------------------【需要设置】
            subtext: '2016',//----------------------------------------------------------------------------------【需要设置】
            left: 'center',
            textStyle: {
                color: 'white'
            },
            subtextStyle: {
                fontSize: 18,
                color: 'white'
            }
        },
        toolbox: {
            feature: {
                dataView: {
                    show: false,
                    readOnly: false
                }, // 数据试图是否在控件中显示
                //magicType: {show: true, type: ['stack', 'tiled']},
                //restore: {show: true},
                saveAsImage: {
                    show: true
                }
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            // orient: 'vertical',
            // top: 'middle',
            bottom: 20,
            left: 'center',
            data: ['直达', '中转', '联程', '邮件'],//----------------------------------------------------------------【需要设置】
            show: false // legend 不显示
        },
        series: [
        {
            name: 'Cargo Source',//-----------------------------------------------------------------------------【需要设置】
            type: 'pie',
            avoidLabelOverlap: false,
            radius: '50%',
            center: ['50%', '58%'],
            selectedMode: 'single',
            label: {
                normal: {
                    show: true,
                    textStyle: {
                        fontSize: '10',
                        //fontWeight: 'bold'
                    },
                    formatter: '{b} : {d}%',
                    position: 'outer'
                },
                emphasis: {
                    show: true,
                    textStyle: {
                        fontSize: '30',
                        fontWeight: 'bold'
                    }
                }
            },
            labelLine: {
                normal: {
                    show: true
                }
            },
            data: [//-------------------------------------------------------------------------------------------【需要设置】
                {
                    name: '直达',
                    value: 61.8
                },
                {
                    name: '联程',
                    value: 13.2
                },
                {
                    name: '中转',
                    value: 24.2
                },
                {
                    name: '邮件',
                    value: 0.8
                }
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }]
    },

    //   visualMap: {                          # 旁边会有 视觉映射组件
    //     type: 'continuous',
    //   dimension: 1,
    // text: ['High', 'Low'],
    //        inverse: true,
    //      itemHeight: 200,
    //    calculable: true,
    //  min: -2,
    //        max: 6,
    //        top: 60,
    //      left: 10,
    //    inRange: {
    //      colorLightness: [0.4, 0.8]
    //},
    //        outOfRange: {
    //          color: '#bbb'
    //    },
    //  controller: {
    //    inRange: {
    //      color: '#01949B'
    //}
    //    }
    //},


];