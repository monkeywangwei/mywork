package com.aia.eservice.srvcore.utility;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JSON {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
    	objectMapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    // 将object 对象转换成 String
    public static String serialize(Object object) {
    	StringWriter writer = new StringWriter();
		try {
		    objectMapper.writeValue(writer, object);
		} catch (Exception e) {
		    e.printStackTrace();
		    try {
			writer.close();
		    } catch (IOException ex) {
			ex.printStackTrace();
		    }
		} finally {
		    try {
			writer.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		return writer.toString();
    }

    //将字符串转换成 MAP 对象
    public static Object deserialize(String json) {
    	return deserialize(json, HashMap.class);
    }

    //将string 对象转换成 objcet 对象
    public static <T> T deserialize(String json, Class<T> classed) {
		try {
			 /**
	         * 这个特性决定parser是否将允许使用非双引号属性名字， （这种形式在Javascript中被允许，但是JSON标准说明书中没有）。
	         *
	         * 注意：由于JSON标准上需要为属性名称使用双引号，所以这也是一个非标准特性，默认是false的。
	         * 同样，需要设置JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES为true，打开该特性。
	         *
	         */
			/**
	         * 该特性决定parser是否允许单引号来包住属性名称和字符串值。
	         *
	         * 注意：默认下，该属性也是关闭的。需要设置JsonParser.Feature.ALLOW_SINGLE_QUOTES为true
	         * ALLOW_SINGLE_QUOTES(false),
	         */
	       

		    ObjectMapper mapper = objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		    mapper = mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		    mapper = mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    mapper = mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		    
		   //DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES	 有时候，返回的JSON字符串中含有我们并不需要的字段，那么当对应的实体类中不含有该字段时，会抛出一个异常，告诉你有些字段没有在实体类中找到。解决办法很简单，在声明ObjectMapper之后，加上上述代码：
		    return mapper.readValue(json, classed);
		} catch (Exception e) {
			System.out.println("---------DESERIALIZE_JSON--------"+e.getMessage());
		    e.printStackTrace();
		}
		return null;
    }
    
	//将XML字符串转换成 MAP对象
    public static <T> T xmlDeserialize(String xml, Class<T> classed) {
    	net.sf.json.xml.XMLSerializer xmlSerializer = new net.sf.json.xml.XMLSerializer();
    	xmlSerializer.setRootName("REPORT");
    	net.sf.json.JSON json = xmlSerializer.read(xml);
    	return deserialize(json.toString(), classed);
    }
}
