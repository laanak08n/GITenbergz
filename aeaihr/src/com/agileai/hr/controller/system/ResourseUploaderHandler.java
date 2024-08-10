package com.agileai.hr.controller.system;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hr.bizmoduler.system.WcmGeneralGroup8ContentManage;
import com.agileai.hotweb.controller.core.FileUploadHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;

public class ResourseUploaderHandler extends FileUploadHandler{
	
	public ResourseUploaderHandler(){
		super();
	}
	public ViewRenderer prepareDisplay(DataParam param) {
		String grpId = param.get("GRP_ID");
		WcmGeneralGroup8ContentManage resourseManage = this.lookupService(WcmGeneralGroup8ContentManage.class);
		DataParam queryParam = new DataParam("GRP_ID",grpId);
		DataRow row = resourseManage.queryTreeRecord(queryParam);
		String resTypeDesc = row.stringValue("GRP_RES_TYPE_DESC");
		String resTypeExts = row.stringValue("GRP_RES_TYPE_EXTS");
		this.setAttributes(param);
		this.setAttribute("GRP_RES_TYPE_DESC", resTypeDesc);
		this.setAttribute("GRP_RES_TYPE_EXTS", resTypeExts);
		return new LocalRenderer(getPage());
	}
	@SuppressWarnings("rawtypes")
	@PageAction
	public ViewRenderer uploadFile(DataParam param){
		String responseText = FAIL;
		try {
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload fileFileUpload = new ServletFileUpload(fac);
			fileFileUpload.setHeaderEncoding("utf-8");
			List fileList = null;
			fileList = fileFileUpload.parseRequest(request);

			Iterator it = fileList.iterator();
			String name = "";
			String fileFullPath = null;
			
			File filePath = null;
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				if (item.isFormField()){
					String fieldName = item.getFieldName();
					if (fieldName.equals("columnId")){
						String columnId = item.getString();
						resourceParam.put("GRP_ID",columnId);
						filePath = this.buildResourseSavePath(columnId);						
					}
					continue;
				}
				name = item.getName();
				
				resourceParam.put("RES_NAME",name);
				String location = resourceRelativePath + "/" + name;
				resourceParam.put("RES_LOCATION",location);
				
				fileFullPath = filePath.getAbsolutePath()+File.separator+name;
				long resourceSize = item.getSize();
				resourceParam.put("RES_SIZE",String.valueOf(resourceSize));
				String suffix = name.substring(name.lastIndexOf("."));
				resourceParam.put("RES_SUFFIX",suffix);
				resourceParam.put("RES_SHAREABLE","Y");
				File tempFile = new File(fileFullPath);
				if (!tempFile.getParentFile().exists()){
					tempFile.getParentFile().mkdirs();
				}
				item.write(tempFile);

				this.insertResourceRecord();
			}
			responseText = SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AjaxRenderer(responseText);
	}
	
	public void insertResourceRecord(){
		WcmGeneralGroup8ContentManage resourseManage = this.lookupService(WcmGeneralGroup8ContentManage.class);
		resourseManage.createtContentRecord("WcmGeneralResource", resourceParam);
		
	}	
}
