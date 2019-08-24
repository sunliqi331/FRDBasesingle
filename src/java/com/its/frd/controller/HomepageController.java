package com.its.frd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/index")
public class HomepageController {

	private static final String INDEX = "index";
	private static final String SHOWPRODUCT = "products";
	private static final String ABOUT = "about";
	private static final String CONTACT = "contact";
	private static final String MES = "mes";
	private static final String SPC = "spc";
	private static final String ECMALL = "ecmall";
	private static final String READ = "read";

	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String release() {
		return INDEX;
	}
	
	@RequestMapping(value="showProduct",method={RequestMethod.GET, RequestMethod.POST})
	public String showProduct() {
	    return SHOWPRODUCT;
	}
	
	@RequestMapping(value="about",method={RequestMethod.GET, RequestMethod.POST})
	public String about() {
		return ABOUT;
	}
	
	@RequestMapping(value="contact",method={RequestMethod.GET, RequestMethod.POST})
	public String contact() {
		return CONTACT;
	}
	
	@RequestMapping(value="mes",method={RequestMethod.GET, RequestMethod.POST})
	public String mes() {
		return MES;
	}
	@RequestMapping(value="spc",method={RequestMethod.GET, RequestMethod.POST})
	public String spc() {
		return SPC;
	}
	@RequestMapping(value="ecmall",method={RequestMethod.GET, RequestMethod.POST})
	public String ecmall() {
		return ECMALL;
	}
	@RequestMapping(value="read",method={RequestMethod.GET, RequestMethod.POST})
	public String read() {
		return READ;
	}
	
	
}

