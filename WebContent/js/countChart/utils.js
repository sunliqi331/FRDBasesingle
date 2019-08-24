// 根据时间选择获取对应的选择的问题子
function getTimeNameByCode(code) {
	var nm = "";
	if("1" == code) {
		nm = "本日";
	} else if("2" == code) {
		nm = "本周";
	} else if("3" == code) {
		nm = "本月";
	} else if("4" == code) {
		nm = "本年";
	} else {
		nm = "该时间段";
	}
	return nm;
}

/**
 * 空置判断
 * @param val
 * @returns
 */
function nullCheck(val) {
	if(null != val 
			&& "undefined" != typeof(val)
			&& "" != val)
		return true;
	return false;
}