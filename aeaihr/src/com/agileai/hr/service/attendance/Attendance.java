package com.agileai.hr.service.attendance;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/rest")
public interface Attendance {
	
    @GET  
    @Path("/find-around-building/{location}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findAroundBuilding(@PathParam("location") String location);
    
    @POST
    @Path("/signIn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String signIn(String info);
    
    @GET  
    @Path("/get-signIn-info/")
    @Produces(MediaType.TEXT_PLAIN)
	public String getSignInInfo();
    
    @POST
    @Path("/signOut")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String signOut(String info);
    
    @GET  
    @Path("/get-signOut-info/")
    @Produces(MediaType.TEXT_PLAIN)
	public String getSignOutInfo();
    
    @POST
    @Path("/location")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
	public String location(String info);
    
    @GET  
    @Path("/get-location-info/")
    @Produces(MediaType.TEXT_PLAIN)
	public String getLocationInfo();
    
    @GET  
    @Path("/find-currentDay-signin-infos/")
    @Produces(MediaType.TEXT_PLAIN)
	public String findCurrentDaySigninInfos();
    
    @GET  
    @Path("/find-lastDay-signin-infos/{date}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findLastDaySigninInfos(@PathParam("date") String date);
    
    @GET  
    @Path("/find-followDay-signin-infos/{date}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findFollowDaySigninInfos(@PathParam("date") String date);
    
    @GET  
    @Path("/find-lastDay-signOut-infos/{date}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findLastDaySignOutInfos(@PathParam("date") String date);
    
    @GET  
    @Path("/find-currentDay-signOut-infos/")
    @Produces(MediaType.TEXT_PLAIN)
	public String findCurrentDaySignOutInfos();
    
    @GET  
    @Path("/find-followDay-signOut-infos/{date}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findFollowDaySignOutInfos(@PathParam("date") String date);
    
    @GET  
    @Path("/find-signLocation-infos/{userId}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findSignLocationInfos(@PathParam("userId") String userId);
    
    @GET  
    @Path("/findUserInfos/{curUserCodes}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findUserInfos(@PathParam("curUserCodes") String curUserCodes);
    
    @GET  
    @Path("/find-active-userId/{userCode}")
    @Produces(MediaType.TEXT_PLAIN)
	public String findActiveUserId(@PathParam("userCode") String userCode);

}
