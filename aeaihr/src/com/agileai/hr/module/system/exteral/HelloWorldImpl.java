package com.agileai.hr.module.system.exteral;

import com.agileai.hotweb.ws.BaseWebService;
import javax.jws.WebService;

@WebService(serviceName="HelloWorld",portName="HelloWorldPort")
public class HelloWorldImpl extends BaseWebService implements HelloWorld {
	public String sayHi(String theGirlName){
		String result = null;
		result = "Hello " + theGirlName + " !";
		return result;
	}
}
