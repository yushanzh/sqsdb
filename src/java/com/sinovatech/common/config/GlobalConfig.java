package com.sinovatech.common.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <ul>
 * <li> <b>目的:</b> <br />
 * <p>
 * 系统配置信息存储文件
 * </p>
 * </li>
 * <li><b>采用的不变量：</b></li>
 * <li><b>并行策略：</b></li>
 * <li> <b>修改历史：</b><br />
 * <p>
 * 创建:Nov 1, 2007 3:23:15 PM<br />
 * 作者:liulibin@sinovatech.com
 * </p>
 * </li>
 * <li><b>已知问题：</b></li>
 * </ul>
 */
public class GlobalConfig
{
	private static Log log = LogFactory.getLog(GlobalConfig.class);

	public final static String SYSTEM_PROPERTIES = "/system.properties";

	// 文件存储路径,最后字符应包含文件分隔符号
	private static String propertiesStorePath;
	// 存防属性列表
	private static Map propertieMap = new HashMap();
	// 存放属性文件
	private static Map propertieFileMap = new HashMap();

	static
	{
		Properties properties = init(SYSTEM_PROPERTIES);
		Iterator it = properties.keySet().iterator();
		propertiesStorePath = properties.getProperty("path");

		while (it.hasNext())
		{
			String name = (String) it.next();
			String file = properties.getProperty(name);

			file = file.trim();

			propertieFileMap.put(name, file);
			Properties p = init("/" + file);
			propertieMap.put(name, p);
		}
	}

	private static Properties init(String propertyFile)
	{
		Properties p = new Properties();
		try
		{
			log.info("Start Loading property file \t" + propertyFile);
			p.load(GlobalConfig.class.getResourceAsStream(propertyFile));
			log.info("Load property file success!\t" + propertyFile);
		} catch (Exception e)
		{
			e.printStackTrace();
			log.error("Could not load property file." + propertyFile, e);
		}

		return p;
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：获取属性</li>
	 * <li>适用的前提条件:</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param cls
	 *            属性文件类别，即system.properties中配置的文件key名称
	 * @param name
	 *            属性名称
	 * @return
	 */
	public static String getProperty(String cls, String name)
	{
		Properties p = (Properties) propertieMap.get(cls);
		if (p != null)
		{
			return p.getProperty(name);
		} else
		{
			return null;
		}
	}

	public static boolean getBooleanProperty(String cls, String name)
	{
		String p = getProperty(cls, name);
		return "true".equals(p);
	}

	public static Integer getIntegerProperty(String cls, String name)
	{
		String p = getProperty(cls, name);
		if (p == null)
		{
			return null;
		}
		return Integer.valueOf(p);
	}

	public static Long getLongProperty(String cls, String name)
	{
		String p = getProperty(cls, name);
		if (p == null)
		{
			return null;
		}
		return Long.valueOf(p);
	}

	public static Double getDoubleProperty(String cls, String name)
	{
		String p = getProperty(cls, name);
		if (p == null)
		{
			return null;
		}
		return Double.valueOf(p);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：保存属性配置文件</li>
	 * <li>适用的前提条件:在配置文件中增加了path属性</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 */
	public static void store()
	{

	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可见性原因：需要被其他应用调用</li>
	 * <li>目的：保存单个配置文件</li>
	 * <li>适用的前提条件:配置文件中增加了配置选项</li>
	 * <li>后置条件：</li>
	 * <li>例外处理：无 </li>
	 * <li>已知问题：</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param cls
	 */
	public static void store(String cls)
	{
		Properties p = (Properties) propertieMap.get(cls);
		FileOutputStream fi;
		try
		{
			fi = new FileOutputStream(new File((String) propertieFileMap
					.get(cls)));
			p.store(fi, "Modified time: " + Calendar.getInstance().getTime());
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
