package com.aia.eservice.srvcore.utility;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class XmlRequestUtil {
	/**
	 * 调用POST XML字符串类型的接口
	 * 
	 * @param url
	 *            服务地址
	 * @param rootName
	 *            根节点名
	 * @param params
	 *            参数
	 * @return
	 *            返回字符串
	 * @throws Exception 
	 */
	public static String postXml(String urlStr, String rootName, Map<String, String> params) throws HttpException, IOException {
		String xmlString = "";

		//转换成XML
		xmlString = "<?xml version=\"1.0\"?>";
		xmlString += "<" + rootName + ">";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			xmlString += "<" + entry.getKey() + ">";
			xmlString += entry.getValue();
			xmlString += "</" + entry.getKey() + ">";
		}
		xmlString += "</" + rootName + ">";
		System.out.println("----------postXml----------:"+xmlString);
		return postXml(urlStr, xmlString);
	}

	/**
	 * 调用POST XML字符串类型的接口
	 * 
	 * @param url
	 *            服务地址
	 * @param rootName
	 *            根节点名
	 * @param subRootName
	 *            根的第一级子节点名
	 * @param params
	 *            参数
	 * @return
	 *            返回字符串
	 * @throws Exception 
	 */
	public static String postXml(String urlStr, String rootName, String subRootName, Map<String, String> params) throws HttpException, IOException {
		String xmlString = "";

		//转换成XML
		xmlString = "<?xml version=\"1.0\"?>";
		xmlString += "<" + rootName + ">";
		xmlString += "<" + subRootName + ">";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			xmlString += "<" + entry.getKey() + ">";
			xmlString += entry.getValue();
			xmlString += "</" + entry.getKey() + ">";
		}
		xmlString += "</" + subRootName + ">";
		xmlString += "</" + rootName + ">";
		
		return postXml(urlStr, xmlString);
	}

	/**
	 * 调用POST XML字符串类型的接口
	 * 
	 * @param url
	 *            服务地址
	 * @param xmlString
	 *            XML字符串
	 * @return
	 *            返回字符串
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String postXml(String url, String xmlBody) throws HttpException, IOException  {
		String result = "";
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		client.getHttpConnectionManager().getParams().setConnectionTimeout(15000); //通过网络与服务器建立连接的超时时间
		client.getHttpConnectionManager().getParams().setSoTimeout(60000); //Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type", "application/xml");
		
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8"); 
		if(null != xmlBody){
			method.setRequestBody(xmlBody);
		}

		try {
	    	client.executeMethod(method);
	    	result = method.getResponseBodyAsString();
	    } catch (HttpException e) {
	    	e.printStackTrace();
	        throw e; // 异常外抛
	    } catch (IOException e) {
	    	e.printStackTrace();
	        throw e; // 异常外抛
	    } finally {
	        if (null != method) {
	        	method.releaseConnection();
	        }
	    }
	    return result;
	}

}
