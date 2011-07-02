package com.sinovatech.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 日期操作类
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：全部静态方法，不能实例化，直接调用即可</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:Nov 13, 2007 6:03:21 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
public class DateUtil
{
	private DateUtil()
	{
		
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：获取本年度的第一天</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param format
	 * @return
	 */
	public static Date getFirstDayOfYear(String format)
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_YEAR, 1);
		return ca.getTime();
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：获取一年的最后一个月</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param format
	 * @return
	 */
	public static Date getLastMonth(String format)
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.MONTH, ca.get(Calendar.MONTH) - 1);
		return ca.getTime();
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：获取本年的最后一天</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param format
	 * @return
	 */
	public static Date getLastDayOfYear()
	{
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MONTH, Calendar.DECEMBER);
		ca.set(Calendar.DAY_OF_MONTH, 30);
		return ca.getTime();
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：Comments</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param i
	 * @return
	 */
	public static Date subDays(Date source, int i)
	{

		return new Date(source.getTime() - 86400000 * i);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：增加i小时</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date subHours(Date source, int i)
	{
		return new Date(source.getTime() - 3600000 * i);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：增加分钟</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date subMinius(Date source, int i)
	{
		return new Date(source.getTime() - 60000 * i);
	}


	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：为时间增加I秒</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date subSeconds(Date source, int i)
	{
		return new Date(source.getTime() - 1000 * i);
	}
	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：Comments</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param i
	 * @return
	 */
	public static Date addDays(Date source, int i)
	{

		return new Date(source.getTime() + 86400000 * i);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：增加i小时</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date addHours(Date source, int i)
	{
		return new Date(source.getTime() + 3600000 * i);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：增加分钟</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子：
	 * </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date addMinius(Date source, int i)
	{
		return new Date(source.getTime() + 60000 * i);
	}


	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：为时间增加I秒</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param source
	 * @param i
	 * @return
	 */
	public static Date addSeconds(Date source, int i)
	{
		return new Date(source.getTime() + 1000 * i);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：Comments</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Date d = new Date();
		System.out.println(DateUtil.format(d, DateUtil.yyyyMMddHHmmssSpt));
		System.out.println(DateUtil.format(DateUtil.addSeconds(d, 1),
				DateUtil.yyyyMMddHHmmssSpt));
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：返回指定格式的当前日期格式化字符串</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param formart
	 * @return
	 */
	public static String format(String format)
	{
		return new SimpleDateFormat(format).format(Calendar.getInstance()
				.getTime());
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：返回指定格式的当前日期格式化字符串</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param formart
	 * @return
	 */
	public static String format(Date date, String format)
	{
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：格式化字符串为日期</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parse(String date, String format)
	{
		try
		{
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static String yyyyMMddHHmm = "yyyyMMddHHmm";
	public static String yyyyMMdd = "yyyyMMdd";
	public static String yyyyMM = "yyyyMM";
	public static String HHmmss = "HHmmss";
	public static String HHmm = "HHmm";

	public static String yyyyMMddHHmmssSpt = "yyyy-MM-dd HH:mm:ss";
	public static String yyyyMMddHHmmSpt = "yyyy-MM-dd HH:mm";
	public static String yyyyMMddSpt = "yyyy-MM-dd";
	public static String yyyyMMSpt = "yyyy-MM";
	public static String HHmmssSpt = "HH:mm:ss";
	public static String HHmmSpt = "HH:mm";
}
