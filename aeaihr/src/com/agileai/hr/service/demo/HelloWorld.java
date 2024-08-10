package com.agileai.hr.service.demo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWorld {
	@WebMethod
	public String sayHi(@WebParam(name="theGirlName") String theGirlName);
}
