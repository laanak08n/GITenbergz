package com.agileai.hr.service.salary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface Salary {
	
    @GET  
    @Path("/find-all-record/")
    @Produces(MediaType.TEXT_PLAIN)
	public String findSalaryList();
    
    @GET  
    @Path("/get-record/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getSalaryRecord(@PathParam("id") String id);
    
    @GET  
    @Path("/get-map-record/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getSalaryDistributionMapInfo(@PathParam("id") String id);
    
    @GET  
    @Path("/get-last-salay-record/")
    @Produces(MediaType.TEXT_PLAIN)
	public String getLastSalayInfo();
}
