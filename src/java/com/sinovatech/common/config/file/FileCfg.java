package com.sinovatech.common.config.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class FileCfg {
	// private static Logger log = Logger.getLogger(GlobalConfig.class);
	public  FileCfg(File path){
		this.path=path;
	}
	
	// 存防属性列表
	private Map propertieMap = new HashMap();

	private Properties init(File file) {
		Properties p = new Properties();
		try {
			if (file.exists()) {
				// log.info("Load property file success!\t" + propertyFile);
				p.load(new FileInputStream(file));
				// log.info("Start Loading property file \t" + propertyFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// log.error("Could not load property file." + propertyFile, e);
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
	public String getProperty(String cls, String name) {
		Properties p = (Properties) propertieMap.get(cls);
		if (p == null) {
			p = init(getFile(cls));
			propertieMap.put(cls, p);
		}
		return p.getProperty(name);
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
	public void putProperty(String cls, String key, String value) {
		Properties p = (Properties) propertieMap.get(cls);
		if (p == null) {
			p = init(getFile(cls));
			propertieMap.put(cls, p);
		}
		p.put(key, value);
	}

	private File getFile(String cls) {
		return new File(path.getPath() + File.separatorChar
				+ cls.replace('.', File.separatorChar) + ".properties");
	}

	public  boolean getBooleanProperty(String cls, String name) {
		String p = getProperty(cls, name);
		return "true".equals(p);
	}

	public  Integer getIntegerProperty(String cls, String name) {
		String p = getProperty(cls, name);
		if (p == null) {
			return null;
		}
		return Integer.valueOf(p);
	}

	public  Long getLongProperty(String cls, String name) {
		String p = getProperty(cls, name);
		if (p == null) {
			return null;
		}
		return Long.valueOf(p);
	}

	public Double getDoubleProperty(String cls, String name) {
		String p = getProperty(cls, name);
		if (p == null) {
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
	public  void store() {
		Iterator iterator = propertieMap.keySet().iterator();
		while (iterator.hasNext()) {
			store((String) iterator.next());
		}
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
	public  void store(String cls) {
		Properties p = (Properties) propertieMap.get(cls);
		FileOutputStream fi;
		try {
			File file = getFile(cls);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			fi = new FileOutputStream(file);
			p.store(fi, "Modified time: " + Calendar.getInstance().getTime());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File path = null;

	/**
	 * @param path the path to set
	 */
	public void setPath(File path) {
		this.path = path;
	}

}
