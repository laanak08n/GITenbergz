package com.agileai.hr.service.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface FormSelectUtil {
	
    @GET  
    @Path("/codeList/{codeType}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findCodeList(@PathParam("codeType") String codeType);
	
    @GET  
    @Path("/userList")
    @Produces(MediaType.APPLICATION_JSON)
	public String findUserList();
}
