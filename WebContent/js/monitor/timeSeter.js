	//告警数据定时查询任务方法
	function TimeSeter(){
		//获取最新设备报警数据
		var alarmTab = $(".ALARM_SHOW");
		if(alarmTab.length != 0){
			$.each(alarmTab,function(idx,item){
				var $currentTable = $(item);
				var className = $currentTable.attr("class");	
				//检索范围
				var scopeStr = className.split(" ")[2];
				var scope = scopeStr.split("_")[0];
				var id = scopeStr.split("_")[1];
				//显示行数
				var lineStr = className.split(" ")[3];
				var line = lineStr.split("_")[1];
				var content = "";
				$.ajax({
		      	       type : "POST",
		      	       url : contextPath + "/MesAlarmShow/getDataForMonitor",
		      	       data: "scope="+scope+"&id="+id+"&line="+line,
		      	       dataType : "json",
		      	       success : function(data) {
		      	        	if(null!=data){
		      	        		//拼接表格数据
		      	        		if(data.length==0){
		      	        			return ;
		      	        		}
		      	        		//行号
		      	        		var lineNum = 0;
		      	        		$.each(data,function(index,obj){
		      	        			var row = "";
		      	        			var td = "<td>" + (++lineNum) + "</td>"
		      	        			 + "<td>" + obj.companyName + "</td>"
      	        					 + "<td>" + obj.productionLineName + "</td>"
      	        					 + "<td>" + obj.mesDriver.name + "</td>"
      	        					 + "<td>" + obj.datetime + "</td>"
      	        					 + "<td>" + obj.mesAlarmRelation.alarmType + "</td>"
      	        					 + "<td>" + obj.mesAlarmRelation.alarmCode  + "</td>"
		      	        			 + "<td width='34%' style='word-wrap:break-word;'>" + obj.mesAlarmRelation.info + "</td>";
		      	        			if("未确认" == obj.status){
		      	        				//td += "<td style='background:#c00;color:#FFF'>" + obj.status + "</td>";
		      	        				td += "<td style='color:#c00'>" + obj.status + "</td>";
		      	        			}else{
		      	        				td += "<td>" + obj.status  + "</td>";
		      	        			}
		      	        			row = "<tr>"+ td + "</tr>";
		      	        			content += row;
		      	        		    //填充表格
									$currentTable.find("tbody").empty();
									$currentTable.find("tbody").append(content);
		      	        		});
		      	        	}
		      	       },
		      		   error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
		      	});
				
			});
		}
		//填充设备报警表格结束
	}


    //量具告警数据定时查询任务方法
    function TimeSeterMT(){
        //获取最新设备报警数据
        var alarmTab = $(".ALARM_MT_SHOW ");
        if(alarmTab.length != 0){
            $.each(alarmTab,function(idx,item){
                var $currentTable = $(item);
                var className = $currentTable.attr("class");
                //检索范围
                var scopeStr = className.split(" ")[2];
                var scope = scopeStr.split("_")[0];
                var id = scopeStr.split("_")[1];
                //显示行数
                var lineStr = className.split(" ")[3];
                var line = lineStr.split("_")[1];
                var content = "";
                $.ajax({
                    type : "POST",
                    url : contextPath + "/measuringToolAlarm/getDataForMeasuringToolAlarm",
                    data: "scope="+scope+"&id="+id+"&line="+line,
                    dataType : "json",
                    success : function(data) {
                        if(null!=data){
                            //拼接表格数据
                            if(data.length==0){
                                for (i = 0; i < line; i++) {
                                    var row = "<tr><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td><td>--</td></tr>";
                                    content += row;
                                    //填充表格
                                    $currentTable.find("tbody").empty();
                                    $currentTable.find("tbody").append(content);
                                }
                            }
                            //行号
                            var lineNum = 0;
                            $.each(data,function(index,obj){
                            	var isenabled = "";
                                if("0" == obj.measuringTool.isenabled){
                                    isenabled = "启用";
                                }else if("1" == obj.measuringTool.isenabled){
                                    isenabled = "停用";
                                }else if("2" == obj.measuringTool.isenabled){
                                    isenabled = "检修";
                                }

                                var spcsiteName = "";
                                if("1" == obj.measuringTool.spcsite){
                                    spcsiteName = "SPC工作站1";
                                }else if("2" == obj.measuringTool.isenabled){
                                    spcsiteName = "SPC工作站2";
                                }

                                var row = "";
                                var td = "<td>" + (++lineNum) + "</td>"
                                    + "<td>" + obj.measuringTool.sn + "</td>"
                                    + "<td>" + obj.measuringTool.name + "</td>"
                                    + "<td>" + obj.measuringTool.type + "</td>"
                                    + "<td>" + isenabled + "</td>"
                                    + "<td>" + obj.measuringTool.mesProductline.linename + "</td>"
                                    + "<td>" + spcsiteName + "</td>"
                                    + "<td>" + obj.alarmtime + "</td>"
                                    + "<td>" + obj.endtime + "</td>"
                                    + "<td>" + obj.hours + "</td>"
                                //     + "<td width='34%' style='word-wrap:break-word;'>" + obj.mesAlarmRelation.info + "</td>";
									if("1" == obj.status){
                                        // td += "<td><font style='background:orange;color:#FFF'>" + "量具预警" + "</td>";
                                        // // td += "<td style='color:#c00'>" + "预警" + "</td>";

                                        td += "<td>" + "量具预警" + "</td>";
                                        td += "<td style='background:orange'></td>";
                                    }else{
                                        // td += "<td><font style='background:#c00;color:#FFF'>" + "量具超时报警" + "</td>";
                                        // // td += "<td style='color:#c00'>" + "报警"  + "</td>";

                                        td += "<td>" + "量具超时报警" + "</td>";
                                        td += "<td style='background:#c00'></td>";
									}
                                row = "<tr>"+ td + "</tr>";
                                content += row;
                                //填充表格
                                $currentTable.find("tbody").empty();
                                $currentTable.find("tbody").append(content);
                            });
                        }
                    },
                    error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
                });

            });
        }
        //填充设备报警表格结束
    }