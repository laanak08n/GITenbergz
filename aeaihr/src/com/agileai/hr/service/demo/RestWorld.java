package com.agileai.hr.service.demo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.agileai.hr.service.model.DataModel;
import com.agileai.hr.service.model.ResultStatus;


@Path("/rest")
public interface RestWorld {
	
    @GET  
    @Path("/list/{type}")
    @Produces(MediaType.APPLICATION_JSON)
	public List<DataModel> retrieveDataModels(@PathParam("type") String type);
    
    @GET
    @Path("/retrieve/{type}/{id}")  
    @Produces(MediaType.APPLICATION_JSON)
	public DataModel retrieveDataModel(@PathParam("type") String type,@PathParam("id") String id);    
    
    @POST
    @Path("/store")  
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public ResultStatus storeDataModels(List<DataModel> models);
    
    @POST  
    @Path("/modify")  
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public ResultStatus modifyDataModel(DataModel model);
}
