package com.agileai.hr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;

import com.agileai.hotweb.common.HttpRequestHelper;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.filter.HotwebUserCacher;

public class HotwebUserCasFilter implements Filter{
	
	@Override
	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    AttributePrincipal attributePrincipal =  (AttributePrincipal)httpRequest.getUserPrincipal();
	    if (attributePrincipal != null) {
	        String loginName = attributePrincipal.getName();
	        Profile profile = getProfile(httpRequest);
	        if (profile == null) {
	        	String fromIpAddress = HttpRequestHelper.getRemoteHost(httpRequest);
				String appName = httpRequest.getContextPath().substring(1);
				HotwebUserCacher userCacher = HotwebUserCacher.getInstance(appName);
				User user = userCacher.getUser(loginName);
				
				profile = new Profile(loginName,fromIpAddress,user);
				HttpSession session = httpRequest.getSession(true);
				session.setAttribute(Profile.PROFILE_KEY, profile);
	        }
	    }
	    chain.doFilter(request, response);		
	}
	
	protected Profile getProfile(HttpServletRequest request){
		Profile profile = null;
		HttpSession session = request.getSession(false);
		if (session != null){
			profile = (Profile)session.getAttribute(Profile.PROFILE_KEY);	
		}
		return profile;
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
