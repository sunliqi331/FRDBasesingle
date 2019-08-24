package com.its.common.utils;

import com.taobao.api.TaobaoClient;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMSUtil {
	
	public static boolean SendResetPassSMS(String phone,String newPass){
		return SendSMS(phone,newPass,"密码重置","SMS_70390120");
	}
	
	//http://www.alidayu.com/admin/service/tpl wng
	public static boolean SendRegSMS(String phone,String vsgineCode){
		return SendSMS(phone,vsgineCode,"悦创智能科技","SMS_70390122");
	}
	
	private static boolean SendSMS(String phone,String code,String tag,String tmplCode){
		String url = "http://gw.api.taobao.com/router/rest";//阿里大雨的公共API地址
		String appkey = "24285441";//TOP分配给应用的AppKey
		String secret = "b760dc5c69c0bc8448431454c9c037a6";//密钥
		// TODO Auto-generated method stub
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);//初始化短信发送客户端
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();//初始化请求实例
		req.setExtend("123456");//公共回传参数，在“消息返回”中会透传回该参数
		req.setSmsType("normal");//短信类型，传入值请填写normal
		req.setSmsFreeSignName(tag);//短信签名，相当于短信发送过程中的“【阿里大于】欢迎使用阿里大于服务”。
		req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"悦创智能科技\"}");//传递参数，传参规则{"key":"value"}，验证码${code}，您正在进行身份验证，打死不要告诉别人哦！”
		req.setRecNum(phone);//电话号码
		req.setSmsTemplateCode(tmplCode);//模板代号，这个是在短信平台上进行管理的。
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//System.out.println(rsp.getBody());
		return true;
	}
	
	public static void main(String[] args) {
		SendRegSMS("18914267302","1231");
		//SendRegSMS("18552077216","1231");
	}

}
