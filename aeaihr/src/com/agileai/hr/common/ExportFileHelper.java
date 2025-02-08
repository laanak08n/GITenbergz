package com.agileai.hr.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.agileai.hotweb.common.TemplateHelper;
import com.agileai.util.IOUtil;
import com.lowagie.text.pdf.BaseFont;

public class ExportFileHelper {
	
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	
	public ExportFileHelper(HttpServletRequest request,HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ByteArrayInputStream buildHtml4Doc(String templateDir,String templateFileName,HashMap model){
		ByteArrayInputStream bais = null;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		TemplateHelper templateHelper = new TemplateHelper();
		String templateFilePath = "/"+templateFileName;
		
		HashMap modelMap = new HashMap();
		modelMap.put("model", model);
		
		templateHelper.generate(templateDir,templateFilePath, byteArrayOutputStream,modelMap);
		bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		
		return bais;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buildHtml4Pdf(String templateDir,String templateFileName,HashMap model) throws IOException{
		String result = null;
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		TemplateHelper templateHelper = new TemplateHelper();
		String templateFilePath = "/"+templateFileName;
		
		HashMap modelMap = new HashMap();
		modelMap.put("model", model);
		
		templateHelper.generate(templateDir,templateFilePath, byteArrayOutputStream,modelMap);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		
		ByteArrayOutputStream htmlOutStream = new ByteArrayOutputStream();
		IOUtil.copy(bais, htmlOutStream);
		IOUtils.closeQuietly(bais);
		
		result = new String(htmlOutStream.toByteArray(),"utf-8");
		
		return result;
	}
	
	public void exportWord(InputStream bais,String exportFileName) throws IOException{
		setReponse(exportFileName);
		
		POIFSFileSystem poifs = new POIFSFileSystem();
		DirectoryEntry directory = poifs.getRoot();
		directory.createDocument("WordDocument", bais);
		
		OutputStream ostream = response.getOutputStream();
		
		poifs.writeFilesystem(ostream);
		bais.close();
		
		ostream.flush();
		ostream.close();
	}
	
	public void exportPdf(String inputFileContent, String exportFileName) throws Exception {
		setReponse(exportFileName);
		
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(inputFileContent);
		
		// 解决中文支持问题
		ITextFontResolver fontResolver = renderer.getFontResolver();
		String fontPath = getFontPath();
		fontResolver.addFont(fontPath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		
		//解决图片的相对路径问题
		//renderer.getSharedContext().setBaseURL("file:/D:/");
		renderer.layout();
		OutputStream os = response.getOutputStream();
		renderer.createPDF(os);
//		os.flush();
		os.close();
	}
	
	
	protected String getFontPath(){
		String result = null;
		try {
			  URL url = Thread.currentThread().getContextClassLoader().getResource("");
			  File classesFolder = new File(url.toURI());
			  result = classesFolder.getParentFile().getAbsolutePath()
					  +File.separatorChar+"lib"+File.separatorChar+"simsun.ttc";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String geTemplateDirPath(){
		String result = null;
		try {
			  URL url = Thread.currentThread().getContextClassLoader().getResource("");
			  File classesFolder = new File(url.toURI());
			  result = classesFolder.getParentFile().getParentFile().getAbsolutePath()
					  +File.separatorChar+"templates";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private void setReponse(String fileName){
    	String agent = request.getHeader("USER-AGENT");
        try {
        	fileName= encodeFileName(fileName,agent);
   	    } catch (UnsupportedEncodingException e) {
   	    	e.printStackTrace();
   		}
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));
	}
	
	private String encodeFileName(String fileName, String agent) throws UnsupportedEncodingException{
		String codedfilename = null;
		fileName=fileName.replaceAll("\n|\r", " ").trim();
		if (null != agent && (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("Trident"))) {
			codedfilename = URLEncoder.encode(fileName, "UTF8");
		} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
			codedfilename = "=?UTF-8?B?"+(new String(Base64.encodeBase64(fileName.getBytes("UTF-8"))))+"?=";
		} else {
			codedfilename = fileName;
		}
		return codedfilename;
	}
}
