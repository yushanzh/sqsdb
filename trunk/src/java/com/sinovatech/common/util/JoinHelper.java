/**
 * 
 */
package com.sinovatech.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 将对象合并字符串的通用工具类
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:Dec 18, 2007 1:53:44 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */

public class JoinHelper
{
	private JoinHelper()
	{

	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>合并一个数组为分隔符分割的字符串 </li>
	 * </ul>
	 * </p>
	 * 
	 * @param s
	 * @param separator
	 * @return
	 */
	public static String join(String[] s, String separator)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++)
		{
			sb.append(s[i] + separator);
		}
		String s2 = sb.toString();
		return sb.toString().substring(0, s2.length() - separator.length());
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>合并一个数组对象中某个属性为分隔符分割的字符串 </li>
	 * </ul>
	 * </p>
	 * 
	 * @param s
	 * @param separator
	 * @return
	 */
	public static String join(Object[] s, String separator, String filed)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++)
		{
			try
			{
				sb.append(BeanUtils.getProperty(s[i], filed) + separator);
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
		String s2 = sb.toString();
		return sb.toString().substring(0, s2.length() - separator.length());
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>合并一个容器对象为分隔符分割的字符串 </li>
	 * </ul>
	 * </p>
	 * 
	 * @param s
	 * @param separator
	 * @return
	 */
	public static String join(Collection co, String separator)
	{
		if(co.size()==0)
			return "";
		StringBuffer sb = new StringBuffer();
		Iterator it = co.iterator();
		while (it.hasNext())
		{
			sb.append(it.next().toString() + separator);
		}

		String s2 = sb.toString();
		return sb.toString().substring(0, s2.length()- separator.length());
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>合并一个容器对象中某个属性为分隔符分割的字符串 </li>
	 * </ul>
	 * </p>
	 * 
	 * @param s
	 * @param separator
	 * @return
	 */
	public static String join(Collection co, String separator, String filed)
	{
		if(co.size()==0)
			return "";
		StringBuffer sb = new StringBuffer();
		Iterator it = co.iterator();
		while (it.hasNext())
		{
			try
			{
				sb.append(BeanUtils.getProperty(it.next(), filed) + separator);
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}

		String s2 = sb.toString();
		return sb.toString().substring(0, s2.length() - separator.length());
	}
}
