package com.agileai.hr.module.system.exteral.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.agileai.com/hr/system")
public class ResultStatus implements Serializable{
	private static final long serialVersionUID = 1240852137815395858L;
	private boolean success = false;
	private String errorMsg = null;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsge) {
		this.errorMsg = errorMsge;
	}
}
