package com.agileai.hr.service.bonuspenalty;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface BonusPenalty {
	
    @GET  
    @Path("/find-all-record")
    @Produces(MediaType.APPLICATION_JSON)
	public String findAllPunRecord();
    
    @POST
    @Path("/add-pun-info")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String addPunInfo(String info);
    
    @GET  
    @Path("/get-record/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	public String findAllPunRecord(@PathParam("id") String id);
}
