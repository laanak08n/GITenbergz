package com.agileai.hr.service.demo;

import com.agileai.hotweb.ws.BaseWebService;
import javax.jws.WebService;

@WebService
public class HelloWorldImpl extends BaseWebService implements HelloWorld {
	public String sayHi(String theGirlName){
		String result = null;
		result = "Hello " + theGirlName + " !";
		return result;
	}
}
