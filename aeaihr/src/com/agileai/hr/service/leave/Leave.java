package com.agileai.hr.service.leave;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface Leave {
	
    @GET  
    @Path("/find-all-record/")
    @Produces(MediaType.TEXT_PLAIN)
	public String findLeaveList();
    
    
    @POST
    @Path("/add-leave-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String addRecord(String info);
    
    @GET  
    @Path("/get-record/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String getLeaveRecord(@PathParam("id") String id);
    
    @POST
    @Path("/update-leave-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String updateLeaveInfo(String info);
    
    @GET  
    @Path("/delete-leave-info/{id}")
    @Produces(MediaType.TEXT_PLAIN)
	public String deleteLeaveRecord(@PathParam("id") String id);
    
    @GET  
    @Path("/submit-leave-info/{id}")
    @Produces(MediaType.TEXT_PLAIN)
	public String submitLeaveInfo(@PathParam("id") String id);
    
}
