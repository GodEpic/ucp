package com.yum.ucp.modules.sys.utils;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CouponUtils {
	
	private transient static  Logger logger = Logger.getLogger(CouponUtils.class.getName());
	/**
	 * 产生10位的数字
	 * 
	 * @return
	 */
	public static String generateCode() {
		StringBuilder builder = new StringBuilder();

		String temp = (new Date()).getTime() + "";

		builder.append(temp.substring(temp.length() - 8));

		Random temp_ran = new Random();

		int m = temp_ran.nextInt(100);

		if (m < 10) {
			builder.append(temp_ran.nextInt(10));
		}

		builder.append(m);

		return builder.toString();
	}

	/**
	 * 获得小写的MD5的值
	 * @param source  需要计算摘要的字符串
	 * @param algorithm  算法
	 * @return  摘要后的字符串
	 */
	
	public static String getMd5Lower(String source,String algorithm)
	{
		if(null==source) return null;
		
		MessageDigest digest = null;

		try {
			if(algorithm==null)
			{
				digest = MessageDigest.getInstance("MD5");
			}else{
				digest = MessageDigest.getInstance(algorithm);
			}
			
			byte[]  result  = digest.digest(source.getBytes());
			
			StringBuffer buffer = new StringBuffer();
			buffer.setLength(0);
			int length = result.length;
			for(int k=0;k<length;k++)
			{
				 String hex = Integer.toHexString(result[k] & 0xFF);//����λ
				 if(hex.length()<2) hex= "0" + hex;
				 
				 buffer.append(hex);
			}
			return buffer.toString();
			
		} catch (Exception e) {
			logger.error("Get MD5 failure", e);
		}
		return null;
	}
	
	/**
	 * 获得大写的MD5值
	 * @param source  需要计算摘要的字符串
	 * @param algorithm  算法
	 * @return  摘要后的字符串
	 */
	public static String getMd5Upper(String source,String algorithm)
	{
		String  result = getMd5Lower(source, algorithm);
		
		if(result==null) return null;
		
		return  result.toUpperCase();
	}
	
	/**
	 * 对字符串进行排序，按照字符串的从小到大排序
	 * @param source
	 * @return
	 */
	public static List<String> couponString(List<String> source)
	{
		if(null==source || source.size()<=0)
		{
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		
		for(int k=0;k<source.size();k++)
		{
			if(k==0)
			{
				result.add(source.get(k));
				continue;
			}
			
			boolean isAdd = false;
			for(int t=0;t<result.size();t++)
			{
				if(source.get(k).compareTo(result.get(t))<=0)
				{
					isAdd = true;
					result.add(t, source.get(k));
					break;
				}
			}
			if(!isAdd)
			{
				result.add(source.get(k));
			}
		}
		
		return result;
	}

}
