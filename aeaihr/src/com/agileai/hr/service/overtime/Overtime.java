package com.agileai.hr.service.overtime;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface Overtime {
	
    @GET  
    @Path("/find-all-record/")
    @Produces(MediaType.TEXT_PLAIN)
	public String findOvertimeList();
    
    
    @POST
    @Path("/add-overtime-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String addRecord(String info);
    
    @GET  
    @Path("/get-record/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getOvertimeRecord(@PathParam("id") String id);
    
    @POST
    @Path("/update-overtime-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String updateOvertimeInfo(String info);
    
    @GET  
    @Path("/submit-overtime-info/{id}")
    @Produces(MediaType.TEXT_PLAIN)
	public String submitOvertimeInfo(@PathParam("id") String id);
    
    @GET  
    @Path("/delete-overtime-info/{id}")
    @Produces(MediaType.TEXT_PLAIN)
	public String deleteOvertimeRecord(@PathParam("id") String id);
}
