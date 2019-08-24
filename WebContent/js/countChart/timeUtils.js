// 获取当前时间
function getNowFormatDate(kind) {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = "";
    // 当前时间
    if("1" === kind) {
        currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    } else if("2" === kind) {
    	// 本日首时间
        currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + seperator2 + "00:00:00";
    }

    return currentdate;
}
// 时间差计算
// kind: 1 产品 2 设备
function minusTime(faultDat,completeTime, kind){
	var stime = Date.parse(new Date(faultDat));
	var etime = Date.parse(new Date(completeTime));
	var usedTime = etime - stime;  //两个时间戳相差的毫秒数
	var month;
	var c = new Date(completeTime).valueOf() - new Date(faultDat).valueOf();
	c = new Date(c);
	month = c.getMonth();
	console.log(c.getFullYear() - 1970 + '年' + (c.getMonth()) + '个月' + (c.getDate()-1) + '天' + (c.getHours()-8) + '个小时' + c.getMinutes() + '分钟' + c.getSeconds() + '秒');
	var days=Math.floor(usedTime/(24*3600*1000));
	days = c.getDate()-1;
	//计算出小时数
	var leave1=usedTime%(24*3600*1000);    //计算天数后剩余的毫秒数
	var hours=Math.floor(leave1/(3600*1000));
	//hours = c.getHours()-8;
	//计算相差分钟数
	var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
	var minutes=Math.floor(leave2/(60*1000));
	minutes = c.getMinutes();
	var seconds=new Date(completeTime).getSeconds();
	seconds = c.getSeconds();
	var time = month + "月" + days + "天"+hours+"时"+minutes+"分"+seconds+"秒";

	if("1" === kind){
		$("#monthSel").text(month);
		$("#daySel").text(days);
		$("#hourSel").text(hours);
		$("#minutesSel").text(minutes);
		$("#secondsSel").text(seconds);
	} else {
		$("#monthSelTab3").text(month);
		$("#daySelTab3").text(days);
		$("#hourSelTab3").text(hours);
		$("#minutesSelTab3").text(minutes);
		$("#secondsSelTab3").text(seconds);
	}
	return time;
}
