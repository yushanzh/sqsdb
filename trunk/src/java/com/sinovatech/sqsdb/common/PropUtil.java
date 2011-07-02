package com.sinovatech.sqsdb.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PropUtil
{
  private static final Log log = LogFactory.getLog(PropUtil.class);
  private static final String minasocket = "/minasocket.properties";
  private static final String sqsconfig = "/sqsconfig.properties";
  private static Map propertieMap = new HashMap();

  static {
	  try {
		log.info("Start Loading property file \t");
		Properties p = new Properties();
		p.load(PropUtil.class.getResourceAsStream(minasocket));
		propertieMap.put("minasocket", p);
		p = new Properties();
		p.load(PropUtil.class.getResourceAsStream(sqsconfig));
		propertieMap.put("sqsconfig", p);
		log.info("Load property file success!\t");
	} catch (IOException e) {
		e.printStackTrace();
		log.error("Could not load property file.", e);
	}
  }

  public static String getString(String cls, String key) {
	  Properties p = (Properties)propertieMap.get(cls);
	  if (p != null){
		  return p.getProperty(key);
	  }
	  return null;
  }
  
  public static byte getByte(String cls, String key) {
	  Properties p = (Properties)propertieMap.get(cls);
	  if (p != null && !"".equals(p)){
		  return Byte.parseByte(p.getProperty(key));
	  }
	  return -1;
  }

  public static int getInt(String cls, String key) {
	  Properties p = (Properties)propertieMap.get(cls);
	  return Integer.parseInt(p.getProperty(key));
  }
  
  public static long getLong(String cls, String key) {
	  Properties p = (Properties)propertieMap.get(cls);
	  return Long.parseLong(p.getProperty(key));
  }

  public static boolean getBoolean(String cls, String key) {
	  Properties p = (Properties)propertieMap.get(cls);
	  return Boolean.valueOf(p.getProperty(key)).booleanValue();
  }


}