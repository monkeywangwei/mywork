/*
 * Create Date: 2007-4-28
 */
package com.aia.eservice.srvcore.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Michael Yang, CITO-MIS, AIA. NT Account: NSNP200
 * Class Description:
 * 
 * Revise History:
 * 
 */
public class XML2ObjectParser
{
	Document document= null;
	HashMap elementTypeInList=null;
	
	/**
	 * Initialize a parser with the xml filename
	 * @param fileName
	 */
	public XML2ObjectParser(String fileName)
	{
		super();
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder= factory.newDocumentBuilder();
			document=builder.parse(fileName);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		document.normalize();
	}
	/**
	 * Initialize a parser with the xml InputStream
	 * @param ins
	 */
	public XML2ObjectParser(InputStream ins)
	{
		super();
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder= factory.newDocumentBuilder();
			document=builder.parse(ins);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		document.normalize();
	}
	/**
	 * Initialize a parser with an xml string. Encoding should be assigned
	 * @param xmlStrBuff
	 */
	public XML2ObjectParser(String xmlStrBuff,String encoding)
	{
		super();
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		ByteArrayInputStream bains;
		if(encoding==null||encoding.length()==0)
		{
			encoding="utf-8";
		}
		try
		{
			bains = new ByteArrayInputStream(xmlStrBuff.toString().getBytes(encoding));
			DocumentBuilder builder= factory.newDocumentBuilder();
			document=builder.parse(bains);
			bains.close();
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		} catch (SAXException e)
		{
			e.printStackTrace();
		}
		document.normalize();
	}
	
	/**
	 * Initialize a parser with an opened xml Document
	 * @param document
	 */
	public XML2ObjectParser(Document document)
	{
		super();
		this.document=document;
		this.document.normalize();
	}
	
	public void addElementType(String listName,String className)
	{
		if(elementTypeInList==null)
		{
			elementTypeInList=new HashMap();
		}
		elementTypeInList.put(listName,className);
	}
	public String getElementClassType(String listName)
	{
		if(elementTypeInList==null)
			return null;
		else return (String)elementTypeInList.get(listName);
	}
	
	/**
	 * Parse xml to an object list according to spec. tag name and class name
	 * @param className
	 * @param tagName
	 * @return
	 */
	public List parseByTagName(String className,String tagName)
	{
		List objList=new ArrayList();
		NodeList nodeList=document.getElementsByTagName(tagName);

		for (int i= 0; i < nodeList.getLength(); i++)
		{
			Element node= (Element) nodeList.item(i);
			Object cfg=convertElement2Obj(className,node);
			if(cfg!=null)
			{
				objList.add(cfg);
			}
		}
		return objList;
	}
	/**
	 * Convert an Element to an instance created with specific POJO class name 
	 * @param className
	 * @param element
	 * @return instance of specific class name 
	 */
	public Object convertElement2Obj(String className,Element element)
	{
		Object target=null;
		try
		{
			target=Class.forName(className).newInstance();
			
			//to find out all set methods
			Method[] methods= target.getClass().getMethods();
			for(int i=0;i<methods.length;i++)
			{
				String methodName=methods[i].getName();
				if((methodName.indexOf("set")!=0))
				{
					continue;//bypass all methods whose prefix is not "set"
				}
				Class[] paramTypes=methods[i].getParameterTypes();
				if(paramTypes.length!=1)
				{
					continue;//bypass all methods which have no parameters 
				}
				
				Object attr=null;
				if(Collection.class.isAssignableFrom(paramTypes[0]))//Test the class of parameter is the class/subclass of Collection
				{
					attr=new ArrayList();
					Element listNode=(Element)element.getElementsByTagName(getAttrNameFromMethod(methodName)).item(0);
					if(listNode!=null)
					{
						String classType=getElementClassType(getAttrNameFromMethod(methodName));
						int position=classType.lastIndexOf(".");
						String tagName4EleInList= position==-1?classType:classType.substring(classType.lastIndexOf(".")+1);
						
						NodeList params=listNode.getElementsByTagName(tagName4EleInList);
						
						for(int j=0;j<params.getLength();j++)
						{
							//String value=params.item(j).getFirstChild().getNodeValue();
							Object value=convertElement2Obj(classType,(Element)(params.item(j)));
							((List)attr).add(value);
						}
						//����set����
						Object returnData=methods[i].invoke(target, new Object[]{attr});
					}
				}
				else
				{
					String nodeValue=getNodeValueByTagName(element,getAttrNameFromMethod(methodName));
					if(nodeValue!=null)
					{
						if(String.class.isAssignableFrom(paramTypes[0]))
						{
							attr=nodeValue;
						}
						else if(Boolean.class.isAssignableFrom(paramTypes[0])||paramTypes[0].getName().equalsIgnoreCase("boolean"))
						{
							attr=Boolean.valueOf(nodeValue);
						}
						else if(Long.class.isAssignableFrom(paramTypes[0])||paramTypes[0].getName().equalsIgnoreCase("long"))
						{
							attr=Long.valueOf(nodeValue);
						}
						else if(Double.class.isAssignableFrom(paramTypes[0])||paramTypes[0].getName().equalsIgnoreCase("double"))
						{
							attr=Double.valueOf(nodeValue);
						}
						else if(Integer.class.isAssignableFrom(paramTypes[0])||paramTypes[0].getName().equalsIgnoreCase("int"))
						{
							attr=Integer.valueOf(nodeValue);
						}
						else if(Float.class.isAssignableFrom(paramTypes[0])||paramTypes[0].getName().equalsIgnoreCase("float"))
						{
							attr=Float.valueOf(nodeValue);
						}
						else if(Date.class.isAssignableFrom(paramTypes[0]))
						{
							//attr=new Date(nodeValue);
							attr=null;
						}
					
						else
						{
							//attr=paramTypes[0].newInstance();
							attr=convertElement2Obj(paramTypes[0].getName(),(Element)element.getElementsByTagName(getAttrNameFromMethod(methodName)).item(0));
						}
						
						//����set����
						Object returnData=methods[i].invoke(target, new Object[]{attr});
					}
					else
					{
						attr=convertElement2Obj(paramTypes[0].getName(),(Element)element.getElementsByTagName(getAttrNameFromMethod(methodName)).item(0));
						//����set����
						Object returnData=methods[i].invoke(target, new Object[]{attr});
						
					}
				}
			}		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return target;
	}

	/**
	 * 
	 * @param menuItem
	 * @param string
	 * @return
	 */
	private static String getNodeValueByTagName(Element element, String tagName)
	{
		try
		{
			return element.getElementsByTagName(tagName).item(0).getFirstChild().getNodeValue();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * convert set method name to tag name. The name is sperated by '-'
	 * eg. getBizCtrlClassName will be converted tobiz-ctrl-class-name
	 * @param methodName
	 * @return
	 */
	private static String getAttrNameFromMethod(String methodName)
	{
		StringBuffer attriName=new StringBuffer();
		String temp=null;
		
		if(methodName.indexOf("set")==0)
		{
			temp=methodName.substring(3);
		}
		else 
			return attriName.toString();
		
		
		return temp.toString();
	}
	
	public static void main(String args[])
	{
		XML2ObjectParser paser=new XML2ObjectParser("D:\\PlayerData.xml");
		//System.out.print(paser.parseByTagName(Player.class.getName(),"Player"));
	}
}
