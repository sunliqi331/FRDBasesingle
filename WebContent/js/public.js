/**
 * 填写手机号应用
 */
var code; //在全局 定义验证码   
function createCode() {
    code = "";
    var codeLength = 6;//验证码的长度   
    var checkCode = document.getElementById("checkCode");
    var selectChar = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');//所有候选组成验证码的字符，当然也可以用中文的   
   
    for (var i = 0; i < codeLength; i++) {
        var charIndex = Math.floor(Math.random() * 36);
        code += selectChar[charIndex];
    }
    //alert(code);
    if (checkCode) {
        checkCode.className = "code";
        checkCode.value = code;
    }
}
   
function validate() {
    var inputCode = document.getElementById("imgcode").value;
    if (inputCode.length <= 0) {
    	document.getElementById("imgcode").className="validate[required] login_input form-control";
    } else if (inputCode != code) {
    	swal("验证码输入错误！");
    createCode();//刷新验证码   
    } else {
    
    }
}
//验证邮箱
function verify(){
	if($("#eml").val() == ''){
		document.getElementById("eml").className="form-control input-medium validate[required]";
	}else{
		document.getElementById("eml").className="form-control input-medium validate[custom[email],maxSize[128]] required";
	}
}

//邮箱注册
function next(){
	window.location.href="${contextPath}/register/email";
}
/*function testphone(str) {
	return (/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/i
			.test(str));
}
*/
