package com.its.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用于测试与Cas集成时，当没有账号时，注册时访问
 */
@Controller
@RequestMapping("/test")
public class TestController {
	
	@RequestMapping("one")
	public String test1(){
		
		return "test1";
	}
}
