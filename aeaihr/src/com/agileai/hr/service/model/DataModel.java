package com.agileai.hr.service.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.companyname.com/projectname/service")
public class DataModel implements Serializable{
	private static final long serialVersionUID = -195179097139582084L;
	
	private String id = null;
	private String type = null;
	private String name = null;
	private String code = null;
	
	public DataModel(){
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
