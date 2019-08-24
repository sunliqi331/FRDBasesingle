package com.its.frd.schedule;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.its.frd.util.APIHttpClient;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${localURL}")
	private String localURL;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if(event.getApplicationContext().getParent() != null){//root application context 没有parent，他就是老大.  
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
			System.out.println("--------------------------------------------------------------------------------------------------------");
			System.out.println(new Date() + "----------------------------------FRDMES管理系统");
			System.out.println("--------------------------------------------------------------------------------------------------------");
			/*APIHttpClient ac = new APIHttpClient(localURL+"/remoteAccess/sendTemplate");
			ac.post("{}");*/
			//mesPointTemplateService.sendTemplate(mesPointsDao.findAll());
			Timer timer=new Timer();//实例化Timer类   
			timer.schedule(new TimerTask(){   
				public void run(){   
					System.out.println(localURL+"/remoteAccess/sendTemplate");
					APIHttpClient ac = new APIHttpClient(localURL+"/remoteAccess/sendTemplate");
					ac.post("{}");
					this.cancel();
				}
			},8000);
       } 
	}

}
