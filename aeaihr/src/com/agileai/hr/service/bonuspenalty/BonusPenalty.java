package com.agileai.hr.service.bonuspenalty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface BonusPenalty {
	
    @GET  
    @Path("/find-all-record")
    @Produces(MediaType.APPLICATION_JSON)
	public String findAllPunRecord();
    
}
