var option = {
    title : {
        show : false,
        text: '产品合格率',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        show : true,
        orient : 'vertical',
        x : 'right',
        data:['合格','不合格']
    },
    toolbox: {
        show : false,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {
                show: true, 
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        x: '25%',
                        width: '50%',
                        funnelAlign: 'left',
                        max: 1548
                    }
                }
            },
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : [
        {
            name:'产品合格率',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            itemStyle: {
                normal: {
                    color: function(params) {
                        // build a color map as your need.
                        var colorList = [
                          '#4f81bd','#c0504d'
                        ];
                        return colorList[params.dataIndex]
                    },
                    label: {
                        show: true,
                        position: 'top',
//                         formatter: '{c}' + '%'
                        formatter: '{b}'+'率 '+'{c}' + '%',
                        textStyle : {
                            fontSize : 25,
                            align: 'left'
                        }
                    }
                }
            },
            data:[
                {value:123, name:'合格'},
                {value:321, name:'不合格'}
            ]
        }
    ]
};


var option2 = {
        title : {
            show : false,
            text: '产量柱状图'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['XXX产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时',
                        '13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时','24时'],
                //设置字体倾斜  
                axisLabel: {
                    interval: 0,
                    rotate: -45,
                    //倾斜度 -90 至 90 默认为0  
                    margin: 12,
					fontSize:9
                },
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'产量',
                type:'bar',
                itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#4f81bd'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: false,
                            position: 'top',
//                             formatter: '{c}'
                            formatter: '{b}\n{c}'
                        }
                    }
                },
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'},
                        {type : 'min', name: '最小值'}
                    ]
                },
                markLine : {
                    data : [
                        {type : 'average', name: '平均值'}
                    ]
                }
            }
        ]
    };

var option21 = {
        title : {
            show : false,
            text: '产品',
            // subtext: '纯属虚构'
        },
        tooltip : {
            show : false,
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时',
                    '13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时','24时']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'产量',
                type:'line',
                itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#c0504d'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: false,
                            position: 'top',
//                             formatter: '{c}'
                            formatter: '{b}\n{c}'
                        }
                    }
                },
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100],
                markPoint : {
                    data : [
                        {type : 'max', name: '最大值'}
                    ]
                }
            }
        ]
    };

var option22 = {
        title : {
            show : false,
            text: '产量柱状图'
        },
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['XXX产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : ['1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时',
                        '13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时','24时'],
                //设置字体倾斜  
                axisLabel: {
                    interval: 0,
                    rotate: -45,
                    //倾斜度 -90 至 90 默认为0  
                    margin: 12,
					fontSize:9
                },
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'待机',
                type:'bar',
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100]
            },
            {
                name:'停机',
                type:'bar',
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100]
            }
        ]
    };

var option23 = {
        title : {
            show : false,
            text: '产品',
            // subtext: '纯属虚构'
        },
        tooltip : {
            show : false,
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data : ['1时','2时','3时','4时','5时','6时','7时','8时','9时','10时','11时','12时',
                    '13时','14时','15时','16时','17时','18时','19时','20时','21时','22时','23时','24时']
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                name:'待机',
                type:'line',
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100],
            },
            {
                name:'停机',
                type:'line',
                data:[0, 0, 30, 0, 0, 40, 0, 0, 0, 0, 20, 0, 10, 30, 0, 0, 50, 0, 0, 0, 0, 0, 0, 100],
            }
        ]
    };

var arr = ["1%","2%","3%","4%","5%","6%","7%","8%","9%","10%","11%","12%"];
var indexI = 0;
var option3 = {
        title : {
            text: '产品产量对比',
            padding: [20,0,0,65]
        },
        tooltip : {
            show : false,
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
//        dataZoom : {
//            show : false,
//            realtime : true,
//            height: 20,
//            start : 20,
//            end :100
//        },
        xAxis : [
            {
                type : 'value',
                boundaryGap : [0, 0.01],
            show: false,
            axisLine :{show:false},
            axisTick :{show:false},
            splitLine :{show:false}
            }
        ],
        yAxis : [
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            },
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                axisLabel: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            }
        ],
        series : [
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 0,
                barWidth : 20,
                itemStyle: {
                    normal: {
                        //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#d4f4fb'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: false,
                            position: 'right',
                            //formatter: '{c}'
                            // formatter: '{a}\n{b}\n{c}}'
                            formatter : function(params) {
                                var lab = arr[indexI];
                                indexI++;
                                return lab;
                            },
                            textStyle : {
                                color: '#d4f4fb'
                            }
                        }
                    }
                },
                data:[120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120]
                //data:['1001', '1001','1001','1001','1001','1001','1001','1001','1001','1001']
            },
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 1,
                barWidth : 20,
                itemStyle: {
                    normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#4cd5f4'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: true,
                            position: 'right',
                            formatter: '{c}'
                            //formatter: '{b}\n{c}'
                        }
                    }
                },
                data:[0, 20, 0, 0, 30, 20, 10, 60, 50, 0, 30, 10]
            }
        ]
    };

var option4 = {
        title : {
            text: '产品合格率对比',
            padding: [20,0,0,55]
        },
        tooltip : {
            show : false,
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'value',
                boundaryGap : [0, 0.01],
                show: false,
                axisLine :{show:false},
                axisTick :{show:false},
                splitLine :{show:false}
            }
        ],
        yAxis : [
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            },
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                axisLabel: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            }
        ],
        series : [
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 0,
                itemStyle: {
                    normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#cce7d8'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: true,
                            position: 'right',
                             //formatter: '{c}' + '%'
                            //formatter: '{b}\n{c}'
                             formatter : function(params) {
                                 var lab = arr2[indexI2];
                                 indexI2++;
                                 return lab;
                             },
                             textStyle : {
                                 color: '#00ba52',
                                 fontSize : 15,
                                 align: 'left'
                             }
                        }
                    }
                },
                data:[100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100]
            },
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 1,
                itemStyle: {
                    normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#00ba52'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: false,
                            position: 'right',
                             formatter: '{c}' + '%'
                            //formatter: '{b}\n{c}'
                        }
                    }
                },
                data:[0, 20, 0, 0, 30, 20, 10, 60, 50, 0, 30, 10]
            }
        ]
    };


var option5 = {
        title : {
            show : true,
            text: '产品各批次的产量对比图',
            x:'left'
        },
        tooltip : {
            show : false,
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            show : true,
            orient : 'vertical',
            x : 'right',
            data:['合格','不合格']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true, 
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'产品合格率',
                type:'pie',
                radius : '55%',
                //radius : ['20', '110'],
                center: ['50%', '60%'],
                //center : ['25%', '50%'],
                //roseType : 'radius',
                itemStyle: {
                    normal: {
//                        color: function(params) {
//                            var colorList = [
//                              '#4f81bd','#c0504d'
//                            ];
//                            return colorList[params.dataIndex]
//                        },
                        label: {
                            show: true,
                            position: 'top',
//                             formatter: '{c}' + '%'
                            // formatter: '{b}'+'率 '+'{c}' + '%',
                            formatter: '{c}' + '%',
                            textStyle : {
                                fontSize : 25,
                                align: 'left'
                            }
                        }
                    }
                },
                data:[
                    {value:123, name:'合格'},
                    {value:321, name:'不合格'}
                ]
            }
        ]
    };

var option6 = {
        title : {
            show : true,
            text: '产品各批次产量柱状图',
            padding: [20,0,0,65]
        },
        tooltip : {
            show : false,
            trigger: 'axis'
        },
        legend: {
            show : false,
            data:['产量']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'value',
                boundaryGap : [0, 0.01],
                show: false,
                axisLine :{show:false},
                axisTick :{show:false},
                splitLine :{show:false}
            }
        ],
        yAxis : [
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            },
            {
                type : 'category',
                data : ['12月','11月','10月','9月','8月','7月','6月','5月','4月','3月','2月','1月'],
                axisLine: {show:false},
                axisTick: {show:false},
                axisLabel: {show:false},
                splitArea: {show:false},
                splitLine: {show:false}
            }
        ],
        series : [
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 0,
                itemStyle: {
                    normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#cce7d8'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: true,
                            position: 'right',
                             //formatter: '{c}' + '%'
                            //formatter: '{b}\n{c}'
                             formatter : function(params) {
                                 var lab = arr3[indexI3];
                                 indexI3++;
                                 return lab;
                             },
                             textStyle : {
                                 color: '#00ba52',
                                 fontSize : 15,
                                 align: 'left'
                             }
                        }
                    }
                },
                data:[100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100]
            },
            {
                name:'产量',
                type:'bar',
                yAxisIndex : 1,
                itemStyle: {
                    normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#00ba52'
                            ];
                            return colorList[params.dataIndex]
                        },
                        //以下为是否显示，显示位置和显示格式的设置了
                        label: {
                            show: false,
                            position: 'right',
                             formatter: '{c}' + '%'
                            //formatter: '{b}\n{c}'
                        }
                    }
                },
                data:[0, 20, 0, 0, 30, 20, 10, 60, 50, 0, 30, 10]
            }
        ]
    };

var option7 = {
        title : {
            show : true,
            text: 'TOP5时间段',
            x:'left',
            padding: [105, 0, 0, 0],    // [5, 10, 15, 20]
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            show : true,
            orient : 'vertical',
            x : 'left',
            padding: [155, 0, 0, 0],    // [5, 10, 15, 20]
            data:['工厂A','工厂B', '工厂C']
        },
        toolbox: {
            show : false,
            feature : {
                mark : {show: true},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true, 
                    type: ['pie', 'funnel'],
                    option: {
                        funnel: {
                            x: '25%',
                            width: '50%',
                            funnelAlign: 'left',
                            max: 1548
                        }
                    }
                },
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        calculable : true,
        series : [
            {
                name:'产品合格率',
                type:'pie',
                radius : '55%',
                center: ['50%', '60%'],
                itemStyle: {
                    normal: {
                        color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              //'#4f81bd','#c0504d'
                              '#e6b600','red','green'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {
                            show: true,
                            position: 'top',
                             formatter: '{c}' + '%',
//                            formatter: '{b}'+'率 '+'{c}' + '%',
                            textStyle : {
                                fontSize : 15,
                                align: 'left'
                            }
                        }
                    }
                },
                data:[
                    {value:123, name:'合格'},
                    {value:321, name:'不合格'}
                ]
            }
        ]
    };