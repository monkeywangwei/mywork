package com.aia.eservice.common.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 功能、用途、现存BUG: 帮助实现一些通用的字符串处理
 * 
 * @author
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */
public class StringHelperTools {

	protected final static Logger logger = Logger
			.getLogger(StringHelperTools.class);

	/**
	 * null值转换
	 * 
	 * @param args
	 * @return 返回转换后数组
	 */
	public static Object[] nullToString(Object args[]) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null || "null".equals(args[i])) {
				args[i] = "";
			}
		}
		return args;
	}

	/**
	 * null值转换
	 * 
	 * @param args
	 * @return 返回转换后数组
	 */
	public static Object[] STringnullToString(Object args[]) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				args[i] = "";
			}
		}
		return args;
	}

	/**
	 * NULL字符串转换，参数对象为null时，返回空字符串
	 * 
	 * @param o
	 *            转换原对象
	 * @return 字符串
	 */
	public static String nvl(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString().trim();
	}

	/**
	 * NULL字符串转换，参数对象为null时，返回空值为0的BigDecimal对象
	 * 
	 * @param o
	 *            转换原对象
	 * @return 字符串
	 */
	public static BigDecimal nvlToBigDecimal(Object o) {
		if (o == null) {
			return new BigDecimal(0);
		} else {
			return (BigDecimal) o;
		}
	}

	/**
	 * NULL字符串转换，参数对象为null时，返回空字符串 将" " 替换为 "&nbsp;" 用于页面显示多个空格
	 * 
	 * @param o
	 *            转换原对象
	 * @return 字符串
	 */
	public static String nvlShow(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString().trim().replaceAll(" ", "&nbsp;");
	}

	/**
	 * NULL字符串转换，参数对象为null时，返回默认值
	 * 
	 * @param o
	 *            转换原对象
	 * @param res
	 *            默认值
	 * @return 字符串
	 */
	public static String nvl(Object o, String res) {
		if (o == null) {
			return res;
		}
		return o.toString().trim();
	}

	/**
	 * NULL或空字符串转换，参数对象为null或空时，返回默认值
	 * 
	 * @param o
	 *            转换原对象
	 * @param res
	 *            默认值
	 * @return 字符串
	 */
	public static String nvlHtml(Object o, String res) {
		if (o == null || o.toString().trim().equals("")) {
			return res;
		}
		return o.toString().trim();
	}

	/**
	 * 字符串替换。如果不需要用正则表达式请用此方法； 否则用String.replaceAll(String regex, String
	 * replacement)
	 * 
	 * @param text
	 *            需要被处理的字符串
	 * @param from
	 *            需要被替换掉的字符串
	 * @param to
	 *            被替换成的字符串
	 * @return 被替换后的字符串
	 * @see String#replaceAll(String, String)
	 */
	public static String replace(String text, String from, String to) {
		if (text == null || from == null || to == null) {
			return null;
		}
		StringBuffer newText = new StringBuffer(text.length() * 2);
		int pos1 = 0;
		int pos2 = text.indexOf(from);
		while (pos2 >= 0) {
			newText.append(text.substring(pos1, pos2) + to);
			pos1 = pos2 + from.length();
			pos2 = text.indexOf(from, pos1);
		}
		newText.append(text.substring(pos1, text.length()));
		return newText.toString();
	}

	/**
	 * 替换回车为br
	 * 
	 * @param text
	 *            原文本
	 * @return 替换后文本
	 */
	public static String replaceLineFeedCode(String text) {
		return replace(text, "\n", "<br>\n");
	}

	/**
	 * 替换\t为4个html空格
	 * 
	 * @param text
	 *            原文本
	 * @return 替换后文本
	 */
	public static String replaceTab24Space(String text) {
		return replace(text, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
	}

	/**
	 * 对html标签或特殊字符串编码
	 * 
	 * @param html
	 *            html代码
	 * @return String 替换后代码
	 */
	public static String replaceQuotes(String html) {

		html = replace(html, "\"", "&quot;");

		return html;
	}

	/**
	 * 对html标签或特殊字符串编码
	 * 
	 * @param html
	 *            html代码
	 * @return String 替换后代码
	 */
	public static String replaceAllQuotes(String html) {

		html = replace(html, "\"", "&quot;");
		html = replace(html, "\'", "&apos;");

		return html;
	}

	/**
	 * 对html中js中引用的字符（比如alert中）转换 单引号
	 * 
	 * @param html
	 *            html代码
	 * @return String 替换后代码
	 */
	public static String replaceJsApos(String html) {

		html = replace(html, "'", "\\'");

		return html;
	}

	/**
	 * 对html中js中引用的字符（比如alert中）转换 单引号
	 * 
	 * @param html
	 *            html代码
	 * @return String 替换后代码
	 */
	public static String replaceJsQuote(String html) {

		html = replace(html, "\"", "\\\"");

		return html;
	}

	/**
	 * 检查是否为数字。可以包含小数点，但是小数点个数不能多于一个； 可以包含负号，但是不能只有负号而没有其他数字； 不允许包含逗号
	 * 
	 * @param s
	 *            被检查的字符串
	 * @return true 表示是数字, false 表示不是数字
	 */
	public static boolean isNumber(String s) {
		boolean pointfirsttime = true;

		int i = 0;
		if (s == null || s.length() < 1) {
			return false;
		}

		boolean negative = false;

		if (s.charAt(0) == '-') {
			i++;
			negative = true;
		}

		while (i < s.length()) {
			if (!Character.isDigit(s.charAt(i))) {
				if ('.' == s.charAt(i) && pointfirsttime) {
					pointfirsttime = false;
				} else {
					return false;
				}
			}
			i++;
		}

		if (negative && (i == 1)) {
			return false;
		}

		return true;
	}

	/**
	 * 检查是否为整数。可以为负整数，但是不能只有负号而没有其他数字
	 * 
	 * @param s
	 *            被检查的字符串
	 * @return true 表示是整数, false 表示不是整数
	 */
	public static boolean isInteger(String s) {
		int i = 0;
		if (s == null || s.trim().length() < 1) {
			return false;
		}

		boolean negative = false;

		if (s.charAt(0) == '-') {
			i++;
			negative = true;
		}

		while (i < s.length()) {
			if (!Character.isDigit(s.charAt(i))) {
				return false;
			}
			i++;
		}
		if (negative && (i == 1)) {
			return false;
		}

		return true;
	}

	/**
	 * 检查是否为合法的Email
	 * 
	 * @param mail
	 *            字符串
	 * @return true 合法，false 非法
	 */
	public static boolean checkMailAddress(String mail) {
		if (mail == null) {
			return false;
		}

		String mailstr = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pn = Pattern.compile(mailstr);

		boolean b = pn.matcher(mail).matches();

		logger.info(b);

		return b;

	}

	/**
	 * 判断字符串是否为null或者trim后长度小于1
	 * 
	 * @param arg
	 *            要被判断的字符串
	 * @return true 为null或者trim后长度小于1
	 */
	public static boolean isEmpty(String arg) {
		if (arg == null || arg.trim().length() < 1 || "null".equals(arg)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据从REQUEST中取出的语种，得到数据库中定义的MESSAGE的语种。
	 * 
	 * @param langHeader
	 *            原语种代码
	 * @return 语种代码
	 */
	public static String getLangIDFromString(String langHeader) {
		String langId = "";
		final String CN = "CN";
		final String EN = "EN";
		if (langHeader == null) {
			langId = CN;
		} else if (langId.startsWith("zh")) {
			langId = CN;
		} else {
			langId = EN;
		}

		return CN;
	}

	/**
	 * 查找字符串
	 * 
	 * @param strSource
	 *            原始字符串
	 * @param strFrom
	 *            开始字符串
	 * @param strTo
	 *            结束字符串
	 * @return 字符串
	 */
	public static String find(String strSource, String strFrom, String strTo) {
		String strDest = "";
		int intFromIndex = strSource.indexOf(strFrom) + strFrom.length();
		int intToIndex = strSource.indexOf(strTo);
		if (intFromIndex < intToIndex) {
			strDest = strSource.substring(intFromIndex, intToIndex);
		}
		return strDest;
	}

	/**
	 * 格式化金额字符串
	 * 
	 * @param strPrice
	 *            原始金额字符串
	 * @return 金额字符串。空时返回html空格，其他返回“＄金额”
	 */
	public static String addMoney(String strPrice) {
		strPrice = nvl(strPrice).trim();
		if (strPrice.equals("")) {
			return "&nbsp;";
		} else {
			return "＄" + strPrice;
		}
	}

	/**
	 * 对html标签或特殊字符串编码
	 * 
	 * @param html
	 *            html代码
	 * @return String 替换后代码
	 */
	public static String HtmlEncode(String html) {

		if (isEmpty(html)) {
			return html;
		}

		html = replace(html, "&", "&amp;");
		html = replace(html, "<", "&lt;");
		html = replace(html, ">", "&gt;");
		html = replace(html, "\n", "<br>");
		html = replace(html, "\"", "&quot;");

		return html;
	}

	/**
	 * 从交款信息表的支付备注中得到电子支付时，所输入的金额（出境，国内，包机 专用） <br>
	 * 
	 * @param str
	 *            金额串
	 * @return 金额。参数对象为null或""时，返回0.00
	 */
	public static String getMoney(String str) {

		if (str == null || str.trim().equals("")) {
			return "0.00";
		}
		int start = -1, end = str.length();
		for (int i = 0; i < str.length(); i++) {
			if (start == -1 && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				start = i;
			}
			if (start != -1
					&& !((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str
							.charAt(i) == '.')) {
				end = i;
				break;
			}
		}
		return str.substring(start, end);
	}

	/**
	 * 空字符串转化为NULL
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static final String str2Null(String str) {
		if (str == null || str != null && "".equals(str.trim()))
			return null;
		else
			return str.trim();
	}

	/**
	 * 字符串拆分
	 * 
	 * @param sInputStr
	 *            字符串
	 * @param cDelimiter
	 *            拆分字符
	 * @return 字符串数组
	 */
	public static String[] split(String sInputStr, char cDelimiter) {
		int iStrLen = sInputStr.length();
		int iTokCount = 0;

		if (0 == iStrLen)
			return null;

		for (int p = 0; p < iStrLen; p++)
			if (sInputStr.charAt(p) == cDelimiter)
				iTokCount++;

		String Tokens[] = new String[iTokCount + 1];

		int iToken = 0;
		int iLast = 0;
		for (int iNext = 0; iNext < iStrLen; iNext++) {
			if (sInputStr.charAt(iNext) == cDelimiter) {
				if (iLast == iNext)
					Tokens[iToken] = "";
				else
					Tokens[iToken] = sInputStr.substring(iLast, iNext);
				iLast = iNext + 1;
				iToken++;
			} // fi (sInputStr[iNext]==cDelimiter)
		} // next

		if (iLast >= iStrLen)
			Tokens[iToken] = "";
		else
			Tokens[iToken] = sInputStr.substring(iLast, iStrLen);

		return Tokens;
	} // split

	/**
	 * 增加日期
	 * 
	 * @param strDate
	 *            日期串"yyyy-MM-dd"
	 * @param add
	 *            天数
	 * @return 日期串
	 */
	public static String getAddDate(String strDate, int add) {
		// 返回日期
		String strReturn = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(strDate);

			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, add);

			strReturn = sdf.format(c.getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strReturn;
	}

	/**
	 * 判断日期是否在今天之前
	 * 
	 * @param strDate
	 *            日期串"yyyy-MM-dd"
	 * @return 在今天之前，返回true，否则返回false
	 */
	public static boolean judgeBefore(String strDate) {
		// 返回值

		boolean blnReturn = true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(strDate);

			Calendar c = Calendar.getInstance();
			c.setTime(date);

			Calendar c2 = Calendar.getInstance();
			c2.setTime(new Date());

			blnReturn = c.before(c2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return blnReturn;
	}

	/**
	 * 数值转换
	 * 
	 * @param o
	 *            数字对象
	 * @return NULL或空格，返回“0”
	 */
	public static String nvlnum(Object o) {
		if (o == null) {
			return "0";
		}

		if ("".equals(o.toString().trim())) {
			return "0";
		}
		return o.toString();
	}

	/**
	 * 对字符串作MD5加密处理
	 * 
	 * @param inStr
	 *            需要被处理的字符串
	 * @return 被处理后的字符串，被转换为16进制表示的字符串
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String inStr) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(inStr.getBytes("UTF-8"));
			byte[] r = md.digest();

			for (int i = 0; i < r.length; i++) {
				byte b = r[i];
				sb.append(Character.forDigit((b >> 4 & 0x0F), 16));
				sb.append(Character.forDigit((b & 0x0F), 16));
			}
		} catch (Exception e) {

		}
		return sb.toString();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileSrc
	 *            文件路径
	 * @return 判断结果
	 */
	public static boolean fileExist(String fileSrc) {
		File file = new File(fileSrc);
		return (file.exists());
	}

	// /**
	// * 将字符串（包括汉字）分割成固定长度的字符串数组
	// *
	// * @param strParamTarget 字符串
	// * @param nParamLen 固定长度
	// * @return 字符串数组
	// */
	// public static String[] splitLength(String strParamTarget, int nParamLen)
	// {
	// Vector vctWork = new Vector();
	// int nCharLen;
	//
	// int nLen = 0;
	//
	// int i;
	// StringBuffer sbWork = new StringBuffer("");
	// char cWork;
	//
	// if (strParamTarget == null) {
	//
	// } else {
	//
	// for (i = 0; i < strParamTarget.length(); i++) {
	//
	// cWork = strParamTarget.charAt(i);
	//
	// if (String.valueOf(cWork).getBytes().length > 1) {
	// nCharLen = 2;
	// } else {
	// nCharLen = 1;
	// }
	//
	// if ((nLen + nCharLen) > nParamLen) {
	//
	// vctWork.addElement(sbWork.toString());
	//
	// sbWork = new StringBuffer("");
	// nLen = 0;
	// }
	//
	// nLen += nCharLen;
	//
	// sbWork.append(cWork);
	// }
	//
	// vctWork.addElement(sbWork.toString());
	// }
	//
	// return (String[]) vctWork.toArray(new String[0]);
	// }

	/**
	 * 国内机票儿童价和婴儿价格取得
	 * 
	 * @param yPrice
	 *            标准舱位价格
	 * @param flg
	 *            1:儿童；0：婴儿
	 * @return 价格
	 */
	public static String getSpcPrc(String yPrice, int flg) {

		double dYprice = Double.parseDouble(yPrice);
		if (flg == 1) {
			dYprice *= 0.5;
		} else {
			dYprice *= 0.1;
		}

		java.math.BigDecimal b = java.math.BigDecimal.valueOf(dYprice);
		java.math.BigDecimal ten = new java.math.BigDecimal("10");
		return String.valueOf(b.divide(ten, 0,
				java.math.BigDecimal.ROUND_HALF_UP).floatValue() * 10);
	}

	/**
	 * 国内机票儿童价和婴儿价格取得
	 * 
	 * @param adultPrice
	 *            成人价格
	 * @param yPrice
	 *            标准舱位价格
	 * @param bunkId
	 *            舱位ID
	 * @param flg
	 *            1:儿童；2：婴儿
	 * @return 价格
	 */
	public static String getGnPersonPrice(String adultPrice, String yPrice,
			String bunkId, String flg) {

		double dAdultPrice = Double.parseDouble(adultPrice);
		double dYprice = Double.parseDouble(yPrice);
		bunkId = nvl(bunkId).toUpperCase();
		if ("F".equals(bunkId) || "C".equals(bunkId) || dAdultPrice >= dYprice) {
			dYprice = dAdultPrice;
		}
		if (flg.equals("1")) {
			dYprice *= 0.5;
		} else {
			dYprice *= 0.1;
		}

		java.math.BigDecimal b = java.math.BigDecimal.valueOf(dYprice);
		java.math.BigDecimal ten = new java.math.BigDecimal("10");
		return String.valueOf(b.divide(ten, 0,
				java.math.BigDecimal.ROUND_HALF_UP).floatValue() * 10);
	}

	/**
	 * 对字符串进行base64编码，主要用于网页汉字拼url
	 * 
	 * @param s
	 *            待编码字符串
	 * @return 编码字符串
	 */
	public static String encodeBase64(String s) {
		s = nvl(s);
		s = new BASE64Encoder().encode(s.getBytes());
		try {
			s = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return s;
	}

	public static String encodeBase64NoneUTF8(String s) {
		s = nvl(s);
		s = new BASE64Encoder().encode(s.getBytes());
		s = s.replaceAll("\r\n","");
		return s;
	}
	/**
	 * 对字符串进行base64解码
	 * 
	 * @param s
	 *            待解码字符串
	 * @return 解码字符串
	 */
	public static String decodeBase64(String s) {
		String res = "";
		s = nvl(s);
		BASE64Decoder base64 = new BASE64Decoder();
		try {
			res = new String(base64.decodeBuffer(s));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return res;
	}

	/**
	 * 对字符串进行base64编码，主要用于网页汉字拼url
	 * 
	 * @param o
	 *            待编码对象
	 * @return 编码字符串
	 */
	public static String encodeBase64(Object o) {
		return encodeBase64((String) o);
	}

	/**
	 * 对字符串进行base64解码
	 * 
	 * @param o
	 *            待解码对象
	 * @return 解码字符串
	 */
	public static String decodeBase64(Object o) {
		return decodeBase64((String) o);
	}

	/**
	 * 对GBK字符串进行转码成UTF-8
	 * 
	 * @param str
	 *            待解码字符串
	 * @return 字符串
	 * @throws Exception
	 */
	public static String strGBKtoUtf8(String str) throws Exception {

		String toStr = null;

		if (str != null) {
			try {
				toStr = new String(str.getBytes("gbk"), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		return toStr;
	}

	/**
	 * 对字符串进行转码成UTF-8
	 * 
	 * @param str
	 *            待解码字符串
	 * @return 字符串
	 * @throws Exception
	 */
	public static String strtoUtf8(String str) throws Exception {

		String toStr = null;

		if (str != null) {
			try {
				toStr = new String(str.getBytes(), "utf8");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		return toStr;
	}

	/**
	 * 格式化int类型为String类型
	 * 
	 * @param iFormat
	 *            数值
	 * @return String 字符串。一位的int在个位前加0;否则返回int的String值
	 */
	public static String intFormatTOStr(int iFormat) {
		String strFormat = String.valueOf(iFormat);
		if (strFormat.length() < 2) {
			strFormat = "0" + strFormat;
		}
		return strFormat;
	}

	/**
	 * 将传入的航空城市英文名字取第一个字母并大写
	 * 
	 * @param strEname
	 *            城市英文名字
	 * @return String 第一个字母
	 */
	public static String AirCitysEnameOne(String strEname) {
		String strEnameOne = nvl(strEname).trim();
		if (strEnameOne.length() > 0) {
			strEnameOne = strEnameOne.substring(0, 1).toUpperCase();
		}

		return strEnameOne;
	}

	/**
	 * 将传入的价格加人民币符号（￥）和千分撇（,）
	 * 
	 * @param strPrice
	 *            价格字符串
	 * @return 处理后字符串
	 */
	public static String addRMBQFP(String strPrice) {
		strPrice = nvl(strPrice).trim();
		// 负数标志
		String flag = "no";
		if (!"".equals(strPrice)) {
			if ("-".equals(strPrice.substring(0, 1))) {
				strPrice = strPrice.substring(1);
				flag = "yes";
			}
			int pointIndex = strPrice.indexOf(".");
			String strPriceZ = "";
			String strPriceX = "";
			if (pointIndex > 0) {
				strPriceZ = strPrice.substring(0, pointIndex);
				strPriceX = strPrice.substring(pointIndex);
			} else {
				strPriceZ = strPrice;
				strPriceX = "";
			}

			if (strPriceZ.length() >= 4) {
				int qfpNum = (strPriceZ.length() - (strPriceZ.length() % 3)) / 3;
				String tempPrice = "";
				for (int i = 0; i < qfpNum; i++) {
					tempPrice = ","
							+ strPriceZ.substring(strPriceZ.length() - (i + 1)
									* 3, strPriceZ.length() - i * 3)
							+ tempPrice;
				}
				if (strPriceZ.length() % 3 == 0) {
					tempPrice = tempPrice.trim().substring(1);
				} else {
					tempPrice = strPriceZ.substring(0, strPriceZ.length() % 3)
							+ tempPrice;
				}
				if ("yes".equals(flag)) {
					strPrice = "¥-" + tempPrice + strPriceX;
				} else {
					strPrice = "¥" + tempPrice + strPriceX;
				}
			} else {
				if (!"".equals(strPrice) && !".".equals(strPrice)) {
					if ("yes".equals(flag)) {
						strPrice = "¥-" + strPrice;
					} else {
						strPrice = "¥" + strPrice;
					}
				}
			}
		}
		return strPrice;
	}

	/**
	 * 将传入的美元加美元符号（$）和千分撇（,）
	 * 
	 * @param strPrice
	 *            价格字符串
	 * @return 处理后字符串
	 */
	public static String addMYQFP(String strPrice) {
		strPrice = nvl(strPrice).trim();
		// 负数标志
		String flag = "no";
		if (!"".equals(strPrice)) {
			if ("-".equals(strPrice.substring(0, 1))) {
				strPrice = strPrice.substring(1);
				flag = "yes";
			}
			int pointIndex = strPrice.indexOf(".");
			String strPriceZ = "";
			String strPriceX = "";
			if (pointIndex > 0) {
				strPriceZ = strPrice.substring(0, pointIndex);
				strPriceX = strPrice.substring(pointIndex);
			} else {
				strPriceZ = strPrice;
				strPriceX = "";
			}

			if (strPriceZ.length() >= 4) {
				int qfpNum = (strPriceZ.length() - (strPriceZ.length() % 3)) / 3;
				String tempPrice = "";
				for (int i = 0; i < qfpNum; i++) {
					tempPrice = ","
							+ strPriceZ.substring(strPriceZ.length() - (i + 1)
									* 3, strPriceZ.length() - i * 3)
							+ tempPrice;
				}
				if (strPriceZ.length() % 3 == 0) {
					tempPrice = tempPrice.trim().substring(1);
				} else {
					tempPrice = strPriceZ.substring(0, strPriceZ.length() % 3)
							+ tempPrice;
				}
				if ("yes".equals(flag)) {
					strPrice = "$-" + tempPrice + strPriceX;
				} else {
					strPrice = "$" + tempPrice + strPriceX;
				}
			} else {
				if (!"".equals(strPrice) && !".".equals(strPrice)) {
					if ("yes".equals(flag)) {
						strPrice = "$-" + strPrice;
					} else {
						strPrice = "$" + strPrice;
					}
				}
			}
		}
		return strPrice;
	}

	/**
	 * 对字符串按照长度换行
	 * 
	 * @param s
	 *            需要换行的字符传
	 * @param len
	 *            多长时，需要换行
	 * @return HTML字符串
	 */
	public static String autoChangeRow(String s, int len) {
		String sReurlt = "";
		for (int i = 0; i < s.length(); i = i + len) {
			if (i == 0) {
				sReurlt = s.substring(0, s.length() > len ? len : s.length());
			} else {
				sReurlt = sReurlt + "</br>";
				sReurlt = sReurlt
						+ s.substring(i,
								s.length() > i + len ? i + len : s.length());
			}
		}
		return sReurlt;
	}

	/**
	 * 对字符串按照长度换行(当文字中有CSS样式时，适用)
	 * 
	 * @param s
	 *            需要换行的字符传
	 * @param len
	 *            多长时，需要换行
	 * @return HTML字符串
	 */
	public static String autoChangeRowWithCSS(String s, int len) {
		String[] oldS = s.split(",");
		String[] newS = s.split(",");
		String strNewS = "";
		String strOldS = "";
		for (int i = 0; i < newS.length; i++) {
			if (newS[i].charAt(0) == '<') {
				newS[i] = newS[i].substring(newS[i].indexOf(">") + 1,
						newS[i].indexOf("<", 2));
			}
			strNewS += "," + newS[i];
		}
		strNewS = strNewS.substring(1);
		strNewS = autoChangeRow(strNewS, len);
		newS = strNewS.split(",");
		for (int i = 0; i < oldS.length; i++) {
			if (oldS[i].charAt(0) == '<') {
				oldS[i] = oldS[i].substring(0, oldS[i].indexOf(">") + 1)
						+ newS[i]
						+ oldS[i].substring(oldS[i].indexOf("<", 2),
								oldS[i].length());
			} else {
				oldS[i] = newS[i];
			}
			strOldS += "," + oldS[i];
		}
		strOldS = strOldS.substring(1);
		return strOldS;
	}

	/**
	 * 计算人民币转化为美元的值，向上取整
	 * 
	 * @param strRmbValue
	 *            人民币值
	 * @param strRate
	 *            汇率
	 * @return String
	 */
	public static String convertRmbToUsdRoundUp(String strRmbValue,
			String strRate) {
		strRmbValue = nvlnum(strRmbValue);
		strRate = nvlnum(strRate);
		if ("0".equals(strRate)) {
			strRate = "1";
		}
		return nvlnum((new BigDecimal(strRmbValue).divide(new BigDecimal(
				strRate), 0, BigDecimal.ROUND_CEILING)));
	}

	/**
	 * 计算其它币种转人民币，保留2位小数截取
	 * 
	 * @param strOtherCur
	 *            其它币种值
	 * @param strToRMBRate
	 *            对人民币汇率
	 * @return String
	 */
	public static BigDecimal convertToRmbRoundDown(BigDecimal strOtherCur,
			BigDecimal strToRMBRate) {

		if ("0".equals(nvlnum(strToRMBRate))) {
			strToRMBRate = new BigDecimal(1);
		}
		return strOtherCur.multiply(strToRMBRate).setScale(2,
				BigDecimal.ROUND_DOWN);
	}

	/**
	 * 获取字符串中非GB2312字符
	 * 
	 * @param str
	 *            需要处理的字符串
	 * @return 字符串
	 */
	public static String getNotGB2312(String str) {
		str = nvl(str);
		char[] chars = str.toCharArray();
		String GB2312 = "";
		for (int i = 0; i < chars.length; i++) {
			try {
				byte[] bytes = ("" + chars[i]).getBytes("GB2312");
				if (bytes.length == 2) {
					int[] ints = new int[2];
					ints[0] = bytes[0] & 0xff;
					ints[1] = bytes[1] & 0xff;
					if (!(ints[0] >= 0xb0 && ints[0] <= 0xf7 && ints[1] >= 0xa1 && ints[1] <= 0xfe)) {
						GB2312 += chars[i];
					}
				} else {
					GB2312 += chars[i];
				}
			} catch (Exception e) {
				GB2312 += chars[i];
				logger.error("ERR=====" + str);
				e.printStackTrace();
			}
		}
		return GB2312;
	}

	/**
	 * 取字符串的后几位，位数小于字符串，返回本身
	 * 
	 * @param string
	 * @param num
	 *            获取的位数
	 * @return
	 */
	public static String subString(String string, int num) {
		String returnValue = "";
		if (string != null) {
			int length = string.length();

			if (num > length) {
				returnValue = string;
			} else {
				returnValue = string.substring(length - num);
			}
		}
		return returnValue;
	}

	/***
	 * 去掉最后一个字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String subStringEnd(String str) {
		return str.substring(0, str.length() - 1);
	}

	/***
	 * 拼接成('1','2')等等
	 * 
	 * @param objs
	 * @return
	 */
	public static String pjId(Object[] objs) {
		// 拆分id
		String ids = "(";
		for (Object object : objs) {
			ids += "'" + object + "',";
		}
		// 拼接成('1','2','3')
		return subStringEnd(ids) + ")";
	}
	
	
	/***
	 * 拼接成1,2等等
	 * 
	 * @param objs
	 * @return
	 */
	public static String pjIds(Object[] objs) {
		// 拆分id
		String ids = "";
		for (Object object : objs) {
			ids += object + ",";
		}
		// 拼接成('1','2','3')
		return subStringEnd(ids);
	}
	
	public static void main(String[] args) {
		String[] objs = new String[2];
		objs[0] ="73";
		objs[1] ="74";
		System.out.println(pjIds(objs));
	}

	/**
	 * 替代oldStr中head之前，end之后的字符串
	 * 
	 * @param oldStr
	 * @param str
	 * @param head
	 * @param end
	 * @return String
	 * 
	 * @author
	 */
	public static String strReplace(String oldStr, String str, int head, int end) {
		StringBuffer result = new StringBuffer();
		if (oldStr == null || oldStr == "") {
			return result.toString();
		} else if (head > end || end > oldStr.length() || head < 0) {
			return oldStr;
		}

		for (int i = 0; i < head; i++) {
			result.append(str);
		}

		result.append(oldStr.substring(head, end));

		for (int i = end; i < oldStr.length(); i++) {
			result.append(str);
		}
		return result.toString();
	}

	/**
	 * 
	 * 把字符串转换成合适的SQL查询语言,适用于 like
	 * 
	 * @param str
	 *            转换的字符串 - 可以为null
	 * @return String 返回转换后新的SQL文字符串，如果字符串为null就返回null 将用户传入的检索条件中的特殊字符进行转义 <br/>
	 *         将 ' 转换 \' <br/>
	 *         将 " 转换 \" <br/>
	 *         将 % 转换 \% <br/>
	 *         用法 " like '"+name+"'" 改写为 " like '"+CmnUtFunc.escapeSql(name)+"'"
	 */
	public static String escapeSql(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				switch (c) {
				// 将 ' 转换为 ''
				case '\'':
					buf.append("''");
					break;
				// 将 " 转换为 \"
				case '\"':
					buf.append("\"");
					break;
				// 将 % 转换为 \%
				case '%':
					buf.append("\\%");
					break;
				// 将 $ 转换为 \$
				case '$':
					buf.append("\\$");
					break;
				// 将 \ 转换为 \\
				case '\\':
					buf.append("\\\\");
					break;
				default:
					buf.append(c);
					break;
				}
			}
			return buf.toString();
		}
	}

	/**
	 * 把doc格式的字符串转换为html格式,但是对table保持原样
	 * 
	 * @param sourceStr
	 * @return targetStr
	 */
	public static String getTargetStr(String sourceStr) {
		StringBuffer buf = new StringBuffer();
		int tabSIndex = sourceStr.indexOf("<table>");
		int tabEIndex = sourceStr.indexOf("</table>");
		if (tabSIndex == -1 || tabEIndex == -1) {
			return sourceStr;
		}
		String headStr = sourceStr.substring(0, tabSIndex);
		headStr = headStr.replaceAll("\r\n", "<br>");
		String middleStr = sourceStr.substring(tabSIndex + 1, tabEIndex);
		String endStr = sourceStr.substring(tabEIndex + 1,
				sourceStr.length() - 1);
		buf.append(headStr);
		buf.append(middleStr);
		if (endStr.indexOf("<table>") != -1) {
			buf.append(getTargetStr(endStr));
		}
		return buf.toString();
	}

	/**
	 * 截取字符串，输入字符串长度大于要截取的长度，则显示“…”
	 * 
	 * @param input
	 * @param lettersNum
	 *            英文个数 ，一个中文占两个英文
	 * @return
	 */
	public static String subString2(String input, int lettersNum) {

		if (input == null || input.trim() == "") {
			return "";
		}
		String tmpStr = input.trim();

		if (tmpStr.length() * 2 <= lettersNum) {
			return tmpStr;
		}

		int num = 0;
		String temp = "";
		for (int i = 0; i < tmpStr.length() && num < lettersNum; i++) {
			if (tmpStr.substring(i, i + 1).getBytes().length > 1) {
				num += 2;
				temp = tmpStr.substring(0, i + 1);
			} else {
				num += 1;
				temp = tmpStr.substring(0, i + 1);
			}

		}

		if (temp.length() == tmpStr.length()) {
			return temp;
		} else {
			while (num > lettersNum - 2) {
				int i = temp.length();

				if (temp.substring(i - 1, i).getBytes().length > 1) {
					num = num - 2;
				} else {
					num = num - 1;
				}
				temp = temp.substring(0, i - 1);
			}
			temp += "…";
		}

		return temp;

	}

	/**
	 * 国内游出境游用
	 * 
	 * @return
	 */
	public static String replaceImg(String src) {
		if (src == null) {
			return "";
		}
		// System.out.println(src);
		src = src.replaceAll(
				"<div class=\"detail_004_002_left1\"><img.+?</div>", "");
		src = src.replaceAll("<div class=\"detail_004_002_left1\"></div>", "");
		src = src.replaceAll("<div class='detail_004_002_left1'><img.+?</div>",
				"");
		src = src.replaceAll("<div class='detail_004_002_left1'></div>", "");
		src = src.replaceAll("line-height: 24px;margin-left: 201px;",
				"line-height: 24px;margin-left: 15px;");
		// src=src.replaceAll("<img.+?/>","");
		return src;
	}

	/**
	 * 国内游出境游用
	 * 
	 * @return
	 */
	public static String replaceImgIphone(String src) {
		if (src == null) {
			return "";
		}
		// System.out.println(src);
		src = src.replaceAll(
				"<div class=\"detail_004_002_left1\"><img.+?</div>", "");
		src = src.replaceAll("<div class=\"detail_004_002_left1\"></div>", "");
		src = src.replaceAll("<div class='detail_004_002_left1'><img.+?</div>",
				"");
		src = src.replaceAll("<div class='detail_004_002_left1'></div>", "");
		src = src.replaceAll("line-height: 24px;margin-left: 201px;",
				"line-height: 24px;margin-left: 15px;");
		src = src.replaceAll("<img.+?/>", "");
		return src;
	}

	/**
	 * 按单位给字符串加换行
	 * 
	 * @param sourceStr
	 * @param cutUnit
	 *            字符串的单位
	 * @return
	 */
	public static String addBr(String sourceStr, int cutUnit) {
		if (sourceStr == null || cutUnit <= 0) {
			return "";
		} else {
			int len = sourceStr.length();
			if (len <= cutUnit) {
				return sourceStr;
			} else {
				String targetStr = "";
				int strCount = len / cutUnit;
				int raminder = len % cutUnit;
				for (int i = 0; i < strCount; i++) {
					String frontSubStr = sourceStr.substring(cutUnit * i,
							cutUnit * (i + 1));
					if ((i == (strCount - 1)) && (raminder == 0)) {
						targetStr += frontSubStr;
					} else {
						targetStr += frontSubStr + "<br>";
					}
				}
				if (raminder != 0) {
					targetStr += sourceStr.substring(cutUnit * strCount, len);
				}

				return targetStr;
			}
		}
	}

	/**
	 * 按字节截取字符串
	 * 
	 * @param sourceStr
	 * @param byteLen
	 * @return
	 */
	public static String cutStringByByte(String sourceStr, int byteLen) {
		if (sourceStr == null)
			return "";
		String targetStr = sourceStr;
		byte[] sourceByte = sourceStr.getBytes();
		if (sourceByte.length > byteLen) {
			targetStr = new String(sourceByte, 0, byteLen);
		}
		return targetStr;
	}

	/***
	 * 判断字符串是否是数字
	 * 
	 * @param str
	 * @return boolean
	 * */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 判断list的是否为空
	 * 
	 * @param <T>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean listNotValue(List<T> list) {
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/***
	 * 将List转化为a,b,c,
	 * 
	 * @param list
	 * @return
	 */
	public static String listToString(List<String> list) {
		String result = "";
		if (listNotValue(list)) {
			for (String str : list) {
				result += str + ",";
			}
		}

		return result;
	}

	/***
	 * 将a,b,c转化为list
	 * 
	 * @param str
	 * @return
	 */
	public static List<String> stringToList(String str) {
		List<String> result = new ArrayList<String>();
		if (!isEmpty(str)) {
			String[] strArr = str.split(",");
			for (String string : strArr) {
				result.add(string);
			}
		}

		return result;
	}

	/**
	 * 判断map是否为空
	 * 
	 * @param <T>
	 * 
	 * @param map
	 * @return
	 */
	public static <T> boolean mapNotValue(Map<Object, T> map) {
		if (map != null && map.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否在定义的字符串数组中存在，不区分大小写。目标字符串会trim后比较
	 * 
	 * @param stringArray
	 *            定义的字符串数组
	 * @param source
	 *            目标字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(String[] stringArray, String source) {
		if (stringArray == null || stringArray.length == 0 || source == null) {
			return false;
		}
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i].equalsIgnoreCase(source.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将现存的 a,c,d,x, 转为 ('a','c','d','x')
	 * 
	 * @param str
	 * @author fanhaiyang
	 * @return
	 */
	public static String converFormatStr(String str) {
		String resultStr = "";
		StringBuffer sbStr = new StringBuffer();
		sbStr.append("(");
		String[] arrStr = str.split(",");
		for (int i = 0; i < arrStr.length; i++) {
			sbStr.append("'" + arrStr[i] + "',");
		}
		resultStr = sbStr.substring(0, sbStr.lastIndexOf(","));
		return resultStr + ")";
	}

	/**
	 * 将BigDecimal 格式化"#0.00"
	 * 
	 * @param dec
	 * @return
	 */
	public static String converFormatStr(BigDecimal dec) {
		DecimalFormat formatter = new DecimalFormat("#0.00");

		return formatter.format(dec);
	}

	/**
	 * 讲BigDecimal 格式化 "#0"
	 * 
	 * @param dec
	 * @return
	 */
	public static String converFormatStrPrice(BigDecimal dec) {
		DecimalFormat formatter = new DecimalFormat("#0");

		return formatter.format(dec);
	}

	/***
	 * 将带有","转换为list
	 * 
	 * @param objects
	 * @return
	 */
	public static List<Object> converForList(Object[] objects) {
		List<Object> list = new ArrayList<Object>();

		return list;
	}

	/**
	 * 创建验证码(长度六位)
	 * 
	 * @return
	 */
	public static String buildValidateCode() {
		Random rand = new Random();
		int tmp = Math.abs(999999);
		String code = String.valueOf(tmp % (999999 - 100000 + 1) + 100000);
		return code;
	}

	/**
	 * 生成取货码
	 * 
	 * @return
	 */
	public static String buildPickupCode() {
		String chars = "abcdefghijklmnopqrstuvwxyz";
		String rdmLetter = String
				.valueOf(chars.charAt((int) (Math.random() * 26)));
		Random rand = new Random();
		int tmp = Math.abs(999999);
		String code = String
				.valueOf(tmp % (99999999 - 10000000 + 1) + 10000000);
		return rdmLetter + code;
	}

	/**
	 * 
	 * unicode 转换成 中文
	 * 
	 * @param theString
	 * @return
	 */

	public static String decodeUnicode(String theString) {

		char aChar;

		int len = theString.length();

		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}

		return outBuffer.toString();

	}

	/**
	 * 输入流转化为字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {

		InputStreamReader isr = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			while ((line = reader.readLine()) != null) {

				sb.append(line);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {

				if (is != null) {
					is.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				is = null;
			}
		}
		return sb.toString();
	}

	/**
	 * 设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），
	 * 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String generateKeyValuePair(Object obj) throws Exception {

		StringBuffer stringA = new StringBuffer();
		Class<? extends Object> objClass = obj.getClass();
		Field[] fieldArray = objClass.getDeclaredFields();
		List<String> fieldNameList = new ArrayList<String>();
		for (Field field : fieldArray) {

			fieldNameList.add(field.getName());
		}
		Collections.sort(fieldNameList);
		Object fieldVal = null;
		String fieldValStr = null;
		Class<?> fieldType = null;
		for (String fieldName : fieldNameList) {

			if (!"sign".equals(fieldName)) {

				fieldVal = objClass.getDeclaredMethod(
						"get" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1), new Class[] {})
						.invoke(obj, new Object[] {});
				if (fieldVal != null) {

					fieldType = objClass.getDeclaredField(fieldName).getType();
					if (fieldType == Long.class) {

						fieldValStr = String.valueOf(fieldVal);
					} else {
						fieldValStr = (String) fieldVal;
					}
					// package关键字用作变量名，进行特殊处理
					if ("package0".equals(fieldName)) {

						fieldName = "package";
					}
					stringA.append(fieldName + "=" + fieldValStr + "&");
				}
			}
		}
		if (!StringHelperTools.isEmpty(stringA.toString())) {

			stringA.deleteCharAt(stringA.length() - 1);
		}

		return stringA.toString();
	}

	/**
	 * 把对象转换为xml格式
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String generateXml(Object obj) throws Exception {

		StringBuffer stringA = new StringBuffer();
		stringA.append("<xml>");
		Class<? extends Object> objClass = obj.getClass();
		Field[] fieldArray = objClass.getDeclaredFields();
		String fieldName = null;
		String fieldVal = null;
		for (Field field : fieldArray) {

			fieldName = field.getName();
			fieldVal = (String) objClass.getDeclaredMethod(
					"get" + fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1), new Class[] {}).invoke(
					obj, new Object[] {});
			if (!StringHelperTools.isEmpty(fieldVal)) {

				stringA.append("<" + fieldName + ">" + fieldVal + "</"
						+ fieldName + ">");
			}
		}
		stringA.append("</xml>");
		return stringA.toString();
	}

	/**
	 * 把一个xml字符串转换为一个对象
	 * 
	 * @param ObjXml
	 * @param objClass
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static Object generateObj(String objXml, Class<?> objClass) throws
	 * Exception {
	 * 
	 * // 创建一个对象 Object obj = objClass.newInstance(); // 解析xml InputStream
	 * inputStream = new ByteArrayInputStream(objXml.getBytes("UTF-8"));
	 * SAXReader reader = new SAXReader(); Document document =
	 * reader.read(inputStream); Element rootElement =
	 * document.getRootElement(); // 给对象赋值 Field[] fieldArray =
	 * objClass.getDeclaredFields(); if (fieldArray != null && fieldArray.length
	 * > 0) {
	 * 
	 * String fieldName = null; Class<?> fieldType = null; Object fieldVal =
	 * null; Element fieldElement = null; for (Field field : fieldArray) {
	 * 
	 * fieldName = field.getName(); fieldType = field.getType(); fieldElement =
	 * rootElement.element(fieldName); if (fieldElement != null) {
	 * 
	 * // 1.字符串 fieldVal = fieldElement.getTextTrim(); // 2.长整型 if (fieldType ==
	 * Long.class) { fieldVal = new Long((String)fieldVal); }
	 * objClass.getMethod( "set" + fieldName.substring(0, 1).toUpperCase() +
	 * fieldName.substring(1), new Class[] { fieldType }).invoke( obj, new
	 * Object[] { fieldVal }); }
	 * 
	 * } } return obj; }
	 */

	/**
	 * 判断某个字符串是否存在于数组中
	 * 
	 * @param stringArray
	 *            原数组
	 * @param source
	 *            查找的字符串
	 * @return 是否找到
	 */
	public static boolean contains(String[] stringArray, String source) {
		// 转换为list
		List<String> tempList = Arrays.asList(stringArray);
		// 利用list的包含方法,进行判断
		if (tempList.contains(source)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 随机生成字符串
	 * 
	 * @param size
	 * @return
	 */
	public static String generateRandomString(int size) {

		// 由a-z和0-9组成
		char[] charArray = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z' };

		StringBuilder sb = new StringBuilder("");
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			sb.append(charArray[random.nextInt(36)]);
		}

		return sb.toString();
	}

	/**
	 * 随机生成字符串
	 * 
	 * @param size
	 * @return
	 */
	public static String generateRandomStringByNumber(int size) {

		// 由0-9组成
		char[] charArray = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9' };

		StringBuilder sb = new StringBuilder("");
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			sb.append(charArray[random.nextInt(10)]);
		}

		return sb.toString();
	}

	/**
	 * 把一个json转换为一个对象
	 * 
	 * @param json
	 * @param objClass
	 * @return
	 */
	public static Object convertJsonToObj(String json, Class<?> objClass)
			throws Exception {

		// 创建一个对象
		Object obj = objClass.newInstance();
		// 解析json
		JSONObject jsonObject = JSONObject.fromObject(json);
		// 给对象赋值
		Field[] fieldArray = objClass.getDeclaredFields();
		if (fieldArray != null && fieldArray.length > 0) {

			String fieldName = null;
			Class<?> fieldType = null;
			Object fieldVal = null;
			for (Field field : fieldArray) {

				fieldName = field.getName();
				fieldType = field.getType();
				logger.info(fieldType);
				if (jsonObject.get(fieldName) != null) {

					// 1.字符串
					if (fieldType == String.class) {
						fieldVal = jsonObject.getString(fieldName);
					}
					// 2.长整型
					if (fieldType == Long.class) {
						fieldVal = new Long(jsonObject.getLong(fieldName));
					}
					// 3.BigDecimal
					if (fieldType == BigDecimal.class) {
						fieldVal = new BigDecimal(
								jsonObject.getDouble(fieldName));
					}
					objClass.getMethod(
							"set" + fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1),
							new Class[] { fieldType }).invoke(obj,
							new Object[] { fieldVal });
				}
			}
		}
		return obj;
	}

	/**
	 * 把一个对象转换为json
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String convertObjToJson(Object obj) throws Exception {

		StringBuffer stringA = new StringBuffer();
		stringA.append("{");
		Class<? extends Object> objClass = obj.getClass();
		Field[] fieldArray = objClass.getDeclaredFields();
		String fieldName = null;
		Class<?> fieldType = null;
		Object fieldVal = null;
		for (Field field : fieldArray) {

			fieldType = field.getType();
			fieldName = field.getName();
			fieldVal = objClass.getDeclaredMethod(
					"get" + fieldName.substring(0, 1).toUpperCase()
							+ fieldName.substring(1), new Class[] {}).invoke(
					obj, new Object[] {});
			if (fieldVal != null) {

				// 字符串
				if (fieldType == String.class) {

					stringA.append("\"" + fieldName + "\":\"" + fieldVal
							+ "\",");
				}
				// BigDecimal
				if (fieldType == BigDecimal.class) {

					stringA.append("\"" + fieldName + "\":"
							+ ((BigDecimal) fieldVal).doubleValue() + ",");
				}
			}
		}
		if (stringA.length() > 1) {

			stringA.deleteCharAt(stringA.length() - 1);
		}
		stringA.append("}");
		return stringA.toString();
	}

	/**
	 * 解析出html代码中的图片url列表
	 * 
	 * @param htmlCode
	 * @return
	 */
	public static List<String> parseImgUrl(String htmlCode) {

		List<String> imgUrlList = new ArrayList<String>();
		int index = htmlCode.indexOf("<img src=", 0);
		while (index != -1) {

			logger.info(htmlCode.substring(index + 10,
					htmlCode.indexOf("\"", index + 10)));
			imgUrlList.add(htmlCode.substring(index + 10,
					htmlCode.indexOf("\"", index + 10)));
			index = htmlCode.indexOf("<img src=", index + 10);
		}
		return imgUrlList;
	}

	/**
	 * 把毫秒数转换为时间
	 * 
	 * @param ms
	 * @return
	 */
	public static Date convertTimesStampToDate(long ms) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ms);
		return c.getTime();
	}

	/**
	 * 获取当前UUID，用做数据关联 新增表结构请使用此ID替换seq
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = (uuid.toString()).replaceAll("-", "");
		return uuidStr;
	}

	/**
	 * 获取随机数
	 * 
	 * @author sys
	 * @since 2015年7月14日
	 * @param len
	 * @return
	 */
	public static String getRandom(int len) {
		return getUUID().substring(0, len);
	}

	/**
	 * 获取整数随机数
	 * 
	 * @author sys
	 * @since 2015年7月20日
	 * @param count
	 * @return
	 */
	public static String getNumberRandom(int count) {
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}
		return sb.toString();
	}

	/**
	 * 
	 * 将第一个字母变成小写
	 * 
	 * @author sys
	 * @since 2015年8月10日
	 * @param fildeName
	 * @return
	 */
	public static String getMethodName(String fildeName) {
		char[] chars = fildeName.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	/**
	 * 将字符串变成小写
	 * 
	 * @author sys
	 * @since 2015年8月10日
	 * @param fildeName
	 * @return
	 */
	public static String changeLowerCate(String fildeName) {
		return fildeName.toLowerCase();
	}

	/**
	 * 获取现在时间yyyy-MM-dd
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 获取现在时间yyyyMMdd
	 */
	public static String getStrDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 获取现在时间yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 获取现在时间加n天(返回结果格式yyyy-MM-dd)
	 */
	public static String getStringDateAddN(int n) {
		Date currentTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		calendar.add(Calendar.DATE,n);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	/**
	 * 获取现在时间加n天(返回结果格式yyyyMMdd)
	 */
	public static String getStrDateAddN(int n) {
		Date currentTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentTime);
		calendar.add(Calendar.DATE,n);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	
	/**
	 * 获取字符串时间加n天
	 * @param data
	 * @param n
	 * @return
	 */
	public static String getStringDateAddN(String data,int n) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (data.length() == 8) {
			StringBuilder build = new StringBuilder();
			for (int i = 0; i < data.length(); i++) {
				if (i == 4 || i == 6) {
					build.append("-");
				}
				build.append(data.charAt(i));
			}
			data = build.toString();
		}
		Date date = null;
		try {
			date = formatter.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,n);
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}

	/**
	 * 根据日期计算年龄
	 */
	public static String getYear(String brithday) {
		if(StringHelperTools.isEmpty(brithday)){
			return "";
		}
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			String currentTime = formatDate.format(calendar.getTime());
			Date today = formatDate.parse(currentTime);
			if (brithday.length() == 8) {
				StringBuilder build = new StringBuilder();
				for (int i = 0; i < brithday.length(); i++) {
					if (i == 4 || i == 6) {
						build.append("-");
					}
					build.append(brithday.charAt(i));
				}
				brithday = build.toString();
			}
			Date brithDay = formatDate.parse(brithday);
			int age = today.getYear() - brithDay.getYear();
			if (brithDay.getMonth() > today.getMonth()) {
				age -= 1;
			} else if (brithDay.getMonth() == today.getMonth()) {
				if (brithDay.getDate() > today.getDate()) {
					age -= 1;
				}
			}
			return age + "";
		} catch (Exception e) {
			return "0";
		}
	}
	


	/**
	 * 随机生成E电子理赔申请号（ E+公司序号（4位）+年份（4位）+流水号（6位，000001—999999），譬如E2586200600000）
	 * 
	 * @param size
	 * @return
	 */
	public static String getERandomString() {
		Calendar a = Calendar.getInstance();
		StringBuilder sb = new StringBuilder("");
		sb.append("E");
		sb.append("2586");// 公司编号
		sb.append(a.get(Calendar.YEAR));// 年
		// 由0-9组成
		char[] charArray = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9' };

		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			sb.append(charArray[random.nextInt(10)]);
		}
		return sb.toString();
	}
	

	
	/**
	 * 于5年前比较
	 * @return
	 */
	public static boolean compareFiveYearAgo(String date){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        System.out.println("之前:" + f.format(c.getTime()));
        c.add(Calendar.YEAR, -5);
        System.out.println("之后:" + f.format(c.getTime()));
       return compareDateString(date,f.format(c.getTime()));
		
	}
	// 日期比较
	public static boolean compareDateString(String data1 ,String data2) {
		if (data1.length() == 8) {
			StringBuilder build = new StringBuilder();
			for (int i = 0; i < data1.length(); i++) {
				if (i == 4 || i == 6) {
					build.append("-");
				}
				build.append(data1.charAt(i));
			}
			data1 = build.toString();
		}
		if (data1.compareTo(data2) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 随机生成数字字符串，参数为位数
	 * 
	 * @param size
	 * @return
	 */
	public static String randomNumGen(int size) {
		StringBuilder str = new StringBuilder();// 定义变长字符串
		Random random = new Random();
		// 随机生成数字，并添加到字符串
		for (int i = 0; i < size; i++) {
			str.append(random.nextInt(10));
		}
		return str.toString();
	}

	// 生成日期
	public static String generateDateToday(String strDate) {
		Date dateTime = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateTime = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dateTime);
		c.set(Calendar.HOUR_OF_DAY, 15);

		StringBuffer newDateBuffer = new StringBuffer();
		newDateBuffer.append(c.get(Calendar.YEAR));
		newDateBuffer.append("-");
		newDateBuffer.append(c.get(Calendar.MONTH) + 1);
		newDateBuffer.append("-");
		newDateBuffer.append(c.get(Calendar.DATE));
		newDateBuffer.append(" ");
		newDateBuffer.append(c.get(Calendar.HOUR_OF_DAY));
		newDateBuffer.append(":");
		newDateBuffer.append(00);
		newDateBuffer.append(":");
		newDateBuffer.append(00);

		return newDateBuffer.toString();
	}

	public static String generateDateTomorrow(String strDate) {
		Date dateTime = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateTime = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(dateTime);
		c.set(Calendar.HOUR_OF_DAY, 15);

		StringBuffer newDateBuffer = new StringBuffer();
		newDateBuffer.append(c.get(Calendar.YEAR));
		newDateBuffer.append("-");
		newDateBuffer.append(c.get(Calendar.MONTH) + 1);
		newDateBuffer.append("-");
		newDateBuffer.append(c.get(Calendar.DATE) + 1);
		newDateBuffer.append(" ");
		newDateBuffer.append(c.get(Calendar.HOUR_OF_DAY));
		newDateBuffer.append(":");
		newDateBuffer.append(00);
		newDateBuffer.append(":");
		newDateBuffer.append(00);

		return newDateBuffer.toString();
	}

	public static String generateDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	// 比较日期 如果strDate1<strDate2 返回True;
	public static boolean compareDate(String strDate1, String strDate2) {
		Date strdate1 = new Date();
		Date strdate2 = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strdate1 = sdf.parse(strDate1);
			strdate2 = sdf.parse(strDate2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (strdate1.getTime() < strdate2.getTime());
	}
	


	public static String doDateFormat(String str) {
		if (str != null && str.length() >= 10) {
			str = str.trim();
			str = str.substring(0, 10);
			return str;
		} else if (str != null && str.length() == 8) {
			StringBuilder build = new StringBuilder();
			for (int i = 0; i < str.length(); i++) {
				if (i == 4 || i == 6) {
					build.append("-");
				}
				build.append(str.charAt(i));
			}
			str = build.toString();
			return str;
		} else {
			return str;
		}
	}

	/**
	 * 判断是否是白天 供保全QueryStatus 根据时间不同调用不同接口 白天同步，晚上异步
	 * 
	 * @param startTime
	 *            白天起始时间
	 * @param endTime
	 *            晚上截止时间
	 * @return 白天返回true 晚上返回false
	 */
	public static boolean isDaytime(String startTime, String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String time = sdf.format(new Date());
		Date currentTime = new Date();
		Date dayTimeStartTime = new Date();
		Date dayTimeEndTime = new Date();
		sdf.format(dayTimeEndTime);
		sdf.format(dayTimeStartTime);
		try {
			currentTime = sdf.parse(time);
			dayTimeStartTime = sdf.parse(startTime);
			dayTimeEndTime = sdf.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (currentTime.after(dayTimeStartTime)
				&& currentTime.before(dayTimeEndTime)) {
			return true;
		}
		return false;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**   
	 * 全角转半角   
	 * @param input String.   
	 * @return 半角字符串   
	 */    
	public static String ToDBC(String input) {     
	
	         char c[] = input.toCharArray();     
	         for (int i = 0; i < c.length; i++) {     
	           if (c[i] == '\u3000') {     
	             c[i] = ' ';     
	           } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {     
	             c[i] = (char) (c[i] - 65248);     
	           }     
	         }     
	    String returnString = new String(c);     
	         
	         return returnString;     
	}  
	
	/**
	 * 判断字符串是否含有中文
	 * @param str
	 * @return
	 */
	 public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
	    }
	 
	public static String amoutOfMoney(String str){
		BigDecimal amount = new BigDecimal(str);
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return amount.toString();
	}

	public static String formatAmoutOfMoney(String str){
		BigDecimal amount = new BigDecimal(str);
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,###.00");
		return myformat.format(amount);
	}
	
	/**
	 * 验证是否为手机号
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		mobiles = mobiles.trim();
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
	
	public static boolean isMobilePhone(String mobiles) 
	{
		/*mobiles = mobiles.trim();
		System.out.println("mobiles" + mobiles);
		String reg = "/^1[0-9]{10}/";
		if(mobiles.matches(reg)) {
			return true;
		}
		*/
		if( StringUtils.isBlank(mobiles))
		{
			return false;
		}
		if( mobiles.length() !=11)
		{
			return false;
		}
		if(!mobiles.startsWith("1"))
		{
			return false;
		}
		return true;
	}
}
